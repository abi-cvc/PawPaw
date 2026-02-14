package controller;

import model.dao.UserDAO;
import model.dao.PetDAO;
import model.entity.User;
import model.entity.Pet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet para el panel principal del usuario
 */
@WebServlet("/user/panel")
public class UserPanelController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Verificar que haya sesión activa
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("userId") == null) {
            // No hay sesión, redirigir al login
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Obtener datos del usuario de la sesión
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Obtener datos completos del usuario de la BD
        UserDAO userDAO = new UserDAO();
        User user = userDAO.findById(userId);
        
        if (user == null || !user.getActive()) {
            // Usuario no existe o está inactivo, cerrar sesión
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Obtener mascotas del usuario
        PetDAO petDAO = new PetDAO();
        List<Pet> pets = petDAO.findByUserId(userId);
        int totalPets = petDAO.countByUserId(userId);
        int activePets = petDAO.countActiveByUserId(userId);
        
        // Pasar datos a la vista
        request.setAttribute("user", user);
        request.setAttribute("pets", pets);
        request.setAttribute("totalPets", totalPets);
        request.setAttribute("activePets", activePets);
        // TODO: Cuando implementemos QRCodeDAO
        // request.setAttribute("totalQRCodes", qrCodeDAO.countByUserId(userId));
        request.setAttribute("totalQRCodes", totalPets); // Por ahora asumimos 1 QR por mascota
        
        // Forward a la vista del panel
        request.getRequestDispatcher("/view/internalUser/panel.jsp").forward(request, response);
    }
}