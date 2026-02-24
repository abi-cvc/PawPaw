package controller;

import model.dao.UserDAO;
import model.dao.PetDAO;
import model.dao.QRCodeDAO;
import model.dao.AdminAuditLogDAO;
import model.entity.User;
import model.entity.AdminAuditLog;

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
 * Servlet para gestionar usuarios (Administrador)
 */
@WebServlet("/admin/users")
public class ManageUsersServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    private PetDAO petDAO = new PetDAO();
    private QRCodeDAO qrCodeDAO = new QRCodeDAO();
    private AdminAuditLogDAO auditLogDAO = new AdminAuditLogDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar sesión y rol de administrador
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String userRole = (String) session.getAttribute("userRole");
        if (!"admin".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/user/panel");
            return;
        }
        
        Integer adminId = (Integer) session.getAttribute("userId");
        User admin = userDAO.findById(adminId);
        if (admin != null) {
            request.setAttribute("user", admin);
        }
        
        // Obtener filtro si existe
        String filter = request.getParameter("filter");
        
        // Obtener todos los usuarios
        List<User> users = userDAO.findAll();
        
        // Filtrar si es necesario
        if (filter != null && !filter.isEmpty()) {
            if ("active".equals(filter)) {
                users.removeIf(u -> !u.getActive());
            } else if ("inactive".equals(filter)) {
                users.removeIf(User::getActive);
            } else if ("admin".equals(filter)) {
                users.removeIf(u -> !"admin".equalsIgnoreCase(u.getRol()));
            } else if ("user".equals(filter)) {
                users.removeIf(u -> !"user".equalsIgnoreCase(u.getRol()));
            }
        }
        
        // Obtener estadísticas de cada usuario
        Map<Integer, Integer> userPetCounts = new HashMap<>();
        Map<Integer, Integer> userQRCounts = new HashMap<>();
        
        for (User user : users) {
            try {
                int petCount = petDAO.countByUserId(user.getIdUser());
                int qrCount = qrCodeDAO.countActiveByUserId(user.getIdUser());
                userPetCounts.put(user.getIdUser(), petCount);
                userQRCounts.put(user.getIdUser(), qrCount);
            } catch (Exception e) {
                System.err.println("Error obteniendo estadísticas para usuario " + user.getIdUser());
                userPetCounts.put(user.getIdUser(), 0);
                userQRCounts.put(user.getIdUser(), 0);
            }
        }
        
        // Estadísticas generales
        int totalUsers = userDAO.count();
        long activeUsers = users.stream().filter(User::getActive).count();
        long inactiveUsers = users.stream().filter(u -> !u.getActive()).count();
        long adminUsers = users.stream().filter(u -> "admin".equalsIgnoreCase(u.getRol())).count();
        
        // Mensajes
        String successMessage = (String) session.getAttribute("successMessage");
        String errorMessage = (String) session.getAttribute("errorMessage");
        session.removeAttribute("successMessage");
        session.removeAttribute("errorMessage");
        
        request.setAttribute("users", users);
        request.setAttribute("userPetCounts", userPetCounts);
        request.setAttribute("userQRCounts", userQRCounts);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("activeUsers", activeUsers);
        request.setAttribute("inactiveUsers", inactiveUsers);
        request.setAttribute("adminUsers", adminUsers);
        request.setAttribute("currentFilter", filter);
        request.setAttribute("successMessage", successMessage);
        request.setAttribute("errorMessage", errorMessage);
        
        System.out.println("👥 Admin viendo gestión de usuarios. Total: " + users.size());
        
        request.getRequestDispatcher("/view/admin/manage-users.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar sesión y rol
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String userRole = (String) session.getAttribute("userRole");
        if (!"admin".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/user/panel");
            return;
        }
        
        Integer adminId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");
        String userIdStr = request.getParameter("userId");
        
        if (userIdStr == null || userIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/users?error=invalid");
            return;
        }
        
        try {
            Integer userId = Integer.parseInt(userIdStr);
            User targetUser = userDAO.findById(userId);
            
            if (targetUser == null) {
                session.setAttribute("errorMessage", "Usuario no encontrado");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }
            
            // Obtener IP para auditoría
            String ipAddress = request.getRemoteAddr();
            String userAgent = request.getHeader("User-Agent");
            
            if ("toggleStatus".equals(action)) {
                // Cambiar estado activo/inactivo
                boolean newStatus = !targetUser.getActive();
                targetUser.setActive(newStatus);
                
                if (userDAO.update(targetUser)) {
                    // Guardar en audit log
                    AdminAuditLog log = new AdminAuditLog(
                        adminId,
                        newStatus ? "ACTIVATE_USER" : "DEACTIVATE_USER",
                        "user",
                        userId,
                        !newStatus ? "active" : "inactive",
                        newStatus ? "active" : "inactive",
                        ipAddress
                    );
                    log.setUserAgent(userAgent);
                    log.setDetails("User " + targetUser.getEmail() + " " + (newStatus ? "activated" : "deactivated"));
                    auditLogDAO.create(log);
                    
                    session.setAttribute("successMessage", 
                        "Usuario " + (newStatus ? "activado" : "desactivado") + " correctamente");
                    
                    System.out.println("✅ Usuario " + userId + " " + (newStatus ? "activado" : "desactivado"));
                } else {
                    session.setAttribute("errorMessage", "Error al actualizar el estado del usuario");
                }
                
            } else if ("delete".equals(action)) {
                // Eliminar usuario (solo si no tiene mascotas)
                int petCount = petDAO.countByUserId(userId);
                
                if (petCount > 0) {
                    session.setAttribute("errorMessage", 
                        "No se puede eliminar el usuario porque tiene " + petCount + " mascota(s) registrada(s)");
                } else if (userDAO.delete(userId)) {
                    // Guardar en audit log
                    AdminAuditLog log = new AdminAuditLog(
                        adminId,
                        "DELETE_USER",
                        "user",
                        userId,
                        targetUser.getEmail(),
                        "DELETED",
                        ipAddress
                    );
                    log.setUserAgent(userAgent);
                    log.setDetails("User " + targetUser.getEmail() + " permanently deleted");
                    auditLogDAO.create(log);
                    
                    session.setAttribute("successMessage", "Usuario eliminado correctamente");
                    System.out.println("✅ Usuario " + userId + " eliminado");
                } else {
                    session.setAttribute("errorMessage", "Error al eliminar el usuario");
                }
            }
            
        } catch (NumberFormatException e) {
            System.err.println("❌ ID de usuario inválido: " + userIdStr);
            session.setAttribute("errorMessage", "ID de usuario inválido");
        } catch (Exception e) {
            System.err.println("❌ Error al procesar acción: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error al procesar la solicitud");
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}