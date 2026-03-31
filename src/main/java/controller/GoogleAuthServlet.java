package controller;

import model.dao.UserDAO;
import model.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Maneja la autenticacion con Google Sign-In.
 * Recibe el ID token de Google, lo verifica, y crea/autentica al usuario.
 */
@WebServlet("/auth/google/callback")
public class GoogleAuthServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(GoogleAuthServlet.class);
    private static final String GOOGLE_TOKENINFO_URL = "https://oauth2.googleapis.com/tokeninfo?id_token=";

    private UserDAO userDAO = new UserDAO();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String credential = request.getParameter("credential");

        String googleClientId = System.getenv("GOOGLE_CLIENT_ID");

        if (credential == null || credential.trim().isEmpty()) {
            logger.warn("Google Auth: credential vacio");
            forwardToLoginWithError(request, response, "Error al autenticar con Google. Intenta de nuevo.", googleClientId);
            return;
        }

        try {
            // Verificar el token con Google
            JsonObject tokenInfo = verifyGoogleToken(credential);

            if (tokenInfo == null) {
                logger.warn("Google Auth: token invalido");
                forwardToLoginWithError(request, response, "Token de Google invalido. Intenta de nuevo.", googleClientId);
                return;
            }

            // Verificar que el token es para nuestra app
            String clientId = System.getenv("GOOGLE_CLIENT_ID");
            String tokenAud = tokenInfo.has("aud") ? tokenInfo.get("aud").getAsString() : "";
            if (!tokenAud.equals(clientId)) {
                logger.warn("Google Auth: aud no coincide. Esperado: {}, Recibido: {}", clientId, tokenAud);
                forwardToLoginWithError(request, response, "Error de verificacion con Google.", googleClientId);
                return;
            }

            // Verificar que el email esta verificado
            boolean emailVerified = tokenInfo.has("email_verified")
                    && "true".equals(tokenInfo.get("email_verified").getAsString());
            if (!emailVerified) {
                forwardToLoginWithError(request, response, "Tu cuenta de Google no tiene email verificado.", googleClientId);
                return;
            }

            String email = tokenInfo.get("email").getAsString();
            String name = tokenInfo.has("name") ? tokenInfo.get("name").getAsString() : email;

            // Buscar usuario existente
            User user = userDAO.findByEmail(email);

            if (user == null) {
                // Crear cuenta nueva automaticamente
                user = new User();
                user.setNameUser(name);
                user.setEmail(email);
                // Generar password random (el usuario usa Google, no la necesita)
                user.setPassword(generateRandomPassword());
                user.setRol("user");
                user.setActive(true);

                boolean created = userDAO.create(user);
                if (!created) {
                    logger.error("Google Auth: error al crear usuario para {}", email);
                    forwardToLoginWithError(request, response, "Error al crear tu cuenta. Intenta de nuevo.", googleClientId);
                    return;
                }

                // Recargar usuario para obtener el ID generado
                user = userDAO.findByEmail(email);
                logger.info("Google Auth: cuenta creada para {}", email);
            }

            // Verificar que el usuario esta activo
            if (!user.getActive()) {
                forwardToLoginWithError(request, response, "Tu cuenta esta desactivada. Contacta al administrador.", googleClientId);
                return;
            }

            // Crear sesion (misma logica que AuthenticationController)
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getIdUser());
            session.setAttribute("user", user.getEmail());
            session.setAttribute("userName", user.getNameUser());
            session.setAttribute("userRole", user.getRol());

            logger.info("Google Auth: login exitoso - {} ({})", user.getEmail(), user.getRol());

            // Redirigir segun rol
            if ("admin".equals(user.getRol())) {
                response.sendRedirect(request.getContextPath() + "/admin/panel");
            } else {
                response.sendRedirect(request.getContextPath() + "/user/panel");
            }

        } catch (Exception e) {
            logger.error("Google Auth: error inesperado: {}", e.getMessage(), e);
            forwardToLoginWithError(request, response, "Error al autenticar con Google. Intenta de nuevo.", googleClientId);
        }
    }

    /**
     * Verifica el ID token con el endpoint de Google tokeninfo.
     */
    private JsonObject verifyGoogleToken(String idToken) {
        try {
            URL url = new URL(GOOGLE_TOKENINFO_URL + idToken);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();
            if (status != 200) {
                logger.warn("Google tokeninfo respondio con status {}", status);
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            return gson.fromJson(sb.toString(), JsonObject.class);

        } catch (Exception e) {
            logger.error("Error verificando token de Google: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Redirige al login mostrando un error y manteniendo el boton de Google.
     */
    private void forwardToLoginWithError(HttpServletRequest request, HttpServletResponse response,
                                          String error, String googleClientId) throws ServletException, IOException {
        request.setAttribute("error", error);
        request.setAttribute("googleClientId", googleClientId);
        request.getRequestDispatcher("/view/internalUser/login.jsp").forward(request, response);
    }

    /**
     * Genera un password aleatorio para usuarios de Google.
     * No lo necesitan porque se autentican via Google.
     */
    private String generateRandomPassword() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
