package controller;

import model.dao.PetTransferRequestDAO;
import model.dao.UserDAO;
import model.entity.PetTransferRequest;
import model.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet para aceptar/rechazar transferencia de mascota
 * URL: /accept-transfer (GET/POST)
 */
@WebServlet("/accept-transfer")
public class AcceptTransferServlet extends HttpServlet {
    
    private PetTransferRequestDAO transferDAO = new PetTransferRequestDAO();
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        
        if (token == null || token.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token inválido");
            return;
        }
        
        // Buscar transferencia
        PetTransferRequest transfer = transferDAO.findByToken(token);
        
        if (transfer == null) {
            request.setAttribute("error", "Transferencia no encontrada");
            request.getRequestDispatcher("/view/public/transfer-error.jsp").forward(request, response);
            return;
        }
        
        // Verificar si ya fue aceptada
        if (transfer.isAccepted()) {
            request.setAttribute("message", "Esta transferencia ya fue aceptada anteriormente");
            request.getRequestDispatcher("/view/public/transfer-completed.jsp").forward(request, response);
            return;
        }
        
        // Verificar si expiró
        if (transfer.isExpired()) {
            request.setAttribute("error", "Esta transferencia ha expirado");
            request.setAttribute("transfer", transfer);
            request.getRequestDispatcher("/view/public/transfer-expired.jsp").forward(request, response);
            return;
        }
        
        // Verificar si fue rechazada
        if (transfer.isRejected()) {
            request.setAttribute("error", "Esta transferencia fue rechazada");
            request.getRequestDispatcher("/view/public/transfer-error.jsp").forward(request, response);
            return;
        }
        
        // Verificar si el usuario ya está logueado
        HttpSession session = request.getSession(false);
        
        if (session != null && session.getAttribute("userId") != null) {
            // Usuario logueado, mostrar confirmación
            Integer userId = (Integer) session.getAttribute("userId");
            User user = userDAO.findById(userId);
            
            request.setAttribute("transfer", transfer);
            request.setAttribute("user", user);
            request.setAttribute("token", token);
            request.getRequestDispatcher("/view/public/accept-transfer.jsp").forward(request, response);
        } else {
            // Usuario no logueado, guardar token y redirigir a login
            request.getSession(true).setAttribute("transferToken", token);
            request.getSession().setAttribute("redirectAfterLogin", "/accept-transfer?token=" + token);
            request.setAttribute("message", "Por favor inicia sesión o regístrate para aceptar esta transferencia");
            response.sendRedirect(request.getContextPath() + "/login");
        }
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
        
        String token = request.getParameter("token");
        String action = request.getParameter("action");
        
        if (token == null || token.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token inválido");
            return;
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        
        try {
            if ("accept".equals(action)) {
                // Aceptar transferencia
                if (transferDAO.accept(token, userId)) {
                    PetTransferRequest transfer = transferDAO.findByToken(token);
                    session.setAttribute("successMessage", 
                        "¡Felicidades! " + transfer.getPetName() + " ahora es oficialmente tuyo/a 🎉");
                    
                    System.out.println("✅ Transferencia aceptada: " + transfer.getPetName() + 
                                     " → Usuario " + userId);
                    
                    response.sendRedirect(request.getContextPath() + "/user/pets");
                } else {
                    session.setAttribute("errorMessage", "Error al aceptar la transferencia");
                    response.sendRedirect(request.getContextPath() + "/accept-transfer?token=" + token);
                }
                
            } else if ("reject".equals(action)) {
                // Rechazar transferencia
                if (transferDAO.reject(token)) {
                    session.setAttribute("successMessage", "Transferencia rechazada");
                    
                    System.out.println("❌ Transferencia rechazada por usuario " + userId);
                    
                    response.sendRedirect(request.getContextPath() + "/user/panel");
                } else {
                    session.setAttribute("errorMessage", "Error al rechazar la transferencia");
                    response.sendRedirect(request.getContextPath() + "/accept-transfer?token=" + token);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción inválida");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al procesar transferencia: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error al procesar la transferencia");
            response.sendRedirect(request.getContextPath() + "/accept-transfer?token=" + token);
        }
    }
}
