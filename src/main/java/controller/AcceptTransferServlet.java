package controller;

import config.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.PetTransferRequestDAO;
import model.entity.PetTransferRequest;

import java.io.IOException;
import java.sql.*;

/**
 * Maneja la aceptación/rechazo de una transferencia de mascota.
 * El adoptante llega aquí desde el link en su email.
 *
 * GET  /accept-transfer?token=...  → muestra la página de confirmación
 * POST /accept-transfer            → procesa la aceptación o rechazo
 *
 * LÓGICA DE TRANSFERENCIA:
 *   - El id_pet de la mascota NO cambia → el QR sigue funcionando igual.
 *   - Solo cambia id_user en la tabla pets (de fundación → adoptante).
 *   - Se agrega 1 slot al adoptante automáticamente para que el pet no quede sin espacio.
 *   - adoption_status pasa a 'adopted_transferred' (funciona como badge histórico).
 */
@WebServlet("/accept-transfer")
public class AcceptTransferServlet extends HttpServlet {

    // ──────────────────────────────────────────────────────────────────
    // GET: mostrar página de aceptación
    // ──────────────────────────────────────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");
        if (token == null || token.isBlank()) {
            forward(request, response, "/view/public/transfer-error.jsp", "Token no válido.");
            return;
        }

        try {
            PetTransferRequestDAO dao = new PetTransferRequestDAO();
            PetTransferRequest transfer = dao.findByToken(token);

            if (transfer == null) {
                forward(request, response, "/view/public/transfer-error.jsp", "El enlace de adopción no existe o ya no es válido.");
                return;
            }

            // ¿Expirado?
            if (transfer.getExpiresAt() != null
                    && transfer.getExpiresAt().before(new Timestamp(System.currentTimeMillis()))) {
                request.setAttribute("transfer", transfer);
                request.getRequestDispatcher("/view/public/transfer-expired.jsp").forward(request, response);
                return;
            }

            // ¿Ya procesado?
            if ("accepted".equals(transfer.getStatus())) {
                request.setAttribute("transfer", transfer);
                loadPetInfo(request, transfer.getIdPet());
                request.getRequestDispatcher("/view/public/transfer-completed.jsp").forward(request, response);
                return;
            }
            if ("rejected".equals(transfer.getStatus())) {
                forward(request, response, "/view/public/transfer-error.jsp", "Esta solicitud de adopción ya fue rechazada.");
                return;
            }

            // ¿Sesión activa? ¿Email coincide?
            String sessionEmail = (String) request.getSession().getAttribute("user"); // email guardado en login
            boolean loggedIn  = sessionEmail != null;
            boolean emailMatch = loggedIn && sessionEmail.equalsIgnoreCase(transfer.getAdopterEmail());

            request.setAttribute("transfer",   transfer);
            request.setAttribute("loggedIn",   loggedIn);
            request.setAttribute("emailMatch", emailMatch);
            request.setAttribute("sessionEmail", sessionEmail);
            loadPetInfo(request, transfer.getIdPet());

            request.getRequestDispatcher("/view/public/accept-transfer.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            forward(request, response, "/view/public/transfer-error.jsp", "Error interno al procesar la solicitud.");
        }
    }

    // ──────────────────────────────────────────────────────────────────
    // POST: procesar aceptación o rechazo
    // ──────────────────────────────────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token  = request.getParameter("token");
        String action = request.getParameter("action"); // "accept" | "reject"

        if (token == null || action == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        // Debe estar logueado y el email debe coincidir
        String sessionEmail = (String) request.getSession().getAttribute("user");
        if (sessionEmail == null) {
            response.sendRedirect(request.getContextPath() + "/login?returnUrl=" +
                java.net.URLEncoder.encode("/accept-transfer?token=" + token, "UTF-8"));
            return;
        }

        try {
            PetTransferRequestDAO dao = new PetTransferRequestDAO();
            PetTransferRequest transfer = dao.findByToken(token);

            if (transfer == null
                    || "accepted".equals(transfer.getStatus())
                    || "rejected".equals(transfer.getStatus())) {
                forward(request, response, "/view/public/transfer-error.jsp", "Esta solicitud ya no está disponible.");
                return;
            }

            // Verificar que el email de sesión coincide
            if (!sessionEmail.equalsIgnoreCase(transfer.getAdopterEmail())) {
                forward(request, response, "/view/public/transfer-error.jsp",
                        "Tu cuenta no corresponde al destinatario de esta transferencia.");
                return;
            }

            // ── RECHAZAR ──────────────────────────────────────────────
            if ("reject".equals(action)) {
                dao.updateStatus(transfer.getIdTransfer(), "rejected", null);
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(
                             "UPDATE pets SET adoption_status = 'available' WHERE id_pet = ?")) {
                    ps.setInt(1, transfer.getIdPet());
                    ps.executeUpdate();
                }
                forward(request, response, "/view/public/transfer-error.jsp",
                        "Has rechazado la adopción. La mascota quedó nuevamente disponible.");
                return;
            }

            // ── ACEPTAR ───────────────────────────────────────────────
            if ("accept".equals(action)) {
                Integer adopterId = (Integer) request.getSession().getAttribute("userId");

                try (Connection conn = DatabaseConnection.getConnection()) {

                    // Transferir propiedad: cambia id_user pero id_pet permanece igual → QR intacto
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE pets SET id_user = ?, adoption_status = 'adopted_transferred' WHERE id_pet = ?")) {
                        ps.setInt(1, adopterId);
                        ps.setInt(2, transfer.getIdPet());
                        ps.executeUpdate();
                    }

                    // Agregar 1 slot al adoptante (para que el pet no quede fuera del límite)
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE users SET pet_limit = pet_limit + 1 WHERE id_user = ?")) {
                        ps.setInt(1, adopterId);
                        ps.executeUpdate();
                    }

                    // Marcar transferencia como aceptada
                    dao.updateStatus(transfer.getIdTransfer(), "accepted",
                            new Timestamp(System.currentTimeMillis()));

                    // Cargar nombre de mascota para la página de éxito
                    loadPetInfo(request, transfer.getIdPet());
                }

                request.setAttribute("transfer", transfer);
                request.getRequestDispatcher("/view/public/transfer-completed.jsp").forward(request, response);
                return;
            }

            // Acción desconocida
            response.sendRedirect(request.getContextPath() + "/");

        } catch (Exception e) {
            e.printStackTrace();
            forward(request, response, "/view/public/transfer-error.jsp", "Error interno al procesar la adopción.");
        }
    }

    // ──────────────────────────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────────────────────────
    private void loadPetInfo(HttpServletRequest request, int petId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT p.name_pet, p.photo_url, p.species, p.breed, u.name_user AS foundation_name " +
                     "FROM pets p JOIN users u ON p.id_user = u.id_user WHERE p.id_pet = ?")) {
            ps.setInt(1, petId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("petName",        rs.getString("name_pet"));
                    request.setAttribute("petPhoto",       rs.getString("photo_url"));
                    request.setAttribute("petSpecies",     rs.getString("species"));
                    request.setAttribute("petBreed",       rs.getString("breed"));
                    request.setAttribute("foundationName", rs.getString("foundation_name"));
                    request.setAttribute("petId",          petId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void forward(HttpServletRequest req, HttpServletResponse res, String path, String msg)
            throws ServletException, IOException {
        req.setAttribute("errorMsg", msg);
        req.getRequestDispatcher(path).forward(req, res);
    }
}