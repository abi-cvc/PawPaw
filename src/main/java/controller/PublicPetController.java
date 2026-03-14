package controller;

import model.dao.PetDAO;
import model.dao.UserDAO;
import model.dao.QRCodeDAO;
import model.dao.ScanLogDAO;
import model.dao.PetContactMessageDAO;
import model.entity.Pet;
import model.entity.User;
import model.entity.QRcode;
import model.entity.ScanLog;
import model.entity.PetContactMessage;
import service.EmailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controlador para vista pública de mascotas (QR Code)
 * Maneja visualización y envío de mensajes
 */
@WebServlet("/pet/*")
public class PublicPetController extends HttpServlet {
    
    private PetDAO petDAO = new PetDAO();
    private UserDAO userDAO = new UserDAO();
    private QRCodeDAO qrCodeDAO = new QRCodeDAO();
    private ScanLogDAO scanLogDAO = new ScanLogDAO();
    private PetContactMessageDAO messageDAO = new PetContactMessageDAO();
    private EmailService emailService = new EmailService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        try {
            String petIdStr = pathInfo.substring(1);
            Integer petId = Integer.parseInt(petIdStr);
            
            Pet pet = petDAO.findById(petId);
            
            if (pet == null) {
                request.getRequestDispatcher("/view/externalUser/pet-not-found.jsp").forward(request, response);
                return;
            }
            
            User owner = userDAO.findById(pet.getIdUser());
            String ownerName = owner != null ? owner.getNameUser() : "Dueño";
            
            request.setAttribute("pet", pet);
            request.setAttribute("owner", owner);
            request.setAttribute("ownerName", ownerName);
            
            System.out.println("📱 Vista pública de mascota: " + pet.getNamePet() + " (ID: " + petId + ")");

            // BUG-003: Registrar escaneo de QR en scan_logs
            try {
                QRcode qr = qrCodeDAO.findByPetId(petId);
                if (qr != null) {
                    qrCodeDAO.incrementScanCount(qr.getIdQR());
                    ScanLog scanLog = new ScanLog(
                            qr.getIdQR(),
                            request.getRemoteAddr(),
                            request.getHeader("User-Agent"),
                            null
                    );
                    scanLogDAO.create(scanLog);
                }
            } catch (Exception e) {
                System.err.println("Error al registrar escaneo: " + e.getMessage());
            }

            request.getRequestDispatcher("/view/externalUser/pet-public.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("send-message".equals(action)) {
            handleSendMessage(request, response);
        } else if ("report-found".equals(action)) {
            handleReportFound(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
    
    /**
     * Maneja el envío de mensaje al dueño
     */
    private void handleSendMessage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String petIdStr = request.getParameter("petId");
            String senderName = request.getParameter("senderName");
            String senderPhone = request.getParameter("senderPhone");
            String message = request.getParameter("message");
            
            if (petIdStr == null || senderName == null || senderPhone == null || message == null ||
                senderName.trim().isEmpty() || senderPhone.trim().isEmpty() || message.trim().isEmpty()) {
                
                redirectWithError(request, response, petIdStr, "Por favor completa todos los campos");
                return;
            }
            
            Integer petId = Integer.parseInt(petIdStr);
            Pet pet = petDAO.findById(petId);
            
            if (pet == null) {
                response.sendRedirect(request.getContextPath() + "/");
                return;
            }
            
            User owner = userDAO.findById(pet.getIdUser());
            
            if (owner == null) {
                redirectWithError(request, response, petIdStr, "No se pudo contactar al dueño");
                return;
            }
            
            // NUEVO: Guardar mensaje en BD
            PetContactMessage contactMessage = new PetContactMessage(
                pet.getIdPet(),
                pet.getIdUser(),
                senderName,
                senderPhone,
                message
            );
            
            boolean savedInDB = messageDAO.create(contactMessage);
            
            // Construir email
            String subject = "🐾 Mensaje sobre " + pet.getNamePet() + " - PawPaw";
            String emailBody = "Hola " + owner.getNameUser() + ",\n\n" +
                             "Has recibido un mensaje sobre tu mascota " + pet.getNamePet() + ":\n\n" +
                             "━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                             "DE: " + senderName + "\n" +
                             "TELÉFONO: " + senderPhone + "\n" +
                             "━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                             "MENSAJE:\n" +
                             message + "\n\n" +
                             "━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                             "Por favor, contacta a " + senderName + " al número " + senderPhone + " " +
                             "para coordinar.\n\n" +
                             "También puedes ver este mensaje en tu panel de PawPaw:\n" +
                             request.getScheme() + "://" + request.getServerName() + 
                             (request.getServerPort() != 80 && request.getServerPort() != 443 
                                 ? ":" + request.getServerPort() : "") +
                             request.getContextPath() + "/user/messages\n\n" +
                             "Saludos,\n" +
                             "Equipo PawPaw 🐾";
            
            boolean emailSent = emailService.sendNotificationEmail(
                owner.getEmail(),
                owner.getNameUser(),
                subject,
                emailBody
            );
            
            if (emailSent || savedInDB) {
                System.out.println("✅ Mensaje procesado - Email: " + emailSent + " | BD: " + savedInDB);
                redirectWithSuccess(request, response, petIdStr, 
                    "¡Mensaje enviado! El dueño recibirá tu mensaje en su email y podrá verlo en su panel.");
            } else {
                System.err.println("❌ Error al procesar mensaje");
                redirectWithError(request, response, petIdStr, 
                    "Hubo un error al enviar el mensaje. Por favor intenta llamar directamente.");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al procesar mensaje: " + e.getMessage());
            e.printStackTrace();
            String petIdStr = request.getParameter("petId");
            redirectWithError(request, response, petIdStr, "Error al enviar el mensaje");
        }
    }
    
    /**
     * Maneja el reporte de mascota encontrada
     */
    private void handleReportFound(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String petIdStr = request.getParameter("petId");
            Integer petId = Integer.parseInt(petIdStr);
            
            Pet pet = petDAO.findById(petId);
            
            if (pet == null || !"lost".equals(pet.getStatusPet())) {
                response.sendRedirect(request.getContextPath() + "/pet/" + petIdStr);
                return;
            }
            
            pet.setStatusPet("found");
            boolean updated = petDAO.update(pet);
            
            if (updated) {
                User owner = userDAO.findById(pet.getIdUser());
                
                if (owner != null) {
                    String subject = "🎉 ¡Buenas noticias sobre " + pet.getNamePet() + "!";
                    String emailBody = "Hola " + owner.getNameUser() + ",\n\n" +
                                     "¡Excelentes noticias! 🎉\n\n" +
                                     "Alguien ha reportado que encontró a " + pet.getNamePet() + ".\n\n" +
                                     "Por favor revisa tu perfil de PawPaw para más detalles y " +
                                     "coordinar la reunión con tu mascota.\n\n" +
                                     "Link directo: " + request.getScheme() + "://" + 
                                     request.getServerName() + 
                                     (request.getServerPort() != 80 && request.getServerPort() != 443 
                                         ? ":" + request.getServerPort() : "") +
                                     request.getContextPath() + "/user/pets\n\n" +
                                     "Saludos,\n" +
                                     "Equipo PawPaw 🐾";
                    
                    emailService.sendNotificationEmail(
                        owner.getEmail(),
                        owner.getNameUser(),
                        subject,
                        emailBody
                    );
                }
                
                System.out.println("✅ Mascota " + petId + " reportada como encontrada");
                redirectWithSuccess(request, response, petIdStr, 
                    "¡Gracias por reportar! El dueño ha sido notificado.");
            } else {
                redirectWithError(request, response, petIdStr, "Error al actualizar el estado");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al reportar mascota encontrada: " + e.getMessage());
            e.printStackTrace();
            String petIdStr = request.getParameter("petId");
            redirectWithError(request, response, petIdStr, "Error al procesar la solicitud");
        }
    }
    
    private void redirectWithSuccess(HttpServletRequest request, HttpServletResponse response, 
                                     String petId, String message) throws IOException {
        request.getSession().setAttribute("successMessage", message);
        response.sendRedirect(request.getContextPath() + "/pet/" + petId);
    }
    
    private void redirectWithError(HttpServletRequest request, HttpServletResponse response, 
                                   String petId, String message) throws IOException {
        request.getSession().setAttribute("errorMessage", message);
        response.sendRedirect(request.getContextPath() + "/pet/" + petId);
    }
}