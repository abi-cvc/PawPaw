package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.DatabaseConnection;

/**
 * Servlet para mostrar lista pública de fundaciones aliadas
 * URL: /foundations/public
 */
@WebServlet("/foundations/public")
public class PublicFoundationsServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Obtener fundaciones públicas desde la vista v_public_foundations
            List<Map<String, Object>> foundations = getPublicFoundations();
            
            request.setAttribute("foundations", foundations);
            request.setAttribute("totalFoundations", foundations.size());
            
            System.out.println("🏢 Mostrando fundaciones públicas. Total: " + foundations.size());
            
            request.getRequestDispatcher("/view/public/foundations-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("❌ Error al cargar fundaciones públicas: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Error al cargar fundaciones");
        }
    }
    
    /**
     * Obtiene lista de fundaciones públicas desde la vista
     */
    private List<Map<String, Object>> getPublicFoundations() {
        List<Map<String, Object>> foundations = new ArrayList<>();
        String sql = "SELECT * FROM v_public_foundations ORDER BY foundation_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> foundation = new HashMap<>();
                foundation.put("idUser", rs.getInt("id_user"));
                foundation.put("foundationName", rs.getString("foundation_name"));
                foundation.put("contactName", rs.getString("contact_name"));
                foundation.put("email", rs.getString("email"));
                foundation.put("phone", rs.getString("phone"));
                foundation.put("website", rs.getString("website"));
                foundation.put("description", rs.getString("description"));
                foundation.put("availablePets", rs.getInt("available_pets"));
                foundation.put("adoptedPets", rs.getInt("adopted_pets"));
                foundation.put("totalPets", rs.getInt("total_pets"));
                
                foundations.add(foundation);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener fundaciones: " + e.getMessage());
            e.printStackTrace();
        }
        
        return foundations;
    }
}
