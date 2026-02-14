package controller;

import model.dao.UserDAO;
import model.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controlador para gestionar el perfil del usuario
 */
@WebServlet("/user/profile")
public class UserProfileController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Verificar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Obtener datos del usuario
        User user = userDAO.findById(userId);
        
        if (user == null || !user.getActive()) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Pasar datos a la vista
        request.setAttribute("user", user);
        request.getRequestDispatcher("/view/internalUser/profile.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Verificar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");
        
        if ("updateProfile".equals(action)) {
            updateProfile(request, response, userId, session);
        } else if ("updatePassword".equals(action)) {
            updatePassword(request, response, userId);
        } else {
            response.sendRedirect(request.getContextPath() + "/user/profile");
        }
    }
    
    /**
     * Actualizar datos del perfil
     */
    private void updateProfile(HttpServletRequest request, HttpServletResponse response, 
                               Integer userId, HttpSession session) 
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        
        // Validar campos
        if (name == null || name.trim().isEmpty() || 
            email == null || email.trim().isEmpty()) {
            User user = userDAO.findById(userId);
            request.setAttribute("user", user);
            request.setAttribute("error", "Todos los campos son obligatorios");
            request.getRequestDispatcher("/view/internalUser/profile.jsp").forward(request, response);
            return;
        }
        
        // Verificar si el email ya existe (en otro usuario)
        User existingUser = userDAO.findByEmail(email.trim());
        if (existingUser != null && !existingUser.getIdUser().equals(userId)) {
            User user = userDAO.findById(userId);
            request.setAttribute("user", user);
            request.setAttribute("error", "El email ya está registrado por otro usuario");
            request.getRequestDispatcher("/view/internalUser/profile.jsp").forward(request, response);
            return;
        }
        
        // Actualizar usuario
        User user = userDAO.findById(userId);
        user.setNameUser(name.trim());
        user.setEmail(email.trim());
        
        if (userDAO.update(user)) {
            // Actualizar datos en sesión
            session.setAttribute("userName", user.getNameUser());
            session.setAttribute("user", user.getEmail());
            
            System.out.println("✅ Perfil actualizado - Usuario ID: " + userId);
            
            request.setAttribute("user", user);
            request.setAttribute("success", "Perfil actualizado correctamente");
            request.getRequestDispatcher("/view/internalUser/profile.jsp").forward(request, response);
        } else {
            request.setAttribute("user", user);
            request.setAttribute("error", "Error al actualizar el perfil");
            request.getRequestDispatcher("/view/internalUser/profile.jsp").forward(request, response);
        }
    }
    
    /**
     * Actualizar contraseña
     */
    private void updatePassword(HttpServletRequest request, HttpServletResponse response, Integer userId) 
            throws ServletException, IOException {
        
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        User user = userDAO.findById(userId);
        
        // Validar campos
        if (currentPassword == null || currentPassword.trim().isEmpty() ||
            newPassword == null || newPassword.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("user", user);
            request.setAttribute("errorPassword", "Todos los campos son obligatorios");
            request.getRequestDispatcher("/view/internalUser/profile.jsp").forward(request, response);
            return;
        }
        
        // Validar contraseña actual
        User validUser = userDAO.findByEmailAndPassword(user.getEmail(), currentPassword);
        if (validUser == null) {
            request.setAttribute("user", user);
            request.setAttribute("errorPassword", "La contraseña actual es incorrecta");
            request.getRequestDispatcher("/view/internalUser/profile.jsp").forward(request, response);
            return;
        }
        
        // Validar nueva contraseña
        if (newPassword.length() < 6) {
            request.setAttribute("user", user);
            request.setAttribute("errorPassword", "La nueva contraseña debe tener al menos 6 caracteres");
            request.getRequestDispatcher("/view/internalUser/profile.jsp").forward(request, response);
            return;
        }
        
        // Validar confirmación
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("user", user);
            request.setAttribute("errorPassword", "Las contraseñas no coinciden");
            request.getRequestDispatcher("/view/internalUser/profile.jsp").forward(request, response);
            return;
        }
        
        // Actualizar contraseña
        if (userDAO.updatePassword(userId, newPassword)) {
            System.out.println("✅ Contraseña actualizada - Usuario ID: " + userId);
            
            request.setAttribute("user", user);
            request.setAttribute("successPassword", "Contraseña actualizada correctamente");
            request.getRequestDispatcher("/view/internalUser/profile.jsp").forward(request, response);
        } else {
            request.setAttribute("user", user);
            request.setAttribute("errorPassword", "Error al actualizar la contraseña");
            request.getRequestDispatcher("/view/internalUser/profile.jsp").forward(request, response);
        }
    }
}