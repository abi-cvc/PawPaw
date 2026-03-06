package controller;

import model.dao.PetContactMessageDAO;
import model.dao.UserDAO;
import model.entity.PetContactMessage;
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
 * Servlet para panel de mensajes del usuario
 */
@WebServlet("/user/messages")
public class MyMessagesServlet extends HttpServlet {
    
    private PetContactMessageDAO messageDAO = new PetContactMessageDAO();
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        User user = userDAO.findById(userId);
        
        if (user != null) {
            request.setAttribute("user", user);
        }
        
        // Obtener filtro
        String filter = request.getParameter("filter");
        
        List<PetContactMessage> messages;
        if ("unread".equals(filter)) {
            messages = messageDAO.findUnreadByUserId(userId);
        } else {
            messages = messageDAO.findByUserId(userId);
        }
        
        // Contar mensajes no leídos
        int unreadCount = messageDAO.countUnreadByUserId(userId);
        
        request.setAttribute("messages", messages);
        request.setAttribute("unreadCount", unreadCount);
        request.setAttribute("currentFilter", filter);
        
        // Mensajes de sesión
        String successMessage = (String) session.getAttribute("successMessage");
        String errorMessage = (String) session.getAttribute("errorMessage");
        session.removeAttribute("successMessage");
        session.removeAttribute("errorMessage");
        
        request.setAttribute("successMessage", successMessage);
        request.setAttribute("errorMessage", errorMessage);
        
        System.out.println("📬 Usuario " + userId + " viendo mensajes. Total: " + messages.size() + ", No leídos: " + unreadCount);
        
        request.getRequestDispatcher("/view/internalUser/my-messages.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");
        
        try {
            if ("mark-read".equals(action)) {
                markAsRead(request, session);
            } else if ("mark-all-read".equals(action)) {
                markAllAsRead(userId, session);
            } else if ("delete".equals(action)) {
                deleteMessage(request, session);
            }
        } catch (Exception e) {
            System.err.println("❌ Error en acción de mensaje: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error al procesar la acción");
        }
        
        response.sendRedirect(request.getContextPath() + "/user/messages");
    }
    
    /**
     * Marcar mensaje como leído
     */
    private void markAsRead(HttpServletRequest request, HttpSession session) {
        try {
            Integer messageId = Integer.parseInt(request.getParameter("messageId"));
            
            if (messageDAO.markAsRead(messageId)) {
                session.setAttribute("successMessage", "Mensaje marcado como leído");
            } else {
                session.setAttribute("errorMessage", "Error al marcar mensaje");
            }
            
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al procesar la solicitud");
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    /**
     * Marcar todos los mensajes como leídos
     */
    private void markAllAsRead(Integer userId, HttpSession session) {
        if (messageDAO.markAllAsRead(userId)) {
            session.setAttribute("successMessage", "Todos los mensajes marcados como leídos");
        } else {
            session.setAttribute("errorMessage", "Error al marcar mensajes");
        }
    }
    
    /**
     * Eliminar mensaje
     */
    private void deleteMessage(HttpServletRequest request, HttpSession session) {
        try {
            Integer messageId = Integer.parseInt(request.getParameter("messageId"));
            
            if (messageDAO.delete(messageId)) {
                session.setAttribute("successMessage", "Mensaje eliminado");
            } else {
                session.setAttribute("errorMessage", "Error al eliminar mensaje");
            }
            
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al procesar la solicitud");
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}
