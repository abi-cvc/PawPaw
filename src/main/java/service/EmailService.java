package service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

/**
 * Servicio para enviar emails usando Brevo SMTP
 * (Alternativa más simple y estable que el SDK)
 */
public class EmailService {
    
    private static final String SMTP_HOST = "smtp-relay.brevo.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USERNAME = "pawpawsystem@gmail.com"; // Tu email en Brevo
    private static final String SMTP_PASSWORD = System.getenv("BREVO_API_KEY"); // Tu API Key
    private static final String FROM_EMAIL = "pawpawsystem@gmail.com";
    private static final String FROM_NAME = System.getenv("BREVO_FROM_NAME");
    private static final String APP_BASE_URL = System.getenv("APP_BASE_URL");
    
    /**
     * Envía email de recuperación de contraseña
     */
    public boolean sendPasswordResetEmail(String toEmail, String toName, String resetToken) {
        try {
            // Construir URL de reset
            String resetUrl = APP_BASE_URL + "/reset-password?token=" + resetToken;
            
            // Configurar propiedades SMTP
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
            // Crear sesión
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            });
            
            // Crear mensaje
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, FROM_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Recuperar contraseña - PawPaw");
            
            // HTML del email
            String htmlContent = buildPasswordResetEmailHtml(toName, resetUrl);
            message.setContent(htmlContent, "text/html; charset=UTF-8");
            
            // Enviar
            Transport.send(message);
            
            System.out.println("✅ Email enviado exitosamente a: " + toEmail);
            return true;
            
        } catch (Exception e) {
            System.err.println("❌ Error al enviar email: " + e.getMessage());
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
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            
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