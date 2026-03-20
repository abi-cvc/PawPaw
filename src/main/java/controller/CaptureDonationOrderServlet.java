package controller;

import config.PayPalConfig;
import model.dao.DonationDAO;
import model.entity.Donation;
import service.EmailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.JSONObject;
import org.json.JSONArray;

@WebServlet("/api/paypal/capture-donation")
public class CaptureDonationOrderServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CaptureDonationOrderServlet.class);

    private DonationDAO donationDAO = new DonationDAO();
    private EmailService emailService = new EmailService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject requestData = new JSONObject(sb.toString());
            String orderId = requestData.getString("orderID");
            String donorName = requestData.optString("donorName", null);
            String donorEmail = requestData.optString("donorEmail", null);
            String message = requestData.optString("message", null);
            boolean isAnonymous = requestData.optBoolean("isAnonymous", false);

            // Si es anonimo, limpiar nombre y email
            if (isAnonymous) {
                donorName = null;
                donorEmail = null;
            }

            // Obtener userId de sesion si existe (no requerido)
            Integer userId = null;
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("userId") != null) {
                userId = (Integer) session.getAttribute("userId");
            }

            logger.info("Capturando donacion PayPal - Order: {}, Anonimo: {}", orderId, isAnonymous);

            String accessToken = getAccessToken();
            JSONObject captureResult = captureOrder(accessToken, orderId);

            String status = captureResult.getString("status");

            if ("COMPLETED".equals(status)) {
                // Extraer monto y capture ID del resultado
                JSONArray purchaseUnits = captureResult.getJSONArray("purchase_units");
                JSONObject firstUnit = purchaseUnits.getJSONObject(0);
                JSONObject payments = firstUnit.getJSONObject("payments");
                JSONArray captures = payments.getJSONArray("captures");
                JSONObject capture = captures.getJSONObject(0);

                String capturedAmount = capture.getJSONObject("amount").getString("value");
                String captureId = capture.getString("id");

                // Guardar donacion en BD
                Donation donation = new Donation();
                donation.setIdUser(userId);
                donation.setDonorName(donorName);
                donation.setDonorEmail(donorEmail);
                donation.setAmount(new BigDecimal(capturedAmount));
                donation.setMessage(message);
                donation.setAnonymous(isAnonymous);
                donation.setPaymentStatus("completed");
                donation.setPaypalOrderId(orderId);
                donation.setPaypalCaptureId(captureId);

                boolean saved = donationDAO.create(donation);

                if (saved) {
                    // Enviar email de confirmacion si hay email
                    if (donorEmail != null && !donorEmail.isEmpty()) {
                        sendDonationConfirmationEmail(donorName, donorEmail, capturedAmount, captureId);
                    }

                    logger.info("Donacion completada: ${} - Order: {}", capturedAmount, orderId);

                    JSONObject successResponse = new JSONObject();
                    successResponse.put("status", "success");
                    successResponse.put("message", "Donacion procesada exitosamente");

                    response.getWriter().write(successResponse.toString());
                } else {
                    throw new Exception("Error al guardar donacion en base de datos");
                }
            } else {
                throw new Exception("Pago no completado. Estado: " + status);
            }

        } catch (Exception e) {
            logger.error("Error al capturar donacion", e);

            JSONObject errorResponse = new JSONObject();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(errorResponse.toString());
        }
    }

    private String getAccessToken() throws IOException {
        String auth = PayPalConfig.getClientId() + ":" + PayPalConfig.getSecret();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        URL url = new URL(PayPalConfig.getApiBase() + "/v1/oauth2/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        String data = "grant_type=client_credentials";
        OutputStream os = conn.getOutputStream();
        os.write(data.getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder resp = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            resp.append(line);
        }
        br.close();

        JSONObject jsonResponse = new JSONObject(resp.toString());
        return jsonResponse.getString("access_token");
    }

    private JSONObject captureOrder(String accessToken, String orderId) throws IOException {
        URL url = new URL(PayPalConfig.getApiBase() + "/v2/checkout/orders/" + orderId + "/capture");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder resp = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            resp.append(line);
        }
        br.close();

        return new JSONObject(resp.toString());
    }

    private void sendDonationConfirmationEmail(String name, String email, String amount, String transactionId) {
        try {
            String displayName = (name != null && !name.isEmpty()) ? name : "Donante";

            String subject = "Gracias por tu donacion - PawPaw";
            String messageBody = "Hola " + displayName + ",\n\n" +
                    "Tu donacion ha sido procesada exitosamente.\n\n" +
                    "DETALLES DE LA DONACION:\n\n" +
                    "Monto: $" + amount + " USD\n" +
                    "ID de transaccion: " + transactionId + "\n" +
                    "Metodo de pago: PayPal\n\n" +
                    "Tu generosidad nos ayuda a seguir protegiendo mascotas y conectandolas con sus familias.\n\n" +
                    "Gracias por apoyar a PawPaw!\n\n" +
                    "Saludos,\n" +
                    "Equipo PawPaw";

            emailService.sendNotificationEmail(email, displayName, subject, messageBody);

        } catch (Exception e) {
            logger.error("Error al enviar email de confirmacion de donacion: {}", e.getMessage());
        }
    }
}
