<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="model.entity.User" %>
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
    
    // Estadísticas
    Integer totalUsers = (Integer) request.getAttribute("totalUsers");
    Integer totalPets = (Integer) request.getAttribute("totalPets");
    Integer totalQRCodes = (Integer) request.getAttribute("totalQRCodes");
    Integer totalSuggestions = (Integer) request.getAttribute("totalSuggestions");
    Integer pendingSuggestions = (Integer) request.getAttribute("pendingSuggestions");
    
    if (totalUsers == null) totalUsers = 0;
    if (totalPets == null) totalPets = 0;
    if (totalQRCodes == null) totalQRCodes = 0;
    if (totalSuggestions == null) totalSuggestions = 0;
    if (pendingSuggestions == null) pendingSuggestions = 0;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Administración - PawPaw</title>
    
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="dashboard">

        <!-- Sidebar -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <a href="${pageContext.request.contextPath}/view/index.jsp" class="sidebar-logo">
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="PawPaw Logo">
                    <span class="sidebar-logo-text">PawPaw</span>
                </a>
            </div>
            
            <div class="sidebar-user">
                <div class="user-info">
                    <div class="user-avatar" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                        <c:out value="${userName != null ? userName.substring(0, 1).toUpperCase() : 'A'}"/>
                    </div>
                    <div class="user-details">
                        <h3><c:out value="${userName}"/></h3>
                        <p>Administrador</p>
                    </div>
                </div>
            </div>
            
            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/admin/panel" class="nav-item active">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
                    </svg>
                    Dashboard
                </a>
                
                <a href="${pageContext.request.contextPath}/admin/users" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path>
                    </svg>
                    Usuarios
                </a>
                
                <a href="${pageContext.request.contextPath}/admin/suggestions" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"></path>
                    </svg>
                    Sugerencias
                    <% if (pendingSuggestions > 0) { %>
                        <span style="margin-left: auto; background: var(--color-error); color: white; padding: 0.25rem 0.5rem; border-radius: var(--radio-full); font-size: 0.75rem; font-weight: 700;"><c:out value="${pendingSuggestions}"/></span>
                    <% } %>
                </a>               
                
                <div class="nav-divider"></div>
		        
		        <a href="${pageContext.request.contextPath}/logout" class="nav-item">
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
                    <h1>Panel de Administración</h1>
                </div>
            </div>
            
            <!-- Content -->
            <div class="content">
                <!-- Welcome Section -->
                <div class="welcome-section">
                    <h2>¡Bienvenido, <c:out value="${userName}"/>! 👋</h2>
                    <p>Administra PawPaw desde aquí</p>
                </div>
                
                <!-- Stats Cards -->
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                            👥
                        </div>
                        <div class="stat-info">
                            <h3>Total Usuarios</h3>
                            <p><c:out value="${totalUsers}"/></p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon pets">
                            🐾
                        </div>
                        <div class="stat-info">
                            <h3>Total Mascotas</h3>
                            <p><c:out value="${totalPets}"/></p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon qr">
                            📱
                        </div>
                        <div class="stat-info">
                            <h3>Códigos QR Activos</h3>
                            <p><c:out value="${totalQRCodes}"/></p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                            💬
                        </div>
                        <div class="stat-info">
                            <h3>Sugerencias</h3>
                            <p><c:out value="${totalSuggestions}"/></p>
                            <% if (pendingSuggestions > 0) { %>
                                <small style="color: var(--color-error); font-weight: 600;"><c:out value="${pendingSuggestions}"/> pendientes</small>
                            <% } %>
                        </div>
                    </div>
                </div>
                
                <!-- Quick Actions -->
                <div class="quick-actions">
                    <h3>Acciones Rápidas</h3>
                    
                    <div class="actions-grid">
                        <a href="${pageContext.request.contextPath}/admin/suggestions" class="action-btn">
                            <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
                                	d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"></path>
                            </svg>
                            Gestionar Sugerencias
                        </a>
                        
                        <a href="${pageContext.request.contextPath}/admin/users" class="action-btn">
						    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
						        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" 
						              d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path>
						    </svg>
						    Gestionar Usuarios
						</a>
                    </div>
                </div>
                
                <!-- Info adicional -->
                <div style="margin-top: 2rem; padding: 1.5rem; background: white; border-radius: var(--radio-lg); box-shadow: var(--sombra-sm);">
                    <h3 style="margin: 0 0 1rem 0; color: var(--color-2);">📊 Sistema PawPaw</h3>
                    <p style="color: #666; margin: 0; line-height: 1.6;">
                        El sistema está funcionando correctamente. Aquí puedes gestionar sugerencias de usuarios y ver estadísticas generales.
                    </p>
                </div>
                
            </div>
        </div>
        
    </div>
    
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
