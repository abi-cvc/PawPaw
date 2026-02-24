package controller;

import model.dao.SuggestionDAO;
import model.dao.UserDAO;
import model.entity.Suggestion;
import model.entity.User;

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
        
        String action = request.getParameter("action");
        String suggestionIdStr = request.getParameter("suggestionId");
        
        if (suggestionIdStr == null || suggestionIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/suggestions?error=invalid");
            return;
        }
        
        try {
            Integer suggestionId = Integer.parseInt(suggestionIdStr);
            
            if ("updateStatus".equals(action)) {
                String newStatus = request.getParameter("status");
                String adminResponse = request.getParameter("adminResponse");
                
                if (newStatus != null && !newStatus.isEmpty()) {
                    boolean success;
                    
                    if (adminResponse != null && !adminResponse.trim().isEmpty()) {
                        // Actualizar con respuesta
                        success = suggestionDAO.updateStatusWithResponse(suggestionId, newStatus, adminResponse.trim());
                    } else {
                        // Solo actualizar estado
                        success = suggestionDAO.updateStatus(suggestionId, newStatus);
                    }
                    
                    if (success) {
                        session.setAttribute("successMessage", "Estado actualizado correctamente");
                    } else {
                        session.setAttribute("errorMessage", "Error al actualizar el estado");
                    }
                }
                
            } else if ("delete".equals(action)) {
                if (suggestionDAO.delete(suggestionId)) {
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
}