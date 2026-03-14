package controller;

import config.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Muestra el perfil público de una fundación con sus mascotas.
 * URL: /foundations/{id}
 *
 * Nota: /foundations/public es capturado PRIMERO por PublicFoundationsServlet
 * (mapeo exacto tiene prioridad sobre wildcard en el spec de Servlet).
 */
@WebServlet("/foundations/*")
public class FoundationProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo(); // ej: "/42"

        // Guardia: evitar colisión con /foundations/public en caso de contenedores que no respetan prioridad
        if (pathInfo == null || pathInfo.equals("/") || "/public".equals(pathInfo)) {
            response.sendRedirect(request.getContextPath() + "/foundations/public");
            return;
        }

        // Extraer ID de la fundación
        String[] parts = pathInfo.split("/");
        if (parts.length < 2) {
            response.sendError(404);
            return;
        }

        int foundationId;
        try {
            foundationId = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            response.sendError(404, "ID de fundación no válido");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            // ── 1. Info de la fundación (desde la vista) ──────────────
            String foundationSql = """
                SELECT id_user, foundation_name, contact_name, email,
                       phone, whatsapp, website, description,
                       available_pets, adopted_pets, total_pets
                FROM v_public_foundations
                WHERE id_user = ?
                """;

            Map<String, Object> foundation = null;
            try (PreparedStatement ps = conn.prepareStatement(foundationSql)) {
                ps.setInt(1, foundationId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        foundation = new LinkedHashMap<>();
                        foundation.put("id_user",         rs.getInt("id_user"));
                        foundation.put("foundation_name", rs.getString("foundation_name"));
                        foundation.put("contact_name",    rs.getString("contact_name"));
                        foundation.put("email",           rs.getString("email"));
                        foundation.put("phone",           rs.getString("phone"));
                        foundation.put("whatsapp",        rs.getString("whatsapp"));
                        foundation.put("website",         rs.getString("website"));
                        foundation.put("description",     rs.getString("description"));
                        foundation.put("available_pets",  rs.getInt("available_pets"));
                        foundation.put("adopted_pets",    rs.getInt("adopted_pets"));
                        foundation.put("total_pets",      rs.getInt("total_pets"));
                    }
                }
            }

            if (foundation == null) {
                response.sendError(404, "Fundación no encontrada");
                return;
            }

            // ── 2. Mascotas de la fundación ────────────────────────────
            String petsSql = """
                SELECT id_pet, name_pet, breed, age_pet,
                       medical_conditions, photo, adoption_status, adoption_description
                FROM pets
                WHERE id_user = ?
                  AND status_pet = 'active'
                ORDER BY
                    CASE adoption_status
                        WHEN 'available'           THEN 1
                        WHEN 'adopted_pending'     THEN 2
                        WHEN 'adopted_transferred' THEN 3
                        ELSE 4
                    END,
                    name_pet ASC
                """;

            List<Map<String, Object>> availablePets  = new ArrayList<>();
            List<Map<String, Object>> adoptedPets    = new ArrayList<>();

            try (PreparedStatement ps = conn.prepareStatement(petsSql)) {
                ps.setInt(1, foundationId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> pet = new LinkedHashMap<>();
                        pet.put("id_pet",              rs.getInt("id_pet"));
                        pet.put("name_pet",             rs.getString("name_pet"));
                        pet.put("breed",                rs.getString("breed"));
                        pet.put("age",                  rs.getObject("age_pet"));
                        pet.put("description",          rs.getString("adoption_description"));
                        pet.put("photo_url",            rs.getString("photo"));
                        pet.put("adoption_status",      rs.getString("adoption_status"));

                        String status = rs.getString("adoption_status");
                        if ("available".equals(status) || "adopted_pending".equals(status)) {
                            availablePets.add(pet);
                        } else if ("adopted_transferred".equals(status)) {
                            adoptedPets.add(pet);
                        }
                    }
                }
            }

            request.setAttribute("foundation",    foundation);
            request.setAttribute("availablePets", availablePets);
            request.setAttribute("adoptedPets",   adoptedPets);

            request.getRequestDispatcher("/view/public/foundation-profile.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }
}