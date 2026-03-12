<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> foundations = (List<Map<String, Object>>) request.getAttribute("foundations");
    Integer totalFoundations = (Integer) request.getAttribute("totalFoundations");
    if (totalFoundations == null) totalFoundations = 0;
    int totalAvailable = (foundations != null)
        ? foundations.stream().mapToInt(f -> {
            Object v = f.get("availablePets");
            return v instanceof Integer ? (Integer) v : 0;
          }).sum()
        : 0;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fundaciones Aliadas - PawPaw</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>

    <!-- ══ NAVBAR ══ -->
    <header class="header">
        <nav class="navbar">
            <div class="navbar-contenedor">
                <a href="<%= request.getContextPath() %>/" class="navbar-logo" style="text-decoration:none;">
                    <img src="<%= request.getContextPath() %>/images/logo.png" alt="PawPaw" class="logo-img">
                    <span class="logo-texto">PawPaw</span>
                </a>
                <div class="navbar-botones">
                    <a href="<%= request.getContextPath() %>/" class="fl-nav-link">Inicio</a>
                    <a href="<%= request.getContextPath() %>/foundations/public" class="fl-nav-link fl-nav-active">Fundaciones</a>
                    <a href="<%= request.getContextPath() %>/foundation/apply" class="btn btn-outline">Unirme como fundación</a>
                    <a href="<%= request.getContextPath() %>/login" class="btn btn-primario">Iniciar sesión</a>
                </div>
            </div>
        </nav>
    </header>

    <!-- ══ HERO ══ -->
    <section class="fl-hero">
        <div class="fl-hero-inner">
            <div class="fl-hero-text">
                <span class="fl-hero-tag">🐾 Red de adopción PawPaw</span>
                <h1 class="fl-hero-title">Fundaciones Aliadas</h1>
                <p class="fl-hero-subtitle">
                    Conectamos a animales rescatados con familias que los están esperando.
                    Cada código QR que llevamos es una historia que empieza de nuevo.
                </p>
                <a href="<%= request.getContextPath() %>/foundation/apply" class="btn btn-primario btn-grande">
                    Unir mi fundación →
                </a>
            </div>
            <div class="fl-hero-stats">
                <div class="fl-stat-card">
                    <span class="fl-stat-num"><%= totalFoundations %></span>
                    <span class="fl-stat-lbl">Fundaciones activas</span>
                </div>
                <div class="fl-stat-card">
                    <span class="fl-stat-num"><%= totalAvailable %></span>
                    <span class="fl-stat-lbl">Mascotas en adopción</span>
                </div>
            </div>
        </div>
    </section>

    <!-- ══ GRID DE FUNDACIONES ══ -->
    <section class="fl-section">
        <div class="fl-container">

            <% if (foundations != null && !foundations.isEmpty()) { %>

            <div class="fl-grid">
                <% for (Map<String, Object> foundation : foundations) {
                    Integer availablePets = (Integer) foundation.get("availablePets");
                    Integer adoptedPets   = (Integer) foundation.get("adoptedPets");
                    if (availablePets == null) availablePets = 0;
                    if (adoptedPets   == null) adoptedPets   = 0;
                    String fname = (String) foundation.get("foundationName");
                    String initial = (fname != null && !fname.isEmpty())
                        ? String.valueOf(fname.charAt(0)).toUpperCase() : "F";
                %>

                <div class="fl-card">

                    <!-- Cabecera -->
                    <div class="fl-card-header">
                        <div class="fl-avatar"><%= initial %></div>
                        <div class="fl-card-meta">
                            <h3 class="fl-card-name"><%= fname %></h3>
                            <span class="fl-badge-partner">✓ Partner PawPaw</span>
                        </div>
                    </div>

                    <!-- Descripción -->
                    <% if (foundation.get("description") != null) { %>
                    <p class="fl-card-desc">
                        <%= ((String) foundation.get("description")).length() > 110
                            ? ((String) foundation.get("description")).substring(0, 110) + "…"
                            : foundation.get("description") %>
                    </p>
                    <% } %>

                    <!-- Contactos -->
                    <div class="fl-card-contacts">
                        <% if (foundation.get("contactName") != null) { %>
                        <span class="fl-contact-pill">👤 <%= foundation.get("contactName") %></span>
                        <% } %>
                        <% if (foundation.get("phone") != null) { %>
                        <a href="tel:<%= foundation.get("phone") %>" class="fl-contact-pill fl-contact-link">
                            📞 <%= foundation.get("phone") %>
                        </a>
                        <% } %>
                    </div>

                    <!-- Stats -->
                    <div class="fl-card-stats">
                        <div class="fl-mini-stat fl-stat-green">
                            <span class="fl-mini-num"><%= availablePets %></span>
                            <span class="fl-mini-lbl">En adopción</span>
                        </div>
                        <div class="fl-mini-stat fl-stat-blue">
                            <span class="fl-mini-num"><%= adoptedPets %></span>
                            <span class="fl-mini-lbl">Adoptados</span>
                        </div>
                    </div>

                    <!-- Acción -->
                    <div class="fl-card-footer">
                        <a href="<%= request.getContextPath() %>/foundations/<%= foundation.get("idUser") %>"
                           class="btn btn-primario btn-block">
                            Ver mascotas 🐾
                        </a>
                    </div>
                </div>

                <% } %>
            </div>

            <% } else { %>

            <div class="fl-empty">
                <div class="fl-empty-icon">🏠</div>
                <h3>No hay fundaciones disponibles aún</h3>
                <p>Sé la primera fundación aliada de PawPaw y dale visibilidad a tus animales.</p>
                <a href="<%= request.getContextPath() %>/foundation/apply" class="btn btn-primario">
                    Unir mi fundación →
                </a>
            </div>

            <% } %>

        </div>
    </section>

    <!-- ══ FOOTER ══ -->
    <footer class="footer">
        <div class="footer-contenedor">
            <div class="footer-contenido">
                <div class="footer-brand">
                    <img src="<%= request.getContextPath() %>/images/logo.png" alt="PawPaw" class="footer-logo-img">
                    <span class="footer-logo-texto">PawPaw</span>
                </div>
                <nav class="footer-links">
                    <a href="<%= request.getContextPath() %>/" class="footer-link">Inicio</a>
                    <a href="<%= request.getContextPath() %>/foundations/public" class="footer-link">Fundaciones</a>
                    <a href="<%= request.getContextPath() %>/foundation/apply" class="footer-link">Ser fundación aliada</a>
                    <a href="<%= request.getContextPath() %>/login" class="footer-link">Iniciar sesión</a>
                </nav>
            </div>
            <div class="footer-bottom">
                <p class="footer-copyright">© 2025 PawPaw. Todos los derechos reservados.</p>
            </div>
        </div>
    </footer>

</body>
</html>
