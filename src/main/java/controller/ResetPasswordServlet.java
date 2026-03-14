package controller;

import model.dao.UserDAO;
import model.dao.PasswordResetTokenDAO;
import model.entity.User;
import model.entity.PasswordResetToken;
import service.EmailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

/**
 * Servlet para manejar el reseteo de contraseña con token
 */
@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    private PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAO();
    private EmailService emailService = new EmailService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        
        // Validar que el token esté presente
        if (token == null || token.trim().isEmpty()) {
            request.setAttribute("error", "Token inválido o expirado");
            request.getRequestDispatcher("/view/internalUser/reset-password.jsp").forward(request, response);
            return;
        }
        
        System.out.println("🔑 Validando token: " + token.substring(0, 10) + "...");
        
        // Buscar token en BD
        PasswordResetToken resetToken = tokenDAO.findByToken(token);
        
        if (resetToken == null) {
            System.out.println("   ❌ Token no encontrado");
            request.setAttribute("error", "El enlace de recuperación no es válido");
            request.getRequestDispatcher("/view/internalUser/reset-password.jsp").forward(request, response);
            return;
        }
        
        // Verificar si el token es válido
        if (!resetToken.isValid()) {
            System.out.println("   ❌ Token expirado o ya usado");
            
            String errorMsg = resetToken.getUsed() 
                ? "Este enlace ya fue utilizado" 
                : "Este enlace ha expirado. Solicita uno nuevo.";
            
            request.setAttribute("error", errorMsg);
            request.setAttribute("tokenExpired", true);
            request.getRequestDispatcher("/view/internalUser/reset-password.jsp").forward(request, response);
            return;
        }
        
        System.out.println("   ✅ Token válido");
        
        // Token válido - mostrar formulario
        request.setAttribute("token", token);
        request.setAttribute("tokenValid", true);
        request.getRequestDispatcher("/view/internalUser/reset-password.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validaciones
        if (token == null || token.trim().isEmpty()) {
            request.setAttribute("error", "Token inválido");
            request.getRequestDispatcher("/view/internalUser/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (newPassword == null || newPassword.isEmpty()) {
            request.setAttribute("error", "Por favor ingresa una contraseña");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/view/internalUser/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (newPassword.length() < 6) {
            request.setAttribute("error", "La contraseña debe tener al menos 6 caracteres");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/view/internalUser/reset-password.jsp").forward(request, response);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Las contraseñas no coinciden");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/view/internalUser/reset-password.jsp").forward(request, response);
            return;
        }
        
        System.out.println("🔑 Intentando resetear contraseña con token");
        
        // Buscar token
        PasswordResetToken resetToken = tokenDAO.findByToken(token);
        
        if (resetToken == null || !resetToken.isValid()) {
            request.setAttribute("error", "El enlace de recuperación no es válido o ha expirado");
            request.getRequestDispatcher("/view/internalUser/reset-password.jsp").forward(request, response);
            return;
        }
        
        // Buscar usuario
        User user = userDAO.findById(resetToken.getIdUser());
        
        if (user == null) {
            request.setAttribute("error", "Usuario no encontrado");
            request.getRequestDispatcher("/view/internalUser/reset-password.jsp").forward(request, response);
            return;
        }
        
        try {
            // Hashear nueva contraseña
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            
            // Actualizar contraseña del usuario
            user.setPassword(hashedPassword);
            
            if (userDAO.resetPassword(user.getIdUser(), hashedPassword)) {
                System.out.println("   ✅ Contraseña actualizada para: " + user.getEmail());
                
                // Marcar token como usado
                tokenDAO.markAsUsed(token);
                
                // Invalidar otros tokens del usuario
                tokenDAO.invalidateUserTokens(user.getIdUser());
                
                // Enviar email de confirmación
                emailService.sendPasswordChangedEmail(user.getEmail(), user.getNameUser());
                
                // Redirigir a login con mensaje de éxito
                response.sendRedirect(request.getContextPath() + "/login?passwordChanged=true");
                
            } else {
                System.err.println("   ❌ Error al actualizar contraseña en BD");
                request.setAttribute("error", "Error al actualizar la contraseña. Intenta nuevamente.");
                request.setAttribute("token", token);
                request.getRequestDispatcher("/view/internalUser/reset-password.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al resetear contraseña: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar la solicitud. Intenta nuevamente.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("/view/internalUser/reset-password.jsp").forward(request, response);
        }
    }
}