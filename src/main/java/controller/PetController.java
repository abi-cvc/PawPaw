package controller;

import model.dao.PetDAO;
import model.entity.Pet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controlador para gestión de mascotas
 * Maneja: crear, editar, eliminar y listar mascotas
 */
@WebServlet("/user/pets/*")
public class PetController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private PetDAO petDAO = new PetDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Verificar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Listar todas las mascotas del usuario
            listPets(request, response, userId);
        } else if (pathInfo.equals("/new")) {
            // Mostrar formulario de nueva mascota
            showNewPetForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            // Mostrar formulario de edición
            showEditPetForm(request, response, userId);
        } else if (pathInfo.equals("/delete")) {
            // Eliminar mascota
            deletePet(request, response, userId);
        } else {
            response.sendRedirect(request.getContextPath() + "/user/panel");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Verificar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        String pathInfo = request.getPathInfo();
        
        if (pathInfo != null && pathInfo.equals("/new")) {
            // Crear nueva mascota
            createPet(request, response, userId);
        } else if (pathInfo != null && pathInfo.equals("/edit")) {
            // Actualizar mascota existente
            updatePet(request, response, userId);
        } else {
            response.sendRedirect(request.getContextPath() + "/user/panel");
        }
    }
    
    /**
     * Lista todas las mascotas del usuario
     */
    private void listPets(HttpServletRequest request, HttpServletResponse response, Integer userId) 
            throws ServletException, IOException {
        
        var pets = petDAO.findByUserId(userId);
        request.setAttribute("pets", pets);
        request.getRequestDispatcher("/view/internalUser/pets-list.jsp").forward(request, response);
    }
    
    /**
     * Muestra el formulario para crear una nueva mascota
     */
    private void showNewPetForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setAttribute("action", "new");
        request.getRequestDispatcher("/view/internalUser/pet-form.jsp").forward(request, response);
    }
    
    /**
     * Muestra el formulario para editar una mascota
     */
    private void showEditPetForm(HttpServletRequest request, HttpServletResponse response, Integer userId) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/panel");
            return;
        }
        
        try {
            Integer petId = Integer.parseInt(idParam);
            
            // Verificar que la mascota pertenece al usuario
            if (!petDAO.belongsToUser(petId, userId)) {
                response.sendRedirect(request.getContextPath() + "/user/panel");
                return;
            }
            
            Pet pet = petDAO.findById(petId);
            
            if (pet == null) {
                response.sendRedirect(request.getContextPath() + "/user/panel");
                return;
            }
            
            request.setAttribute("pet", pet);
            request.setAttribute("action", "edit");
            request.getRequestDispatcher("/view/internalUser/pet-form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user/panel");
        }
    }
    
    /**
     * Crea una nueva mascota
     */
    private void createPet(HttpServletRequest request, HttpServletResponse response, Integer userId) 
            throws ServletException, IOException {
        
        // Obtener datos del formulario
        String name = request.getParameter("name");
        String ageStr = request.getParameter("age");
        String breed = request.getParameter("breed");
        String sex = request.getParameter("sex");
        String medicalConditions = request.getParameter("medicalConditions");
        String contactPhone = request.getParameter("contactPhone");
        String photo = request.getParameter("photo");
        String extraComments = request.getParameter("extraComments");
        
        // Validar campos obligatorios
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "El nombre de la mascota es obligatorio");
            request.setAttribute("action", "new");
            request.getRequestDispatcher("/view/internalUser/pet-form.jsp").forward(request, response);
            return;
        }
        
        // Crear objeto Pet
        Pet pet = new Pet();
        pet.setIdUser(userId);
        pet.setNamePet(name.trim());
        
        // Edad (opcional)
        if (ageStr != null && !ageStr.trim().isEmpty()) {
            try {
                pet.setAgePet(Integer.parseInt(ageStr));
            } catch (NumberFormatException e) {
                // Ignorar si no es un número válido
            }
        }
        
        pet.setBreed(breed != null && !breed.trim().isEmpty() ? breed.trim() : null);
        pet.setSexPet(sex);
        pet.setMedicalConditions(medicalConditions);
        pet.setContactPhone(contactPhone != null && !contactPhone.trim().isEmpty() ? contactPhone.trim() : null);
        pet.setPhoto(photo != null && !photo.trim().isEmpty() ? photo.trim() : null);
        pet.setStatusPet("active");
        pet.setExtraComments(extraComments);
        
        // Guardar en BD
        if (petDAO.create(pet)) {
            System.out.println("✅ Mascota creada - ID: " + pet.getIdPet() + " - Nombre: " + pet.getNamePet());
            response.sendRedirect(request.getContextPath() + "/user/panel");
        } else {
            request.setAttribute("error", "Error al registrar la mascota. Intenta nuevamente.");
            request.setAttribute("action", "new");
            request.getRequestDispatcher("/view/internalUser/pet-form.jsp").forward(request, response);
        }
    }
    
    /**
     * Actualiza una mascota existente
     */
    private void updatePet(HttpServletRequest request, HttpServletResponse response, Integer userId) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/panel");
            return;
        }
        
        try {
            Integer petId = Integer.parseInt(idParam);
            
            // Verificar que la mascota pertenece al usuario
            if (!petDAO.belongsToUser(petId, userId)) {
                response.sendRedirect(request.getContextPath() + "/user/panel");
                return;
            }
            
            // Obtener datos del formulario
            String name = request.getParameter("name");
            String ageStr = request.getParameter("age");
            String breed = request.getParameter("breed");
            String sex = request.getParameter("sex");
            String medicalConditions = request.getParameter("medicalConditions");
            String contactPhone = request.getParameter("contactPhone");
            String photo = request.getParameter("photo");
            String status = request.getParameter("status");
            String extraComments = request.getParameter("extraComments");
            
            // Validar nombre
            if (name == null || name.trim().isEmpty()) {
                Pet pet = petDAO.findById(petId);
                request.setAttribute("pet", pet);
                request.setAttribute("error", "El nombre de la mascota es obligatorio");
                request.setAttribute("action", "edit");
                request.getRequestDispatcher("/view/internalUser/pet-form.jsp").forward(request, response);
                return;
            }
            
            // Actualizar objeto Pet
            Pet pet = petDAO.findById(petId);
            pet.setNamePet(name.trim());
            
            if (ageStr != null && !ageStr.trim().isEmpty()) {
                try {
                    pet.setAgePet(Integer.parseInt(ageStr));
                } catch (NumberFormatException e) {
                    pet.setAgePet(null);
                }
            } else {
                pet.setAgePet(null);
            }
            
            pet.setBreed(breed != null && !breed.trim().isEmpty() ? breed.trim() : null);
            pet.setSexPet(sex);
            pet.setMedicalConditions(medicalConditions);
            pet.setContactPhone(contactPhone);
            pet.setPhoto(photo != null && !photo.trim().isEmpty() ? photo.trim() : null);
            pet.setStatusPet(status != null ? status : "active");
            pet.setExtraComments(extraComments);
            
            // Actualizar en BD
            if (petDAO.update(pet)) {
                System.out.println("✅ Mascota actualizada - ID: " + pet.getIdPet());
                response.sendRedirect(request.getContextPath() + "/user/panel");
            } else {
                request.setAttribute("pet", pet);
                request.setAttribute("error", "Error al actualizar la mascota. Intenta nuevamente.");
                request.setAttribute("action", "edit");
                request.getRequestDispatcher("/view/internalUser/pet-form.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user/panel");
        }
    }
    
    /**
     * Elimina una mascota
     */
    private void deletePet(HttpServletRequest request, HttpServletResponse response, Integer userId) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/user/panel");
            return;
        }
        
        try {
            Integer petId = Integer.parseInt(idParam);
            
            // Verificar que la mascota pertenece al usuario
            if (!petDAO.belongsToUser(petId, userId)) {
                response.sendRedirect(request.getContextPath() + "/user/panel");
                return;
            }
            
            if (petDAO.delete(petId)) {
                System.out.println("✅ Mascota eliminada - ID: " + petId);
            } else {
                System.out.println("❌ Error al eliminar mascota - ID: " + petId);
            }
            
            response.sendRedirect(request.getContextPath() + "/user/panel");
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user/panel");
        }
    }
}