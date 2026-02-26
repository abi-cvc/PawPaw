package controller;

import model.dao.FoundationRequestDAO;
import model.entity.FoundationRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet para formulario público de solicitud de fundaciones
 */
@WebServlet("/foundation/apply")
public class FoundationFormServlet extends HttpServlet {
    
    private FoundationRequestDAO foundationDAO = new FoundationRequestDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Formulario público - no requiere login
        request.getRequestDispatcher("/view/externalUser/foundation-form.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Obtener datos del formulario
        String foundationName = request.getParameter("foundationName");
        String contactName = request.getParameter("contactName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String whatsapp = request.getParameter("whatsapp");
        String animalTypes = request.getParameter("animalTypes");
        String currentAnimalsStr = request.getParameter("currentAnimals");
        String description = request.getParameter("description");
        String website = request.getParameter("website");
        
        // Validaciones básicas
        if (foundationName == null || foundationName.trim().isEmpty() ||
            contactName == null || contactName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty() ||
            animalTypes == null || animalTypes.trim().isEmpty() ||
            currentAnimalsStr == null || currentAnimalsStr.trim().isEmpty()) {
            
            request.setAttribute("errorMessage", "Por favor completa todos los campos obligatorios");
            request.setAttribute("foundationName", foundationName);
            request.setAttribute("contactName", contactName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("animalTypes", animalTypes);
            request.getRequestDispatcher("/view/externalUser/foundation-form.jsp").forward(request, response);
            return;
        }
        
        // Validar email único
        if (foundationDAO.findByEmail(email) != null) {
            request.setAttribute("errorMessage", "Ya existe una solicitud con este email");
            request.setAttribute("foundationName", foundationName);
            request.setAttribute("contactName", contactName);
            request.setAttribute("phone", phone);
            request.setAttribute("animalTypes", animalTypes);
            request.getRequestDispatcher("/view/externalUser/foundation-form.jsp").forward(request, response);
            return;
        }
        
        try {
            Integer currentAnimals = Integer.parseInt(currentAnimalsStr);
            
            // Crear solicitud
            FoundationRequest foundationRequest = new FoundationRequest(
                foundationName, contactName, email, phone, animalTypes, currentAnimals
            );
            foundationRequest.setWhatsapp(whatsapp);
            foundationRequest.setDescription(description);
            foundationRequest.setWebsite(website);
            
            if (foundationDAO.create(foundationRequest)) {
                request.setAttribute("successMessage", 
                    "¡Solicitud enviada exitosamente! Te contactaremos pronto a " + email);
                System.out.println("✅ Nueva solicitud de fundación: " + foundationName);
                
                // Limpiar formulario
                request.getRequestDispatcher("/view/externalUser/foundation-success.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Error al enviar la solicitud. Inténtalo de nuevo.");
                request.getRequestDispatcher("/view/externalUser/foundation-form.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "La cantidad de animales debe ser un número");
            request.setAttribute("foundationName", foundationName);
            request.setAttribute("contactName", contactName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("animalTypes", animalTypes);
            request.getRequestDispatcher("/view/externalUser/foundation-form.jsp").forward(request, response);
        }
    }
}