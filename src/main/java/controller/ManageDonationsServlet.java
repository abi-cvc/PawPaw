package controller;

import model.dao.DonationDAO;
import model.entity.Donation;
import model.entity.User;
import model.dao.UserDAO;

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
import java.math.RoundingMode;
import java.time.Year;
import java.util.List;

@WebServlet("/admin/donations")
public class ManageDonationsServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ManageDonationsServlet.class);

    private DonationDAO donationDAO = new DonationDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

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
        User admin = userDAO.findById(userId);
        if (admin != null) {
            request.setAttribute("user", admin);
        }

        try {
            // Stats
            int totalDonations = donationDAO.count();
            BigDecimal totalAmount = donationDAO.sumTotal();
            BigDecimal avgDonation = totalDonations > 0
                    ? totalAmount.divide(new BigDecimal(totalDonations), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            request.setAttribute("totalDonations", totalDonations);
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("avgDonation", avgDonation);

            // Año para la grafica
            int currentYear = Year.now().getValue();
            String yearParam = request.getParameter("year");
            int selectedYear = currentYear;
            if (yearParam != null) {
                try {
                    selectedYear = Integer.parseInt(yearParam);
                } catch (NumberFormatException e) {
                    selectedYear = currentYear;
                }
            }

            request.setAttribute("selectedYear", selectedYear);
            request.setAttribute("currentYear", currentYear);

            // Stats mensuales para la grafica
            BigDecimal[] monthlyStats = donationDAO.getMonthlyStats(selectedYear);
            StringBuilder monthlyData = new StringBuilder("[");
            for (int i = 0; i < 12; i++) {
                if (i > 0) monthlyData.append(",");
                monthlyData.append(monthlyStats[i].toString());
            }
            monthlyData.append("]");
            request.setAttribute("monthlyData", monthlyData.toString());

            // Lista de donaciones
            List<Donation> donations = donationDAO.findAll();
            request.setAttribute("donations", donations);

            logger.info("Admin donaciones - Total: {}, Monto: ${}", totalDonations, totalAmount);

        } catch (Exception e) {
            logger.error("Error al obtener datos de donaciones", e);
        }

        request.getRequestDispatcher("/view/admin/manage-donations.jsp").forward(request, response);
    }
}
