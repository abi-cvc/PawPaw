package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet para manejar el cierre de sesión
 * Complementa a AuthenticationController
 * Basado en el diseño OOD del sistema PawPaw
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Maneja la solicitud de logout
     * Invalida la sesión actual y redirige a la página de inicio
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Obtener la sesión actual (sin crear una nueva)
        HttpSession session = request.getSession(false);
        
        // Si existe sesión, invalidarla
        if (session != null) {
            session.invalidate();
        }
        
        // Redirigir a la página de inicio
        response.sendRedirect(request.getContextPath() + "/view/index.jsp");
    }

    /**
     * También manejamos POST por si acaso
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}