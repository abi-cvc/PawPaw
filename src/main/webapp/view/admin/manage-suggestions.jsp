<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.User" %>
<%@ page import="model.entity.Suggestion" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // Verificar sesión y rol
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    String userRole = (String) session.getAttribute("userRole");
    if (!"admin".equalsIgnoreCase(userRole)) {
        response.sendRedirect(request.getContextPath() + "/user/panel");
        return;
    }
    
    User user = (User) request.getAttribute("user");
    String userName = user != null ? user.getNameUser() : (String) session.getAttribute("userName");
    
    // Mensajes
    String successMessage = (String) session.getAttribute("successMessage");
    String errorMessage = (String) session.getAttribute("errorMessage");
    session.removeAttribute("successMessage");
    session.removeAttribute("errorMessage");
    
    // Sugerencias y estadísticas
    @SuppressWarnings("unchecked")
    List<Suggestion> suggestions = (List<Suggestion>) request.getAttribute("suggestions");
    Integer totalSuggestions = (Integer) request.getAttribute("totalSuggestions");
    Integer pendingSuggestions = (Integer) request.getAttribute("pendingSuggestions");
    Integer reviewedSuggestions = (Integer) request.getAttribute("reviewedSuggestions");
    Integer resolvedSuggestions = (Integer) request.getAttribute("resolvedSuggestions");
    String currentFilter = (String) request.getAttribute("currentFilter");
    
    if (totalSuggestions == null) totalSuggestions = 0;
    if (pendingSuggestions == null) pendingSuggestions = 0;
    if (reviewedSuggestions == null) reviewedSuggestions = 0;
    if (resolvedSuggestions == null) resolvedSuggestions = 0;
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Sugerencias - PawPaw</title>
    
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
    
    <style>
        /* Dropdown elegante */
        .custom-select {
            transition: all 0.3s ease;
        }
        
        .custom-select:hover {
            border-color: var(--color-2) !important;
            box-shadow: 0 2px 8px rgba(136, 74, 57, 0.15);
        }
        
        .custom-select:focus {
            outline: none;
            border-color: var(--color-1) !important;
            box-shadow: 0 0 0 3px rgba(136, 74, 57, 0.1);
        }
        
        /* Opciones del select */
        .custom-select option {
            padding: 0.75rem;
            font-size: 1rem;
        }
        
        /* Animación suave */
        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(-5px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        .custom-select-wrapper {
            animation: fadeIn 0.3s ease;
        }
    </style>
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
                    <div class="user-avatar" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                        <%= userName != null ? userName.substring(0, 1).toUpperCase() : "A" %>
                    </div>
                    <div class="user-details">
                        <h3><%= userName %></h3>
                        <p>Administrador</p>
                    </div>
                </div>
            </div>
            
            <nav class="sidebar-nav">
                <a href="<%= request.getContextPath() %>/admin/panel" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
                    </svg>
                    Dashboard
                </a>
                
                <a href="<%= request.getContextPath() %>/admin/suggestions" class="nav-item active">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"></path>
                    </svg>
                    Sugerencias
                    <% if (pendingSuggestions > 0) { %>
                        <span style="margin-left: auto; background: var(--color-error); color: white; padding: 0.25rem 0.5rem; border-radius: var(--radio-full); font-size: 0.75rem; font-weight: 700;"><%= pendingSuggestions %></span>
                    <% } %>
                </a>
                
                <div class="nav-divider"></div>
                
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
                    <h1>Gestionar Sugerencias</h1>
                </div>
            </div>
            
            <!-- Content -->
            <div class="content">
                
                <% if (successMessage != null && !successMessage.isEmpty()) { %>
                    <div class="mensaje mensaje-exito" style="margin-bottom: 1.5rem;">
                        <%= successMessage %>
                    </div>
                <% } %>
                
                <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
                    <div class="mensaje mensaje-error" style="margin-bottom: 1.5rem;">
                        <%= errorMessage %>
                    </div>
                <% } %>
                
                <!-- Estadísticas rápidas -->
                <div class="stats-grid" style="margin-bottom: 2rem;">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">📊</div>
                        <div class="stat-info">
                            <h3>Total</h3>
                            <p><%= totalSuggestions %></p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #ffd700 0%, #ff8c00 100%);">⏳</div>
                        <div class="stat-info">
                            <h3>Pendientes</h3>
                            <p><%= pendingSuggestions %></p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">👁️</div>
                        <div class="stat-info">
                            <h3>Revisadas</h3>
                            <p><%= reviewedSuggestions %></p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">✅</div>
                        <div class="stat-info">
                            <h3>Resueltas</h3>
                            <p><%= resolvedSuggestions %></p>
                        </div>
                    </div>
                </div>
                
                <!-- Filtro Dropdown -->
                <div style="margin-bottom: 2rem; display: flex; align-items: center; gap: 1rem;">
                    <label for="suggestionFilter" style="font-weight: 600; color: var(--color-2); font-size: 1rem;">
                        Filtrar por:
                    </label>
                    <div class="custom-select-wrapper" style="position: relative; min-width: 250px;">
                        <select id="suggestionFilter" class="custom-select" onchange="window.location.href='<%= request.getContextPath() %>/admin/suggestions?filter=' + this.value" 
                                style="width: 100%; padding: 0.75rem 1rem; border: 2px solid #e0e0e0; border-radius: var(--radio-md); background: white; font-size: 1rem; cursor: pointer; appearance: none; background-image: url('data:image/svg+xml;utf8,<svg fill=\"%23884A39\" viewBox=\"0 0 24 24\" xmlns=\"http://www.w3.org/2000/svg\"><path d=\"M7 10l5 5 5-5z\"/></svg>'); background-repeat: no-repeat; background-position: right 0.75rem center; background-size: 1.5rem; transition: all 0.3s ease;">
                            <option value="" <%= currentFilter == null || currentFilter.isEmpty() ? "selected" : "" %>>
                                ✓ Todas (<%= totalSuggestions %>)
                            </option>
                            <option value="pending" <%= "pending".equals(currentFilter) ? "selected" : "" %>>
                                ⏳ Pendientes (<%= pendingSuggestions %>)
                            </option>
                            <option value="reviewed" <%= "reviewed".equals(currentFilter) ? "selected" : "" %>>
                                👁️ Revisadas (<%= reviewedSuggestions %>)
                            </option>
                            <option value="resolved" <%= "resolved".equals(currentFilter) ? "selected" : "" %>>
                                ✅ Resueltas (<%= resolvedSuggestions %>)
                            </option>
                            <option value="rejected" <%= "rejected".equals(currentFilter) ? "selected" : "" %>>
                                ❌ Rechazadas (<%= totalSuggestions - pendingSuggestions - reviewedSuggestions - resolvedSuggestions %>)
                            </option>
                        </select>
                    </div>
                </div>
                
                <!-- Lista de Sugerencias -->
                <% if (suggestions != null && !suggestions.isEmpty()) { %>
                    <% for (Suggestion suggestion : suggestions) { %>
                        <div class="suggestion-card">
                            <div class="suggestion-header">
                                <div>
                                    <span class="suggestion-user">
                                        <%= suggestion.getUserName() != null ? suggestion.getUserName() : "Usuario" %>
                                    </span>
                                    <span style="color: #999; margin-left: 0.5rem;">
                                        (<%= suggestion.getUserEmail() %>)
                                    </span>
                                </div>
                                <div style="display: flex; align-items: center; gap: 0.5rem;">
                                    <span class="status-badge <%= suggestion.getStatusClass() %>">
                                        <%= suggestion.getStatusText() %>
                                    </span>
                                    <span class="suggestion-date">
                                        <%= suggestion.getSubmissionDate() != null ? dateFormat.format(suggestion.getSubmissionDate()) : "" %>
                                    </span>
                                </div>
                            </div>
                            
                            <div class="suggestion-message">
                                <%= suggestion.getMessage() != null ? suggestion.getMessage().replace("\n", "<br>") : "" %>
                            </div>
                            
                            <% if (suggestion.getAdminResponse() != null && !suggestion.getAdminResponse().isEmpty()) { %>
                                <div class="admin-response">
                                    <strong style="color: var(--color-2);">Respuesta del administrador:</strong><br>
                                    <%= suggestion.getAdminResponse().replace("\n", "<br>") %>
                                    <% if (suggestion.getResponseDate() != null) { %>
                                        <div style="font-size: 0.85rem; color: #999; margin-top: 0.5rem;">
                                            Respondido: <%= dateFormat.format(suggestion.getResponseDate()) %>
                                        </div>
                                    <% } %>
                                </div>
                            <% } %>
                            
                            <!-- Formulario de acciones -->
                            <div class="suggestion-actions" style="margin-top: 1rem; padding-top: 1rem; border-top: 1px solid #e0e0e0;">
                                <form method="post" action="<%= request.getContextPath() %>/admin/suggestions" style="width: 100%;">
                                    <input type="hidden" name="action" value="updateStatus">
                                    <input type="hidden" name="suggestionId" value="<%= suggestion.getIdSuggestion() %>">
                                    
                                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem;">
                                        <div class="form-group" style="margin: 0;">
                                            <label for="status-<%= suggestion.getIdSuggestion() %>" class="form-label">Cambiar estado:</label>
                                            <select id="status-<%= suggestion.getIdSuggestion() %>" name="status" class="form-input">
                                                <option value="pending" <%= "pending".equals(suggestion.getStatusSuggestion()) ? "selected" : "" %>>Pendiente</option>
                                                <option value="reviewed" <%= "reviewed".equals(suggestion.getStatusSuggestion()) ? "selected" : "" %>>Revisada</option>
                                                <option value="resolved" <%= "resolved".equals(suggestion.getStatusSuggestion()) ? "selected" : "" %>>Resuelta</option>
                                                <option value="rejected" <%= "rejected".equals(suggestion.getStatusSuggestion()) ? "selected" : "" %>>Rechazada</option>
                                            </select>
                                        </div>
                                        
                                        <div style="display: flex; align-items: flex-end; gap: 0.5rem;">
                                            <button type="submit" class="btn btn-primario" style="flex: 1;">
                                                Actualizar Estado
                                            </button>
                                            <button type="button" onclick="toggleResponse(<%= suggestion.getIdSuggestion() %>)" class="btn btn-secundario">
                                                💬
                                            </button>
                                        </div>
                                    </div>
                                    
                                    <div id="response-<%= suggestion.getIdSuggestion() %>" style="display: none;">
                                        <div class="form-group" style="margin: 0;">
                                            <label for="response-<%= suggestion.getIdSuggestion() %>" class="form-label">Respuesta del administrador (opcional):</label>
                                            <textarea id="response-<%= suggestion.getIdSuggestion() %>" name="adminResponse" class="form-input" rows="3" placeholder="Escribe una respuesta..."><%= suggestion.getAdminResponse() != null ? suggestion.getAdminResponse() : "" %></textarea>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    <% } %>
                <% } else { %>
                    <div class="empty-state">
                        <div style="font-size: 4rem; margin-bottom: 1rem;">📭</div>
                        <h3 style="margin: 0 0 0.5rem 0; color: var(--color-2);">No hay sugerencias</h3>
                        <p style="color: #999; margin: 0;">
                            <%= currentFilter != null && !currentFilter.isEmpty() 
                                ? "No hay sugerencias con el estado '" + currentFilter + "'" 
                                : "Aún no se han recibido sugerencias" %>
                        </p>
                    </div>
                <% } %>
                
            </div>
        </div>
        
    </div>
    
    <script>
        function toggleResponse(id) {
            const responseDiv = document.getElementById('response-' + id);
            if (responseDiv.style.display === 'none') {
                responseDiv.style.display = 'block';
            } else {
                responseDiv.style.display = 'none';
            }
        }
    </script>
    
    <script src="<%= request.getContextPath() %>/js/main.js"></script>
</body>
</html>
