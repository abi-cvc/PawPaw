package controller;

import model.dao.UserDAO;
import model.entity.User;

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
 */
@WebServlet("/register")
public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Patrón para validar email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private UserDAO userDAO = new UserDAO();

    /**
     * Muestra el formulario de registro
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Verificar si ya hay una sesión activa
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            // Si ya está logueado, redirigir al panel
            response.sendRedirect(request.getContextPath() + "/user/panel");
            return;
        }
        
        // Mostrar la página de registro
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
        
        System.out.println("🔵 Intento de registro:");
        System.out.println("   Nombre: " + name);
        System.out.println("   Email: " + email);
        
        // Validaciones
        String errorMessage = validateRegistration(name, email, password, confirmPassword);
        
        if (errorMessage != null) {
            System.out.println("❌ Error de validación: " + errorMessage);
            // Hay errores, volver al formulario
            request.setAttribute("error", errorMessage);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/view/internalUser/register.jsp").forward(request, response);
            return;
        }
        
        // Intentar registrar usuario
        if (registerUser(name, email, password)) {
            System.out.println("✅ Usuario registrado exitosamente");
            // Registro exitoso, redirigir al login con mensaje
            request.setAttribute("success", "Cuenta creada exitosamente. Por favor inicia sesión.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/view/internalUser/login.jsp").forward(request, response);
        } else {
            System.out.println("❌ Error: Email ya existe o error en BD");
            // Error al registrar (ej: email ya existe)
            request.setAttribute("error", "El email ya está registrado. Intenta con otro o inicia sesión.");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/view/internalUser/register.jsp").forward(request, response);
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
    private boolean registerUser(String name, String email, String password) {
        try {
            // 1. Verificar que el email no exista
            if (userDAO.emailExists(email.trim())) {
                System.out.println("   ⚠️ Email ya existe en BD");
                return false;
            }
            
            System.out.println("   📝 Email disponible, creando usuario...");
            
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
                System.out.println("   ✅ Usuario guardado en BD con ID: " + newUser.getIdUser());
                return true;
            } else {
                System.out.println("   ❌ Error al guardar en BD");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Excepción al registrar usuario:");
            e.printStackTrace();
            return false;
        }
    }
}