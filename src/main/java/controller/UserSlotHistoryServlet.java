package controller;

import model.dao.UserDAO;
import model.dao.PetDAO;
import model.dao.SlotAdjustmentDAO;
import model.entity.User;
import model.entity.SlotAdjustment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet para ver historial de ajustes de slots de un usuario
 */
@WebServlet("/admin/users/*/slot-history")
public class UserSlotHistoryServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    private PetDAO petDAO = new PetDAO();
    private SlotAdjustmentDAO adjustmentDAO = new SlotAdjustmentDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar que es admin
        if (session == null || session.getAttribute("role") == null || 
            !"admin".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            // Extraer userId de la URL
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.split("/").length < 2) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            String userIdStr = pathInfo.split("/")[1];
            Integer userId = Integer.parseInt(userIdStr);
            
            // Obtener usuario
            User user = userDAO.findById(userId);
            if (user == null) {
                session.setAttribute("errorMessage", "Usuario no encontrado");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            // Obtener historial de ajustes
            List<SlotAdjustment> adjustments = adjustmentDAO.findByUserId(userId);
            
            // Contar mascotas actuales
            int currentPetCount = petDAO.countByUserId(userId);
            
            // Estadísticas
            int totalAdjustments = adjustments.size();
            long increases = adjustments.stream().filter(SlotAdjustment::isIncrease).count();
            long decreases = adjustments.stream().filter(SlotAdjustment::isDecrease).count();
            
            request.setAttribute("user", user);
            request.setAttribute("adjustments", adjustments);
            request.setAttribute("currentPetCount", currentPetCount);
            request.setAttribute("totalAdjustments", totalAdjustments);
            request.setAttribute("increases", increases);
            request.setAttribute("decreases", decreases);
            
            System.out.println("📜 Viendo historial de slots para usuario " + userId + ". Total ajustes: " + totalAdjustments);
            
            request.getRequestDispatcher("/view/admin/user-slot-history.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } catch (Exception e) {
            System.err.println("❌ Error al cargar historial: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error al cargar historial");
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }
}
