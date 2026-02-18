<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recuperar Contraseña - PawPaw</title>
    
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
                <h1>¿Olvidaste tu contraseña?</h1>
                <p>No te preocupes, te ayudaremos a recuperarla</p>
            </div>
            
            <% 
                String error = (String) request.getAttribute("error");
                String success = (String) request.getAttribute("success");
                Boolean emailSent = (Boolean) request.getAttribute("emailSent");
                
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
            
            <% if (emailSent != null && emailSent) { %>
                <!-- Si el email fue enviado, mostrar instrucciones -->
                <div style="text-align: center; padding: 2rem 0;">
                    <div style="font-size: 4rem; margin-bottom: 1rem;">📧</div>
                    <h3 style="color: var(--color-1); margin-bottom: 1rem;">Revisa tu email</h3>
                    <p style="color: #666; margin-bottom: 2rem;">
                        Si el email existe en nuestro sistema, recibirás un enlace para restablecer tu contraseña.<br>
                        <strong>Revisa tu bandeja de entrada y spam.</strong>
                    </p>
                    <p style="color: #999; font-size: 0.9rem;">
                        El enlace expirará en <strong>1 hora</strong>.
                    </p>
                </div>
                
                <div style="text-align: center; margin-top: 2rem;">
                    <a href="<%= request.getContextPath() %>/login" class="btn btn-primario" style="width: 100%;">
                        Volver al Login
                    </a>
                </div>
                
            <% } else { %>
                <!-- Formulario para ingresar email -->
                <form action="<%= request.getContextPath() %>/forgot-password" method="post" id="forgotPasswordForm">
                    
                    <div class="form-group">
                        <label for="email" class="form-label required">Email</label>
                        <input 
                            type="email" 
                            id="email" 
                            name="email" 
                            class="form-input" 
                            placeholder="tucorreo@ejemplo.com"
                            required
                            autocomplete="email"
                            autofocus>
                        <small style="color: #666; font-size: 0.875rem; display: block; margin-top: 0.5rem;">
                            Ingresa el email con el que te registraste
                        </small>
                    </div>
                    
                    <button type="submit" class="btn btn-primario btn-grande" style="width: 100%; margin-top: 1.5rem;">
                        Enviar Enlace de Recuperación
                    </button>
                </form>
                
                <div class="form-footer" style="margin-top: 2rem; text-align: center;">
                    <p style="color: #666; margin-bottom: 1rem;">¿Recordaste tu contraseña?</p>
                    <a href="<%= request.getContextPath() %>/login" class="btn btn-secundario" style="width: 100%;">
                        Volver al Login
                    </a>
                </div>
            <% } %>
            
        </div>
    </div>
    
    <script src="<%= request.getContextPath() %>/js/main.js"></script>
</body>
</html>