<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.User" %>
<%@ page import="model.entity.FoundationRequest" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    if (session == null || session.getAttribute("userRole") == null || 
        !"admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    User adminUser = (User) request.getAttribute("user");
    String userName = adminUser != null ? adminUser.getNameUser() : (String) session.getAttribute("userName");
    
    @SuppressWarnings("unchecked")
    List<FoundationRequest> requests = (List<FoundationRequest>) request.getAttribute("requests");
    Integer totalRequests = (Integer) request.getAttribute("totalRequests");
    Integer pendingRequests = (Integer) request.getAttribute("pendingRequests");
    Integer approvedRequests = (Integer) request.getAttribute("approvedRequests");
    Integer rejectedRequests = (Integer) request.getAttribute("rejectedRequests");
    String currentFilter = (String) request.getAttribute("currentFilter");
    
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    if (totalRequests == null) totalRequests = 0;
    if (pendingRequests == null) pendingRequests = 0;
    if (approvedRequests == null) approvedRequests = 0;
    if (rejectedRequests == null) rejectedRequests = 0;
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Fundaciones - PawPaw Admin</title>
    
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
                <div>
                    <h1 class="content-title">🏢 Gestión de Fundaciones</h1>
                    <p class="content-subtitle">Revisar y gestionar solicitudes de partners</p>
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
                        📋
                    </div>
                    <div class="stat-info">
                        <h3>Total Solicitudes</h3>
                        <p><%= totalRequests %></p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #FF9800, #F57C00);">
                        ⏳
                    </div>
                    <div class="stat-info">
                        <h3>Pendientes</h3>
                        <p><%= pendingRequests %></p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #4CAF50, #388E3C);">
                        ✓
                    </div>
                    <div class="stat-info">
                        <h3>Aprobadas</h3>
                        <p><%= approvedRequests %></p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #f44336, #c62828);">
                        ✗
                    </div>
                    <div class="stat-info">
                        <h3>Rechazadas</h3>
                        <p><%= rejectedRequests %></p>
                    </div>
                </div>
            </div>
            
            <!-- Filtros -->
            <div class="filter-section">
                <div style="display: flex; gap: 1rem; align-items: center; flex-wrap: wrap;">
                    <a href="<%= request.getContextPath() %>/admin/foundations" 
                       class="filter-btn <%= currentFilter == null || "".equals(currentFilter) ? "active" : "" %>">
                        Todas
                    </a>
                    <a href="<%= request.getContextPath() %>/admin/foundations?filter=pending" 
                       class="filter-btn <%= "pending".equals(currentFilter) ? "active" : "" %>">
                        Pendientes
                    </a>
                    <a href="<%= request.getContextPath() %>/admin/foundations?filter=approved" 
                       class="filter-btn <%= "approved".equals(currentFilter) ? "active" : "" %>">
                        Aprobadas
                    </a>
                    <a href="<%= request.getContextPath() %>/admin/foundations?filter=rejected" 
                       class="filter-btn <%= "rejected".equals(currentFilter) ? "active" : "" %>">
                        Rechazadas
                    </a>
                </div>
            </div>
            
            <!-- Tabla de Solicitudes -->
            <div class="table-container">
                <% if (requests != null && !requests.isEmpty()) { %>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Fundación</th>
                            <th>Email</th>
                            <th>Teléfono</th>
                            <th>Fecha Solicitud</th>
                            <th>Estado</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (FoundationRequest req : requests) { %>
                        <tr>
                            <td>
                                <strong><%= req.getFoundationName() %></strong>
                            </td>
                            <td><%= req.getEmail() %></td>
                            <td><%= req.getPhone() %></td>
                            <td><%= dateFormat.format(req.getCreatedAt()) %></td>
                            <td>
                                <% if ("pending".equals(req.getStatus())) { %>
                                <span class="status-badge status-warning">⏳ Pendiente</span>
                                <% } else if ("approved".equals(req.getStatus())) { %>
                                <span class="status-badge status-active">✓ Aprobada</span>
                                <% } else if ("rejected".equals(req.getStatus())) { %>
                                <span class="status-badge status-inactive">✗ Rechazada</span>
                                <% } %>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <% if ("pending".equals(req.getStatus())) { %>
                                    <button class="btn btn-sm btn-success" 
                                            onclick="openApproveModal(<%= req.getIdRequest() %>, '<%= req.getFoundationName() %>')">
                                        ✓ Aprobar
                                    </button>
                                    <button class="btn btn-sm btn-error" 
                                            onclick="openRejectModal(<%= req.getIdRequest() %>, '<%= req.getFoundationName() %>')">
                                        ✗ Rechazar
                                    </button>
                                    <% } %>
                                    <button class="btn btn-sm btn-secundario" 
                                            onclick="viewDetails(<%= req.getIdRequest() %>, 
                                                                '<%= req.getFoundationName() %>', 
                                                                '<%= req.getEmail() %>', 
                                                                '<%= req.getPhone() %>', 
                                                                '<%= req.getWebsite() != null ? req.getWebsite() : "" %>', 
                                                                '<%= req.getDescription() != null ? req.getDescription().replace("\n", "\\n").replace("'", "\\'") : "" %>')">
                                        👁️ Ver
                                    </button>
                                </div>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
                <% } else { %>
                <div class="empty-state">
                    <div class="empty-icon">🏢</div>
                    <h3>No hay solicitudes</h3>
                    <p>No se encontraron solicitudes de fundaciones con los filtros seleccionados.</p>
                </div>
                <% } %>
            </div>
        </main>
    </div>
    
    <!-- Modal de Detalles -->
    <div class="modal-overlay" id="detailsModal">
        <div class="modal-container">
            <div class="modal-header">
                <h3 id="detailsTitle">Detalles de la Fundación</h3>
                <button class="modal-close" onclick="closeDetailsModal()">&times;</button>
            </div>
            <div class="modal-body">
                <div class="details-grid">
                    <div class="detail-item">
                        <strong>Email:</strong>
                        <span id="detailEmail">-</span>
                    </div>
                    <div class="detail-item">
                        <strong>Teléfono:</strong>
                        <span id="detailPhone">-</span>
                    </div>
                    <div class="detail-item">
                        <strong>Sitio Web:</strong>
                        <span id="detailWebsite">-</span>
                    </div>
                    <div class="detail-item" style="grid-column: 1 / -1;">
                        <strong>Descripción:</strong>
                        <p id="detailDescription" style="white-space: pre-wrap; margin-top: 0.5rem;">-</p>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secundario" onclick="closeDetailsModal()">Cerrar</button>
            </div>
        </div>
    </div>
    
    <!-- Modal de Aprobación -->
    <div class="modal-overlay" id="approveModal">
        <div class="modal-container">
            <div class="modal-header">
                <h3>Aprobar Solicitud</h3>
                <button class="modal-close" onclick="closeApproveModal()">&times;</button>
            </div>
            <form method="post" action="<%= request.getContextPath() %>/admin/foundations">
                <input type="hidden" name="action" value="approve">
                <input type="hidden" name="requestId" id="approveRequestId">
                <div class="modal-body">
                    <p>¿Estás seguro de que deseas aprobar la solicitud de <strong id="approveFoundationName"></strong>?</p>
                    <p style="color: #666; font-size: 0.9rem; margin-top: 1rem;">
                        Al aprobar, el usuario recibirá un email de confirmación y será marcado como Partner en el sistema.
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secundario" onclick="closeApproveModal()">Cancelar</button>
                    <button type="submit" class="btn btn-success">✓ Aprobar</button>
                </div>
            </form>
        </div>
    </div>
    
    <!-- Modal de Rechazo -->
    <div class="modal-overlay" id="rejectModal">
        <div class="modal-container">
            <div class="modal-header">
                <h3>Rechazar Solicitud</h3>
                <button class="modal-close" onclick="closeRejectModal()">&times;</button>
            </div>
            <form method="post" action="<%= request.getContextPath() %>/admin/foundations">
                <input type="hidden" name="action" value="reject">
                <input type="hidden" name="requestId" id="rejectRequestId">
                <div class="modal-body">
                    <p>¿Estás seguro de que deseas rechazar la solicitud de <strong id="rejectFoundationName"></strong>?</p>
                    <div class="form-group" style="margin-top: 1.5rem;">
                        <label for="rejectReason" class="form-label">Razón del rechazo (opcional)</label>
                        <textarea id="rejectReason" 
                                  name="reason" 
                                  class="form-textarea" 
                                  rows="4" 
                                  placeholder="Ej: Documentación incompleta, no cumple requisitos..."></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secundario" onclick="closeRejectModal()">Cancelar</button>
                    <button type="submit" class="btn btn-error">✗ Rechazar</button>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        // Modal de detalles
        function viewDetails(id, name, email, phone, website, description) {
            document.getElementById('detailsTitle').textContent = 'Detalles - ' + name;
            document.getElementById('detailEmail').textContent = email;
            document.getElementById('detailPhone').textContent = phone;
            document.getElementById('detailWebsite').textContent = website || 'No especificado';
            document.getElementById('detailDescription').textContent = description || 'Sin descripción';
            
            document.getElementById('detailsModal').classList.add('active');
            document.body.style.overflow = 'hidden';
        }
        
        function closeDetailsModal() {
            document.getElementById('detailsModal').classList.remove('active');
            document.body.style.overflow = '';
        }
        
        // Modal de aprobación
        function openApproveModal(requestId, foundationName) {
            document.getElementById('approveRequestId').value = requestId;
            document.getElementById('approveFoundationName').textContent = foundationName;
            document.getElementById('approveModal').classList.add('active');
            document.body.style.overflow = 'hidden';
        }
        
        function closeApproveModal() {
            document.getElementById('approveModal').classList.remove('active');
            document.body.style.overflow = '';
        }
        
        // Modal de rechazo
        function openRejectModal(requestId, foundationName) {
            document.getElementById('rejectRequestId').value = requestId;
            document.getElementById('rejectFoundationName').textContent = foundationName;
            document.getElementById('rejectModal').classList.add('active');
            document.body.style.overflow = 'hidden';
        }
        
        function closeRejectModal() {
            document.getElementById('rejectModal').classList.remove('active');
            document.body.style.overflow = '';
        }
        
        // Cerrar modales con ESC
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                closeDetailsModal();
                closeApproveModal();
                closeRejectModal();
            }
        });
        
        // Cerrar al hacer click fuera
        document.querySelectorAll('.modal-overlay').forEach(modal => {
            modal.addEventListener('click', function(e) {
                if (e.target === this) {
                    closeDetailsModal();
                    closeApproveModal();
                    closeRejectModal();
                }
            });
        });
    </script>
</body>
</html>
