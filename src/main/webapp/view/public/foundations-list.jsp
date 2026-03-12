<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> foundations = (List<Map<String, Object>>) request.getAttribute("foundations");
    Integer totalFoundations = (Integer) request.getAttribute("totalFoundations");
    
    if (totalFoundations == null) totalFoundations = 0;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fundaciones Aliadas - PawPaw</title>
    
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
                    <a href="<%= request.getContextPath() %>/foundations/public" class="active">Fundaciones</a>
                    <a href="<%= request.getContextPath() %>/login" class="btn-header">Iniciar Sesión</a>
                </nav>
            </div>
        </div>
    </header>
    
    <!-- Hero Section -->
    <section class="hero-foundations">
        <div class="container">
            <div class="hero-content">
                <h1>🏢 Fundaciones Aliadas PawPaw</h1>
                <p class="hero-subtitle">Ayúdanos a encontrar hogares para mascotas en adopción</p>
                <div class="hero-stats">
                    <div class="hero-stat">
                        <span class="stat-number"><%= totalFoundations %></span>
                        <span class="stat-label">Fundaciones Activas</span>
                    </div>
                    <div class="hero-stat">
                        <span class="stat-number">
                            <%= foundations.stream()
                                .mapToInt(f -> (Integer) f.get("availablePets"))
                                .sum() %>
                        </span>
                        <span class="stat-label">Mascotas en Adopción</span>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Lista de Fundaciones -->
    <section class="foundations-section">
        <div class="container">
            
            <% if (foundations != null && !foundations.isEmpty()) { %>
            
            <div class="foundations-grid">
                <% for (Map<String, Object> foundation : foundations) { 
                    Integer availablePets = (Integer) foundation.get("availablePets");
                    Integer adoptedPets = (Integer) foundation.get("adoptedPets");
                    
                    // Solo mostrar fundaciones con mascotas
                    if (availablePets == null) availablePets = 0;
                    if (adoptedPets == null) adoptedPets = 0;
                %>
                
                <div class="foundation-card">
                    <div class="foundation-header">
                        <div class="foundation-icon">🏢</div>
                        <h3 class="foundation-name"><%= foundation.get("foundationName") %></h3>
                    </div>
                    
                    <div class="foundation-body">
                        <p class="foundation-contact">
                            <svg width="16" height="16" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                            </svg>
                            <%= foundation.get("contactName") %>
                        </p>
                        
                        <% if (foundation.get("phone") != null) { %>
                        <p class="foundation-contact">
                            <svg width="16" height="16" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z"></path>
                            </svg>
                            <%= foundation.get("phone") %>
                        </p>
                        <% } %>
                        
                        <% if (foundation.get("description") != null) { %>
                        <p class="foundation-description">
                            <%= ((String) foundation.get("description")).length() > 120 
                                ? ((String) foundation.get("description")).substring(0, 120) + "..." 
                                : foundation.get("description") %>
                        </p>
                        <% } %>
                        
                        <div class="foundation-stats">
                            <div class="stat-item">
                                <span class="stat-value"><%= availablePets %></span>
                                <span class="stat-text">En Adopción</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-value"><%= adoptedPets %></span>
                                <span class="stat-text">Adoptados</span>
                            </div>
                        </div>
                    </div>
                    
                    <div class="foundation-footer">
                        <a href="<%= request.getContextPath() %>/foundations/<%= foundation.get("idUser") %>" 
                           class="btn btn-primario btn-block">
                            🐾 Ver Mascotas
                        </a>
                    </div>
                </div>
                
                <% } %>
            </div>
            
            <% } else { %>
            
            <div class="empty-state">
                <div class="empty-icon">🏢</div>
                <h3>No hay fundaciones disponibles</h3>
                <p>Actualmente no hay fundaciones aliadas registradas.</p>
                <a href="<%= request.getContextPath() %>/view/index.jsp" class="btn btn-primario">
                    Volver al Inicio
                </a>
            </div>
            
            <% } %>
            
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
                    <h4>¿Eres una fundación?</h4>
                    <p><a href="<%= request.getContextPath() %>/foundation/request">Únete como aliado</a></p>
                </div>
                <div class="footer-section">
                    <p>&copy; 2025 PawPaw. Todos los derechos reservados.</p>
                </div>
            </div>
        </div>
    </footer>
    
</body>
</html>
