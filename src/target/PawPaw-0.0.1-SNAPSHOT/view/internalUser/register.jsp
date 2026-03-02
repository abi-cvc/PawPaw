<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Cuenta - PawPaw</title>
    
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
                <h1>Crear Cuenta</h1>
                <p>Únete a PawPaw hoy</p>
            </div>
            
            <% 
                String error = (String) request.getAttribute("error");
                
                if (error != null && !error.isEmpty()) {
            %>
                <div class="mensaje mensaje-error">
                    ⚠️ <%= error %>
                </div>
            <% } %>
            
            <form action="<%= request.getContextPath() %>/register" method="post" id="registerForm">
                
                <div class="form-group">
                    <label for="name" class="form-label required">Nombre completo</label>
                    <input 
                        type="text" 
                        id="name" 
                        name="name" 
                        class="form-input" 
                        placeholder="Ej: María García"
                        value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : "" %>"
                        required
                        autocomplete="name">
                </div>
                
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
                        placeholder="Mínimo 6 caracteres"
                        required
                        autocomplete="new-password">
                </div>
                
                <div class="form-group">
                    <label for="confirmPassword" class="form-label required">Confirmar contraseña</label>
                    <input 
                        type="password" 
                        id="confirmPassword" 
                        name="confirmPassword" 
                        class="form-input" 
                        placeholder="Repite tu contraseña"
                        required
                        autocomplete="new-password">
                </div>
                
                <button type="submit" class="btn btn-primario btn-grande" style="width: 100%; margin-top: 1rem;">
                    Crear Cuenta
                </button>
            </form>
            
            <div class="form-footer" style="margin-top: 2rem; text-align: center;">
                <p style="color: #666; margin-bottom: 1rem;">¿Ya tienes una cuenta?</p>
                <a href="<%= request.getContextPath() %>/login" class="btn btn-secundario" style="width: 100%;">
                    Iniciar Sesión
                </a>
            </div>
        </div>
    </div>
    
    <script src="<%= request.getContextPath() %>/js/main.js"></script>
</body>
</html>
