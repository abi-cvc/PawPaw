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
 * Muestra la lista pública de fundaciones aliadas PawPaw.
 * URL: /foundations/public
 */
@WebServlet("/foundations/public")
public class PublicFoundationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = """
                SELECT id_user, foundation_name, contact_name, email,
                       phone, whatsapp, website, description,
                       available_pets, adopted_pets, total_pets
                FROM v_public_foundations
                ORDER BY available_pets DESC, foundation_name ASC
                """;

            List<Map<String, Object>> foundations = new ArrayList<>();

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Map<String, Object> f = new LinkedHashMap<>();
                    f.put("id_user",          rs.getInt("id_user"));
                    f.put("foundation_name",  rs.getString("foundation_name"));
                    f.put("contact_name",     rs.getString("contact_name"));
                    f.put("email",            rs.getString("email"));
                    f.put("phone",            rs.getString("phone"));
                    f.put("whatsapp",         rs.getString("whatsapp"));
                    f.put("website",          rs.getString("website"));
                    f.put("description",      rs.getString("description"));
                    f.put("available_pets",   rs.getInt("available_pets"));
                    f.put("adopted_pets",     rs.getInt("adopted_pets"));
                    f.put("total_pets",       rs.getInt("total_pets"));
                    foundations.add(f);
                }
            }

            request.setAttribute("foundations", foundations);
            request.getRequestDispatcher("/view/public/foundations-list.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(500, "Error al cargar las fundaciones");
        }
    }
}