package controller;

import model.dao.PetDAO;
import model.entity.Pet;

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
 * Servlet para mostrar perfil público de una fundación con sus mascotas
 * URL: /foundations/{id}
 */
@WebServlet("/foundations/*")
public class FoundationProfileServlet extends HttpServlet {
    
    private PetDAO petDAO = new PetDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Extraer ID de la fundación desde la URL
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/public")) {
            // Si es /foundations o /foundations/public, redirigir al servlet correcto
            response.sendRedirect(request.getContextPath() + "/foundations/public");
            return;
        }
        
        try {
            // Extraer ID: /foundations/123 -> 123
            String idStr = pathInfo.substring(1);
            Integer foundationId = Integer.parseInt(idStr);
            
            // Obtener información de la fundación
            Map<String, Object> foundation = getFoundationInfo(foundationId);
            
            if (foundation == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Fundación no encontrada");
                return;
            }
            
            // Obtener mascotas de la fundación
            List<Pet> allPets = petDAO.findFoundationPets(foundationId);
            
            // Separar por estado
            List<Pet> availablePets = new ArrayList<>();
            List<Pet> adoptedPets = new ArrayList<>();
            
            for (Pet pet : allPets) {
                if ("available".equals(pet.getAdoptionStatus())) {
                    availablePets.add(pet);
                } else if (pet.getAdoptionStatus() != null && 
                          pet.getAdoptionStatus().toLowerCase().startsWith("adopted")) {
                    adoptedPets.add(pet);
                }
            }
            
            request.setAttribute("foundation", foundation);
            request.setAttribute("availablePets", availablePets);
            request.setAttribute("adoptedPets", adoptedPets);
            request.setAttribute("totalAvailable", availablePets.size());
            request.setAttribute("totalAdopted", adoptedPets.size());
            
            System.out.println("🏢 Mostrando perfil de: " + foundation.get("foundationName"));
            System.out.println("   En adopción: " + availablePets.size());
            System.out.println("   Adoptados: " + adoptedPets.size());
            
            request.getRequestDispatcher("/view/public/foundation-profile.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        } catch (Exception e) {
            System.err.println("❌ Error al cargar perfil de fundación: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Error al cargar perfil");
        }
    }
    
    /**
     * Obtiene información de una fundación específica
     */
    private Map<String, Object> getFoundationInfo(Integer foundationId) {
        String sql = "SELECT * FROM v_public_foundations WHERE id_user = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, foundationId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Map<String, Object> foundation = new HashMap<>();
                foundation.put("idUser", rs.getInt("id_user"));
                foundation.put("foundationName", rs.getString("foundation_name"));
                foundation.put("contactName", rs.getString("contact_name"));
                foundation.put("email", rs.getString("email"));
                foundation.put("phone", rs.getString("phone"));
                foundation.put("whatsapp", rs.getString("whatsapp"));
                foundation.put("website", rs.getString("website"));
                foundation.put("description", rs.getString("description"));
                foundation.put("availablePets", rs.getInt("available_pets"));
                foundation.put("adoptedPets", rs.getInt("adopted_pets"));
                foundation.put("totalPets", rs.getInt("total_pets"));
                
                return foundation;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener fundación: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}
