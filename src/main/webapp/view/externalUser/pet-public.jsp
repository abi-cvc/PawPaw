<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%
    // Redirect if no pet in request
    if (request.getAttribute("pet") == null) {
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }
    // Session flash messages require removeAttribute (no JSTL equivalent)
    String successMessage = (String) session.getAttribute("successMessage");
    String errorMessage = (String) session.getAttribute("errorMessage");
    session.removeAttribute("successMessage");
    session.removeAttribute("errorMessage");
    if (successMessage != null) pageContext.setAttribute("successMsg", successMessage);
    if (errorMessage != null) pageContext.setAttribute("errorMsg", errorMessage);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pet.namePet}"/> - PawPaw</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">

    <c:if test="${not empty successMsg}">
    <script>
        window.addEventListener('DOMContentLoaded', function() {
            setTimeout(function() {
                document.querySelector('.alert-success').scrollIntoView({
                    behavior: 'smooth',
                    block: 'center'
                });
            }, 300);
        });
    </script>
    </c:if>
</head>
<body class="public-page">

    <!-- Header simple -->
    <header class="public-header">
        <div class="public-header-content">
            <a href="${pageContext.request.contextPath}/" class="public-logo">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="PawPaw">
                <span>PawPaw</span>
            </a>
        </div>
    </header>

    <!-- Contenido principal -->
    <main class="public-main">
        <div class="public-container">

            <!-- Mensajes MÁS VISIBLES -->
            <c:if test="${not empty successMsg}">
                <div class="alert alert-success alert-big">
                    <div class="alert-icon">✅</div>
                    <div class="alert-content">
                        <h3>¡Mensaje Enviado!</h3>
                        <p><c:out value="${successMsg}"/></p>
                    </div>
                </div>
            </c:if>

            <c:if test="${not empty errorMsg}">
                <div class="alert alert-error alert-big">
                    <div class="alert-icon">⚠️</div>
                    <div class="alert-content">
                        <h3>Error</h3>
                        <p><c:out value="${errorMsg}"/></p>
                    </div>
                </div>
            </c:if>

            <!-- Card de la mascota -->
            <div class="pet-public-card">

                <!-- Foto grande -->
                <div class="pet-public-photo">
                    <c:choose>
                        <c:when test="${not empty pet.photo}">
                        <img src="${fn:escapeXml(pet.photo)}" alt="<c:out value="${pet.namePet}"/>">
                        </c:when>
                        <c:otherwise>
                        <div class="pet-photo-placeholder-large">
                            🐾
                        </div>
                        </c:otherwise>
                    </c:choose>

                    <!-- Badge de estado -->
                    <c:if test="${pet.statusPet == 'lost'}">
                        <div class="pet-public-status lost">
                            🚨 MASCOTA PERDIDA
                        </div>
                    </c:if>
                    <c:if test="${pet.statusPet == 'found'}">
                        <div class="pet-public-status found">
                            ✅ MASCOTA ENCONTRADA
                        </div>
                    </c:if>
                </div>

                <!-- Información -->
                <div class="pet-public-info">
                    <h1>¡Hola! Soy <c:out value="${pet.namePet}"/></h1>

                    <div class="pet-public-details">
                        <div class="detail-row">
                            <span class="detail-icon">🐕</span>
                            <div class="detail-content">
                                <span class="detail-label">Raza</span>
                                <span class="detail-value"><c:out value="${not empty pet.breed ? pet.breed : 'No especificada'}"/></span>
                            </div>
                        </div>

                        <div class="detail-row">
                            <span class="detail-icon">🎂</span>
                            <div class="detail-content">
                                <span class="detail-label">Edad</span>
                                <span class="detail-value">
                                    <c:choose>
                                        <c:when test="${pet.agePet != null}"><c:out value="${pet.agePet}"/> años</c:when>
                                        <c:otherwise>No especificada</c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </div>

                        <div class="detail-row">
                            <span class="detail-icon">⚧</span>
                            <div class="detail-content">
                                <span class="detail-label">Sexo</span>
                                <span class="detail-value"><c:out value="${not empty pet.sexPet ? pet.sexPet : 'No especificado'}"/></span>
                            </div>
                        </div>

                        <c:if test="${not empty pet.medicalConditions}">
                        <div class="detail-row important">
                            <span class="detail-icon">⚕️</span>
                            <div class="detail-content">
                                <span class="detail-label">Condiciones médicas</span>
                                <span class="detail-value"><c:out value="${pet.medicalConditions}"/></span>
                            </div>
                        </div>
                        </c:if>

                        <c:if test="${not empty pet.extraComments}">
                        <div class="detail-row important">
                            <span class="detail-icon">💬</span>
                            <div class="detail-content">
                                <span class="detail-label">Comentarios Extras</span>
                                <span class="detail-value"><c:out value="${pet.extraComments}"/></span>
                            </div>
                        </div>
                        </c:if>
                    </div>

                    <!-- Información del dueño -->
                    <div class="owner-info">
                        <h3>👤 Mi dueño es <c:out value="${ownerName}"/></h3>
                        <c:if test="${not empty pet.contactPhone}">
                        <a href="tel:${fn:escapeXml(pet.contactPhone)}" class="contact-phone">
                            📞 <c:out value="${pet.contactPhone}"/>
                        </a>
                        </c:if>
                    </div>

                    <!-- Acciones principales -->
                    <div class="public-actions">

                        <!-- Reportar encontrada (solo si está perdida) -->
                        <c:if test="${pet.statusPet == 'lost'}">
                        <form method="POST" action="${pageContext.request.contextPath}/pet/${pet.idPet}" class="inline-form">
                            <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                            <input type="hidden" name="action" value="report-found">
                            <input type="hidden" name="petId" value="${pet.idPet}">
                            <button type="submit" class="btn btn-primario btn-grande" onclick="return confirm('¿Confirmas que encontraste a ${fn:replace(pet.namePet, "'", "\\'")}?');">
                                🏠 ¡Encontré a <c:out value="${pet.namePet}"/>!
                            </button>
                        </form>
                        </c:if>

                        <!-- Botón para llamar -->
                        <c:if test="${not empty pet.contactPhone}">
                        <a href="tel:${fn:escapeXml(pet.contactPhone)}" class="btn btn-secundario btn-grande">
                            📞 Llamar al dueño
                        </a>
                        </c:if>
                    </div>

                    <!-- Formulario de contacto -->
                    <div class="contact-form-section">
                        <h3>💬 Enviar mensaje al dueño</h3>
                        <p class="form-description">Déjale un mensaje al dueño de <c:out value="${pet.namePet}"/></p>

                        <form method="POST" action="${pageContext.request.contextPath}/pet/${pet.idPet}" class="contact-form" id="contactForm">
                            <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                            <input type="hidden" name="action" value="send-message">
                            <input type="hidden" name="petId" value="${pet.idPet}">

                            <div class="form-group">
                                <label for="senderName" class="form-label">Tu nombre *</label>
                                <input type="text"
                                       id="senderName"
                                       name="senderName"
                                       class="form-input"
                                       required
                                       maxlength="100"
                                       placeholder="Ej: Juan Pérez">
                            </div>

                            <div class="form-group">
                                <label for="senderPhone" class="form-label">Tu teléfono *</label>
                                <input type="tel"
                                       id="senderPhone"
                                       name="senderPhone"
                                       class="form-input"
                                       required
                                       maxlength="20"
                                       placeholder="Ej: 099 123 456">
                            </div>

                            <div class="form-group">
                                <label for="message" class="form-label">Mensaje *</label>
                                <textarea id="message"
                                          name="message"
                                          class="form-textarea"
                                          rows="4"
                                          required
                                          maxlength="500"
                                          placeholder="Cuéntale al dueño dónde viste a ${fn:replace(pet.namePet, "'", "\\'")} o cómo pueden contactarte..."></textarea>
                            </div>

                            <button type="submit" class="btn btn-primario btn-grande" id="submitBtn">
                                ✉️ Enviar mensaje
                            </button>
                        </form>
                    </div>

                </div>
            </div>

            <!-- Footer informativo -->
            <div class="public-footer">
                <p>Esta mascota está protegida con <strong>PawPaw</strong></p>
                <p>¿Quieres proteger a tu mascota también? <a href="${pageContext.request.contextPath}/register">Crea tu cuenta gratis</a></p>
            </div>

        </div>
    </main>

    <script>
        // Deshabilitar botón y mostrar "Enviando..." mientras procesa
        document.getElementById('contactForm').addEventListener('submit', function() {
            var btn = document.getElementById('submitBtn');
            btn.disabled = true;
            btn.innerHTML = '⏳ Enviando mensaje...';
        });
    </script>

</body>
</html>
