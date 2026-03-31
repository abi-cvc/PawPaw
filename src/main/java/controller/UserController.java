package controller;

import model.dao.UserDAO;
import model.dao.FoundationRequestDAO;
import model.entity.User;
import model.entity.FoundationRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Servlet implementation class UserController
 * Maneja el registro de nuevos usuarios
 */
@WebServlet("/register")
public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // Patrón para validar email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private UserDAO userDAO = new UserDAO();
    private FoundationRequestDAO foundationDAO = new FoundationRequestDAO();

    /**
     * Muestra el formulario de registro
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar si ya hay una sesión activa
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            response.sendRedirect(request.getContextPath() + "/user/panel");
            return;
        }

        // Verificar si viene con token de fundación aprobada
        String token = request.getParameter("token");
        if (token != null && !token.trim().isEmpty()) {
            FoundationRequest foundationReq = foundationDAO.findByToken(token);
            if (foundationReq != null && "approved".equals(foundationReq.getStatus())) {
                request.setAttribute("foundationToken", token);
                request.setAttribute("foundationEmail", foundationReq.getEmail());
                request.setAttribute("foundationName", foundationReq.getFoundationName());
                request.setAttribute("name", foundationReq.getContactName());
            } else {
                request.setAttribute("error", "El enlace de registro no es válido o ya fue utilizado.");
            }
        }

        request.setAttribute("googleClientId", System.getenv("GOOGLE_CLIENT_ID"));
        request.getRequestDispatcher("/view/internalUser/register.jsp").forward(request, response);
    }

    /**
     * Procesa el formulario de registro
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener parámetros del formulario
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String foundationToken = request.getParameter("foundationToken");

        logger.info("Intento de registro - Nombre: {}, Email: {}", name, email);

        // Validaciones
        String errorMessage = validateRegistration(name, email, password, confirmPassword);

        if (errorMessage != null) {
            logger.info("Error de validacion: {}", errorMessage);
            request.setAttribute("error", errorMessage);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            // Repoblar datos de fundación si aplica
            if (foundationToken != null && !foundationToken.isEmpty()) {
                FoundationRequest fr = foundationDAO.findByToken(foundationToken);
                if (fr != null) {
                    request.setAttribute("foundationToken", foundationToken);
                    request.setAttribute("foundationEmail", fr.getEmail());
                    request.setAttribute("foundationName", fr.getFoundationName());
                }
            }
            request.setAttribute("googleClientId", System.getenv("GOOGLE_CLIENT_ID"));
            request.getRequestDispatcher("/view/internalUser/register.jsp").forward(request, response);
            return;
        }

        // Verificar si es registro de fundación con token
        boolean isFoundationRegistration = false;
        FoundationRequest foundationReq = null;
        if (foundationToken != null && !foundationToken.isEmpty()) {
            foundationReq = foundationDAO.findByToken(foundationToken);
            if (foundationReq != null && "approved".equals(foundationReq.getStatus())) {
                // Forzar el email de la solicitud aprobada
                email = foundationReq.getEmail();
                isFoundationRegistration = true;
            }
        }

        String gClientId = System.getenv("GOOGLE_CLIENT_ID");

        // Intentar registrar usuario
        if (isFoundationRegistration) {
            if (registerFoundationUser(name, email, password, foundationReq)) {
                logger.info("Fundacion registrada exitosamente: {}", foundationReq.getFoundationName());
                request.setAttribute("success", "¡Cuenta de fundación creada exitosamente! Por favor inicia sesión.");
                request.setAttribute("email", email);
                request.setAttribute("googleClientId", gClientId);
                request.getRequestDispatcher("/view/internalUser/login.jsp").forward(request, response);
            } else {
                logger.error("Error al registrar fundacion");
                request.setAttribute("error", "El email ya está registrado o hubo un error. Intenta iniciar sesión.");
                request.setAttribute("name", name);
                request.setAttribute("email", email);
                request.setAttribute("foundationToken", foundationToken);
                request.setAttribute("foundationEmail", email);
                request.setAttribute("foundationName", foundationReq.getFoundationName());
                request.setAttribute("googleClientId", gClientId);
                request.getRequestDispatcher("/view/internalUser/register.jsp").forward(request, response);
            }
        } else {
            if (registerUser(name, email, password)) {
                logger.info("Usuario registrado exitosamente");
                request.setAttribute("success", "Cuenta creada exitosamente. Por favor inicia sesión.");
                request.setAttribute("email", email);
                request.setAttribute("googleClientId", gClientId);
                request.getRequestDispatcher("/view/internalUser/login.jsp").forward(request, response);
            } else {
                logger.error("Error: Email ya existe o error en BD");
                request.setAttribute("error", "El email ya está registrado. Intenta con otro o inicia sesión.");
                request.setAttribute("name", name);
                request.setAttribute("email", email);
                request.setAttribute("googleClientId", gClientId);
                request.getRequestDispatcher("/view/internalUser/register.jsp").forward(request, response);
            }
        }
    }

    /**
     * Valida los datos del registro
     */
    private String validateRegistration(String name, String email, String password, String confirmPassword) {
        // Validar campos vacíos
        if (name == null || name.trim().isEmpty()) {
            return "El nombre es obligatorio";
        }

        if (email == null || email.trim().isEmpty()) {
            return "El email es obligatorio";
        }

        if (password == null || password.isEmpty()) {
            return "La contraseña es obligatoria";
        }

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            return "Debes confirmar la contraseña";
        }

        // Validar formato de email
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "El formato del email no es válido";
        }

        // Validar longitud de contraseña
        if (password.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres";
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            return "Las contraseñas no coinciden";
        }

        // Validar nombre (mínimo 2 caracteres)
        if (name.trim().length() < 2) {
            return "El nombre debe tener al menos 2 caracteres";
        }

        return null; // Todo válido
    }

    /**
     * Registra un nuevo usuario en el sistema
     * AHORA SÍ GUARDA EN LA BASE DE DATOS
     */
    private boolean registerFoundationUser(String name, String email, String password, FoundationRequest foundationReq) {
        try {
            if (userDAO.emailExists(email.trim())) {
                return false;
            }

            User newUser = new User();
            newUser.setNameUser(name.trim());
            newUser.setEmail(email.trim());
            newUser.setPassword(password);
            newUser.setRol("user");
            newUser.setActive(true);
            newUser.setPetLimit(9999); // Slots ilimitados
            newUser.setIsPartner(true);
            newUser.setPartnerBadge("Aliados PawPaw");

            boolean created = userDAO.createFoundation(newUser);

            if (created) {
                // Invalidar el token para que no se use de nuevo
                foundationReq.setStatus("registered");
                foundationDAO.markAsRegistered(foundationReq.getIdRequest());
                logger.info("Fundacion guardada en BD con ID: {}", newUser.getIdUser());
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Excepcion al registrar fundacion", e);
            return false;
        }
    }

    private boolean registerUser(String name, String email, String password) {
        try {
            // 1. Verificar que el email no exista
            if (userDAO.emailExists(email.trim())) {
                logger.info("Email ya existe en BD");
                return false;
            }

            logger.info("Email disponible, creando usuario...");

            // 2. Crear objeto User
            User newUser = new User();
            newUser.setNameUser(name.trim());
            newUser.setEmail(email.trim());
            newUser.setPassword(password); // UserDAO se encarga de hashear con BCrypt
            newUser.setRol("user"); // Rol por defecto
            newUser.setActive(true);

            // 3. Insertar en BD usando UserDAO
            boolean created = userDAO.create(newUser);

            if (created) {
                logger.info("Usuario guardado en BD con ID: {}", newUser.getIdUser());
                return true;
            } else {
                logger.error("Error al guardar en BD");
                return false;
            }

        } catch (Exception e) {
            logger.error("Excepcion al registrar usuario", e);
            return false;
        }
    }
}