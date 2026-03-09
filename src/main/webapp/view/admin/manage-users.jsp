<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%
    if (session == null || session.getAttribute("userRole") == null || 
        !"admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    @SuppressWarnings("unchecked")
    List<User> users = (List<User>) request.getAttribute("users");
    @SuppressWarnings("unchecked")
    Map<Integer, Integer> petCounts = (Map<Integer, Integer>) request.getAttribute("petCounts");
    
    Integer totalUsers = (Integer) request.getAttribute("totalUsers");
    Long normalUsers = (Long) request.getAttribute("normalUsers");
    Long adminUsers = (Long) request.getAttribute("adminUsers");
    Long partners = (Long) request.getAttribute("partners");
    
    String currentSearch = (String) request.getAttribute("currentSearch");
    String currentRole = (String) request.getAttribute("currentRole");
    
    String successMessage = (String) session.getAttribute("successMessage");
    String errorMessage = (String) session.getAttribute("errorMessage");
    session.removeAttribute("successMessage");
    session.removeAttribute("errorMessage");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Usuarios - PawPaw Admin</title>
    
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
                
                <a href="<%= request.getContextPath() %>/admin/promotions" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v13m0-13V6a2 2 0 112 2h-2zm0 0V5.5A2.5 2.5 0 109.5 8H12zm-7 4h14M5 12a2 2 0 110-4h14a2 2 0 110 4M5 12v7a2 2 0 002 2h10a2 2 0 002-2v-7"></path>
                    </svg>
                    Promociones
                </a>
                
                <a href="<%= request.getContextPath() %>/admin/payments" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 9V7a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2m2 4h10a2 2 0 002-2v-6a2 2 0 00-2-2H9a2 2 0 00-2 2v6a2 2 0 002 2zm7-5a2 2 0 11-4 0 2 2 0 014 0z"></path>
                    </svg>
                    Pagos
                </a>
                
                <a href="<%= request.getContextPath() %>/admin/foundations" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"></path>
                    </svg>
                    Fundaciones
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
            <div class="content-header">
                <div>
                    <h1 class="content-title">👥 Gestión de Usuarios</h1>
                    <p class="content-subtitle">Administrar usuarios y sus límites de slots</p>
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
            
            <!-- Stats Cards -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, var(--color-2), var(--color-1));">
                        👥
                    </div>
                    <div class="stat-info">
                        <h3>Total Usuarios</h3>
                        <p><%= totalUsers != null ? totalUsers : 0 %></p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #4CAF50, #388E3C);">
                        👤
                    </div>
                    <div class="stat-info">
                        <h3>Usuarios Normales</h3>
                        <p><%= normalUsers != null ? normalUsers : 0 %></p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #2196F3, #1976D2);">
                        🛡️
                    </div>
                    <div class="stat-info">
                        <h3>Administradores</h3>
                        <p><%= adminUsers != null ? adminUsers : 0 %></p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #FF9800, #F57C00);">
                        🤝
                    </div>
                    <div class="stat-info">
                        <h3>Partners/Fundaciones</h3>
                        <p><%= partners != null ? partners : 0 %></p>
                    </div>
                </div>
            </div>
            
            <!-- Filtros -->
            <div class="filter-section">
                <form method="get" action="<%= request.getContextPath() %>/admin/users" style="display: flex; gap: 1rem; align-items: center; flex-wrap: wrap;">
                    <div style="flex: 1; min-width: 250px;">
                        <input type="text" 
                               name="search" 
                               class="form-input" 
                               placeholder="Buscar por nombre o email..."
                               value="<%= currentSearch != null ? currentSearch : "" %>">
                    </div>
                    
                    <select name="role" class="filter-select">
                        <option value="all" <%= currentRole == null || "all".equals(currentRole) ? "selected" : "" %>>Todos los roles</option>
                        <option value="user" <%= "user".equals(currentRole) ? "selected" : "" %>>Usuarios</option>
                        <option value="admin" <%= "admin".equals(currentRole) ? "selected" : "" %>>Administradores</option>
                    </select>
                    
                    <button type="submit" class="btn btn-primario">
                        🔍 Buscar
                    </button>
                    
                    <a href="<%= request.getContextPath() %>/admin/users" class="btn btn-secundario">
                        ↻ Limpiar
                    </a>
                </form>
            </div>
            
            <!-- Tabla de Usuarios -->
            <div class="table-container">
                <% if (users != null && !users.isEmpty()) { %>
                <table class="data-table users-table">
                    <thead>
                        <tr>
                            <th>Usuario</th>
                            <th>Email</th>
                            <th>Rol</th>
                            <th>Límite Slots</th>
                            <th>Mascotas</th>
                            <th>Partner</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (User user : users) { 
                            Integer petCount = petCounts.get(user.getIdUser());
                            if (petCount == null) petCount = 0;
                            Integer limit = user.getPetLimit() != null ? user.getPetLimit() : 2;
                        %>
                        <tr>
                            <td>
                                <div class="user-cell">
                                    <div class="admin-avatar">
                                        <%= user.getNameUser().substring(0, 1).toUpperCase() %>
                                    </div>
                                    <strong><%= user.getNameUser() %></strong>
                                </div>
                            </td>
                            <td><%= user.getEmail() %></td>
                            <td>
                                <% if ("admin".equals(user.getRol())) { %>
                                <span class="user-role admin">Admin</span>
                                <% } else { %>
                                <span class="user-role user">Usuario</span>
                                <% } %>
                            </td>
                            <td>
                                <span class="slots-display <%= petCount >= limit ? "slots-full" : "" %>">
                                    <%= petCount %>/<%= limit %>
                                </span>
                            </td>
                            <td><%= petCount %></td>
                            <td>
                                <% if (user.getIsPartner() != null && user.getIsPartner()) { %>
                                <span class="status-badge status-active">✓ Partner</span>
                                <% } else { %>
                                <span class="status-badge status-inactive">No</span>
                                <% } %>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <button class="btn btn-icon btn-primario" 
                                            onclick="openAdjustModal(<%= user.getIdUser() %>, '<%= user.getNameUser() %>', <%= limit %>, <%= petCount %>)"
                                            title="Ajustar slots">
                                        ⚙️
                                    </button>
                                    <a href="<%= request.getContextPath() %>/admin/users/<%= user.getIdUser() %>/slot-history" 
                                       class="btn btn-icon btn-secundario"
                                       title="Ver historial">
                                        📜
                                    </a>
                                </div>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
                <% } else { %>
                <div class="empty-state">
                    <div class="empty-icon">👥</div>
                    <h3>No se encontraron usuarios</h3>
                    <p>Intenta ajustar los filtros de búsqueda.</p>
                </div>
                <% } %>
            </div>
        </main>
    </div>
    
    <!-- Modal de Ajuste de Slots -->
    <div class="modal-overlay" id="adjustSlotsModal">
        <div class="modal-container">
            <div class="modal-header">
                <h3 id="modalTitle">Ajustar Slots</h3>
                <button class="modal-close" onclick="closeModal()">&times;</button>
            </div>
            
            <form method="post" action="<%= request.getContextPath() %>/admin/adjust-slots" onsubmit="return validateForm()">
                <div class="modal-body">
                    <input type="hidden" name="userId" id="modalUserId">
                    
                    <div class="modal-info-card">
                        <div class="info-row">
                            <span class="info-label">Límite actual:</span>
                            <span class="info-value" id="modalCurrentLimit">-</span>
                        </div>
                        <div class="info-row">
                            <span class="info-label">Mascotas registradas:</span>
                            <span class="info-value" id="modalPetCount">-</span>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="newLimit" class="form-label">Nuevo límite *</label>
                        <input type="number" 
                               id="newLimit" 
                               name="newLimit" 
                               class="form-input" 
                               required
                               min="0"
                               max="100"
                               onchange="validateLimit()">
                        <div class="form-hint" id="limitHint"></div>
                    </div>
                    
                    <div class="form-group">
                        <label for="reason" class="form-label">Razón del ajuste * (mínimo 10 caracteres)</label>
                        <textarea id="reason" 
                                  name="reason" 
                                  class="form-textarea" 
                                  rows="4" 
                                  required
                                  minlength="10"
                                  maxlength="500"
                                  placeholder="Ej: Fundación aprobada, ajuste por error, compra adicional..."
                                  oninput="updateCharCount()"></textarea>
                        <div class="form-hint" id="charCount">0/500 caracteres</div>
                    </div>
                </div>
                
                <div class="modal-footer">
                    <button type="button" class="btn btn-secundario" onclick="closeModal()">
                        Cancelar
                    </button>
                    <button type="submit" class="btn btn-primario" id="submitBtn">
                        Guardar cambios
                    </button>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        let currentPetCount = 0;
        
        function openAdjustModal(userId, userName, currentLimit, petCount) {
            currentPetCount = petCount;
            
            document.getElementById('modalUserId').value = userId;
            document.getElementById('modalTitle').textContent = 'Ajustar Slots - ' + userName;
            document.getElementById('modalCurrentLimit').textContent = currentLimit;
            document.getElementById('modalPetCount').textContent = petCount;
            document.getElementById('newLimit').value = currentLimit;
            document.getElementById('newLimit').min = petCount;
            document.getElementById('reason').value = '';
            document.getElementById('charCount').textContent = '0/500 caracteres';
            document.getElementById('limitHint').textContent = '';
            
            document.getElementById('adjustSlotsModal').classList.add('active');
            document.body.style.overflow = 'hidden';
        }
        
        function closeModal() {
            document.getElementById('adjustSlotsModal').classList.remove('active');
            document.body.style.overflow = '';
        }
        
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
            document.getElementById('submitBtn').textContent = 'Guardando...';
            
            return true;
        }
        
        // Cerrar modal con ESC
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                closeModal();
            }
        });
        
        // Cerrar modal al hacer click fuera
        document.getElementById('adjustSlotsModal').addEventListener('click', function(e) {
            if (e.target === this) {
                closeModal();
            }
        });
    </script>
</body>
</html>
