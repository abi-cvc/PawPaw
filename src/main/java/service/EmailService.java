package service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Servicio para enviar emails usando Brevo API v3 (HTTP)
 * Esta versión NO usa SMTP, por lo que funciona en Railway
 */
public class EmailService {
    
    private static final String API_KEY = System.getenv("BREVO_API_KEY");
    private static final String FROM_EMAIL = System.getenv("BREVO_FROM_EMAIL");
    private static final String FROM_NAME = System.getenv("BREVO_FROM_NAME");
    private static final String APP_BASE_URL = System.getenv("APP_BASE_URL");
    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";
    
    /**
     * Envía email de recuperación de contraseña usando Brevo API
     */
    public boolean sendPasswordResetEmail(String toEmail, String toName, String resetToken) {
        System.out.println("📧 Iniciando envío de email vía Brevo API");
        System.out.println("   Destinatario: " + toEmail);
        
        // Verificar configuración
        if (API_KEY == null || FROM_EMAIL == null || APP_BASE_URL == null) {
            System.err.println("❌ ERROR: Variables de entorno no configuradas");
            System.err.println("   API_KEY: " + (API_KEY != null ? "✅" : "❌ NULL"));
            System.err.println("   FROM_EMAIL: " + (FROM_EMAIL != null ? "✅" : "❌ NULL"));
            System.err.println("   APP_BASE_URL: " + (APP_BASE_URL != null ? "✅" : "❌ NULL"));
            return false;
        }
        
        try {
            String resetUrl = APP_BASE_URL + "/reset-password?token=" + resetToken;
            
            // Construir JSON del email
            String htmlContent = buildPasswordResetEmailHtml(toName, resetUrl);
            String textContent = "Hola " + toName + ",\n\n" +
                    "Recibimos una solicitud para restablecer tu contraseña en PawPaw.\n\n" +
                    "Para crear una nueva contraseña, visita:\n" +
                    resetUrl + "\n\n" +
                    "Este enlace expirará en 1 hora.\n\n" +
                    "Si no solicitaste restablecer tu contraseña, ignora este email.\n\n" +
                    "Saludos,\n" +
                    "Equipo PawPaw";
            
            String jsonPayload = buildJsonPayload(toEmail, toName, "Recuperar contraseña - PawPaw", htmlContent, textContent);
            
            System.out.println("   Enviando request a Brevo API...");
            
            // Crear conexión HTTP
            URL url = new URL(BREVO_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // Configurar request
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("api-key", API_KEY);
            conn.setRequestProperty("content-type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(15000); // 15 segundos
            conn.setReadTimeout(15000);
            
            // Enviar JSON
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Leer respuesta
            int responseCode = conn.getResponseCode();
            
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("✅ Email enviado exitosamente vía Brevo API");
                System.out.println("   Response code: " + responseCode);
                return true;
            } else {
                System.err.println("❌ Error en Brevo API:");
                System.err.println("   Response code: " + responseCode);
                System.err.println("   Message: " + conn.getResponseMessage());
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Excepción al enviar email:");
            System.err.println("   Tipo: " + e.getClass().getName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Construye el payload JSON para Brevo API
     */
    private String buildJsonPayload(String toEmail, String toName, String subject, String htmlContent, String textContent) {
        // Escapar comillas en el contenido
        htmlContent = escapeJson(htmlContent);
        textContent = escapeJson(textContent);
        toName = escapeJson(toName);
        subject = escapeJson(subject);
        String fromName = escapeJson(FROM_NAME != null ? FROM_NAME : "PawPaw");
        
        return "{" +
                "\"sender\":{" +
                    "\"name\":\"" + fromName + "\"," +
                    "\"email\":\"" + FROM_EMAIL + "\"" +
                "}," +
                "\"to\":[{" +
                    "\"email\":\"" + toEmail + "\"," +
                    "\"name\":\"" + toName + "\"" +
                "}]," +
                "\"subject\":\"" + subject + "\"," +
                "\"htmlContent\":\"" + htmlContent + "\"," +
                "\"textContent\":\"" + textContent + "\"" +
                "}";
    }
    
    /**
     * Escapa caracteres especiales para JSON
     */
    private String escapeJson(String str) {
        if (str == null) return "";
        return str
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
    
    /**
     * Construye el HTML del email de recuperación de contraseña
     */
    private String buildPasswordResetEmailHtml(String userName, String resetUrl) {
        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Recuperar Contraseña - PawPaw</title>" +
                "</head>" +
                "<body style='margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, \"Helvetica Neue\", Arial, sans-serif; background-color: #f5f5f5;'>" +
                "    <table width='100%' cellpadding='0' cellspacing='0' style='background-color: #f5f5f5; padding: 40px 20px;'>" +
                "        <tr>" +
                "            <td align='center'>" +
                "                <table width='600' cellpadding='0' cellspacing='0' style='background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>" +
                "                    " +
                "                    <!-- Header -->" +
                "                    <tr>" +
                "                        <td style='padding: 40px; text-align: center; background: linear-gradient(135deg, #C38154 0%, #884A39 100%); border-radius: 12px 12px 0 0;'>" +
                "                            <h1 style='margin: 0; color: #ffffff; font-size: 32px; font-weight: 700;'>🐾 PawPaw</h1>" +
                "                        </td>" +
                "                    </tr>" +
                "                    " +
                "                    <!-- Content -->" +
                "                    <tr>" +
                "                        <td style='padding: 40px;'>" +
                "                            <h2 style='margin: 0 0 20px 0; color: #884A39; font-size: 24px;'>Hola " + userName + ",</h2>" +
                "                            " +
                "                            <p style='margin: 0 0 20px 0; color: #666; font-size: 16px; line-height: 1.6;'>" +
                "                                Recibimos una solicitud para restablecer tu contraseña en PawPaw." +
                "                            </p>" +
                "                            " +
                "                            <p style='margin: 0 0 30px 0; color: #666; font-size: 16px; line-height: 1.6;'>" +
                "                                Para crear una nueva contraseña, haz clic en el siguiente botón:" +
                "                            </p>" +
                "                            " +
                "                            <!-- Button -->" +
                "                            <table width='100%' cellpadding='0' cellspacing='0'>" +
                "                                <tr>" +
                "                                    <td align='center'>" +
                "                                        <a href='" + resetUrl + "' style='display: inline-block; padding: 16px 40px; background: linear-gradient(135deg, #884A39, #C38154); color: #ffffff; text-decoration: none; border-radius: 8px; font-weight: 600; font-size: 16px;'>Restablecer Contraseña</a>" +
                "                                    </td>" +
                "                                </tr>" +
                "                            </table>" +
                "                            " +
                "                            <p style='margin: 30px 0 0 0; color: #999; font-size: 14px; line-height: 1.6;'>" +
                "                                O copia y pega este enlace en tu navegador:" +
                "                            </p>" +
                "                            " +
                "                            <p style='margin: 10px 0 0 0; padding: 12px; background: #f5f5f5; border-radius: 6px; color: #666; font-size: 14px; word-break: break-all;'>" +
                "                                " + resetUrl +
                "                            </p>" +
                "                            " +
                "                            <div style='margin-top: 30px; padding: 20px; background: #fff3cd; border-left: 4px solid #ffc107; border-radius: 6px;'>" +
                "                                <p style='margin: 0; color: #856404; font-size: 14px; line-height: 1.6;'>" +
                "                                    ⚠️ <strong>Importante:</strong> Este enlace expirará en <strong>1 hora</strong>." +
                "                                </p>" +
                "                            </div>" +
                "                            " +
                "                            <p style='margin: 30px 0 0 0; color: #999; font-size: 14px; line-height: 1.6;'>" +
                "                                Si no solicitaste restablecer tu contraseña, puedes ignorar este email de forma segura." +
                "                            </p>" +
                "                        </td>" +
                "                    </tr>" +
                "                    " +
                "                    <!-- Footer -->" +
                "                    <tr>" +
                "                        <td style='padding: 30px; text-align: center; background: #f9f9f9; border-radius: 0 0 12px 12px;'>" +
                "                            <p style='margin: 0 0 10px 0; color: #999; font-size: 14px;'>" +
                "                                Este email fue enviado por <strong>PawPaw</strong>" +
                "                            </p>" +
                "                            <p style='margin: 0; color: #ccc; font-size: 12px;'>" +
                "                                La seguridad de tu mascota, a un escaneo de distancia" +
                "                            </p>" +
                "                        </td>" +
                "                    </tr>" +
                "                </table>" +
                "            </td>" +
                "        </tr>" +
                "    </table>" +
                "</body>" +
                "</html>";
    }
    
    /**
     * Envía email de confirmación de cambio de contraseña
     */
    public boolean sendPasswordChangedEmail(String toEmail, String toName) {
        System.out.println("📧 Enviando email de confirmación vía Brevo API");
        
        try {
            String htmlContent = "<!DOCTYPE html>" +
                    "<html lang='es'>" +
                    "<body style='font-family: Arial, sans-serif; padding: 20px;'>" +
                    "    <h2>Hola " + toName + ",</h2>" +
                    "    <p>Tu contraseña ha sido actualizada exitosamente.</p>" +
                    "    <p>Si no realizaste este cambio, por favor contacta a soporte inmediatamente.</p>" +
                    "    <p>Saludos,<br>Equipo PawPaw</p>" +
                    "</body>" +
                    "</html>";
            
            String textContent = "Hola " + toName + ",\n\n" +
                    "Tu contraseña ha sido actualizada exitosamente.\n\n" +
                    "Si no realizaste este cambio, por favor contacta a soporte inmediatamente.\n\n" +
                    "Saludos,\nEquipo PawPaw";
            
            String jsonPayload = buildJsonPayload(toEmail, toName, "Contraseña actualizada - PawPaw", htmlContent, textContent);
            
            URL url = new URL(BREVO_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("api-key", API_KEY);
            conn.setRequestProperty("content-type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("✅ Email de confirmación enviado exitosamente");
                return true;
            } else {
                System.err.println("❌ Error al enviar email de confirmación: " + responseCode);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al enviar email de confirmación: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Envía email de notificación administrativa a pawpawsystem@gmail.com
     */
    public boolean sendNotificationEmail(String toEmail, String toName, String subject, String message) {
        System.out.println("📧 Enviando notificación administrativa a " + toEmail);
        
        if (API_KEY == null || FROM_EMAIL == null) {
            System.err.println("❌ ERROR: Variables de entorno no configuradas");
            return false;
        }
        
        try {
            // Construir HTML simple pero claro
            String htmlContent = "<!DOCTYPE html>" +
                    "<html lang='es'>" +
                    "<head>" +
                    "    <meta charset='UTF-8'>" +
                    "</head>" +
                    "<body style='font-family: Arial, sans-serif; padding: 20px; background-color: #f5f5f5;'>" +
                    "    <div style='max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>" +
                    "        <h2 style='color: #884A39; margin-top: 0;'>🔔 Notificación Administrativa</h2>" +
                    "        <div style='background: #f9f9f9; padding: 20px; border-radius: 8px; border-left: 4px solid #884A39;'>" +
                    "            <pre style='font-family: monospace; font-size: 14px; line-height: 1.6; margin: 0; white-space: pre-wrap; word-wrap: break-word;'>" + 
                    escapeHtml(message) + 
                    "</pre>" +
                    "        </div>" +
                    "        <p style='margin-top: 20px; color: #999; font-size: 12px;'>" +
                    "            Este es un email automático de PawPaw. Solo el administrador principal recibe estas notificaciones." +
                    "        </p>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";
            
            String jsonPayload = buildJsonPayload(toEmail, toName, subject, htmlContent, message);
            
            URL url = new URL(BREVO_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("api-key", API_KEY);
            conn.setRequestProperty("content-type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = conn.getResponseCode();
            
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("✅ Notificación administrativa enviada exitosamente");
                return true;
            } else {
                System.err.println("❌ Error al enviar notificación: " + responseCode);
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al enviar notificación: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Escapa caracteres HTML para evitar problemas en el email
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
    
    /**
     * Envía el email de invitación de adopción al adoptante.
     * Usa el mismo mecanismo Brevo HTTP que los demás métodos del servicio.
     *
     * @param adopterEmail   Email del adoptante
     * @param adopterName    Nombre del adoptante
     * @param petName        Nombre de la mascota
     * @param foundationName Nombre de la fundación
     * @param acceptUrl      URL con token: https://...app.../accept-transfer?token=...
     */
    public boolean sendTransferEmail(String adopterEmail, String adopterName,
                                     String petName, String foundationName,
                                     String acceptUrl) {
        System.out.println("📧 Enviando email de transferencia a " + adopterEmail);
 
        if (API_KEY == null || FROM_EMAIL == null) {
            System.err.println("❌ ERROR: Variables de entorno no configuradas para email de transferencia");
            return false;
        }
 
        try {
            String subject = "🐾 " + foundationName + " quiere transferirte a " + petName + " - PawPaw";
 
            String htmlContent = buildTransferEmailHtml(adopterName, foundationName, petName, acceptUrl);
 
            String textContent = "Hola " + adopterName + ",\n\n"
                    + foundationName + " te ha seleccionado como el nuevo dueño de " + petName + ".\n\n"
                    + "Para aceptar la adopción, ingresa aquí:\n"
                    + acceptUrl + "\n\n"
                    + "Este enlace es válido por 7 días.\n\n"
                    + "Si no esperabas este mensaje, puedes ignorarlo.\n\n"
                    + "Equipo PawPaw";
 
            String jsonPayload = buildJsonPayload(adopterEmail, adopterName, subject, htmlContent, textContent);
 
            URL url = new URL(BREVO_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("api-key", API_KEY);
            conn.setRequestProperty("content-type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
 
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
 
            int responseCode = conn.getResponseCode();
 
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("✅ Email de transferencia enviado exitosamente a " + adopterEmail);
                return true;
            } else {
                System.err.println("❌ Error al enviar email de transferencia: " + responseCode + " - " + conn.getResponseMessage());
                return false;
            }
 
        } catch (Exception e) {
            System.err.println("❌ Excepción al enviar email de transferencia: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
 
    /**
     * Construye el HTML del email de invitación de adopción
     */
    private String buildTransferEmailHtml(String adopterName, String foundationName,
                                           String petName, String acceptUrl) {
        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head><meta charset='UTF-8'></head>" +
                "<body style='margin:0;padding:0;font-family:-apple-system,BlinkMacSystemFont,\"Segoe UI\",Roboto,Arial,sans-serif;background-color:#f5f5f5;'>" +
                "<table width='100%' cellpadding='0' cellspacing='0' style='background-color:#f5f5f5;padding:40px 20px;'>" +
                "<tr><td align='center'>" +
                "<table width='600' cellpadding='0' cellspacing='0' style='background-color:#ffffff;border-radius:12px;box-shadow:0 4px 12px rgba(0,0,0,0.1);'>" +
 
                // Header
                "<tr><td style='padding:40px;text-align:center;background:linear-gradient(135deg,#E07B39 0%,#c85e1e 100%);border-radius:12px 12px 0 0;'>" +
                "<h1 style='margin:0;color:#ffffff;font-size:32px;font-weight:700;'>🐾 PawPaw</h1>" +
                "<p style='margin:10px 0 0;color:#ffe8d6;font-size:16px;'>Sistema de identificación de mascotas</p>" +
                "</td></tr>" +
 
                // Body
                "<tr><td style='padding:40px;'>" +
                "<h2 style='margin:0 0 16px;color:#1a1a1a;font-size:22px;'>¡Hola, " + escapeHtml(adopterName) + "! 👋</h2>" +
                "<p style='margin:0 0 16px;color:#555;font-size:15px;line-height:1.6;'>" +
                "La fundación <strong style='color:#c85e1e;'>" + escapeHtml(foundationName) + "</strong> " +
                "te ha seleccionado como el/la nuevo/a dueño/a de <strong>" + escapeHtml(petName) + "</strong>.</p>" +
 
                "<div style='background:#fff7ed;border-left:4px solid #E07B39;border-radius:0 8px 8px 0;padding:16px 20px;margin:20px 0;'>" +
                "<p style='margin:0;color:#7c4516;font-size:14px;line-height:1.6;'>" +
                "🐾 Al aceptar, el perfil QR de <strong>" + escapeHtml(petName) + "</strong> se transferirá " +
                "automáticamente a tu cuenta PawPaw. El mismo código en su placa seguirá funcionando, " +
                "pero ahora mostrará <strong>tu</strong> información de contacto.</p>" +
                "</div>" +
 
                "<p style='margin:0 0 8px;color:#555;font-size:15px;line-height:1.6;'>" +
                "Haz clic en el botón para aceptar la adopción:</p>" +
 
                "<div style='text-align:center;margin:30px 0;'>" +
                "<a href='" + acceptUrl + "' style='background:#E07B39;color:#ffffff;padding:14px 32px;" +
                "border-radius:8px;text-decoration:none;font-size:16px;font-weight:700;display:inline-block;'>" +
                "✅ Aceptar Adopción</a>" +
                "</div>" +
 
                "<p style='margin:0;color:#999;font-size:13px;line-height:1.5;'>" +
                "⏳ Este enlace es válido por 7 días.<br>" +
                "Si no esperabas este mensaje, puedes ignorarlo sin problemas.</p>" +
                "</td></tr>" +
 
                // Footer
                "<tr><td style='padding:24px 40px;text-align:center;border-top:1px solid #f0f0f0;'>" +
                "<p style='margin:0;color:#aaa;font-size:12px;'>" +
                "© PawPaw — La seguridad de tu mascota, a un escaneo de distancia.</p>" +
                "</td></tr>" +
 
                "</table></td></tr></table>" +
                "</body></html>";
    }
}