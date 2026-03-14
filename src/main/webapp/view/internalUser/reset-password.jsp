<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Restablecer Contraseña - PawPaw</title>
    
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
                <h1>Restablecer Contraseña</h1>
                <p>Crea una nueva contraseña segura</p>
            </div>
            
            <c:if test="${not empty error}">
                <div class="mensaje mensaje-error">
                    ⚠️ <c:out value="${error}"/>
                </div>

                <c:choose>
                    <c:when test="${tokenExpired}">
                        <!-- Token expirado - ofrecer solicitar uno nuevo -->
                        <div style="text-align: center; margin-top: 2rem;">
                            <p style="color: #666; margin-bottom: 1rem;">
                                ¿Necesitas un nuevo enlace de recuperación?
                            </p>
                            <a href="${pageContext.request.contextPath}/forgot-password" class="btn btn-primario" style="width: 100%;">
                                Solicitar Nuevo Enlace
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- Error genérico - volver al login -->
                        <div style="text-align: center; margin-top: 2rem;">
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-secundario" style="width: 100%;">
                                Volver al Login
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>

            </c:if>
            <c:if test="${empty error and tokenValid}">
                <!-- Token válido - mostrar formulario -->
                <form action="${pageContext.request.contextPath}/reset-password" method="post" id="resetPasswordForm">
                    <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                    <input type="hidden" name="token" value="<c:out value="${token}"/>">
                    
                    <div class="form-group">
                        <label for="newPassword" class="form-label required">Nueva Contraseña</label>
                        <input 
                            type="password" 
                            id="newPassword" 
                            name="newPassword" 
                            class="form-input" 
                            placeholder="••••••••"
                            required
                            minlength="6"
                            autocomplete="new-password"
                            autofocus>
                        <small style="color: #666; font-size: 0.875rem; display: block; margin-top: 0.5rem;">
                            Mínimo 6 caracteres
                        </small>
                    </div>
                    
                    <div class="form-group">
                        <label for="confirmPassword" class="form-label required">Confirmar Contraseña</label>
                        <input 
                            type="password" 
                            id="confirmPassword" 
                            name="confirmPassword" 
                            class="form-input" 
                            placeholder="••••••••"
                            required
                            minlength="6"
                            autocomplete="new-password">
                    </div>
                    
                    <!-- Indicador de fuerza de contraseña -->
                    <div id="passwordStrength" style="margin-top: 0.5rem; display: none;">
                        <div style="height: 4px; background: #e0e0e0; border-radius: 2px; overflow: hidden;">
                            <div id="strengthBar" style="height: 100%; width: 0%; transition: all 0.3s;"></div>
                        </div>
                        <small id="strengthText" style="color: #666; font-size: 0.875rem; display: block; margin-top: 0.5rem;"></small>
                    </div>
                    
                    <button type="submit" class="btn btn-primario btn-grande" style="width: 100%; margin-top: 1.5rem;">
                        Cambiar Contraseña
                    </button>
                </form>
                
                <!-- Consejos de seguridad -->
                <div style="margin-top: 2rem; padding: 1rem; background: #f5f5f5; border-radius: 8px;">
                    <h4 style="margin: 0 0 0.5rem 0; color: var(--color-1); font-size: 0.9rem;">
                        💡 Consejos para una contraseña segura:
                    </h4>
                    <ul style="margin: 0; padding-left: 1.5rem; color: #666; font-size: 0.875rem; line-height: 1.6;">
                        <li>Al menos 8 caracteres</li>
                        <li>Combina mayúsculas y minúsculas</li>
                        <li>Incluye números y símbolos</li>
                        <li>No uses información personal</li>
                    </ul>
                </div>
                
            </c:if>
            <c:if test="${empty error and not tokenValid}">
                <!-- Sin token - mostrar mensaje y botón para solicitar recuperación -->
                <div style="text-align: center; padding: 2rem 0;">
                    <div style="font-size: 4rem; margin-bottom: 1rem;">🔒</div>
                    <h3 style="color: var(--color-1); margin-bottom: 1rem;">Enlace Inválido</h3>
                    <p style="color: #666; margin-bottom: 2rem;">
                        El enlace de recuperación no es válido o ha expirado.
                    </p>
                </div>
                
                <a href="${pageContext.request.contextPath}/forgot-password" class="btn btn-primario" style="width: 100%;">
                    Solicitar Recuperación
                </a>
            </c:if>
            
        </div>
    </div>
    
    <script>
        // Validación de contraseñas coincidentes
        const form = document.getElementById('resetPasswordForm');
        if (form) {
            const newPassword = document.getElementById('newPassword');
            const confirmPassword = document.getElementById('confirmPassword');
            const strengthIndicator = document.getElementById('passwordStrength');
            const strengthBar = document.getElementById('strengthBar');
            const strengthText = document.getElementById('strengthText');
            
            // Mostrar indicador de fuerza
            if (newPassword && strengthIndicator) {
                newPassword.addEventListener('input', function() {
                    const password = this.value;
                    if (password.length > 0) {
                        strengthIndicator.style.display = 'block';
                        
                        let strength = 0;
                        let text = '';
                        let color = '';
                        
                        // Calcular fuerza
                        if (password.length >= 6) strength += 25;
                        if (password.length >= 8) strength += 25;
                        if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength += 25;
                        if (/[0-9]/.test(password)) strength += 15;
                        if (/[^a-zA-Z0-9]/.test(password)) strength += 10;
                        
                        // Texto y color
                        if (strength < 40) {
                            text = 'Débil';
                            color = '#f44336';
                        } else if (strength < 70) {
                            text = 'Media';
                            color = '#ff9800';
                        } else {
                            text = 'Fuerte';
                            color = '#4caf50';
                        }
                        
                        strengthBar.style.width = strength + '%';
                        strengthBar.style.background = color;
                        strengthText.textContent = 'Seguridad: ' + text;
                        strengthText.style.color = color;
                    } else {
                        strengthIndicator.style.display = 'none';
                    }
                });
            }
            
            form.addEventListener('submit', function(e) {
                if (newPassword && confirmPassword) {
                    if (newPassword.value !== confirmPassword.value) {
                        e.preventDefault();
                        alert('⚠️ Las contraseñas no coinciden');
                        confirmPassword.focus();
                        return false;
                    }
                    
                    if (newPassword.value.length < 6) {
                        e.preventDefault();
                        alert('⚠️ La contraseña debe tener al menos 6 caracteres');
                        newPassword.focus();
                        return false;
                    }
                }
            });
        }
    </script>
    
</body>
</html>