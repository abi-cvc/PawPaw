<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    String errorMessage = (String) request.getAttribute("errorMessage");
    String foundationName = (String) request.getAttribute("foundationName");
    String contactName = (String) request.getAttribute("contactName");
    String email = (String) request.getAttribute("email");
    String phone = (String) request.getAttribute("phone");
    String animalTypes = (String) request.getAttribute("animalTypes");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Únete como Fundación - PawPaw</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <!-- Header -->
    <header class="header">
        <nav class="navbar">
            <div class="navbar-contenedor">
                <a href="${pageContext.request.contextPath}/" class="navbar-logo">
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="PawPaw Logo" class="logo-img">
                    <span class="logo-texto">PawPaw</span>
                </a>
                <div class="navbar-botones">
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">Iniciar sesión</a>
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-primario">Registrarse</a>
                </div>
            </div>
        </nav>
    </header>

    <!-- Foundation Form -->
    <div class="foundation-form-container">
        <div class="foundation-form-card">
            <div class="form-header-foundation">
                <h1>🏢 Únete como Aliado PawPaw</h1>
                <p>¿Eres una fundación o refugio de animales? Obtén acceso ilimitado para registrar todas tus mascotas en adopción.</p>
            </div>

            <% if (errorMessage != null) { %>
                <div class="alert alert-error">
                    <c:out value="${errorMessage}"/>
                </div>
            <% } %>

            <form method="post" action="${pageContext.request.contextPath}/foundation/apply" class="foundation-form">
                <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                <div class="form-section">
                    <h3 class="section-title">Información de la Fundación</h3>

                    <div class="form-group">
                        <label for="foundationName">Nombre de la Fundación *</label>
                        <input type="text" id="foundationName" name="foundationName"
                               value="<c:out value="${foundationName != null ? foundationName : ''}"/>"
                               required maxlength="200" class="form-input">
                    </div>

                    <div class="form-group">
                        <label for="website">Sitio Web</label>
                        <input type="url" id="website" name="website"
                               placeholder="https://www.ejemplo.com" class="form-input">
                    </div>
                </div>

                <div class="form-section">
                    <h3 class="section-title">Persona de Contacto</h3>

                    <div class="form-group">
                        <label for="contactName">Nombre Completo *</label>
                        <input type="text" id="contactName" name="contactName"
                               value="<c:out value="${contactName != null ? contactName : ''}"/>"
                               required maxlength="150" class="form-input">
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="email">Email *</label>
                            <input type="email" id="email" name="email"
                                   value="<c:out value="${email != null ? email : ''}"/>"
                                   required maxlength="150" class="form-input">
                        </div>

                        <div class="form-group">
                            <label for="phone">Teléfono *</label>
                            <input type="tel" id="phone" name="phone"
                                   value="<c:out value="${phone != null ? phone : ''}"/>"
                                   required maxlength="20" class="form-input"
                                   placeholder="0999123456">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="whatsapp">WhatsApp (opcional)</label>
                        <input type="tel" id="whatsapp" name="whatsapp"
                               maxlength="20" class="form-input"
                               placeholder="0999123456">
                    </div>
                </div>

                <div class="form-section">
                    <h3 class="section-title">Sobre los Animales</h3>

                    <div class="form-group">
                        <label for="animalTypes">Tipos de Animales *</label>
                        <input type="text" id="animalTypes" name="animalTypes"
                               value="<c:out value="${animalTypes != null ? animalTypes : ''}"/>"
                               required maxlength="200" class="form-input"
                               placeholder="Ej: Perros, gatos, conejos">
                        <small class="form-hint">Separa con comas los tipos de animales que rescatan</small>
                    </div>

                    <div class="form-group">
                        <label for="currentAnimals">Cantidad Actual de Animales *</label>
                        <input type="number" id="currentAnimals" name="currentAnimals"
                               required min="1" max="9999" class="form-input"
                               placeholder="Ej: 50">
                    </div>

                    <div class="form-group">
                        <label for="description">Descripción de la Fundación</label>
                        <textarea id="description" name="description" rows="4"
                                  maxlength="1000" class="form-textarea"
                                  placeholder="Cuéntanos sobre su misión y actividades..."></textarea>
                    </div>
                </div>

                <div class="benefits-section">
                    <h3 class="section-title">✨ Beneficios como Aliado PawPaw</h3>
                    <ul class="benefits-list">
                        <li>Registro ilimitado de mascotas en adopción</li>
                        <li>Badge especial "Aliados PawPaw 🤝"</li>
                        <li>Página pública de la fundación</li>
                        <li>Sin costos mensuales</li>
                        <li>Soporte prioritario</li>
                    </ul>
                </div>

                <button type="submit" class="btn btn-primario btn-large">
                    Enviar Solicitud
                </button>
            </form>
        </div>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <div class="footer-contenido">
            <p class="footer-copyright">&copy; 2024 PawPaw. Todos los derechos reservados.</p>
        </div>
    </footer>
</body>
</html>
