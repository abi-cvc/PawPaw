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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Servlet para gestionar promociones (Admin)
 */
@WebServlet("/admin/promotions")
public class ManagePromotionsServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ManagePromotionsServlet.class);

    private PromotionDAO promotionDAO = new PromotionDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
        User admin = userDAO.findById(adminId);
        if (admin != null) {
            request.setAttribute("user", admin);
        }

        // Obtener todas las promociones
        List<Promotion> promotions = promotionDAO.findAll();
        request.setAttribute("promotions", promotions);

        // Contar estadísticas
        long activeCount = promotions.stream().filter(p -> p.getIsActive()).count();
        long inactiveCount = promotions.stream().filter(p -> !p.getIsActive()).count();

        request.setAttribute("totalPromotions", promotions.size());
        request.setAttribute("activePromotions", activeCount);
        request.setAttribute("inactivePromotions", inactiveCount);

        // Mensajes
        String successMessage = (String) session.getAttribute("successMessage");
        String errorMessage = (String) session.getAttribute("errorMessage");
        session.removeAttribute("successMessage");
        session.removeAttribute("errorMessage");

        request.setAttribute("successMessage", successMessage);
        request.setAttribute("errorMessage", errorMessage);

        logger.info("Admin viendo promociones. Total: {}", promotions.size());

        request.getRequestDispatcher("/view/admin/manage-promotions.jsp").forward(request, response);
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

        try {
            if ("create".equals(action)) {
                createPromotion(request, adminId, session);
            } else if ("update".equals(action)) {
                updatePromotion(request, adminId, session);
            } else if ("toggle".equals(action)) {
                togglePromotion(request, adminId, session);
            } else if ("delete".equals(action)) {
                deletePromotion(request, session);
            }
        } catch (Exception e) {
            logger.error("Error en accion de promocion: {}", e.getMessage());
            session.setAttribute("errorMessage", "Error al procesar la solicitud");
        }

        response.sendRedirect(request.getContextPath() + "/admin/promotions");
    }

    /**
     * Crear nueva promoción
     */
    private void createPromotion(HttpServletRequest request, Integer adminId, HttpSession session) {
        try {
            String promoName = request.getParameter("promoName");
            String promoDescription = request.getParameter("promoDescription");
            Integer slotsQuantity = Integer.parseInt(request.getParameter("slotsQuantity"));
            BigDecimal promoPrice = new BigDecimal(request.getParameter("promoPrice"));
            BigDecimal regularPrice = new BigDecimal(request.getParameter("regularPrice"));
            String promoCode = request.getParameter("promoCode");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String maxUsesStr = request.getParameter("maxUses");

            Promotion promo = new Promotion(promoName, slotsQuantity, promoPrice, regularPrice);
            promo.setPromoDescription(promoDescription);
            promo.setPromoCode(promoCode != null && !promoCode.isEmpty() ? promoCode : null);
            promo.setCreatedBy(adminId);

            // Fechas
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (startDateStr != null && !startDateStr.isEmpty()) {
                Date startDate = sdf.parse(startDateStr);
                promo.setStartDate(new Timestamp(startDate.getTime()));
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                Date endDate = sdf.parse(endDateStr);
                promo.setEndDate(new Timestamp(endDate.getTime()));
            }

            // Max uses
            if (maxUsesStr != null && !maxUsesStr.isEmpty()) {
                promo.setMaxUses(Integer.parseInt(maxUsesStr));
            }

            if (promotionDAO.create(promo)) {
                session.setAttribute("successMessage", "Promoción creada exitosamente");
                logger.info("Promocion creada por admin {}", adminId);
            } else {
                session.setAttribute("errorMessage", "Error al crear la promoción");
            }

        } catch (ParseException e) {
            session.setAttribute("errorMessage", "Error en el formato de fecha");
            logger.error("Error de fecha: {}", e.getMessage());
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Error en los valores numéricos");
            logger.error("Error numerico: {}", e.getMessage());
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al crear la promoción");
            logger.error("Error al crear promocion", e);
        }
    }

    /**
     * Actualizar promoción
     */
    private void updatePromotion(HttpServletRequest request, Integer adminId, HttpSession session) {
        try {
            Integer idPromotion = Integer.parseInt(request.getParameter("idPromotion"));
            Promotion promo = promotionDAO.findById(idPromotion);

            if (promo == null) {
                session.setAttribute("errorMessage", "Promoción no encontrada");
                return;
            }

            promo.setPromoName(request.getParameter("promoName"));
            promo.setPromoDescription(request.getParameter("promoDescription"));
            promo.setSlotsQuantity(Integer.parseInt(request.getParameter("slotsQuantity")));
            promo.setPromoPrice(new BigDecimal(request.getParameter("promoPrice")));
            promo.setRegularPrice(new BigDecimal(request.getParameter("regularPrice")));

            String promoCode = request.getParameter("promoCode");
            promo.setPromoCode(promoCode != null && !promoCode.isEmpty() ? promoCode : null);

            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String maxUsesStr = request.getParameter("maxUses");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (startDateStr != null && !startDateStr.isEmpty()) {
                Date startDate = sdf.parse(startDateStr);
                promo.setStartDate(new Timestamp(startDate.getTime()));
            } else {
                promo.setStartDate(null);
            }

            if (endDateStr != null && !endDateStr.isEmpty()) {
                Date endDate = sdf.parse(endDateStr);
                promo.setEndDate(new Timestamp(endDate.getTime()));
            } else {
                promo.setEndDate(null);
            }

            if (maxUsesStr != null && !maxUsesStr.isEmpty()) {
                promo.setMaxUses(Integer.parseInt(maxUsesStr));
            } else {
                promo.setMaxUses(null);
            }

            promo.setUpdatedBy(adminId);

            if (promotionDAO.update(promo)) {
                session.setAttribute("successMessage", "Promoción actualizada exitosamente");
                logger.info("Promocion {} actualizada", idPromotion);
            } else {
                session.setAttribute("errorMessage", "Error al actualizar la promoción");
            }

        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al actualizar la promoción");
            logger.error("Error al actualizar promocion", e);
        }
    }

    /**
     * Activar/Desactivar promoción
     */
    private void togglePromotion(HttpServletRequest request, Integer adminId, HttpSession session) {
        try {
            Integer idPromotion = Integer.parseInt(request.getParameter("idPromotion"));

            if (promotionDAO.toggleActive(idPromotion, adminId)) {
                session.setAttribute("successMessage", "Estado de promoción actualizado");
                logger.info("Promocion {} toggle activo", idPromotion);
            } else {
                session.setAttribute("errorMessage", "Error al cambiar estado");
            }

        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al cambiar estado de la promoción");
            logger.error("Error al toggle promocion: {}", e.getMessage());
        }
    }

    /**
     * Eliminar promoción
     */
    private void deletePromotion(HttpServletRequest request, HttpSession session) {
        try {
            Integer idPromotion = Integer.parseInt(request.getParameter("idPromotion"));

            if (promotionDAO.delete(idPromotion)) {
                session.setAttribute("successMessage", "Promoción eliminada exitosamente");
                logger.info("Promocion {} eliminada", idPromotion);
            } else {
                session.setAttribute("errorMessage", "Error al eliminar la promoción");
            }

        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al eliminar la promoción");
            logger.error("Error al eliminar promocion: {}", e.getMessage());
        }
    }
}