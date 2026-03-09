package controller;

import config.PayPalConfig;
import model.dao.UserDAO;
import model.entity.User;
import service.EmailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Servlet para capturar y confirmar pagos de PayPal
 */
@WebServlet("/api/paypal/capture-order")
public class CapturePayPalOrderServlet extends HttpServlet {
    
    private UserDAO userDAO = new UserDAO();
    private EmailService emailService = new EmailService();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Leer order ID del request
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            JSONObject requestData = new JSONObject(sb.toString());
            String orderId = requestData.getString("orderID");
            int slots = requestData.getInt("slots");
            
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            
            if (userId == null) {
                throw new Exception("Usuario no autenticado");
            }
            
            System.out.println("💳 Capturando pago PayPal - Order: " + orderId + ", User: " + userId);
            
            // Obtener access token
            String accessToken = getAccessToken();
            
            // Capturar pago en PayPal
            JSONObject captureResult = captureOrder(accessToken, orderId);
            
            String status = captureResult.getString("status");
            
            if ("COMPLETED".equals(status)) {
                // Pago exitoso - agregar slots al usuario
                boolean slotsAdded = userDAO.incrementPetLimit(userId, slots);
                
                if (slotsAdded) {
                    // Enviar email de confirmación
                    User user = userDAO.findById(userId);
                    if (user != null) {
                        sendConfirmationEmail(user, slots, captureResult);
                    }
                    
                    System.out.println("✅ Pago completado - " + slots + " slots agregados a usuario " + userId);
                    
                    JSONObject successResponse = new JSONObject();
                    successResponse.put("status", "success");
                    successResponse.put("message", "Pago procesado exitosamente");
                    successResponse.put("slots", slots);
                    
                    response.getWriter().write(successResponse.toString());
                } else {
                    throw new Exception("Error al agregar slots");
                }
            } else {
                throw new Exception("Pago no completado. Estado: " + status);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al capturar pago: " + e.getMessage());
            e.printStackTrace();
            
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(errorResponse.toString());
        }
    }
    
    /**
     * Obtener access token de PayPal
     */
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
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();
        
        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse.getString("access_token");
    }
    
    /**
     * Capturar orden en PayPal
     */
    private JSONObject captureOrder(String accessToken, String orderId) throws IOException {
        URL url = new URL(PayPalConfig.getApiBase() + "/v2/checkout/orders/" + orderId + "/capture");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        // Leer respuesta
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();
        
        return new JSONObject(response.toString());
    }
    
    /**
     * Enviar email de confirmación
     */
    private void sendConfirmationEmail(User user, int slots, JSONObject captureResult) {
        try {
            // Extraer información del pago
            JSONArray purchaseUnits = captureResult.getJSONArray("purchase_units");
            JSONObject firstUnit = purchaseUnits.getJSONObject(0);
            JSONObject payments = firstUnit.getJSONObject("payments");
            JSONArray captures = payments.getJSONArray("captures");
            JSONObject capture = captures.getJSONObject(0);
            
            String amount = capture.getJSONObject("amount").getString("value");
            String transactionId = capture.getString("id");
            
            String subject = "✅ Compra confirmada - PawPaw";
            String message = "Hola " + user.getNameUser() + ",\n\n" +
                           "¡Tu pago ha sido procesado exitosamente! 🎉\n\n" +
                           "━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                           "DETALLES DE LA COMPRA:\n" +
                           "━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                           "Slots adquiridos: " + slots + "\n" +
                           "Monto pagado: $" + amount + " USD\n" +
                           "ID de transacción: " + transactionId + "\n" +
                           "Método de pago: PayPal\n\n" +
                           "━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                           "Los slots han sido agregados a tu cuenta y ya puedes registrar más mascotas.\n\n" +
                           "¡Gracias por confiar en PawPaw! 🐾\n\n" +
                           "Saludos,\n" +
                           "Equipo PawPaw";
            
            emailService.sendNotificationEmail(user.getEmail(), user.getNameUser(), subject, message);
            
        } catch (Exception e) {
            System.err.println("⚠️ Error al enviar email de confirmación: " + e.getMessage());
            // No fallar si el email no se envía
        }
    }
}