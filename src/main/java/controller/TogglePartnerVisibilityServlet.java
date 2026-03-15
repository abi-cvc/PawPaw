package controller;

import model.dao.UserDAO;
import model.dao.SystemMessageDAO;
import model.entity.User;
import model.entity.SystemMessage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/admin/toggle-visibility")
public class TogglePartnerVisibilityServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();
    private SystemMessageDAO systemMessageDAO = new SystemMessageDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"admin".equalsIgnoreCase((String) session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Integer userId = Integer.parseInt(request.getParameter("userId"));
            String action = request.getParameter("visibilityAction");
            String reason = request.getParameter("reason");

            User user = userDAO.findById(userId);
            if (user == null || !user.isPartnerUser()) {
                session.setAttribute("errorMessage", "Usuario no encontrado o no es fundación");
                response.sendRedirect(request.getContextPath() + "/admin/users");
                return;
            }

            boolean makeVisible = "show".equals(action);
            boolean success = userDAO.updatePartnerVisibility(userId, makeVisible);

            if (success) {
                if (!makeVisible && reason != null && !reason.trim().isEmpty()) {
                    // Enviar mensaje a la fundación explicando la razón
                    SystemMessage msg = new SystemMessage(
                        userId,
                        "Tu perfil de fundación ha sido ocultado",
                        "Estimado/a " + user.getNameUser() + ",\n\n" +
                        "Tu perfil de fundación ha sido temporalmente ocultado de la página pública de Partners.\n\n" +
                        "Razón: " + reason.trim() + "\n\n" +
                        "Si tienes preguntas, por favor contacta al equipo de PawPaw.\n\n" +
                        "Saludos,\nEquipo PawPaw"
                    );
                    systemMessageDAO.create(msg);
                }

                session.setAttribute("successMessage",
                    makeVisible ? "Fundación visible en la página de Partners"
                                : "Fundación ocultada de la página de Partners");
                System.out.println("✅ Visibilidad de fundación " + userId + " cambiada a: " + makeVisible);
            } else {
                session.setAttribute("errorMessage", "Error al cambiar la visibilidad");
            }

        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al procesar la solicitud");
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
}
