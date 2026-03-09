package controller;

import config.PayPalConfig;
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
 * Servlet para crear órdenes de pago en PayPal
 */
@WebServlet("/api/paypal/create-order")
public class CreatePayPalOrderServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // Leer datos del request
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            JSONObject requestData = new JSONObject(sb.toString());
            int slots = requestData.getInt("slots");
            double amount = requestData.getDouble("amount");
            
            System.out.println("📝 Creando orden PayPal: " + slots + " slots por $" + amount);
            
            // Obtener access token
            String accessToken = getAccessToken();
            
            // Crear orden en PayPal
            String orderId = createOrder(accessToken, slots, amount);
            
            // Responder con el order ID
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("id", orderId);
            jsonResponse.put("status", "success");
            
            response.getWriter().write(jsonResponse.toString());
            
            System.out.println("✅ Orden creada: " + orderId);
            
        } catch (Exception e) {
            System.err.println("❌ Error al crear orden PayPal: " + e.getMessage());
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
     * Crear orden en PayPal
     */
    private String createOrder(String accessToken, int slots, double amount) throws IOException {
        URL url = new URL(PayPalConfig.getApiBase() + "/v2/checkout/orders");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        // Construir JSON de la orden
        JSONObject order = new JSONObject();
        order.put("intent", "CAPTURE");
        
        // Purchase units
        JSONArray purchaseUnits = new JSONArray();
        JSONObject unit = new JSONObject();
        
        // Amount
        JSONObject amountObj = new JSONObject();
        amountObj.put("currency_code", "USD");
        amountObj.put("value", String.format("%.2f", amount));
        
        unit.put("amount", amountObj);
        unit.put("description", slots + " slot(s) adicional(es) - PawPaw");
        
        purchaseUnits.put(unit);
        order.put("purchase_units", purchaseUnits);
        
        // Application context
        JSONObject appContext = new JSONObject();
        appContext.put("brand_name", "PawPaw");
        appContext.put("landing_page", "NO_PREFERENCE");
        appContext.put("user_action", "PAY_NOW");
        appContext.put("return_url", "http://localhost:8080/PawPaw/paypal/success");
        appContext.put("cancel_url", "http://localhost:8080/PawPaw/paypal/cancel");
        
        order.put("application_context", appContext);
        
        // Enviar request
        OutputStream os = conn.getOutputStream();
        os.write(order.toString().getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.close();
        
        // Leer respuesta
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();
        
        JSONObject jsonResponse = new JSONObject(response.toString());
        return jsonResponse.getString("id");
    }
}
