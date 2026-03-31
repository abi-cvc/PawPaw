package controller;

import model.dao.UserDAO;
import model.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Servlet implementation class AuthenticationController
 * Maneja la autenticación de usuarios (login y logout)
 */
@WebServlet("/login")
public class AuthenticationController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private UserDAO userDAO = new UserDAO();

    /**
     * Muestra el formulario de login
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar si ya hay una sesión activa
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            // Si ya está logueado, redirigir al panel correspondiente
            String rol = (String) session.getAttribute("userRole");  // ✅ CORREGIDO
            if ("admin".equals(rol)) {
                response.sendRedirect(request.getContextPath() + "/admin/panel");
            } else {
                response.sendRedirect(request.getContextPath() + "/user/panel");
            }
            return;
        }

        // Pasar Google Client ID al JSP
        request.setAttribute("googleClientId", System.getenv("GOOGLE_CLIENT_ID"));

        // Mostrar la página de login
        request.getRequestDispatcher("/view/internalUser/login.jsp").forward(request, response);
    }

    /**
     * Procesa el formulario de login
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

        // Autenticar usando el DAO
        User user = userDAO.findByEmailAndPassword(email, password);

        if (user != null) {
            // Protección contra Session Fixation: invalidar sesión previa y crear nueva
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getIdUser());
            session.setAttribute("user", user.getEmail());
            session.setAttribute("userName", user.getNameUser());
            session.setAttribute("userRole", user.getRol());  // ✅ CORREGIDO: usar "userRole"

            logger.info("Login exitoso - Usuario: {} - Rol: {}", user.getEmail(), user.getRol());

            // Redirigir según el rol
            if ("admin".equals(user.getRol())) {
                response.sendRedirect(request.getContextPath() + "/admin/panel");
            } else {
                response.sendRedirect(request.getContextPath() + "/user/panel");
            }
        } else {
            // Error de autenticación
            logger.info("Login fallido - Email: {}", email);
            request.setAttribute("error", "Email o contraseña incorrectos");
            request.setAttribute("email", email);
            request.setAttribute("googleClientId", System.getenv("GOOGLE_CLIENT_ID"));
            request.getRequestDispatcher("/view/internalUser/login.jsp").forward(request, response);
        }
    }
}