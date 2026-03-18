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
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servlet para manejar la solicitud de recuperación de contraseña
 */
@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    private PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAO();
    private EmailService emailService = new EmailService();

    // SEC-013: Rate limiting por IP — máximo 5 intentos por minuto
    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_MS = 60_000; // 1 minuto
    private static final ConcurrentHashMap<String, long[]> rateLimitMap = new ConcurrentHashMap<>();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Mostrar formulario de recuperación
        request.getRequestDispatcher("/view/internalUser/forgot-password.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // SEC-013: Rate limiting por IP
        String clientIp = request.getRemoteAddr();
        if (isRateLimited(clientIp)) {
            request.setAttribute("error", "Demasiados intentos. Por favor espera un minuto antes de intentar de nuevo.");
            request.getRequestDispatcher("/view/internalUser/forgot-password.jsp").forward(request, response);
            return;
        }

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
     * SEC-013: Verifica si una IP ha excedido el límite de intentos.
     * Usa ventana deslizante con array de timestamps.
     */
    private boolean isRateLimited(String ip) {
        long now = System.currentTimeMillis();
        long[] attempts = rateLimitMap.compute(ip, (key, existing) -> {
            if (existing == null) {
                long[] arr = new long[MAX_ATTEMPTS];
                arr[0] = now;
                return arr;
            }
            // Contar intentos dentro de la ventana
            int count = 0;
            for (long t : existing) {
                if (now - t < WINDOW_MS) count++;
            }
            if (count >= MAX_ATTEMPTS) {
                return existing; // No registrar, está limitado
            }
            // Buscar slot libre o el más viejo para reemplazar
            int oldestIdx = 0;
            long oldestTime = Long.MAX_VALUE;
            for (int i = 0; i < existing.length; i++) {
                if (existing[i] == 0 || now - existing[i] >= WINDOW_MS) {
                    existing[i] = now;
                    return existing;
                }
                if (existing[i] < oldestTime) {
                    oldestTime = existing[i];
                    oldestIdx = i;
                }
            }
            existing[oldestIdx] = now;
            return existing;
        });

        int count = 0;
        for (long t : attempts) {
            if (now - t < WINDOW_MS) count++;
        }
        return count >= MAX_ATTEMPTS;
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