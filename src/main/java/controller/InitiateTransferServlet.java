package controller;

import config.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.PetTransferRequestDAO;
import model.entity.PetTransferRequest;
import service.EmailService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.UUID;

/**
 * Inicia el proceso de transferencia de una mascota a su adoptante.
 * La fundación envía el email del adoptante → se genera un token → se envía un email.
 * URL: POST /pet/transfer/initiate
 */
@WebServlet("/pet/transfer/initiate")
public class InitiateTransferServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ── Verificar sesión y rol de partner ─────────────────────────
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        Boolean isPartner = (Boolean) request.getSession().getAttribute("isPartner");
        if (isPartner == null || !isPartner) {
            response.sendError(403, "Solo fundaciones pueden iniciar transferencias");
            return;
        }

        // ── Parámetros del formulario ─────────────────────────────────
        String petIdStr     = request.getParameter("petId");
        String adopterEmail = request.getParameter("adopterEmail");
        String adopterName  = request.getParameter("adopterName");
        String adopterPhone = request.getParameter("adopterPhone");
        String message      = request.getParameter("message");

        if (petIdStr == null || adopterEmail == null || adopterEmail.isBlank()
                || adopterName == null || adopterName.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/user/pets?error=missingFields");
            return;
        }

        int petId;
        try {
            petId = Integer.parseInt(petIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/user/pets?error=invalidPet");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            // Verificar que la mascota pertenece a la fundación y está disponible
            String checkSql = "SELECT name_pet FROM pets WHERE id_pet = ? AND id_user = ? AND adoption_status = 'available'";
            String petName = null;
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setInt(1, petId);
                ps.setInt(2, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) petName = rs.getString("name_pet");
                }
            }

            if (petName == null) {
                response.sendRedirect(request.getContextPath() + "/user/pets?error=petNotAvailable");
                return;
            }

            // Generar token único
            String token = UUID.randomUUID().toString();

            // Guardar solicitud de transferencia
            PetTransferRequest req = new PetTransferRequest();
            req.setIdPet(petId);
            req.setIdFoundation(userId);
            req.setAdopterEmail(adopterEmail.trim().toLowerCase());
            req.setAdopterName(adopterName.trim());
            req.setAdopterPhone(adopterPhone != null ? adopterPhone.trim() : "");
            req.setTransferToken(token);
            req.setMessage(message != null ? message.trim() : "");

            new PetTransferRequestDAO().createTransferRequest(req);

            // Marcar mascota como adopted_pending
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE pets SET adoption_status = 'adopted_pending' WHERE id_pet = ?")) {
                ps.setInt(1, petId);
                ps.executeUpdate();
            }

            // Construir URL de aceptación
            String baseUrl = request.getScheme() + "://" + request.getServerName()
                    + (request.getServerPort() == 80 || request.getServerPort() == 443
                    ? "" : ":" + request.getServerPort())
                    + request.getContextPath();
            String acceptUrl = baseUrl + "/accept-transfer?token=" + token;

            String foundationName = (String) request.getSession().getAttribute("userName");

            // Enviar email usando instancia (los métodos helper son de instancia, no static)
            EmailService emailService = new EmailService();
            boolean emailSent = emailService.sendTransferEmail(
                    adopterEmail.trim().toLowerCase(),
                    adopterName.trim(),
                    petName,
                    foundationName != null ? foundationName : "La fundación",
                    acceptUrl
            );

            if (!emailSent) {
                System.err.println("⚠️ Transferencia creada pero el email no se pudo enviar a " + adopterEmail);
            }

            String encoded = URLEncoder.encode(petName, StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/user/pets?success=transferInitiated&petName=" + encoded);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/user/pets?error=transferFailed");
        }
    }
}