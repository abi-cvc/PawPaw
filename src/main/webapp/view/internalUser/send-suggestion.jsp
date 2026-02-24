<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.User" %>
<%
    // Verificar sesión
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    User user = (User) request.getAttribute("user");
    String userName = user != null ? user.getNameUser() : (String) session.getAttribute("userName");
    
    String error = (String) request.getAttribute("error");
    String message = (String) request.getAttribute("message");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Enviar Sugerencia - PawPaw</title>
    
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <div class="dashboard">
        
        <!-- Sidebar -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <a href="<%= request.getContextPath() %>/view/index.jsp" class="sidebar-logo">
                    <img src="<%= request.getContextPath() %>/images/logo.png" alt="PawPaw Logo">
                    <span class="sidebar-logo-text">PawPaw</span>
                </a>
            </div>
            
            <div class="sidebar-user">
                <div class="user-info">
                    <div class="user-avatar">
                        <%= userName != null ? userName.substring(0, 1).toUpperCase() : "U" %>
                    </div>
                    <div class="user-details">
                        <h3><%= userName %></h3>
                        <p>Usuario</p>
                    </div>
                </div>
            </div>
            
            <nav class="sidebar-nav">
                <a href="<%= request.getContextPath() %>/user/panel" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"></path>
                    </svg>
                    Panel Principal
                </a>
                
                <a href="<%= request.getContextPath() %>/user/pets" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                    Mis Mascotas
                </a>
                
                <a href="<%= request.getContextPath() %>/user/qr-codes" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 20h4M4 12h4m12 0h.01M5 8h2a1 1 0 001-1V5a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1zm12 0h2a1 1 0 001-1V5a1 1 0 00-1-1h-2a1 1 0 00-1 1v2a1 1 0 001 1zM5 20h2a1 1 0 001-1v-2a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1z"></path>
                    </svg>
                    Códigos QR
                </a>
                
                <div class="nav-divider"></div>
                
                <a href="<%= request.getContextPath() %>/user/profile" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                    </svg>
                    Mi Perfil
                </a>
                
                <a href="<%= request.getContextPath() %>/user/send-suggestion" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"></path>
                    </svg>
                    Enviar Sugerencia
                </a>
                
                <a href="<%= request.getContextPath() %>/user/my-suggestions" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path>
                    </svg>
                    Mis Sugerencias
                </a>
                
                <a href="<%= request.getContextPath() %>/logout" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
                    </svg>
                    Cerrar Sesión
                </a>
            </nav>
        </aside>
        
        <!-- Main Content -->
        <div class="main-content">
            <!-- Top Bar -->
            <div class="topbar">
                <div class="topbar-title">
                    <h1>Enviar Sugerencia</h1>
                </div>
            </div>
            
            <!-- Content -->
            <div class="content">
                
                <% if (error != null && !error.isEmpty()) { %>
                    <div class="mensaje mensaje-error" style="margin-bottom: 1.5rem;">
                        <%= error %>
                    </div>
                <% } %>
                
                <!-- Formulario de Sugerencia -->
                <div class="tarjeta" style="max-width: 800px; margin: 0 auto;">
                    <div style="padding: 2rem;">
                        <div style="text-align: center; margin-bottom: 2rem;">
                            <div style="font-size: 4rem; margin-bottom: 1rem;">💬</div>
                            <h2 style="margin: 0 0 0.5rem 0; color: var(--color-2);">Tu opinión es importante</h2>
                            <p style="color: #666; margin: 0;">
                                Comparte tus ideas, sugerencias o comentarios para ayudarnos a mejorar PawPaw
                            </p>
                        </div>
                        
                        <form action="<%= request.getContextPath() %>/user/send-suggestion" method="post">
                            <div class="form-group">
                                <label for="message" class="form-label required">Tu sugerencia</label>
                                <textarea 
                                    id="message" 
                                    name="message" 
                                    class="form-input" 
                                    rows="8" 
                                    placeholder="Escribe aquí tu sugerencia, comentario o idea..."
                                    required
                                    minlength="10"
                                    maxlength="1000"
                                    style="resize: vertical; font-family: var(--fuente-texto);"><%= message != null ? message : "" %></textarea>
                                <small style="color: #999; font-size: 0.875rem; display: block; margin-top: 0.5rem;">
                                    Mínimo 10 caracteres, máximo 1000 caracteres
                                </small>
                            </div>
                            
                            <div style="background: #f0f9ff; border-left: 4px solid var(--color-info); padding: 1rem; border-radius: var(--radio-sm); margin-bottom: 1.5rem;">
                                <p style="margin: 0; font-size: 0.9rem; color: #666;">
                                    <strong>💡 Consejo:</strong> Sé específico en tu sugerencia. Cuanto más detalles nos brindes, mejor podremos entender y atender tu solicitud.
                                </p>
                            </div>
                            
                            <div style="display: flex; gap: 1rem; flex-wrap: wrap;">
                                <button type="submit" class="btn btn-primario" style="flex: 1; min-width: 200px;">
                                    Enviar Sugerencia
                                </button>
                                <a href="<%= request.getContextPath() %>/user/panel" class="btn btn-secundario" style="flex: 1; min-width: 200px; text-align: center; display: flex; align-items: center; justify-content: center;">
                                    Cancelar
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
                
                <!-- Información adicional -->
                <div style="max-width: 800px; margin: 2rem auto 0; text-align: center;">
                    <p style="color: #999; font-size: 0.9rem;">
                        Todas las sugerencias son revisadas por nuestro equipo. Te responderemos lo antes posible.
                    </p>
                </div>
                
            </div>
        </div>
        
    </div>
    
    <script src="<%= request.getContextPath() %>/js/main.js"></script>
</body>
</html>
