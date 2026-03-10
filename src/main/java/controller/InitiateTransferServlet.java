package controller;

import model.dao.PetDAO;
import model.dao.PetTransferRequestDAO;
import model.entity.Pet;
import model.entity.PetTransferRequest;
import service.EmailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet para iniciar transferencia de mascota a adoptante
 * Solo para fundaciones (partners)
 * URL: /pet/transfer/initiate (POST)
 */
@WebServlet("/pet/transfer/initiate")
public class InitiateTransferServlet extends HttpServlet {
    
    private PetDAO petDAO = new PetDAO();
    private PetTransferRequestDAO transferDAO = new PetTransferRequestDAO();
    private EmailService emailService = new EmailService();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar sesión
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            Integer foundationId = (Integer) session.getAttribute("userId");
            Integer petId = Integer.parseInt(request.getParameter("petId"));
            String adopterEmail = request.getParameter("adopterEmail");
            String adopterName = request.getParameter("adopterName");
            String adopterPhone = request.getParameter("adopterPhone");
            String message = request.getParameter("message");
            
            // Validar datos
            if (adopterEmail == null || adopterEmail.trim().isEmpty() ||
                adopterName == null || adopterName.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Email y nombre del adoptante son obligatorios");
                response.sendRedirect(request.getContextPath() + "/user/pets");
                return;
            }
            
            // Verificar que la mascota pertenece a la fundación
            Pet pet = petDAO.findById(petId);
            
            if (pet == null || !pet.getIdUser().equals(foundationId)) {
                session.setAttribute("errorMessage", "No tienes permiso para transferir esta mascota");
                response.sendRedirect(request.getContextPath() + "/user/pets");
                return;
            }
            
            // Verificar que no haya transferencia pendiente
            if (transferDAO.hasPendingTransfer(petId)) {
                session.setAttribute("errorMessage", 
                    "Ya existe una transferencia pendiente para " + pet.getNamePet());
                response.sendRedirect(request.getContextPath() + "/user/pets");
                return;
            }
            
            // Generar token único
            String token = UUID.randomUUID().toString();
            
            // Crear solicitud de transferencia
            PetTransferRequest transfer = new PetTransferRequest(
                petId, foundationId, adopterEmail, adopterName, token
            );
            transfer.setAdopterPhone(adopterPhone);
            transfer.setMessage(message);
            
            if (transferDAO.create(transfer)) {
                // Actualizar estado de mascota
                petDAO.updateAdoptionStatus(petId, "adopted_pending");
                
                // Construir link de aceptación
                String acceptLink = request.getScheme() + "://" + 
                                  request.getServerName() + 
                                  (request.getServerPort() != 80 && request.getServerPort() != 443 
                                      ? ":" + request.getServerPort() : "") +
                                  request.getContextPath() + 
                                  "/accept-transfer?token=" + token;
                
                // Enviar email al adoptante
                String subject = "¡Felicidades! Tu adopción de " + pet.getNamePet() + " está lista 🐾";
                String emailMessage = buildTransferEmail(pet, adopterName, acceptLink, message);
                
                emailService.sendNotificationEmail(adopterEmail, adopterName, subject, emailMessage);
                
                session.setAttribute("successMessage", 
                    "Transferencia iniciada. Email enviado a " + adopterEmail);
                
                System.out.println("✅ Transferencia iniciada: " + pet.getNamePet() + 
                                 " → " + adopterEmail);
            } else {
                session.setAttribute("errorMessage", "Error al crear la transferencia");
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Datos inválidos");
        } catch (Exception e) {
            System.err.println("❌ Error al iniciar transferencia: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error al iniciar la transferencia");
        }
        
        response.sendRedirect(request.getContextPath() + "/user/pets");
    }
    
    /**
     * Construye el email de notificación de transferencia
     */
    private String buildTransferEmail(Pet pet, String adopterName, String acceptLink, String message) {
        StringBuilder email = new StringBuilder();
        
        email.append("Estimado/a ").append(adopterName).append(",\n\n");
        email.append("¡Felicidades por tu decisión de adoptar! 🐾\n\n");
        email.append("La fundación ha iniciado el proceso de transferencia para ").append(pet.getNamePet()).append(".\n\n");
        
        if (message != null && !message.trim().isEmpty()) {
            email.append("Mensaje de la fundación:\n");
            email.append("\"").append(message).append("\"\n\n");
        }
        
        email.append("Para completar la adopción y convertirte en el dueño oficial de ").append(pet.getNamePet());
        email.append(", necesitas aceptar esta transferencia.\n\n");
        email.append("PASOS A SEGUIR:\n");
        email.append("1. Haz click en el siguiente enlace:\n");
        email.append(acceptLink).append("\n\n");
        email.append("2. Si ya tienes cuenta en PawPaw, inicia sesión\n");
        email.append("3. Si no tienes cuenta, regístrate (¡es gratis!)\n");
        email.append("4. Acepta la transferencia\n\n");
        email.append("Una vez aceptada:\n");
        email.append("✅ ").append(pet.getNamePet()).append(" será oficialmente tuyo/a\n");
        email.append("✅ Podrás editar su perfil y actualizar su información\n");
        email.append("✅ El código QR seguirá funcionando con tus datos de contacto\n\n");
        email.append("⏰ Este enlace es válido por 7 días.\n\n");
        email.append("¡Bienvenido/a a la familia PawPaw! 🐕🐈\n\n");
        email.append("Saludos,\nEquipo PawPaw");
        
        return email.toString();
    }
}
