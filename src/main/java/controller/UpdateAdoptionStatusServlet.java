package controller;

import config.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.Set;

/**
 * Actualiza el adoption_status de una mascota.
 * Solo para usuarios partner (fundaciones).
 * URL: POST /pet/adoption-status
 */
@WebServlet("/pet/adoption-status")
public class UpdateAdoptionStatusServlet extends HttpServlet {

    private static final Set<String> ALLOWED_STATUSES = Set.of(
        "owned", "available", "adopted_pending", "adopted_transferred"
    );

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ── Verificar sesión ──────────────────────────────────────────
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Boolean isPartner = (Boolean) request.getSession().getAttribute("isPartner");
        if (isPartner == null || !isPartner) {
            response.sendError(403, "Solo fundaciones pueden cambiar el estado de adopción");
            return;
        }

        // ── Parámetros ────────────────────────────────────────────────
        String petIdStr    = request.getParameter("petId");
        String newStatus   = request.getParameter("adoptionStatus");

        if (petIdStr == null || newStatus == null || !ALLOWED_STATUSES.contains(newStatus)) {
            response.sendRedirect(request.getContextPath() + "/user/pets?error=invalidParams");
            return;
        }

        int petId;
        try {
            petId = Integer.parseInt(petIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user/pets?error=invalidPet");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            // Verificar que la mascota pertenece a esta fundación
            try (PreparedStatement check = conn.prepareStatement(
                    "SELECT id_pet FROM pets WHERE id_pet = ? AND id_user = ?")) {
                check.setInt(1, petId);
                check.setInt(2, userId);
                try (ResultSet rs = check.executeQuery()) {
                    if (!rs.next()) {
                        response.sendRedirect(request.getContextPath() + "/user/pets?error=notOwner");
                        return;
                    }
                }
            }

            // Actualizar estado
            try (PreparedStatement update = conn.prepareStatement(
                    "UPDATE pets SET adoption_status = ? WHERE id_pet = ?")) {
                update.setString(1, newStatus);
                update.setInt(2, petId);
                update.executeUpdate();
            }

            response.sendRedirect(request.getContextPath() + "/user/pets?success=statusUpdated");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/user/pets?error=dbError");
        }
    }
}