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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;

/**
 * Servlet para reenviar enlace de recuperación de contraseña
 */
@WebServlet("/resend-password-reset")
public class ResendPasswordResetServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    private PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAO();
    private EmailService emailService = new EmailService();
    
    // Cooldown de 60 segundos entre reenvíos
    private static final long COOLDOWN_MS = 60 * 1000; // 1 minuto
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        HttpSession session = request.getSession();
        
        // Validar email
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email no proporcionado");
            request.getRequestDispatcher("/view/internalUser/forgot-password.jsp").forward(request, response);
            return;
        }
        
        email = email.trim().toLowerCase();
        
        // Verificar cooldown (anti-spam)
        Long lastResendTime = (Long) session.getAttribute("lastResendTime_" + email);
        long currentTime = System.currentTimeMillis();
        
        if (lastResendTime != null && (currentTime - lastResendTime) < COOLDOWN_MS) {
            long remainingSeconds = (COOLDOWN_MS - (currentTime - lastResendTime)) / 1000;
            request.setAttribute("error", "Por favor espera " + remainingSeconds + " segundos antes de reenviar.");
            request.setAttribute("email", email);
            request.setAttribute("emailSent", true);
            request.getRequestDispatcher("/view/internalUser/forgot-password.jsp").forward(request, response);
            return;
        }
        
        System.out.println("🔄 Reenvío de token solicitado para: " + email);
        
        // Buscar usuario
        User user = userDAO.findByEmail(email);
        
        // Mensaje genérico (no revelar si el email existe)
        String successMessage = "Si el email existe en nuestro sistema, recibirás un nuevo enlace de recuperación.";
        
        if (user != null && user.getActive()) {
            try {
                // Invalidar tokens anteriores
                tokenDAO.invalidateUserTokens(user.getIdUser());
                
                // Generar nuevo token
                String token = generateSecureToken();
                Timestamp expirationDate = new Timestamp(System.currentTimeMillis() + (60 * 60 * 1000));
                
                PasswordResetToken resetToken = new PasswordResetToken(
                    user.getIdUser(),
                    token,
                    expirationDate
                );
                
                if (tokenDAO.create(resetToken)) {
                    System.out.println("   ✅ Nuevo token creado");
                    
                    // Enviar email
                    boolean emailSent = emailService.sendPasswordResetEmail(
                        user.getEmail(),
                        user.getNameUser(),
                        token
                    );
                    
                    if (emailSent) {
                        System.out.println("   ✅ Email reenviado a: " + email);
                        
                        // Actualizar timestamp de último reenvío
                        session.setAttribute("lastResendTime_" + email, currentTime);
                    } else {
                        System.err.println("   ❌ Error al reenviar email");
                    }
                }
                
            } catch (Exception e) {
                System.err.println("❌ Error al reenviar token: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("   ⚠️ Email no encontrado o usuario inactivo");
        }
        
        // Siempre mostrar mensaje de éxito
        request.setAttribute("success", successMessage);
        request.setAttribute("resent", true);
        request.setAttribute("email", email);
        request.setAttribute("emailSent", true);
        request.getRequestDispatcher("/view/internalUser/forgot-password.jsp").forward(request, response);
    }
    
    /**
     * Genera un token seguro aleatorio
     */
    private String generateSecureToken() {
        try {
            SecureRandom random = new SecureRandom();
            byte[] bytes = new byte[32];
            random.nextBytes(bytes);
            
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(bytes);
            
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            
        } catch (Exception e) {
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString((System.currentTimeMillis() + "" + Math.random()).getBytes());
        }
    }
}