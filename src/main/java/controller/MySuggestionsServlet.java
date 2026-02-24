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
 * Servlet para que los usuarios vean sus propias sugerencias
 */
@WebServlet("/user/my-suggestions")
public class MySuggestionsServlet extends HttpServlet {
    
    private SuggestionDAO suggestionDAO = new SuggestionDAO();
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar sesión
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Obtener información del usuario
        User user = userDAO.findById(userId);
        if (user != null) {
            request.setAttribute("user", user);
        }
        
        // Obtener todas las sugerencias del usuario
        List<Suggestion> suggestions = suggestionDAO.findByUserId(userId);
        
        // Contar por estado
        long pendingCount = suggestions.stream()
                .filter(s -> "pending".equals(s.getStatusSuggestion()))
                .count();
        long reviewedCount = suggestions.stream()
                .filter(s -> "reviewed".equals(s.getStatusSuggestion()))
                .count();
        long resolvedCount = suggestions.stream()
                .filter(s -> "resolved".equals(s.getStatusSuggestion()))
                .count();
        long rejectedCount = suggestions.stream()
                .filter(s -> "rejected".equals(s.getStatusSuggestion()))
                .count();
        
        request.setAttribute("suggestions", suggestions);
        request.setAttribute("totalSuggestions", suggestions.size());
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("reviewedCount", reviewedCount);
        request.setAttribute("resolvedCount", resolvedCount);
        request.setAttribute("rejectedCount", rejectedCount);
        
        System.out.println("📋 Usuario " + userId + " viendo sus sugerencias. Total: " + suggestions.size());
        
        request.getRequestDispatcher("/view/internalUser/my-suggestions.jsp").forward(request, response);
    }
}