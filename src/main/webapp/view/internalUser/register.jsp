<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Cuenta - PawPaw</title>
    
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
                <c:choose>
                    <c:when test="${not empty foundationToken}">
                        <h1>Crear Cuenta de Fundación</h1>
                        <p>Registro de <strong><c:out value="${foundationName}"/></strong></p>
                    </c:when>
                    <c:otherwise>
                        <h1>Crear Cuenta</h1>
                        <p>Únete a PawPaw hoy</p>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <c:if test="${not empty error}">
                <div class="mensaje mensaje-error">
                    ⚠️ <c:out value="${error}"/>
                </div>
            </c:if>
            
            <!-- Google Sign-In (solo para registro normal, no fundaciones) -->
            <c:if test="${empty foundationToken}">
                <div id="g_id_onload"
                     data-client_id="${googleClientId}"
                     data-context="signup"
                     data-ux_mode="redirect"
                     data-login_uri="${pageContext.request.contextPath}/auth/google/callback"
                     data-auto_prompt="false">
                </div>
                <div class="google-btn-wrapper" style="display: flex; justify-content: center; margin-bottom: 1.5rem;">
                    <div class="g_id_signin"
                         data-type="standard"
                         data-shape="rectangular"
                         data-theme="outline"
                         data-text="signup_with"
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
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post" id="registerForm">
                <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                <c:if test="${not empty foundationToken}">
                    <input type="hidden" name="foundationToken" value="<c:out value="${foundationToken}"/>">
                </c:if>

                <div class="form-group">
                    <label for="name" class="form-label required">Nombre completo</label>
                    <input
                        type="text"
                        id="name"
                        name="name"
                        class="form-input"
                        placeholder="Ej: María García"
                        value="<c:out value="${name}"/>"
                        required
                        autocomplete="name">
                </div>

                <div class="form-group">
                    <label for="email" class="form-label required">Email</label>
                    <c:choose>
                        <c:when test="${not empty foundationEmail}">
                            <input
                                type="email"
                                id="email"
                                name="email"
                                class="form-input"
                                value="<c:out value="${foundationEmail}"/>"
                                readonly
                                style="background-color: #f0f0f0; cursor: not-allowed;">
                            <small style="color: #666; font-size: 0.85rem;">Este email fue verificado en tu solicitud de fundación</small>
                        </c:when>
                        <c:otherwise>
                            <input
                                type="email"
                                id="email"
                                name="email"
                                class="form-input"
                                placeholder="tucorreo@ejemplo.com"
                                value="<c:out value="${email}"/>"
                                required
                                autocomplete="email">
                        </c:otherwise>
                    </c:choose>
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
                <a href="${pageContext.request.contextPath}/login" class="btn btn-secundario" style="width: 100%;">
                    Iniciar Sesión
                </a>
            </div>
        </div>
    </div>
    
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
