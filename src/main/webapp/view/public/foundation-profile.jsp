<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="model.entity.Pet" %>
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
    <title><%= foundation.get("foundationName") %> - PawPaw</title>
    
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <!-- Header Público -->
    <header class="public-header">
        <div class="container">
            <div class="header-content">
                <a href="<%= request.getContextPath() %>/view/index.jsp" class="logo-link">
                    <img src="<%= request.getContextPath() %>/images/logo.png" alt="PawPaw Logo" class="header-logo">
                    <span class="logo-text">PawPaw</span>
                </a>
                
                <nav class="public-nav">
                    <a href="<%= request.getContextPath() %>/view/index.jsp">Inicio</a>
                    <a href="<%= request.getContextPath() %>/foundations/public">Fundaciones</a>
                    <a href="<%= request.getContextPath() %>/login" class="btn-header">Iniciar Sesión</a>
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
                    <h1><%= foundation.get("foundationName") %></h1>
                    <p class="profile-subtitle"><%= foundation.get("contactName") %></p>
                </div>
            </div>
            
            <div class="profile-contact">
                <div class="contact-item">
                    <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path>
                    </svg>
                    <a href="mailto:<%= foundation.get("email") %>"><%= foundation.get("email") %></a>
                </div>
                
                <% if (foundation.get("phone") != null) { %>
                <div class="contact-item">
                    <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"></path>
                    </svg>
                    <a href="tel:<%= foundation.get("phone") %>"><%= foundation.get("phone") %></a>
                </div>
                <% } %>
                
                <% if (foundation.get("website") != null) { %>
                <div class="contact-item">
                    <svg width="20" height="20" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c1.657 0 3-4.03 3-9s-1.343-9-3-9m0 18c-1.657 0-3-4.03-3-9s1.343-9 3-9m-9 9a9 9 0 019-9"></path>
                    </svg>
                    <a href="<%= foundation.get("website") %>" target="_blank" rel="noopener"><%= foundation.get("website") %></a>
                </div>
                <% } %>
            </div>
            
            <% if (foundation.get("description") != null) { %>
            <div class="profile-description">
                <p><%= foundation.get("description") %></p>
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
                <% for (Pet pet : availablePets) { %>
                <div class="pet-card">
                    <div class="pet-image">
                        <% if (pet.getPhoto() != null && !pet.getPhoto().isEmpty()) { %>
                        <img src="<%= pet.getPhoto() %>" alt="<%= pet.getNamePet() %>">
                        <% } else { %>
                        <div class="pet-image-placeholder">🐾</div>
                        <% } %>
                        <span class="pet-badge adoption-available">En Adopción</span>
                    </div>
                    
                    <div class="pet-info">
                        <h3 class="pet-name"><%= pet.getNamePet() %></h3>
                        <p class="pet-details">
                            <span><%= pet.getBreed() != null ? pet.getBreed() : "Mestizo" %></span>
                            <% if (pet.getAgePet() != null) { %>
                            <span>• <%= pet.getAgePet() %> años</span>
                            <% } %>
                        </p>
                        <a href="<%= request.getContextPath() %>/pet/public/<%= pet.getIdPet() %>" 
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
                <% for (Pet pet : adoptedPets) { %>
                <div class="pet-card pet-card-adopted">
                    <div class="pet-image">
                        <% if (pet.getPhoto() != null && !pet.getPhoto().isEmpty()) { %>
                        <img src="<%= pet.getPhoto() %>" alt="<%= pet.getNamePet() %>">
                        <% } else { %>
                        <div class="pet-image-placeholder">🐾</div>
                        <% } %>
                        <span class="pet-badge adoption-transferred">Adoptado</span>
                    </div>
                    
                    <div class="pet-info">
                        <h3 class="pet-name"><%= pet.getNamePet() %></h3>
                        <p class="pet-details">
                            <span><%= pet.getBreed() != null ? pet.getBreed() : "Mestizo" %></span>
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
                <a href="mailto:<%= foundation.get("email") %>" class="btn btn-lg btn-primario">
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
                    <p><a href="<%= request.getContextPath() %>/foundations/public">Ver todas las fundaciones</a></p>
                </div>
                <div class="footer-section">
                    <p>&copy; 2025 PawPaw. Todos los derechos reservados.</p>
                </div>
            </div>
        </div>
    </footer>
    
</body>
</html>
