package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet implementation class AuthenticationController
 * Maneja la autenticación de usuarios (login y logout)
 * Basado en el diseño OOD del sistema PawPaw
 */
@WebServlet("/login")
public class AuthenticationController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Muestra el formulario de login
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Verificar si ya hay una sesión activa
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // Si ya está logueado, redirigir al panel correspondiente
            String rol = (String) session.getAttribute("rol");
            if ("admin".equals(rol)) {
                response.sendRedirect(request.getContextPath() + "/admin/panel");
            } else {
                response.sendRedirect(request.getContextPath() + "/user/panel");
            }
            return;
        }
        
        // Mostrar la página de login
        request.getRequestDispatcher("/view/internalUser/login.jsp").forward(request, response);
    }

    /**
     * Procesa el formulario de login
     * Método: authenticate(email, password)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Obtener parámetros del formulario
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // Validación básica
        if (email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Por favor completa todos los campos");
            request.getRequestDispatcher("/view/internalUser/login.jsp").forward(request, response);
            return;
        }
        
        // Llamar al método authenticate
        if (authenticate(email, password)) {
            // Crear sesión
            HttpSession session = request.getSession(true);
            session.setAttribute("user", email);
            
            // Determinar rol (esto debería venir de la BD)
            String rol = determineUserRole(email);
            session.setAttribute("rol", rol);
            
            // Redirigir según el rol
            if ("admin".equals(rol)) {
                response.sendRedirect(request.getContextPath() + "/admin/panel");
            } else {
                response.sendRedirect(request.getContextPath() + "/user/panel");
            }
        } else {
            // Error de autenticación
            request.setAttribute("error", "Email o contraseña incorrectos");
            request.setAttribute("email", email); // Mantener el email ingresado
            request.getRequestDispatcher("/view/internalUser/login.jsp").forward(request, response);
        }
    }
    
    /**
     * Método authenticate(email, password) del diagrama OOD
     * Autentica las credenciales del usuario
     * 
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return true si las credenciales son válidas, false en caso contrario
     */
    public boolean authenticate(String email, String password) {
        // TODO: Implementar validación con base de datos
        // 1. Buscar usuario por email en la tabla User
        // 2. Verificar que la contraseña hasheada coincida
        // 3. Verificar que la cuenta esté activa
        
        // Ejemplo de implementación con DAO (a implementar):
        /*
        UserDAO userDAO = new UserDAO();
        User user = userDAO.findUser(email, password);
        return user != null && user.isActive();
        */
        
        // Implementación temporal para testing
        return email != null && password != null && password.length() >= 6;
    }
    
    /**
     * Método auxiliar para determinar el rol del usuario
     * (Debe obtenerse desde la base de datos)
     */
    private String determineUserRole(String email) {
        // TODO: Obtener rol desde base de datos usando UserDAO
        // Ejemplo temporal
        if (email.contains("admin")) {
            return "admin";
        }
        return "user";
    }
}