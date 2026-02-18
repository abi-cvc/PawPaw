<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - PawPaw</title>
    
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <div class="contenedor-formulario">
        <div class="tarjeta-formulario">
            
            <div class="formulario-encabezado">
                <a href="<%= request.getContextPath() %>/view/index.jsp">
                    <img src="<%= request.getContextPath() %>/images/logo.png" alt="Logo PawPaw">
                </a>
                <h1>Iniciar Sesión</h1>
                <p>Bienvenido de nuevo</p>
            </div>
            
            <% 
                String error = (String) request.getAttribute("error");
                String success = (String) request.getAttribute("success");
                String passwordChanged = request.getParameter("passwordChanged");
                
                if (passwordChanged != null && passwordChanged.equals("true")) {
                    success = "¡Contraseña actualizada exitosamente! Ya puedes iniciar sesión.";
                }
                
                if (error != null && !error.isEmpty()) {
            %>
                <div class="mensaje mensaje-error">
                    ⚠️ <%= error %>
                </div>
            <% } %>
            
            <% if (success != null && !success.isEmpty()) { %>
                <div class="mensaje mensaje-exito">
                    ✅ <%= success %>
                </div>
            <% } %>
            
            <form action="<%= request.getContextPath() %>/login" method="post" id="loginForm">
                
                <div class="form-group">
                    <label for="email" class="form-label required">Email</label>
                    <input 
                        type="email" 
                        id="email" 
                        name="email" 
                        class="form-input" 
                        placeholder="tucorreo@ejemplo.com"
                        value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>"
                        required
                        autocomplete="email">
                </div>
                
                <div class="form-group">
                    <label for="password" class="form-label required">Contraseña</label>
                    <input 
                        type="password" 
                        id="password" 
                        name="password" 
                        class="form-input" 
                        placeholder="••••••••"
                        required
                        autocomplete="current-password">
                </div>
                
                <div style="text-align: right; margin-top: 0.5rem;">
                    <a href="<%= request.getContextPath() %>/forgot-password" style="color: var(--color-2); text-decoration: none; font-size: 0.9rem;">
                        ¿Olvidaste tu contraseña?
                    </a>
                </div>
                
                <button type="submit" class="btn btn-primario btn-grande" style="width: 100%; margin-top: 1rem;">
                    Iniciar Sesión
                </button>
            </form>
            
            <div class="form-footer" style="margin-top: 2rem; text-align: center;">
                <p style="color: #666; margin-bottom: 1rem;">¿No tienes una cuenta?</p>
                <a href="<%= request.getContextPath() %>/register" class="btn btn-secundario" style="width: 100%;">
                    Crear Cuenta
                </a>
            </div>
        </div>
    </div>
    
    <script src="<%= request.getContextPath() %>/js/main.js"></script>
</body>
</html>
