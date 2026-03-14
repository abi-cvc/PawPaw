<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="model.entity.Pet" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    @SuppressWarnings("unchecked")
    Map<String, Object> foundation = (Map<String, Object>) request.getAttribute("foundation");
    @SuppressWarnings("unchecked")
    List<Pet> availablePets = (List<Pet>) request.getAttribute("availablePets");
    @SuppressWarnings("unchecked")
    List<Pet> adoptedPets = (List<Pet>) request.getAttribute("adoptedPets");
    Integer totalAvailable = (Integer) request.getAttribute("totalAvailable");
    Integer totalAdopted = (Integer) request.getAttribute("totalAdopted");

    if (totalAvailable == null) totalAvailable = 0;
    if (totalAdopted == null) totalAdopted = 0;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${foundation.foundationName}"/> - PawPaw</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <!-- CSS — usar SIEMPRE contextPath, nunca ruta relativa -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <!-- Header Público -->
    <header class="public-header">
        <div class="container">
            <div class="header-content">
                <a href="${pageContext.request.contextPath}/view/index.jsp" class="logo-link">
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="PawPaw Logo" class="header-logo">
                    <span class="logo-text">PawPaw</span>
                </a>

                <nav class="public-nav">
                    <a href="${pageContext.request.contextPath}/view/index.jsp">Inicio</a>
                    <a href="${pageContext.request.contextPath}/foundations/public">Fundaciones</a>
                    <a href="${pageContext.request.contextPath}/login" class="btn-header">Iniciar Sesión</a>
                </nav>
            </div>
        </div>
    </header>

    <!-- Perfil de Fundación -->
    <section class="foundation-profile-hero">
        <div class="container">
            <div class="profile-header">
                <div class="profile-icon">🏢</div>
                <div class="profile-info">
                    <h1><c:out value="${foundation.foundationName}"/></h1>
                    <p class="profile-subtitle"><c:out value="${foundation.contactName}"/></p>
                </div>
            </div>

            <div class="profile-contact">
                <div class="contact-item">
                    <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path>
                    </svg>
                    <a href="mailto:<c:out value="${foundation.email}"/>"><c:out value="${foundation.email}"/></a>
                </div>

                <% if (foundation.get("phone") != null) { %>
                <div class="contact-item">
                    <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"></path>
                    </svg>
                    <a href="tel:<c:out value="${foundation.phone}"/>"><c:out value="${foundation.phone}"/></a>
                </div>
                <% } %>

                <% if (foundation.get("website") != null) { %>
                <div class="contact-item">
                    <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c1.657 0 3-4.03 3-9s-1.343-9-3-9m0 18c-1.657 0-3-4.03-3-9s1.343-9 3-9m-9 9a9 9 0 019-9"></path>
                    </svg>
                    <a href="<c:out value="${foundation.website}"/>" target="_blank" rel="noopener"><c:out value="${foundation.website}"/></a>
                </div>
                <% } %>
            </div>

            <% if (foundation.get("description") != null) { %>
            <div class="profile-description">
                <p><c:out value="${foundation.description}"/></p>
            </div>
            <% } %>
        </div>
    </section>

    <!-- Mascotas en Adopción -->
    <% if (availablePets != null && !availablePets.isEmpty()) { %>
    <section class="pets-section">
        <div class="container">
            <div class="section-header">
                <h2>🏠 En Adopción (<%= totalAvailable %>)</h2>
                <p>Estas mascotas están buscando un hogar</p>
            </div>

            <div class="pets-grid">
                <% for (Pet pet : availablePets) {
                    pageContext.setAttribute("petItem", pet);
                %>
                <div class="pet-card">
                    <div class="pet-image">
                        <% if (pet.getPhoto() != null && !pet.getPhoto().isEmpty()) { %>
                        <img src="<c:out value="${petItem.photo}"/>" alt="<c:out value="${petItem.namePet}"/>">
                        <% } else { %>
                        <div class="pet-image-placeholder">🐾</div>
                        <% } %>
                        <span class="pet-badge adoption-available">En Adopción</span>
                    </div>

                    <div class="pet-info">
                        <h3 class="pet-name"><c:out value="${petItem.namePet}"/></h3>
                        <p class="pet-details">
                            <span><c:out value="${petItem.breed != null ? petItem.breed : 'Mestizo'}"/></span>
                            <% if (pet.getAgePet() != null) { %>
                            <span>• <%= pet.getAgePet() %> años</span>
                            <% } %>
                        </p>
                        <a href="${pageContext.request.contextPath}/pet/public/<%= pet.getIdPet() %>"
                           class="btn btn-sm btn-primario">
                            Ver Perfil
                        </a>
                    </div>
                </div>
                <% } %>
            </div>
        </div>
    </section>
    <% } %>

    <!-- Mascotas Adoptadas -->
    <% if (adoptedPets != null && !adoptedPets.isEmpty()) { %>
    <section class="pets-section pets-section-gray">
        <div class="container">
            <div class="section-header">
                <h2>✅ Adoptados (<%= totalAdopted %>)</h2>
                <p>Historias de éxito - Estos peluditos ya encontraron hogar</p>
            </div>

            <div class="pets-grid">
                <% for (Pet pet : adoptedPets) {
                    pageContext.setAttribute("petItem", pet);
                %>
                <div class="pet-card pet-card-adopted">
                    <div class="pet-image">
                        <% if (pet.getPhoto() != null && !pet.getPhoto().isEmpty()) { %>
                        <img src="<c:out value="${petItem.photo}"/>" alt="<c:out value="${petItem.namePet}"/>">
                        <% } else { %>
                        <div class="pet-image-placeholder">🐾</div>
                        <% } %>
                        <span class="pet-badge adoption-transferred">Adoptado</span>
                    </div>

                    <div class="pet-info">
                        <h3 class="pet-name"><c:out value="${petItem.namePet}"/></h3>
                        <p class="pet-details">
                            <span><c:out value="${petItem.breed != null ? petItem.breed : 'Mestizo'}"/></span>
                            <% if (pet.getAgePet() != null) { %>
                            <span>• <%= pet.getAgePet() %> años</span>
                            <% } %>
                        </p>
                    </div>
                </div>
                <% } %>
            </div>
        </div>
    </section>
    <% } %>

    <!-- Si no hay mascotas -->
    <% if ((availablePets == null || availablePets.isEmpty()) &&
           (adoptedPets == null || adoptedPets.isEmpty())) { %>
    <section class="pets-section">
        <div class="container">
            <div class="empty-state">
                <div class="empty-icon">🐾</div>
                <h3>Sin mascotas registradas</h3>
                <p>Esta fundación aún no ha registrado mascotas en PawPaw.</p>
            </div>
        </div>
    </section>
    <% } %>

    <!-- Call to Action -->
    <section class="cta-section">
        <div class="container">
            <div class="cta-content">
                <h2>¿Quieres adoptar?</h2>
                <p>Contáctate directamente con la fundación para más información</p>
                <a href="mailto:<c:out value="${foundation.email}"/>" class="btn btn-lg btn-primario">
                    📧 Contactar Fundación
                </a>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer class="public-footer">
        <div class="container">
            <div class="footer-content">
                <div class="footer-section">
                    <h4>PawPaw</h4>
                    <p>Sistema de identificación de mascotas</p>
                </div>
                <div class="footer-section">
                    <p><a href="${pageContext.request.contextPath}/foundations/public">Ver todas las fundaciones</a></p>
                </div>
                <div class="footer-section">
                    <p>&copy; 2025 PawPaw. Todos los derechos reservados.</p>
                </div>
            </div>
        </div>
    </footer>

</body>
</html>
