package controller;

import model.dao.SuggestionDAO;
import model.dao.UserDAO;
import model.dao.AdminAuditLogDAO;
import model.entity.Suggestion;
import model.entity.User;
import model.entity.AdminAuditLog;
import service.EmailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet para gestionar sugerencias (Administrador)
 */
@WebServlet("/admin/suggestions")
public class ManageSuggestionsServlet extends HttpServlet {
    
    private SuggestionDAO suggestionDAO = new SuggestionDAO();
    private UserDAO userDAO = new UserDAO();
    private AdminAuditLogDAO auditLogDAO = new AdminAuditLogDAO();
    private EmailService emailService = new EmailService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar sesión y rol de administrador
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String userRole = (String) session.getAttribute("userRole");
        if (!"admin".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/user/panel");
            return;
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        User user = userDAO.findById(userId);
        if (user != null) {
            request.setAttribute("user", user);
        }
        
        String filter = request.getParameter("filter");
        List<Suggestion> suggestions;
        
        // Filtrar por estado si se especifica
        if (filter != null && !filter.isEmpty()) {
            suggestions = suggestionDAO.findByStatus(filter);
        } else {
            suggestions = suggestionDAO.findAll();
        }
        
        // Obtener estadísticas
        int totalSuggestions = suggestionDAO.count();
        int pendingSuggestions = suggestionDAO.countByStatus("pending");
        int reviewedSuggestions = suggestionDAO.countByStatus("reviewed");
        int resolvedSuggestions = suggestionDAO.countByStatus("resolved");
        
        request.setAttribute("suggestions", suggestions);
        request.setAttribute("totalSuggestions", totalSuggestions);
        request.setAttribute("pendingSuggestions", pendingSuggestions);
        request.setAttribute("reviewedSuggestions", reviewedSuggestions);
        request.setAttribute("resolvedSuggestions", resolvedSuggestions);
        request.setAttribute("currentFilter", filter);
        
        request.getRequestDispatcher("/view/admin/manage-suggestions.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar sesión y rol
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String userRole = (String) session.getAttribute("userRole");
        if (!"admin".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/user/panel");
            return;
        }
        
        Integer adminId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");
        String suggestionIdStr = request.getParameter("suggestionId");
        
        if (suggestionIdStr == null || suggestionIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/suggestions?error=invalid");
            return;
        }
        
        try {
            Integer suggestionId = Integer.parseInt(suggestionIdStr);
            
            // Obtener IP del admin para auditoría
            String ipAddress = request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");
            
            if ("updateStatus".equals(action)) {
                String newStatus = request.getParameter("status");
                String adminResponse = request.getParameter("adminResponse");
                
                if (newStatus != null && !newStatus.isEmpty()) {
                    // Obtener sugerencia actual para comparar
                    Suggestion suggestion = suggestionDAO.findById(suggestionId);
                    String oldStatus = suggestion != null ? suggestion.getStatusSuggestion() : "unknown";
                    
                    boolean success;
                    
                    if (adminResponse != null && !adminResponse.trim().isEmpty()) {
                        // Actualizar con respuesta
                        success = suggestionDAO.updateStatusWithResponse(suggestionId, newStatus, adminResponse.trim());
                        
                        // Guardar en audit log
                        if (success) {
                            AdminAuditLog log = new AdminAuditLog(
                                adminId, 
                                "ADD_RESPONSE", 
                                "suggestion", 
                                suggestionId, 
                                oldStatus, 
                                newStatus, 
                                ipAddress
                            );
                            log.setUserAgent(userAgent);
                            log.setDetails("Response added: " + adminResponse.substring(0, Math.min(50, adminResponse.length())));
                            auditLogDAO.create(log);
                            
                            // Enviar notificación por email
                            sendAdminNotification(adminId, "ADD_RESPONSE", suggestionId, oldStatus, newStatus, suggestion);
                        }
                    } else {
                        // Solo actualizar estado
                        success = suggestionDAO.updateStatus(suggestionId, newStatus);
                        
                        // Guardar en audit log
                        if (success) {
                            AdminAuditLog log = new AdminAuditLog(
                                adminId, 
                                "UPDATE_STATUS", 
                                "suggestion", 
                                suggestionId, 
                                oldStatus, 
                                newStatus, 
                                ipAddress
                            );
                            log.setUserAgent(userAgent);
                            auditLogDAO.create(log);
                            
                            // Enviar notificación por email
                            sendAdminNotification(adminId, "UPDATE_STATUS", suggestionId, oldStatus, newStatus, suggestion);
                        }
                    }
                    
                    if (success) {
                        session.setAttribute("successMessage", "Estado actualizado correctamente");
                    } else {
                        session.setAttribute("errorMessage", "Error al actualizar el estado");
                    }
                }
                
            } else if ("delete".equals(action)) {
                // Obtener info antes de eliminar
                Suggestion suggestion = suggestionDAO.findById(suggestionId);
                
                if (suggestionDAO.delete(suggestionId)) {
                    // Guardar en audit log
                    AdminAuditLog log = new AdminAuditLog(
                        adminId, 
                        "DELETE_SUGGESTION", 
                        "suggestion", 
                        suggestionId, 
                        suggestion != null ? suggestion.getStatusSuggestion() : null, 
                        "DELETED", 
                        ipAddress
                    );
                    log.setUserAgent(userAgent);
                    log.setDetails("Deleted suggestion from user: " + (suggestion != null ? suggestion.getUserEmail() : "unknown"));
                    auditLogDAO.create(log);
                    
                    // Enviar notificación por email
                    sendAdminNotification(adminId, "DELETE_SUGGESTION", suggestionId, null, "DELETED", suggestion);
                    
                    session.setAttribute("successMessage", "Sugerencia eliminada correctamente");
                } else {
                    session.setAttribute("errorMessage", "Error al eliminar la sugerencia");
                }
            }
            
        } catch (NumberFormatException e) {
            System.err.println("❌ ID de sugerencia inválido: " + suggestionIdStr);
        } catch (Exception e) {
            System.err.println("❌ Error al procesar acción: " + e.getMessage());
            e.printStackTrace();
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/suggestions");
    }
    
    /**
     * Envía notificación por email a pawpawsystem@gmail.com sobre acciones administrativas
     */
    private void sendAdminNotification(Integer adminId, String action, Integer suggestionId, 
                                      String oldValue, String newValue, Suggestion suggestion) {
        try {
            User admin = userDAO.findById(adminId);
            String adminName = admin != null ? admin.getNameUser() : "Admin";
            String adminEmail = admin != null ? admin.getEmail() : "unknown";
            
            String subject = "🔔 Acción Administrativa en PawPaw";
            String actionText = getActionText(action);
            
            StringBuilder message = new StringBuilder();
            message.append("Se ha realizado una acción administrativa en PawPaw:\n\n");
            message.append("📋 DETALLES:\n");
            message.append("━━━━━━━━━━━━━━━━━━━━━━\n");
            message.append("Acción: ").append(actionText).append("\n");
            message.append("Administrador: ").append(adminName).append(" (").append(adminEmail).append(")\n");
            message.append("Sugerencia ID: ").append(suggestionId).append("\n");
            
            if (suggestion != null) {
                message.append("Usuario afectado: ").append(suggestion.getUserName())
                       .append(" (").append(suggestion.getUserEmail()).append(")\n");
            }
            
            if (oldValue != null && newValue != null) {
                message.append("Cambio: ").append(oldValue).append(" → ").append(newValue).append("\n");
            }
            
            message.append("\n📧 Este es un email automático de notificación.");
            message.append("\n🔐 Solo tú recibes estas notificaciones.");
            
            // Enviar a pawpawsystem@gmail.com
            emailService.sendNotificationEmail(
                "pawpawsystem@gmail.com", 
                "PawPaw Admin", 
                subject, 
                message.toString()
            );
            
            System.out.println("✅ Notificación enviada a pawpawsystem@gmail.com");
            
        } catch (Exception e) {
            System.err.println("❌ Error al enviar notificación: " + e.getMessage());
            // No lanzar excepción para no interrumpir el flujo principal
        }
    }
    
    private String getActionText(String action) {
        switch (action) {
            case "UPDATE_STATUS": return "Actualización de estado";
            case "ADD_RESPONSE": return "Respuesta agregada";
            case "DELETE_SUGGESTION": return "Sugerencia eliminada";
            default: return action;
        }
    }
}