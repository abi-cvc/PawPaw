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

/**
 * Servlet para enviar sugerencias (Usuario)
 */
@WebServlet("/user/send-suggestion")
public class SendSuggestionServlet extends HttpServlet {
    
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
        
        // Mostrar formulario
        request.getRequestDispatcher("/view/internalUser/send-suggestion.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar sesión
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        String message = request.getParameter("message");
        
        // Validaciones
        if (message == null || message.trim().isEmpty()) {
            request.setAttribute("error", "Por favor ingresa tu sugerencia");
            
            User user = userDAO.findById(userId);
            if (user != null) {
                request.setAttribute("user", user);
            }
            
            request.getRequestDispatcher("/view/internalUser/send-suggestion.jsp").forward(request, response);
            return;
        }
        
        message = message.trim();
        
        // Validar longitud mínima
        if (message.length() < 10) {
            request.setAttribute("error", "La sugerencia debe tener al menos 10 caracteres");
            request.setAttribute("message", message);
            
            User user = userDAO.findById(userId);
            if (user != null) {
                request.setAttribute("user", user);
            }
            
            request.getRequestDispatcher("/view/internalUser/send-suggestion.jsp").forward(request, response);
            return;
        }
        
        // Validar longitud máxima (opcional)
        if (message.length() > 1000) {
            request.setAttribute("error", "La sugerencia no puede exceder 1000 caracteres");
            request.setAttribute("message", message);
            
            User user = userDAO.findById(userId);
            if (user != null) {
                request.setAttribute("user", user);
            }
            
            request.getRequestDispatcher("/view/internalUser/send-suggestion.jsp").forward(request, response);
            return;
        }
        
        try {
            // Crear sugerencia
            Suggestion suggestion = new Suggestion(userId, message);
            
            if (suggestionDAO.create(suggestion)) {
                System.out.println("✅ Sugerencia creada exitosamente por usuario ID: " + userId);
                
                // Redirigir al panel con mensaje de éxito
                session.setAttribute("successMessage", "¡Gracias! Tu sugerencia ha sido enviada exitosamente.");
                response.sendRedirect(request.getContextPath() + "/user/panel");
                
            } else {
                System.err.println("❌ Error al guardar sugerencia en BD");
                request.setAttribute("error", "Error al enviar tu sugerencia. Por favor intenta nuevamente.");
                request.setAttribute("message", message);
                
                User user = userDAO.findById(userId);
                if (user != null) {
                    request.setAttribute("user", user);
                }
                
                request.getRequestDispatcher("/view/internalUser/send-suggestion.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al procesar sugerencia: " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("error", "Error al procesar tu solicitud. Por favor intenta nuevamente.");
            request.setAttribute("message", message);
            
            User user = userDAO.findById(userId);
            if (user != null) {
                request.setAttribute("user", user);
            }
            
            request.getRequestDispatcher("/view/internalUser/send-suggestion.jsp").forward(request, response);
        }
    }
}