package controller;

import model.dao.UserDAO;
import model.dao.SlotAdjustmentDAO;
import model.dao.PetDAO;
import model.entity.User;
import model.entity.SlotAdjustment;
import service.EmailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet para ajustar slots de usuarios (usado por modal y página dedicada)
 */
@WebServlet("/admin/adjust-slots")
public class AdjustUserSlotsServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    private SlotAdjustmentDAO adjustmentDAO = new SlotAdjustmentDAO();
    private PetDAO petDAO = new PetDAO();
    private EmailService emailService = new EmailService();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar que es admin
        if (session == null || session.getAttribute("role") == null || 
            !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            Integer adminId = (Integer) session.getAttribute("userId");
            Integer userId = Integer.parseInt(request.getParameter("userId"));
            Integer newLimit = Integer.parseInt(request.getParameter("newLimit"));
            String reason = request.getParameter("reason");
            
            // Validar razón
            if (reason == null || reason.trim().length() < 10) {
                session.setAttribute("errorMessage", "La razón debe tener al menos 10 caracteres");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            // Validar límite
            if (newLimit < 0) {
                session.setAttribute("errorMessage", "El límite no puede ser negativo");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            if (newLimit > 100) {
                session.setAttribute("errorMessage", "El límite máximo es 100 slots");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            // Obtener usuario
            User user = userDAO.findById(userId);
            if (user == null) {
                session.setAttribute("errorMessage", "Usuario no encontrado");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            Integer currentLimit = user.getPetLimit();
            
            // Validar que no baje por debajo de mascotas registradas
            int currentPetCount = petDAO.countByUserId(userId);
            if (newLimit < currentPetCount) {
                session.setAttribute("errorMessage", 
                    "No puedes bajar el límite a " + newLimit + ". El usuario tiene " + currentPetCount + " mascotas registradas.");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            // Verificar si hay cambio real
            if (newLimit.equals(currentLimit)) {
                session.setAttribute("errorMessage", "El nuevo límite es igual al actual");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            // Actualizar límite
            boolean updated = userDAO.updatePetLimit(userId, newLimit);
            
            if (updated) {
                // Registrar en auditoría
                SlotAdjustment adjustment = new SlotAdjustment(
                    userId, 
                    adminId, 
                    currentLimit, 
                    newLimit, 
                    reason.trim()
                );
                adjustmentDAO.create(adjustment);
                
                // Enviar email al usuario
                sendNotificationEmail(user, currentLimit, newLimit, reason);
                
                int change = newLimit - currentLimit;
                String changeText = change > 0 ? "+" + change : String.valueOf(change);
                
                session.setAttribute("successMessage", 
                    "Límite actualizado: " + currentLimit + " → " + newLimit + " (" + changeText + ") para " + user.getNameUser());
                
                System.out.println("✅ Admin " + adminId + " ajustó slots de usuario " + userId + 
                                 ": " + currentLimit + " → " + newLimit);
            } else {
                session.setAttribute("errorMessage", "Error al actualizar el límite");
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Datos inválidos");
            System.err.println("❌ Error en formato de números: " + e.getMessage());
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al procesar la solicitud");
            System.err.println("❌ Error al ajustar slots: " + e.getMessage());
            e.printStackTrace();
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
    
    /**
     * Enviar email de notificación al usuario
     */
    private void sendNotificationEmail(User user, int oldLimit, int newLimit, String reason) {
        try {
            int change = newLimit - oldLimit;
            boolean isIncrease = change > 0;
            String changeText = change > 0 ? "+" + change : String.valueOf(change);
            
            String subject = isIncrease ? 
                "🎉 ¡Tus slots han sido actualizados! - PawPaw" :
                "⚠️ Actualización de tu cuenta - PawPaw";
            
            String emoji = isIncrease ? "🎉" : "ℹ️";
            
            String message = "Hola " + user.getNameUser() + ",\n\n" +
                           emoji + " Tu límite de mascotas ha sido actualizado:\n\n" +
                           "━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                           "Límite anterior: " + oldLimit + " mascota(s)\n" +
                           "Límite nuevo: " + newLimit + " mascota(s) (" + changeText + ")\n" +
                           "━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                           "Razón: " + reason + "\n\n";
            
            if (isIncrease) {
                message += "¡Ahora puedes registrar más mascotas en PawPaw! 🐾\n\n";
            } else {
                message += "Si tienes dudas sobre este cambio, puedes contactarnos.\n\n";
            }
            
            message += "Saludos,\n" +
                      "Equipo PawPaw";
            
            emailService.sendNotificationEmail(user.getEmail(), user.getNameUser(), subject, message);
            
            System.out.println("✅ Email de notificación enviado a " + user.getEmail());
            
        } catch (Exception e) {
            System.err.println("⚠️ Error al enviar email de notificación: " + e.getMessage());
        }
    }
}
