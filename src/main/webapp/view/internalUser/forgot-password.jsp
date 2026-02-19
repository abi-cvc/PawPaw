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
                Boolean resent = (Boolean) request.getAttribute("resent");
                String email = (String) request.getAttribute("email");
                
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
                <!-- Si el email fue enviado, mostrar instrucciones y opción de reenviar -->
                <div style="text-align: center; padding: 2rem 0;">
                    <div style="font-size: 4rem; margin-bottom: 1rem;">📧</div>
                    
                    <% if (resent != null && resent) { %>
                        <h3 style="color: var(--color-1); margin-bottom: 1rem;">¡Enlace reenviado!</h3>
                        <p style="color: #666; margin-bottom: 1rem;">
                            Hemos enviado un nuevo enlace de recuperación a tu correo.
                        </p>
                    <% } else { %>
                        <h3 style="color: var(--color-1); margin-bottom: 1rem;">Revisa tu email</h3>
                        <p style="color: #666; margin-bottom: 1rem;">
                            Si el email existe en nuestro sistema, recibirás un enlace para restablecer tu contraseña.
                        </p>
                    <% } %>
                    
                    <p style="color: #666; margin-bottom: 1.5rem;">
                        <strong>Revisa tu bandeja de entrada y spam.</strong>
                    </p>
                    
                    <div style="background: #fff3cd; border-left: 4px solid #ffc107; padding: 1rem; border-radius: 8px; margin-bottom: 1.5rem; text-align: left;">
                        <p style="margin: 0; color: #856404; font-size: 0.9rem;">
                            ⚠️ <strong>El enlace expirará en 1 hora.</strong>
                        </p>
                    </div>
                    
                    <!-- Opción para reenviar -->
                    <div style="margin-top: 2rem; padding-top: 2rem; border-top: 1px solid #e0e0e0;">
                        <p style="color: #666; margin-bottom: 1rem; font-size: 0.95rem;">
                            ¿No recibiste el email?
                        </p>
                        
                        <form action="<%= request.getContextPath() %>/resend-password-reset" method="post" style="margin-bottom: 1rem;">
                            <input type="hidden" name="email" value="<%= email != null ? email : "" %>">
                            <button type="submit" class="btn btn-secundario" id="resendBtn" style="width: 100%;">
                                🔄 Reenviar Enlace
                            </button>
                        </form>
                        
                        <p style="color: #999; font-size: 0.85rem; margin-top: 0.5rem;">
                            Puedes reenviar el enlace después de 1 minuto
                        </p>
                    </div>
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
                            value="<%= email != null ? email : "" %>"
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
    
    <script>
        // Deshabilitar botón de reenviar por 60 segundos después de hacer click
        const resendBtn = document.getElementById('resendBtn');
        if (resendBtn) {
            resendBtn.addEventListener('click', function() {
                // Deshabilitar botón
                this.disabled = true;
                this.style.opacity = '0.6';
                this.style.cursor = 'not-allowed';
                
                let countdown = 60;
                const originalText = this.innerHTML;
                
                const timer = setInterval(() => {
                    countdown--;
                    this.innerHTML = `⏳ Espera ${countdown}s`;
                    
                    if (countdown <= 0) {
                        clearInterval(timer);
                        this.disabled = false;
                        this.style.opacity = '1';
                        this.style.cursor = 'pointer';
                        this.innerHTML = originalText;
                    }
                }, 1000);
            });
        }
    </script>
    
    <script src="<%= request.getContextPath() %>/js/main.js"></script>
</body>
</html>
