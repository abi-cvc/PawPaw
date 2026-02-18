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

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;

/**
 * Servlet para manejar la solicitud de recuperación de contraseña
 */
@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    private PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAO();
    private EmailService emailService = new EmailService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Mostrar formulario de recuperación
        request.getRequestDispatcher("/view/internalUser/forgot-password.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        
        // Validar que el email no esté vacío
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Por favor ingresa tu email");
            request.getRequestDispatcher("/view/internalUser/forgot-password.jsp").forward(request, response);
            return;
        }
        
        email = email.trim().toLowerCase();
        
        System.out.println("🔑 Solicitud de recuperación para: " + email);
        
        // Buscar usuario por email
        User user = userDAO.findByEmail(email);
        
        // Por seguridad, siempre mostramos el mismo mensaje
        // (para no revelar si el email existe o no)
        String successMessage = "Si el email existe en nuestro sistema, recibirás un enlace de recuperación en los próximos minutos.";
        
        if (user != null && user.getActive()) {
            try {
                // Invalidar tokens anteriores del usuario
                tokenDAO.invalidateUserTokens(user.getIdUser());
                
                // Generar token único y seguro
                String token = generateSecureToken();
                
                // Token expira en 1 hora
                Timestamp expirationDate = new Timestamp(System.currentTimeMillis() + (60 * 60 * 1000));
                
                // Crear token en BD
                PasswordResetToken resetToken = new PasswordResetToken(
                    user.getIdUser(),
                    token,
                    expirationDate
                );
                
                if (tokenDAO.create(resetToken)) {
                    System.out.println("   ✅ Token creado en BD");
                    
                    // Enviar email con token
                    boolean emailSent = emailService.sendPasswordResetEmail(
                        user.getEmail(),
                        user.getNameUser(),
                        token
                    );
                    
                    if (emailSent) {
                        System.out.println("   ✅ Email enviado a: " + email);
                    } else {
                        System.err.println("   ❌ Error al enviar email");
                        // Aun así mostramos mensaje de éxito por seguridad
                    }
                } else {
                    System.err.println("   ❌ Error al crear token en BD");
                }
                
            } catch (Exception e) {
                System.err.println("❌ Error en proceso de recuperación: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("   ⚠️ Email no encontrado o usuario inactivo");
            // Por seguridad, no revelamos que el email no existe
        }
        
        // Siempre mostrar mensaje de éxito
        request.setAttribute("success", successMessage);
        request.setAttribute("emailSent", true);
        request.getRequestDispatcher("/view/internalUser/forgot-password.jsp").forward(request, response);
    }
    
    /**
     * Genera un token seguro aleatorio
     */
    private String generateSecureToken() {
        try {
            // Generar 32 bytes aleatorios
            SecureRandom random = new SecureRandom();
            byte[] bytes = new byte[32];
            random.nextBytes(bytes);
            
            // Crear hash SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(bytes);
            
            // Convertir a Base64 URL-safe
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            
        } catch (Exception e) {
            // Fallback: usar timestamp + random
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString((System.currentTimeMillis() + "" + Math.random()).getBytes());
        }
    }
}