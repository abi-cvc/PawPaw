package controller;

import model.dao.PetDAO;
import model.entity.Pet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet para actualizar el estado de adopción de una mascota
 * Solo para fundaciones (partners)
 * URL: /pet/adoption-status (POST)
 */
@WebServlet("/pet/adoption-status")
public class UpdateAdoptionStatusServlet extends HttpServlet {
    
    private PetDAO petDAO = new PetDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar sesión
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            Integer userId = (Integer) session.getAttribute("userId");
            Integer petId = Integer.parseInt(request.getParameter("petId"));
            String newStatus = request.getParameter("status");
            
            // Validar estado
            if (!isValidStatus(newStatus)) {
                session.setAttribute("errorMessage", "Estado de adopción inválido");
                response.sendRedirect(request.getContextPath() + "/user/pets");
                return;
            }
            
            // Verificar que la mascota pertenece al usuario
            Pet pet = petDAO.findById(petId);
            
            if (pet == null || !pet.getIdUser().equals(userId)) {
                session.setAttribute("errorMessage", "No tienes permiso para modificar esta mascota");
                response.sendRedirect(request.getContextPath() + "/user/pets");
                return;
            }
            
            // Actualizar estado
            if (petDAO.updateAdoptionStatus(petId, newStatus)) {
                String statusText = getStatusText(newStatus);
                session.setAttribute("successMessage", 
                    "Estado de " + pet.getNamePet() + " actualizado a: " + statusText);
                
                System.out.println("✅ Estado de adopción actualizado: " + pet.getNamePet() + 
                                 " → " + newStatus);
            } else {
                session.setAttribute("errorMessage", "Error al actualizar el estado");
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Datos inválidos");
        } catch (Exception e) {
            System.err.println("❌ Error al actualizar estado de adopción: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error al actualizar el estado");
        }
        
        response.sendRedirect(request.getContextPath() + "/user/pets");
    }
    
    /**
     * Valida que el estado sea uno de los permitidos
     */
    private boolean isValidStatus(String status) {
        if (status == null) return false;
        
        return status.equals("owned") || 
               status.equals("available") || 
               status.equals("adopted_pending") ||
               status.equals("adopted_transferred");
    }
    
    /**
     * Obtiene texto legible del estado
     */
    private String getStatusText(String status) {
        switch (status) {
            case "owned": return "Propia";
            case "available": return "En Adopción";
            case "adopted_pending": return "Adoptado (Pendiente)";
            case "adopted_transferred": return "Adoptado (Transferido)";
            default: return status;
        }
    }
}
