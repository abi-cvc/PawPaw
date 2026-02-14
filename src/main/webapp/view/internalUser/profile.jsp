<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.User" %>
<%
    // Verificar sesión
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    User user = (User) request.getAttribute("user");
    String userName = user != null ? user.getNameUser() : "";
    String userEmail = user != null ? user.getEmail() : "";
    
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    String errorPassword = (String) request.getAttribute("errorPassword");
    String successPassword = (String) request.getAttribute("successPassword");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Perfil - PawPaw</title>
    
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
                
                <a href="<%= request.getContextPath() %>/user/profile" class="nav-item active">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                    </svg>
                    Mi Perfil
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
                    <h1>Mi Perfil</h1>
                </div>
            </div>
            
            <!-- Content -->
            <div class="content">
                
                <div class="profile-container">
                    
                    <!-- Sección de Información Personal -->
                    <div class="profile-section">
                        <h2>Información Personal</h2>
                        
                        <% if (error != null) { %>
                            <div class="mensaje mensaje-error">
                                ⚠️ <%= error %>
                            </div>
                        <% } %>
                        
                        <% if (success != null) { %>
                            <div class="mensaje mensaje-exito">
                                ✅ <%= success %>
                            </div>
                        <% } %>
                        
                        <form method="POST" action="<%= request.getContextPath() %>/user/profile">
                            <input type="hidden" name="action" value="updateProfile">
                            
                            <div class="form-group">
                                <label for="name" class="form-label required">Nombre completo</label>
                                <input type="text" 
                                       id="name" 
                                       name="name" 
                                       class="form-input" 
                                       value="<%= userName %>"
                                       required>
                            </div>
                            
                            <div class="form-group">
                                <label for="email" class="form-label required">Email</label>
                                <input type="email" 
                                       id="email" 
                                       name="email" 
                                       class="form-input" 
                                       value="<%= userEmail %>"
                                       required>
                            </div>
                            
                            <div class="form-actions">
                                <button type="submit" class="btn btn-primario">
                                    💾 Guardar Cambios
                                </button>
                            </div>
                        </form>
                    </div>
                    
                    <!-- Sección de Cambiar Contraseña -->
                    <div class="profile-section">
                        <h2>Cambiar Contraseña</h2>
                        
                        <% if (errorPassword != null) { %>
                            <div class="mensaje mensaje-error">
                                ⚠️ <%= errorPassword %>
                            </div>
                        <% } %>
                        
                        <% if (successPassword != null) { %>
                            <div class="mensaje mensaje-exito">
                                ✅ <%= successPassword %>
                            </div>
                        <% } %>
                        
                        <form method="POST" action="<%= request.getContextPath() %>/user/profile">
                            <input type="hidden" name="action" value="updatePassword">
                            
                            <div class="form-group">
                                <label for="currentPassword" class="form-label required">Contraseña actual</label>
                                <input type="password" 
                                       id="currentPassword" 
                                       name="currentPassword" 
                                       class="form-input" 
                                       required>
                            </div>
                            
                            <div class="form-group">
                                <label for="newPassword" class="form-label required">Nueva contraseña</label>
                                <input type="password" 
                                       id="newPassword" 
                                       name="newPassword" 
                                       class="form-input" 
                                       placeholder="Mínimo 6 caracteres"
                                       required>
                            </div>
                            
                            <div class="form-group">
                                <label for="confirmPassword" class="form-label required">Confirmar nueva contraseña</label>
                                <input type="password" 
                                       id="confirmPassword" 
                                       name="confirmPassword" 
                                       class="form-input" 
                                       required>
                            </div>
                            
                            <div class="form-actions">
                                <button type="submit" class="btn btn-primario">
                                    🔐 Cambiar Contraseña
                                </button>
                            </div>
                        </form>
                    </div>
                    
                    <!-- Información de la Cuenta -->
                    <div class="profile-section">
                        <h2>Información de la Cuenta</h2>
                        
                        <div class="account-info">
                            <div class="info-item">
                                <strong>Rol:</strong>
                                <span class="badge <%= user != null && "admin".equals(user.getRol()) ? "badge-admin" : "badge-user" %>">
                                    <%= user != null ? user.getRol().toUpperCase() : "USER" %>
                                </span>
                            </div>
                            
                            <div class="info-item">
                                <strong>Estado:</strong>
                                <span class="badge badge-success">
                                    ✓ ACTIVO
                                </span>
                            </div>
                            
                            <div class="info-item">
                                <strong>Fecha de registro:</strong>
                                <span><%= user != null && user.getRegistrationDate() != null ? user.getRegistrationDate() : "N/A" %></span>
                            </div>
                        </div>
                    </div>
                    
                </div>
                
            </div>
        </div>
    </div>
</body>
</html>
