package controller;

import config.PayPalConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

@WebServlet("/api/paypal/create-donation")
public class CreateDonationOrderServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CreateDonationOrderServlet.class);

    private static final double MIN_AMOUNT = 1.00;
    private static final double MAX_AMOUNT = 500.00;

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
            double amount = requestData.getDouble("amount");

            // Validacion server-side del monto
            if (amount < MIN_AMOUNT || amount > MAX_AMOUNT) {
                JSONObject errorResponse = new JSONObject();
                errorResponse.put("status", "error");
                errorResponse.put("message", "El monto debe estar entre $" + MIN_AMOUNT + " y $" + MAX_AMOUNT);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(errorResponse.toString());
                return;
            }

            logger.info("Creando orden de donacion PayPal: ${}", String.format("%.2f", amount));

            String accessToken = getAccessToken();
            String orderId = createOrder(accessToken, amount);

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("id", orderId);
            jsonResponse.put("status", "success");

            response.getWriter().write(jsonResponse.toString());
            logger.info("Orden de donacion creada: {}", orderId);

        } catch (Exception e) {
            logger.error("Error al crear orden de donacion: {}", e.getMessage(), e);

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

    private String createOrder(String accessToken, double amount) throws IOException {
        URL url = new URL(PayPalConfig.getApiBase() + "/v2/checkout/orders");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject order = new JSONObject();
        order.put("intent", "CAPTURE");

        JSONArray purchaseUnits = new JSONArray();
        JSONObject unit = new JSONObject();

        JSONObject amountObj = new JSONObject();
        amountObj.put("currency_code", "USD");
        amountObj.put("value", String.format("%.2f", amount));

        unit.put("amount", amountObj);
        unit.put("description", "Donacion a PawPaw - $" + String.format("%.2f", amount));

        purchaseUnits.put(unit);
        order.put("purchase_units", purchaseUnits);

        JSONObject appContext = new JSONObject();
        appContext.put("brand_name", "PawPaw");
        appContext.put("landing_page", "NO_PREFERENCE");
        appContext.put("user_action", "PAY_NOW");

        order.put("application_context", appContext);

        OutputStream os = conn.getOutputStream();
        os.write(order.toString().getBytes(StandardCharsets.UTF_8));
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
        return jsonResponse.getString("id");
    }
}
