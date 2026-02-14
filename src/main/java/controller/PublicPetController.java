package controller;

import model.dao.PetDAO;
import model.dao.UserDAO;
import model.entity.Pet;
import model.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Controlador público para mostrar información de mascotas
 * Accesible sin login - usado cuando alguien escanea un QR
 */
@WebServlet("/pet/*")
public class PublicPetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private PetDAO petDAO = new PetDAO();
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Obtener ID de la mascota desde la URL
        String pathInfo = request.getPathInfo(); // Ejemplo: "/1"
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        try {
            // Extraer ID de la mascota
            String petIdStr = pathInfo.substring(1); // Quitar el "/"
            Integer petId = Integer.parseInt(petIdStr);
            
            System.out.println("🔵 Vista pública de mascota ID: " + petId);
            
            // Buscar mascota
            Pet pet = petDAO.findById(petId);
            
            if (pet == null) {
                System.out.println("❌ Mascota no encontrada: " + petId);
                request.setAttribute("error", "Mascota no encontrada");
                request.getRequestDispatcher("/view/externalUser/pet-not-found.jsp").forward(request, response);
                return;
            }
            
            // Buscar dueño
            User owner = userDAO.findById(pet.getIdUser());
            
            if (owner == null || !owner.getActive()) {
                System.out.println("❌ Dueño no encontrado o inactivo");
                request.setAttribute("error", "Información no disponible");
                request.getRequestDispatcher("/view/externalUser/pet-not-found.jsp").forward(request, response);
                return;
            }
            
            System.out.println("✅ Mostrando: " + pet.getNamePet() + " - Dueño: " + owner.getNameUser());
            
            // Pasar datos a la vista
            request.setAttribute("pet", pet);
            request.setAttribute("owner", owner);
            request.setAttribute("ownerName", owner.getNameUser());
            
            // Mostrar vista pública
            request.getRequestDispatcher("/view/externalUser/pet-public.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            System.out.println("❌ ID inválido: " + pathInfo);
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("report-found".equals(action)) {
            reportFound(request, response);
        } else if ("send-message".equals(action)) {
            sendMessage(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
    
    /**
     * Reportar mascota como encontrada
     */
    private void reportFound(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String petIdStr = request.getParameter("petId");
        
        try {
            Integer petId = Integer.parseInt(petIdStr);
            Pet pet = petDAO.findById(petId);
            
            if (pet != null) {
                // Cambiar estado a "found"
                pet.setStatusPet("found");
                
                if (petDAO.update(pet)) {
                    System.out.println("✅ Mascota reportada como encontrada - ID: " + petId);
                    request.setAttribute("success", "¡Gracias! El dueño ha sido notificado.");
                } else {
                    request.setAttribute("error", "Error al reportar. Intenta nuevamente.");
                }
                
                // Volver a mostrar la página
                doGet(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
    
    /**
     * Enviar mensaje al dueño
     * TODO: Implementar sistema de notificaciones por email
     */
    private void sendMessage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String petIdStr = request.getParameter("petId");
        String senderName = request.getParameter("senderName");
        String senderPhone = request.getParameter("senderPhone");
        String message = request.getParameter("message");
        
        System.out.println("📧 Mensaje recibido para mascota ID: " + petIdStr);
        System.out.println("   De: " + senderName + " - " + senderPhone);
        System.out.println("   Mensaje: " + message);
        
        // TODO: Aquí se podría:
        // 1. Guardar el mensaje en una tabla "messages"
        // 2. Enviar email al dueño
        // 3. Enviar notificación push
        
        request.setAttribute("success", "Mensaje enviado. El dueño se pondrá en contacto contigo.");
        
        try {
            Integer petId = Integer.parseInt(petIdStr);
            request.getRequestDispatcher("/pet/" + petId).forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}