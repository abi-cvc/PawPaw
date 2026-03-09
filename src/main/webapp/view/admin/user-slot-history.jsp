<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.User" %>
<%@ page import="model.entity.SlotAdjustment" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    if (session == null || session.getAttribute("userRole") == null || 
        !"admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    User user = (User) request.getAttribute("user");
    @SuppressWarnings("unchecked")
    List<SlotAdjustment> adjustments = (List<SlotAdjustment>) request.getAttribute("adjustments");
    Integer currentPetCount = (Integer) request.getAttribute("currentPetCount");
    Integer totalAdjustments = (Integer) request.getAttribute("totalAdjustments");
    Long increases = (Long) request.getAttribute("increases");
    Long decreases = (Long) request.getAttribute("decreases");
    
    String successMessage = (String) session.getAttribute("successMessage");
    String errorMessage = (String) session.getAttribute("errorMessage");
    session.removeAttribute("successMessage");
    session.removeAttribute("errorMessage");
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Slots - <%= user != null ? user.getNameUser() : "Usuario" %> - PawPaw Admin</title>
    
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <div class="dashboard">
        <!-- Sidebar -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <a href="<%= request.getContextPath() %>/admin/panel" class="sidebar-logo">
                    <img src="<%= request.getContextPath() %>/images/logo.png" alt="PawPaw Logo">
                    <span class="sidebar-logo-text">PawPaw Admin</span>
                </a>
            </div>
            
            <nav class="sidebar-nav">
                <a href="<%= request.getContextPath() %>/admin/panel" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"></path>
                    </svg>
                    Panel
                </a>
                
                <a href="<%= request.getContextPath() %>/admin/users" class="nav-item active">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path>
                    </svg>
                    Usuarios
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
            <% if (user != null) { %>
            
            <div class="content-header">
                <div>
                    <a href="<%= request.getContextPath() %>/admin/users" class="btn btn-secundario" style="margin-bottom: 1rem;">
                        ← Volver a usuarios
                    </a>
                    <h1 class="content-title">📜 Historial de Slots - <%= user.getNameUser() %></h1>
                    <p class="content-subtitle">Registro completo de ajustes de límite de mascotas</p>
                </div>
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
            
            <!-- User Info Card -->
            <div class="user-info-card">
                <div class="user-info-header">
                    <div class="admin-avatar large">
                        <%= user.getNameUser().substring(0, 1).toUpperCase() %>
                    </div>
                    <div>
                        <h2><%= user.getNameUser() %></h2>
                        <p><%= user.getEmail() %></p>
                        <% if (user.getIsPartner() != null && user.getIsPartner()) { %>
                        <span class="status-badge status-active">🤝 Partner</span>
                        <% } %>
                    </div>
                </div>
                
                <div class="user-info-stats">
                    <div class="stat-item">
                        <span class="stat-label">Límite actual</span>
                        <span class="stat-value"><%= user.getPetLimit() != null ? user.getPetLimit() : 2 %></span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-label">Mascotas registradas</span>
                        <span class="stat-value"><%= currentPetCount != null ? currentPetCount : 0 %></span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-label">Slots disponibles</span>
                        <% 
                        int limit = user.getPetLimit() != null ? user.getPetLimit() : 2;
                        int petCount = currentPetCount != null ? currentPetCount : 0;
                        int available = limit - petCount;
                        %>
                        <span class="stat-value <%= available <= 0 ? "text-danger" : "text-success" %>">
                            <%= available %>
                        </span>
                    </div>
                </div>
            </div>
            
            <!-- Formulario de Ajuste -->
            <div class="adjustment-form-card">
                <h3>⚙️ Ajustar Límite de Slots</h3>
                
                <form method="post" action="<%= request.getContextPath() %>/admin/adjust-slots" onsubmit="return validateForm()">
                    <input type="hidden" name="userId" value="<%= user.getIdUser() %>">
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label for="newLimit" class="form-label">Nuevo límite *</label>
                            <input type="number" 
                                   id="newLimit" 
                                   name="newLimit" 
                                   class="form-input" 
                                   required
                                   min="<%= currentPetCount %>"
                                   max="100"
                                   value="<%= user.getPetLimit() %>"
                                   onchange="validateLimit()">
                            <div class="form-hint" id="limitHint"></div>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="reason" class="form-label">Razón del ajuste * (mínimo 10 caracteres)</label>
                        <textarea id="reason" 
                                  name="reason" 
                                  class="form-textarea" 
                                  rows="3" 
                                  required
                                  minlength="10"
                                  maxlength="500"
                                  placeholder="Ej: Fundación aprobada, ajuste por error, compra adicional..."
                                  oninput="updateCharCount()"></textarea>
                        <div class="form-hint" id="charCount">0/500 caracteres</div>
                    </div>
                    
                    <button type="submit" class="btn btn-primario" id="submitBtn">
                        💾 Guardar ajuste
                    </button>
                </form>
            </div>
            
            <!-- Stats de Historial -->
            <div class="stats-grid" style="margin-bottom: 2rem;">
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #2196F3, #1976D2);">
                        📊
                    </div>
                    <div class="stat-info">
                        <h3>Total Ajustes</h3>
                        <p><%= totalAdjustments != null ? totalAdjustments : 0 %></p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #4CAF50, #388E3C);">
                        📈
                    </div>
                    <div class="stat-info">
                        <h3>Aumentos</h3>
                        <p><%= increases != null ? increases : 0 %></p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #FF9800, #F57C00);">
                        📉
                    </div>
                    <div class="stat-info">
                        <h3>Disminuciones</h3>
                        <p><%= decreases != null ? decreases : 0 %></p>
                    </div>
                </div>
            </div>
            
            <!-- Tabla de Historial -->
            <div class="table-container">
                <h3 style="margin-bottom: 1rem;">📜 Historial Completo de Ajustes</h3>
                
                <% if (adjustments != null && !adjustments.isEmpty()) { %>
                <table class="data-table history-table">
                    <thead>
                        <tr>
                            <th>Fecha y Hora</th>
                            <th>Administrador</th>
                            <th>Anterior</th>
                            <th>Nuevo</th>
                            <th>Cambio</th>
                            <th>Razón</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (SlotAdjustment adj : adjustments) { %>
                        <tr>
                            <td><%= dateFormat.format(adj.getCreatedAt()) %></td>
                            <td>
                                <% if (adj.getAdminName() != null) { %>
                                <%= adj.getAdminName() %>
                                <% } else { %>
                                <em>Sistema</em>
                                <% } %>
                            </td>
                            <td><%= adj.getPreviousLimit() %></td>
                            <td><%= adj.getNewLimit() %></td>
                            <td>
                                <% if (adj.isIncrease()) { %>
                                <span class="change-badge increase">+<%= adj.getAdjustmentAmount() %></span>
                                <% } else if (adj.isDecrease()) { %>
                                <span class="change-badge decrease"><%= adj.getAdjustmentAmount() %></span>
                                <% } else { %>
                                <span class="change-badge neutral">0</span>
                                <% } %>
                            </td>
                            <td class="reason-cell"><%= adj.getAdjustmentReason() %></td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
                <% } else { %>
                <div class="empty-state">
                    <div class="empty-icon">📭</div>
                    <h3>No hay ajustes registrados</h3>
                    <p>Este usuario aún no tiene historial de ajustes de slots.</p>
                </div>
                <% } %>
            </div>
            
            <% } else { %>
            <div class="empty-state">
                <div class="empty-icon">⚠️</div>
                <h3>Usuario no encontrado</h3>
                <p><a href="<%= request.getContextPath() %>/admin/users">Volver a la lista de usuarios</a></p>
            </div>
            <% } %>
        </main>
    </div>
    
    <script>
        const currentPetCount = <%= currentPetCount != null ? currentPetCount : 0 %>;
        
        function validateLimit() {
            const newLimit = parseInt(document.getElementById('newLimit').value);
            const hint = document.getElementById('limitHint');
            
            if (isNaN(newLimit)) return;
            
            if (newLimit < currentPetCount) {
                hint.textContent = '⚠️ No puedes bajar el límite por debajo de ' + currentPetCount + ' (mascotas registradas)';
                hint.style.color = '#dc3545';
                return false;
            } else if (newLimit > 100) {
                hint.textContent = '⚠️ El límite máximo es 100 slots';
                hint.style.color = '#dc3545';
                return false;
            } else {
                hint.textContent = '✓ Límite válido';
                hint.style.color = '#28a745';
                return true;
            }
        }
        
        function updateCharCount() {
            const reason = document.getElementById('reason').value;
            const count = document.getElementById('charCount');
            count.textContent = reason.length + '/500 caracteres';
            
            if (reason.length < 10) {
                count.style.color = '#dc3545';
            } else {
                count.style.color = '#28a745';
            }
        }
        
        function validateForm() {
            const reason = document.getElementById('reason').value;
            
            if (reason.trim().length < 10) {
                alert('La razón debe tener al menos 10 caracteres');
                return false;
            }
            
            if (!validateLimit()) {
                alert('El nuevo límite no es válido');
                return false;
            }
            
            document.getElementById('submitBtn').disabled = true;
            document.getElementById('submitBtn').textContent = '⏳ Guardando...';
            
            return true;
        }
    </script>
</body>
</html>
