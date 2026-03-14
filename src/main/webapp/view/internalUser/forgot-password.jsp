<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recuperar Contraseña - PawPaw</title>
    
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="contenedor-formulario">
        <div class="tarjeta-formulario">
            
            <div class="formulario-encabezado">
                <a href="${pageContext.request.contextPath}/view/index.jsp">
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo PawPaw">
                </a>
                <h1>¿Olvidaste tu contraseña?</h1>
                <p>No te preocupes, te ayudaremos a recuperarla</p>
            </div>
            
            <c:if test="${not empty error}">
                <div class="mensaje mensaje-error">
                    ⚠️ <c:out value="${error}"/>
                </div>
            </c:if>

            <c:if test="${not empty success}">
                <div class="mensaje mensaje-exito">
                    ✅ <c:out value="${success}"/>
                </div>
            </c:if>
            
            <c:if test="${emailSent}">
                <!-- Si el email fue enviado, mostrar instrucciones y opción de reenviar -->
                <div style="text-align: center; padding: 2rem 0;">
                    <div style="font-size: 4rem; margin-bottom: 1rem;">📧</div>
                    
                    <c:choose>
                        <c:when test="${resent}">
                            <h3 style="color: var(--color-1); margin-bottom: 1rem;">¡Enlace reenviado!</h3>
                            <p style="color: #666; margin-bottom: 1rem;">
                                Hemos enviado un nuevo enlace de recuperación a tu correo.
                            </p>
                        </c:when>
                        <c:otherwise>
                            <h3 style="color: var(--color-1); margin-bottom: 1rem;">Revisa tu email</h3>
                            <p style="color: #666; margin-bottom: 1rem;">
                                Si el email existe en nuestro sistema, recibirás un enlace para restablecer tu contraseña.
                            </p>
                        </c:otherwise>
                    </c:choose>
                    
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
                        
                        <form action="${pageContext.request.contextPath}/resend-password-reset" method="post" style="margin-bottom: 1rem;">
                            <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                            <input type="hidden" name="email" value="<c:out value="${email}"/>">
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
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-primario" style="width: 100%;">
                        Volver al Login
                    </a>
                </div>
                
            </c:if>
            <c:if test="${not emailSent}">
                <!-- Formulario para ingresar email -->
                <form action="${pageContext.request.contextPath}/forgot-password" method="post" id="forgotPasswordForm">
                    <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">

                    <div class="form-group">
                        <label for="email" class="form-label required">Email</label>
                        <input 
                            type="email" 
                            id="email" 
                            name="email" 
                            class="form-input" 
                            placeholder="tucorreo@ejemplo.com"
                            value="<c:out value="${email}"/>"
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
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-secundario" style="width: 100%;">
                        Volver al Login
                    </a>
                </div>
            </c:if>
            
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
    
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
