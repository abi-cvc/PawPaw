<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.User" %>
<%@ page import="model.entity.Suggestion" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // Verificar sesión
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    User user = (User) request.getAttribute("user");
    String userName = user != null ? user.getNameUser() : (String) session.getAttribute("userName");
    
    @SuppressWarnings("unchecked")
    List<Suggestion> suggestions = (List<Suggestion>) request.getAttribute("suggestions");
    Integer totalSuggestions = (Integer) request.getAttribute("totalSuggestions");
    Long pendingCount = (Long) request.getAttribute("pendingCount");
    Long reviewedCount = (Long) request.getAttribute("reviewedCount");
    Long resolvedCount = (Long) request.getAttribute("resolvedCount");
    Long rejectedCount = (Long) request.getAttribute("rejectedCount");
    
    if (totalSuggestions == null) totalSuggestions = 0;
    if (pendingCount == null) pendingCount = 0L;
    if (reviewedCount == null) reviewedCount = 0L;
    if (resolvedCount == null) resolvedCount = 0L;
    if (rejectedCount == null) rejectedCount = 0L;
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Sugerencias - PawPaw</title>
    
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
                
                <a href="<%= request.getContextPath() %>/user/my-suggestions" class="nav-item active">
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
                    <h1>Mis Sugerencias</h1>
                </div>
                <div class="topbar-actions">
                    <a href="<%= request.getContextPath() %>/user/send-suggestion" class="btn btn-primario">
                        + Nueva Sugerencia
                    </a>
                </div>
            </div>
            
            <!-- Content -->
            <div class="content">
                
                <!-- Estadísticas -->
                <div class="stats-grid" style="margin-bottom: 2rem;">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">📊</div>
                        <div class="stat-info">
                            <h3>Total Enviadas</h3>
                            <p><%= totalSuggestions %></p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #ffd700 0%, #ff8c00 100%);">⏳</div>
                        <div class="stat-info">
                            <h3>Pendientes</h3>
                            <p><%= pendingCount %></p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">👁️</div>
                        <div class="stat-info">
                            <h3>Revisadas</h3>
                            <p><%= reviewedCount %></p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">✅</div>
                        <div class="stat-info">
                            <h3>Resueltas</h3>
                            <p><%= resolvedCount %></p>
                        </div>
                    </div>
                </div>
                
                <!-- Lista de Sugerencias -->
                <% if (suggestions != null && !suggestions.isEmpty()) { %>
                    <% for (Suggestion suggestion : suggestions) { %>
                        <div class="suggestion-card">
                            <div class="suggestion-header">
                                <div>
                                    <span class="status-badge <%= suggestion.getStatusClass() %>">
                                        <%= suggestion.getStatusText() %>
                                    </span>
                                </div>
                                <span class="suggestion-date">
                                    <%= suggestion.getSubmissionDate() != null ? dateFormat.format(suggestion.getSubmissionDate()) : "" %>
                                </span>
                            </div>
                            
                            <div class="suggestion-message">
                                <%= suggestion.getMessage() != null ? suggestion.getMessage().replace("\n", "<br>") : "" %>
                            </div>
                            
                            <% if (suggestion.getAdminResponse() != null && !suggestion.getAdminResponse().isEmpty()) { %>
                                <div class="admin-response" style="margin-top: 1rem;">
                                    <strong style="color: var(--color-2); display: flex; align-items: center; gap: 0.5rem;">
                                        <svg fill="none" stroke="currentColor" viewBox="0 0 24 24" style="width: 20px; height: 20px;">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h10a8 8 0 018 8v2M3 10l6 6m-6-6l6-6"></path>
                                        </svg>
                                        Respuesta del equipo PawPaw:
                                    </strong>
                                    <p style="margin: 0.75rem 0 0 0; color: #333; line-height: 1.6;">
                                        <%= suggestion.getAdminResponse().replace("\n", "<br>") %>
                                    </p>
                                    <% if (suggestion.getResponseDate() != null) { %>
                                        <div style="font-size: 0.85rem; color: #999; margin-top: 0.5rem;">
                                            Respondido el <%= dateFormat.format(suggestion.getResponseDate()) %>
                                        </div>
                                    <% } %>
                                </div>
                            <% } else if ("reviewed".equals(suggestion.getStatusSuggestion()) || "resolved".equals(suggestion.getStatusSuggestion())) { %>
                                <div style="margin-top: 1rem; padding: 0.75rem; background: #f0f9ff; border-left: 3px solid var(--color-info); border-radius: var(--radio-sm);">
                                    <p style="margin: 0; color: #666; font-size: 0.9rem;">
                                        ℹ️ Tu sugerencia está siendo <%= "reviewed".equals(suggestion.getStatusSuggestion()) ? "revisada" : "procesada" %>. Pronto recibirás una respuesta.
                                    </p>
                                </div>
                            <% } %>
                        </div>
                    <% } %>
                <% } else { %>
                    <div class="empty-state">
                        <div style="font-size: 4rem; margin-bottom: 1rem;">📭</div>
                        <h3 style="margin: 0 0 0.5rem 0; color: var(--color-2);">No has enviado sugerencias</h3>
                        <p style="color: #999; margin: 0 0 1.5rem 0;">
                            ¿Tienes alguna idea para mejorar PawPaw? ¡Nos encantaría escucharte!
                        </p>
                        <a href="<%= request.getContextPath() %>/user/send-suggestion" class="btn btn-primario">
                            Enviar mi primera sugerencia
                        </a>
                    </div>
                <% } %>
                
            </div>
        </div>
        
    </div>
    
    <script src="<%= request.getContextPath() %>/js/main.js"></script>
</body>
</html>
