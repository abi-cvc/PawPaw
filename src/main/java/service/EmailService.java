package service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

/**
 * Servicio para enviar emails usando Brevo SMTP
 */
public class EmailService {
    
    private static final String SMTP_HOST = "smtp-relay.brevo.com";
    private static final String SMTP_PORT = "465"; // CAMBIADO: Puerto SSL en vez de STARTTLS
    private static final String SMTP_USERNAME = System.getenv("BREVO_SMTP_USERNAME");
    private static final String SMTP_PASSWORD = System.getenv("BREVO_API_KEY");
    private static final String FROM_EMAIL = System.getenv("BREVO_FROM_EMAIL");
    private static final String FROM_NAME = System.getenv("BREVO_FROM_NAME");
    private static final String APP_BASE_URL = System.getenv("APP_BASE_URL");
    
    /**
     * Envía email de recuperación de contraseña
     */
    public boolean sendPasswordResetEmail(String toEmail, String toName, String resetToken) {
        System.out.println("📧 Iniciando envío de email de recuperación");
        System.out.println("   Destinatario: " + toEmail);
        
        // Verificar configuración
        if (SMTP_USERNAME == null || SMTP_PASSWORD == null || FROM_EMAIL == null) {
            System.err.println("❌ ERROR: Variables de entorno no configuradas");
            System.err.println("   SMTP_USERNAME: " + (SMTP_USERNAME != null ? "✅" : "❌ NULL"));
            System.err.println("   SMTP_PASSWORD: " + (SMTP_PASSWORD != null ? "✅" : "❌ NULL"));
            System.err.println("   FROM_EMAIL: " + (FROM_EMAIL != null ? "✅" : "❌ NULL"));
            return false;
        }
        
        try {
            String resetUrl = APP_BASE_URL + "/reset-password?token=" + resetToken;
            
            // Configurar propiedades SMTP con SSL (puerto 465)
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true"); // SSL directo
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
            // TIMEOUTS importantes para evitar que se cuelgue
            props.put("mail.smtp.connectiontimeout", "10000"); // 10 segundos
            props.put("mail.smtp.timeout", "10000"); // 10 segundos
            props.put("mail.smtp.writetimeout", "10000"); // 10 segundos
            
            System.out.println("   Conectando a SMTP: " + SMTP_HOST + ":" + SMTP_PORT);
            
            // Crear sesión
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            });
            
            // Habilitar debug solo si es necesario
            // session.setDebug(true);
            
            System.out.println("   Creando mensaje...");
            
            // Crear mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Recuperar contraseña - PawPaw");
            
            // HTML del email
            String htmlContent = buildPasswordResetEmailHtml(toName, resetUrl);
            message.setContent(htmlContent, "text/html; charset=UTF-8");
            
            System.out.println("   Enviando mensaje...");
            
            // Enviar
            Transport.send(message);
            
            System.out.println("✅ Email enviado exitosamente a: " + toEmail);
            return true;
            
        } catch (MessagingException e) {
            System.err.println("❌ MessagingException al enviar email:");
            System.err.println("   Tipo: " + e.getClass().getSimpleName());
            System.err.println("   Mensaje: " + e.getMessage());
            
            // Detallar el error específico
            if (e instanceof AuthenticationFailedException) {
                System.err.println("   Causa: Credenciales incorrectas");
                System.err.println("   Verifica BREVO_SMTP_USERNAME y BREVO_API_KEY");
            } else if (e.getMessage() != null && e.getMessage().contains("timeout")) {
                System.err.println("   Causa: Timeout de conexión");
                System.err.println("   El servidor SMTP no responde");
            } else if (e.getMessage() != null && e.getMessage().contains("Connection refused")) {
                System.err.println("   Causa: Conexión rechazada");
                System.err.println("   Verifica el host y puerto SMTP");
            }
            
            e.printStackTrace();
            return false;
            
        } catch (Exception e) {
            System.err.println("❌ Error inesperado al enviar email:");
            System.err.println("   Tipo: " + e.getClass().getName());
            System.err.println("   Mensaje: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
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
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true"); // SSL directo
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.connectiontimeout", "10000");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.writetimeout", "10000");
            
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Contraseña actualizada - PawPaw");
            
            String htmlContent = "<!DOCTYPE html>" +
                    "<html lang='es'>" +
                    "<body style='font-family: Arial, sans-serif; padding: 20px;'>" +
                    "    <h2>Hola " + toName + ",</h2>" +
                    "    <p>Tu contraseña ha sido actualizada exitosamente.</p>" +
                    "    <p>Si no realizaste este cambio, por favor contacta a soporte inmediatamente.</p>" +
                    "    <p>Saludos,<br>Equipo PawPaw</p>" +
                    "</body>" +
                    "</html>";
            
            message.setContent(htmlContent, "text/html; charset=UTF-8");
            Transport.send(message);
            
            System.out.println("✅ Email de confirmación enviado a: " + toEmail);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error al enviar email de confirmación: " + e.getMessage());
            return false;
        }
    }
}