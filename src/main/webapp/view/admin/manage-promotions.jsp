<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.User" %>
<%@ page import="model.entity.Promotion" %>
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
    
    @SuppressWarnings("unchecked")
    List<Promotion> promotions = (List<Promotion>) request.getAttribute("promotions");
    Integer totalPromotions = (Integer) request.getAttribute("totalPromotions");
    Long activePromotions = (Long) request.getAttribute("activePromotions");
    Long inactivePromotions = (Long) request.getAttribute("inactivePromotions");
    
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Promociones - PawPaw Admin</title>
    
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <div class="dashboard">
        <!-- Sidebar Admin -->
        <aside class="sidebar">
		    <div class="sidebar-header">
		        <a href="<%= request.getContextPath() %>/admin/panel" class="sidebar-logo">
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
		        
		        <a href="<%= request.getContextPath() %>/admin/users" class="nav-item">
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
        <main class="main-content">
            <div class="content-header">
                <h1 class="content-title">Gestión de Promociones</h1>
                <button class="btn btn-primario" onclick="openCreateModal()">
                    + Nueva Promoción
                </button>
            </div>
            
            <!-- Mensajes -->
            <% if (successMessage != null) { %>
                <div class="alert alert-success">
                    <%= successMessage %>
                </div>
            <% } %>
            
            <% if (errorMessage != null) { %>
                <div class="alert alert-error">
                    <%= errorMessage %>
                </div>
            <% } %>
            
            <!-- Stats Cards -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, var(--color-2), var(--color-1));">
                        📊
                    </div>
                    <div class="stat-info">
                        <h3>Total</h3>
                        <p><%= totalPromotions != null ? totalPromotions : 0 %></p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #4CAF50, #45a049);">
                        ✅
                    </div>
                    <div class="stat-info">
                        <h3>Activas</h3>
                        <p><%= activePromotions != null ? activePromotions : 0 %></p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #9E9E9E, #757575);">
                        ⏸️
                    </div>
                    <div class="stat-info">
                        <h3>Inactivas</h3>
                        <p><%= inactivePromotions != null ? inactivePromotions : 0 %></p>
                    </div>
                </div>
            </div>
            
            <!-- Promotions Table -->
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Slots</th>
                            <th>Precio</th>
                            <th>Ahorro</th>
                            <th>Estado</th>
                            <th>Usos</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (promotions != null && !promotions.isEmpty()) {
                            for (Promotion promo : promotions) { %>
                        <tr>
                            <td>
                                <strong><%= promo.getPromoName() %></strong>
                                <% if (promo.getPromoCode() != null) { %>
                                <br><small class="promo-code">Código: <%= promo.getPromoCode() %></small>
                                <% } %>
                            </td>
                            <td><%= promo.getSlotsQuantity() %> slots</td>
                            <td>$<%= promo.getPromoPrice() %></td>
                            <td class="savings-cell">$<%= promo.calculateSavings() %></td>
                            <td>
                                <span class="status-badge <%= promo.getIsActive() ? "status-active" : "status-inactive" %>">
                                    <%= promo.getIsActive() ? "Activa" : "Inactiva" %>
                                </span>
                            </td>
                            <td>
                                <%= promo.getCurrentUses() != null ? promo.getCurrentUses() : 0 %>
                                <% if (promo.getMaxUses() != null) { %>
                                / <%= promo.getMaxUses() %>
                                <% } else { %>
                                / ∞
                                <% } %>
                            </td>
                            <td class="action-buttons">
                                <form method="post" style="display:inline;" onsubmit="return confirm('¿Cambiar estado de esta promoción?');">
                                    <input type="hidden" name="action" value="toggle">
                                    <input type="hidden" name="idPromotion" value="<%= promo.getIdPromotion() %>">
                                    <button type="submit" class="btn-icon" title="<%= promo.getIsActive() ? "Desactivar" : "Activar" %>">
                                        <%= promo.getIsActive() ? "⏸️" : "▶️" %>
                                    </button>
                                </form>
                                
                                <button class="btn-icon" onclick="openEditModal(<%= promo.getIdPromotion() %>)" title="Editar">
                                    ✏️
                                </button>
                                
                                <form method="post" style="display:inline;" onsubmit="return confirm('¿Eliminar esta promoción?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="idPromotion" value="<%= promo.getIdPromotion() %>">
                                    <button type="submit" class="btn-icon btn-danger" title="Eliminar">
                                        🗑️
                                    </button>
                                </form>
                            </td>
                        </tr>
                        <% }
                        } else { %>
                        <tr>
                            <td colspan="7" class="empty-state">
                                <p>📭 No hay promociones registradas</p>
                                <button class="btn btn-primario" onclick="openCreateModal()">
                                    Crear primera promoción
                                </button>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </main>
    </div>
    
    <!-- Modal Create/Edit (placeholder por ahora) -->
    <script>
        function openCreateModal() {
            alert('Modal de crear promoción\n\nPróximamente: Formulario completo');
        }
        
        function openEditModal(id) {
            alert('Modal de editar promoción ID: ' + id + '\n\nPróximamente: Formulario completo');
        }
    </script>
</body>
</html>
