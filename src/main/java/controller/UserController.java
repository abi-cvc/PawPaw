package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Servlet implementation class UserController
 * Maneja el registro de nuevos usuarios
 * Basado en el diseño OOD del sistema PawPaw
 */
@WebServlet("/register")
public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Patrón para validar email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    /**
     * Muestra el formulario de registro
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Verificar si ya hay una sesión activa
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // Si ya está logueado, redirigir al panel
            response.sendRedirect(request.getContextPath() + "/user/panel");
            return;
        }
        
        // Mostrar la página de registro
        request.getRequestDispatcher("/view/internalUser/register.jsp").forward(request, response);
    }

    /**
     * Procesa el formulario de registro
     * Crea un nuevo usuario en el sistema
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Obtener parámetros del formulario
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validaciones
        String errorMessage = validateRegistration(name, email, password, confirmPassword);
        
        if (errorMessage != null) {
            // Hay errores, volver al formulario
            request.setAttribute("error", errorMessage);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/view/internalUser/register.jsp").forward(request, response);
            return;
        }
        
        // TODO: Crear usuario en la base de datos usando UserDAO
        // User newUser = new User(name, email, hashedPassword);
        // UserDAO userDAO = new UserDAO();
        // boolean success = userDAO.createUser(newUser);
        
        // Simulación de registro exitoso (REEMPLAZAR CON LÓGICA REAL)
        if (registerUser(name, email, password)) {
            // Registro exitoso, redirigir al login con mensaje
            request.setAttribute("success", "Cuenta creada exitosamente. Por favor inicia sesión.");
            request.getRequestDispatcher("/view/internalUser/login.jsp").forward(request, response);
        } else {
            // Error al registrar (ej: email ya existe)
            request.setAttribute("error", "El email ya está registrado. Intenta con otro o inicia sesión.");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/view/internalUser/register.jsp").forward(request, response);
        }
    }
    
    /**
     * Valida los datos del registro
     * 
     * @return null si todo es válido, mensaje de error en caso contrario
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
     * 
     * @param name Nombre del usuario
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return true si el registro fue exitoso, false en caso contrario
     */
    private boolean registerUser(String name, String email, String password) {
        // TODO: Implementar inserción en base de datos
        // 1. Verificar que el email no exista (findUser)
        // 2. Hashear la contraseña usando BCrypt
        // 3. Crear objeto User con los datos
        // 4. Insertar en la BD usando UserDAO
        // 5. Generar fecha de registro
        
        // Ejemplo de implementación con DAO (a implementar):
        /*
        UserDAO userDAO = new UserDAO();
        
        // Verificar si el email ya existe
        if (userDAO.emailExists(email)) {
            return false;
        }
        
        // Hashear contraseña
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        // Crear usuario
        User newUser = new User();
        newUser.setNameUser(name);
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        newUser.setRegistrationDate(new Date());
        newUser.setRol("user"); // Rol por defecto
        
        // Insertar en BD
        return userDAO.create(newUser);
        */
        
        // Por ahora retorna true (éxito simulado)
        return true;
    }
}