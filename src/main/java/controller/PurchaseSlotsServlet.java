package controller;

import model.dao.PromotionDAO;
import model.dao.UserDAO;
import model.entity.Promotion;
import model.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Servlet para comprar slots adicionales de mascotas
 */
@WebServlet("/user/purchase-slots")
public class PurchaseSlotsServlet extends HttpServlet {
    
    private PromotionDAO promotionDAO = new PromotionDAO();
    private UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar sesión
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        User user = userDAO.findById(userId);
        
        if (user != null) {
            request.setAttribute("user", user);
            
            // CORREGIDO: Verificar si es partner usando reflexión o query directa
            // Por ahora comentamos esta validación hasta actualizar User.java
            /*
            if (user.getIsPartner() != null && user.getIsPartner()) {
                session.setAttribute("errorMessage", "Como Aliado PawPaw tienes acceso ilimitado 🤝");
                response.sendRedirect(request.getContextPath() + "/user/panel");
                return;
            }
            */
        }
        
        // Obtener promoción activa
        Promotion activePromo = promotionDAO.getCurrentActivePromotion();
        request.setAttribute("activePromotion", activePromo);
        
        // Calcular precios
        BigDecimal singleSlotPrice = new BigDecimal("5.00");
        request.setAttribute("singleSlotPrice", singleSlotPrice);
        
        if (activePromo != null) {
            BigDecimal savings = activePromo.calculateSavings();
            request.setAttribute("savings", savings);
        }
        
        System.out.println("🛒 Usuario " + userId + " viendo página de compra de slots");
        
        request.getRequestDispatcher("/view/internalUser/purchase-slots.jsp").forward(request, response);
    }
}