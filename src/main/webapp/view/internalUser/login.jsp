<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - PawPaw</title>
    
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script src="https://accounts.google.com/gsi/client" async defer></script>
</head>
<body>
    <div class="contenedor-formulario">
        <div class="tarjeta-formulario">
            
            <div class="formulario-encabezado">
                <a href="${pageContext.request.contextPath}/view/index.jsp">
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo PawPaw">
                </a>
                <h1>Iniciar Sesión</h1>
                <p>Bienvenido de nuevo</p>
            </div>
            
            <c:set var="success" value="${success}" />
            <c:if test="${param.passwordChanged == 'true'}">
                <c:set var="success" value="¡Contraseña actualizada exitosamente! Ya puedes iniciar sesión." />
            </c:if>

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
            
            <!-- Google Sign-In -->
            <div id="g_id_onload"
                 data-client_id="${googleClientId}"
                 data-context="signin"
                 data-ux_mode="redirect"
                 data-login_uri="${pageContext.request.contextPath}/auth/google/callback"
                 data-auto_prompt="false">
            </div>
            <div class="google-btn-wrapper" style="display: flex; justify-content: center; margin-bottom: 1.5rem;">
                <div class="g_id_signin"
                     data-type="standard"
                     data-shape="rectangular"
                     data-theme="outline"
                     data-text="signin_with"
                     data-size="large"
                     data-locale="es"
                     data-logo_alignment="left"
                     data-width="100%">
                </div>
            </div>

            <div class="separador-o" style="display: flex; align-items: center; gap: 1rem; margin-bottom: 1.5rem;">
                <hr style="flex: 1; border: none; border-top: 1px solid #ddd;">
                <span style="color: #999; font-size: 0.85rem;">o</span>
                <hr style="flex: 1; border: none; border-top: 1px solid #ddd;">
            </div>

            <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm">
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
                    <a href="${pageContext.request.contextPath}/forgot-password" style="color: var(--color-2); text-decoration: none; font-size: 0.9rem;">
                        ¿Olvidaste tu contraseña?
                    </a>
                </div>
                
                <button type="submit" class="btn btn-primario btn-grande" style="width: 100%; margin-top: 1rem;">
                    Iniciar Sesión
                </button>
            </form>
            
            <div class="form-footer" style="margin-top: 2rem; text-align: center;">
                <p style="color: #666; margin-bottom: 1rem;">¿No tienes una cuenta?</p>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-secundario" style="width: 100%;">
                    Crear Cuenta
                </a>
            </div>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
