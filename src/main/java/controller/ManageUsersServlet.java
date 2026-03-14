package controller;

import model.dao.UserDAO;
import model.dao.PetDAO;
import model.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet para gestión de usuarios (Admin)
 */
@WebServlet("/admin/users")
public class ManageUsersServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    private PetDAO petDAO = new PetDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar que es admin - CORREGIDO: usar "userRole"
        if (session == null || session.getAttribute("userRole") == null || 
            !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            // Obtener filtros
            String search = request.getParameter("search");
            String roleFilter = request.getParameter("role");
            
            // Obtener todos los usuarios
            List<User> users = userDAO.findAll();
            
            // Aplicar filtros
            if (search != null && !search.trim().isEmpty()) {
                String searchLower = search.toLowerCase();
                users.removeIf(u -> 
                    !u.getNameUser().toLowerCase().contains(searchLower) && 
                    !u.getEmail().toLowerCase().contains(searchLower)
                );
            }
            
            if (roleFilter != null && !roleFilter.isEmpty() && !"all".equals(roleFilter)) {
                users.removeIf(u -> !roleFilter.equals(u.getRol()));
            }
            
            // DB-003: Contar mascotas en bulk (1 query en vez de N)
            Map<Integer, Integer> petCounts = petDAO.countByUserIdBulk();
            
            // Estadísticas
            int totalUsers = users.size();
            long normalUsers = users.stream().filter(u -> "user".equals(u.getRol())).count();
            long adminUsers = users.stream().filter(u -> "admin".equals(u.getRol())).count();
            long partners = users.stream().filter(u -> u.getIsPartner() != null && u.getIsPartner()).count();
            
            request.setAttribute("users", users);
            request.setAttribute("petCounts", petCounts);
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("normalUsers", normalUsers);
            request.setAttribute("adminUsers", adminUsers);
            request.setAttribute("partners", partners);
            request.setAttribute("currentSearch", search);
            request.setAttribute("currentRole", roleFilter);
            
            System.out.println("📊 Admin viendo usuarios. Total: " + totalUsers);
            
            request.getRequestDispatcher("/view/admin/manage-users.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("❌ Error al cargar usuarios: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error al cargar usuarios");
            response.sendRedirect(request.getContextPath() + "/admin/panel");
        }
    }
}