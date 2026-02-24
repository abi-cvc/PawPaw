<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
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
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");
    
    // Usuarios y estadísticas
    @SuppressWarnings("unchecked")
    List<User> users = (List<User>) request.getAttribute("users");
    
    @SuppressWarnings("unchecked")
    Map<Integer, Integer> userPetCounts = (Map<Integer, Integer>) request.getAttribute("userPetCounts");
    
    @SuppressWarnings("unchecked")
    Map<Integer, Integer> userQRCounts = (Map<Integer, Integer>) request.getAttribute("userQRCounts");
    
    Integer totalUsers = (Integer) request.getAttribute("totalUsers");
    Long activeUsers = (Long) request.getAttribute("activeUsers");
    Long inactiveUsers = (Long) request.getAttribute("inactiveUsers");
    Long adminUsers = (Long) request.getAttribute("adminUsers");
    String currentFilter = (String) request.getAttribute("currentFilter");
    
    if (totalUsers == null) totalUsers = 0;
    if (activeUsers == null) activeUsers = 0L;
    if (inactiveUsers == null) inactiveUsers = 0L;
    if (adminUsers == null) adminUsers = 0L;
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Usuarios - PawPaw</title>
    
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
                
                <a href="<%= request.getContextPath() %>/admin/users" class="nav-item active">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path>
                    </svg>
                    Usuarios
                </a>
                
                <a href="<%= request.getContextPath() %>/admin/suggestions" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"></path>
                    </svg>
                    Sugerencias
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
                    <h1>Gestionar Usuarios</h1>
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
                
                <!-- Estadísticas -->
                <div class="stats-grid" style="margin-bottom: 2rem;">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">👥</div>
                        <div class="stat-info">
                            <h3>Total Usuarios</h3>
                            <p><%= totalUsers %></p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">✅</div>
                        <div class="stat-info">
                            <h3>Activos</h3>
                            <p><%= activeUsers %></p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);">❌</div>
                        <div class="stat-info">
                            <h3>Inactivos</h3>
                            <p><%= inactiveUsers %></p>
                        </div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">👑</div>
                        <div class="stat-info">
                            <h3>Administradores</h3>
                            <p><%= adminUsers %></p>
                        </div>
                    </div>
                </div>
                
                <!-- Filtros -->
                <div class="filter-tabs">
                    <a href="<%= request.getContextPath() %>/admin/users" 
                       class="filter-tab <%= currentFilter == null || currentFilter.isEmpty() ? "active" : "" %>">
                        Todos
                    </a>
                    <a href="<%= request.getContextPath() %>/admin/users?filter=active" 
                       class="filter-tab <%= "active".equals(currentFilter) ? "active" : "" %>">
                        Activos
                    </a>
                    <a href="<%= request.getContextPath() %>/admin/users?filter=inactive" 
                       class="filter-tab <%= "inactive".equals(currentFilter) ? "active" : "" %>">
                        Inactivos
                    </a>
                    <a href="<%= request.getContextPath() %>/admin/users?filter=admin" 
                       class="filter-tab <%= "admin".equals(currentFilter) ? "active" : "" %>">
                        Admins
                    </a>
                    <a href="<%= request.getContextPath() %>/admin/users?filter=user" 
                       class="filter-tab <%= "user".equals(currentFilter) ? "active" : "" %>">
                        Users
                    </a>
                </div>
                
                <!-- Tabla de Usuarios -->
                <% if (users != null && !users.isEmpty()) { %>
                    <div style="background: white; border-radius: var(--radio-lg); box-shadow: var(--sombra-sm); overflow: hidden;">
                        <div style="overflow-x: auto;">
                            <table style="width: 100%; border-collapse: collapse;">
                                <thead style="background: linear-gradient(135deg, var(--color-2) 0%, var(--color-1) 100%); color: white;">
                                    <tr>
                                        <th style="padding: 1rem; text-align: left; font-weight: 600;">Usuario</th>
                                        <th style="padding: 1rem; text-align: left; font-weight: 600;">Email</th>
                                        <th style="padding: 1rem; text-align: center; font-weight: 600;">Rol</th>
                                        <th style="padding: 1rem; text-align: center; font-weight: 600;">Estado</th>
                                        <th style="padding: 1rem; text-align: center; font-weight: 600;">Mascotas</th>
                                        <th style="padding: 1rem; text-align: center; font-weight: 600;">QR Activos</th>
                                        <th style="padding: 1rem; text-align: center; font-weight: 600;">Registro</th>
                                        <th style="padding: 1rem; text-align: center; font-weight: 600;">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (User u : users) { 
                                        int petCount = userPetCounts.get(u.getIdUser()) != null ? userPetCounts.get(u.getIdUser()) : 0;
                                        int qrCount = userQRCounts.get(u.getIdUser()) != null ? userQRCounts.get(u.getIdUser()) : 0;
                                    %>
                                        <tr style="border-bottom: 1px solid #e0e0e0;">
                                            <td style="padding: 1rem;">
                                                <div style="display: flex; align-items: center; gap: 0.75rem;">
                                                    <div style="width: 40px; height: 40px; border-radius: 50%; background: var(--color-3); display: flex; align-items: center; justify-content: center; font-weight: 600; color: var(--color-1);">
                                                        <%= u.getNameUser() != null ? u.getNameUser().substring(0, 1).toUpperCase() : "?" %>
                                                    </div>
                                                    <div>
                                                        <div style="font-weight: 600; color: var(--color-1);"><%= u.getNameUser() %></div>
                                                        <div style="font-size: 0.85rem; color: #999;">ID: <%= u.getIdUser() %></div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td style="padding: 1rem; color: #666;">
                                                <%= u.getEmail() %>
                                            </td>
                                            <td style="padding: 1rem; text-align: center;">
                                                <% if ("admin".equalsIgnoreCase(u.getRol())) { %>
                                                    <span style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: white; padding: 0.25rem 0.75rem; border-radius: var(--radio-full); font-size: 0.85rem; font-weight: 600;">
                                                        👑 Admin
                                                    </span>
                                                <% } else { %>
                                                    <span style="background: #e3f2fd; color: #1976d2; padding: 0.25rem 0.75rem; border-radius: var(--radio-full); font-size: 0.85rem; font-weight: 600;">
                                                        👤 Usuario
                                                    </span>
                                                <% } %>
                                            </td>
                                            <td style="padding: 1rem; text-align: center;">
                                                <% if (u.getActive()) { %>
                                                    <span style="background: #d4edda; color: #155724; padding: 0.25rem 0.75rem; border-radius: var(--radio-full); font-size: 0.85rem; font-weight: 600;">
                                                        ✅ Activo
                                                    </span>
                                                <% } else { %>
                                                    <span style="background: #f8d7da; color: #721c24; padding: 0.25rem 0.75rem; border-radius: var(--radio-full); font-size: 0.85rem; font-weight: 600;">
                                                        ❌ Inactivo
                                                    </span>
                                                <% } %>
                                            </td>
                                            <td style="padding: 1rem; text-align: center; font-weight: 600; color: var(--color-2);">
                                                <%= petCount %>
                                            </td>
                                            <td style="padding: 1rem; text-align: center; font-weight: 600; color: var(--color-2);">
                                                <%= qrCount %>
                                            </td>
                                            <td style="padding: 1rem; text-align: center; color: #999; font-size: 0.9rem;">
                                                <%= u.getRegistrationDate() != null ? dateFormat.format(u.getRegistrationDate()) : "-" %>
                                            </td>
                                            <td style="padding: 1rem; text-align: center;">
                                                <div style="display: flex; gap: 0.5rem; justify-content: center; flex-wrap: wrap;">
                                                    <!-- Botón Activar/Desactivar -->
                                                    <form method="post" action="<%= request.getContextPath() %>/admin/users" style="display: inline;">
                                                        <input type="hidden" name="action" value="toggleStatus">
                                                        <input type="hidden" name="userId" value="<%= u.getIdUser() %>">
                                                        <button type="submit" 
                                                                style="padding: 0.5rem 1rem; border: none; border-radius: var(--radio-sm); font-weight: 600; cursor: pointer; font-size: 0.85rem; transition: var(--transicion); <%= u.getActive() ? "background: #ff9800; color: white;" : "background: #4caf50; color: white;" %>"
                                                                onmouseover="this.style.opacity='0.8'"
                                                                onmouseout="this.style.opacity='1'"
                                                                onclick="return confirm('<%= u.getActive() ? "¿Desactivar este usuario?" : "¿Activar este usuario?" %>')">
                                                            <%= u.getActive() ? "Desactivar" : "Activar" %>
                                                        </button>
                                                    </form>
                                                    
                                                    <!-- Botón Eliminar (solo si no tiene mascotas) -->
                                                    <% if (petCount == 0) { %>
                                                        <form method="post" action="<%= request.getContextPath() %>/admin/users" style="display: inline;">
                                                            <input type="hidden" name="action" value="delete">
                                                            <input type="hidden" name="userId" value="<%= u.getIdUser() %>">
                                                            <button type="submit" 
                                                                    style="padding: 0.5rem 1rem; border: none; border-radius: var(--radio-sm); background: #f44336; color: white; font-weight: 600; cursor: pointer; font-size: 0.85rem; transition: var(--transicion);"
                                                                    onmouseover="this.style.opacity='0.8'"
                                                                    onmouseout="this.style.opacity='1'"
                                                                    onclick="return confirm('⚠️ ADVERTENCIA: Esta acción es IRREVERSIBLE.\\n\\n¿Estás seguro de eliminar permanentemente a <%= u.getNameUser() %>?')">
                                                                🗑️ Eliminar
                                                            </button>
                                                        </form>
                                                    <% } else { %>
                                                        <button disabled 
                                                                style="padding: 0.5rem 1rem; border: none; border-radius: var(--radio-sm); background: #ccc; color: #666; font-weight: 600; cursor: not-allowed; font-size: 0.85rem;"
                                                                title="No se puede eliminar porque tiene <%= petCount %> mascota(s)">
                                                            🔒 Bloqueado
                                                        </button>
                                                    <% } %>
                                                </div>
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                <% } else { %>
                    <div class="empty-state">
                        <div style="font-size: 4rem; margin-bottom: 1rem;">👥</div>
                        <h3 style="margin: 0 0 0.5rem 0; color: var(--color-2);">No hay usuarios</h3>
                        <p style="color: #999; margin: 0;">
                            <%= currentFilter != null && !currentFilter.isEmpty() 
                                ? "No hay usuarios con el filtro '" + currentFilter + "'" 
                                : "No hay usuarios registrados en el sistema" %>
                        </p>
                    </div>
                <% } %>
                
            </div>
        </div>
        
    </div>
    
    <script src="<%= request.getContextPath() %>/js/main.js"></script>
</body>
</html>
