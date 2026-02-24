package controller;

import config.DatabaseConnection;
import model.dao.UserDAO;
import model.dao.PetDAO;
import model.dao.QRCodeDAO;
import model.dao.SuggestionDAO;
import model.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Servlet para el panel principal del administrador
 */
@WebServlet("/admin/panel")
public class AdminPanelServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    private PetDAO petDAO = new PetDAO();
    private QRCodeDAO qrCodeDAO = new QRCodeDAO();
    private SuggestionDAO suggestionDAO = new SuggestionDAO();
    
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
        
        Integer userId = (Integer) session.getAttribute("userId");
        
        // Obtener información del administrador
        User admin = userDAO.findById(userId);
        if (admin != null) {
            request.setAttribute("user", admin);
        }
        
        // Obtener estadísticas generales
        try {
            int totalUsers = userDAO.count();
            
            // PetDAO sí tiene findAll()
            int totalPets = 0;
            try {
                totalPets = petDAO.findAll().size();
            } catch (Exception e) {
                System.err.println("Error contando mascotas: " + e.getMessage());
            }
            
            // QRCodeDAO NO tiene findAll() global, usar query manual
            int totalQRCodes = 0;
            try {
                String sql = "SELECT COUNT(*) FROM qrcodes";
                try (Connection conn = DatabaseConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    if (rs.next()) {
                        totalQRCodes = rs.getInt(1);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error contando QR codes: " + e.getMessage());
            }
            
            int totalSuggestions = suggestionDAO.count();
            int pendingSuggestions = suggestionDAO.countByStatus("pending");
            
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalPets", totalPets);
            request.setAttribute("totalQRCodes", totalQRCodes);
            request.setAttribute("totalSuggestions", totalSuggestions);
            request.setAttribute("pendingSuggestions", pendingSuggestions);
            
            System.out.println("📊 Dashboard stats - Users: " + totalUsers + ", Pets: " + totalPets + ", Suggestions: " + totalSuggestions);
            
        } catch (Exception e) {
            System.err.println("❌ Error al obtener estadísticas: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Redirigir a la vista del panel de administrador
        request.getRequestDispatcher("/view/admin/admin-panel.jsp").forward(request, response);
    }
}