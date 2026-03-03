<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.User" %>
<%@ page import="model.entity.PaymentRequest" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
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
    List<PaymentRequest> payments = (List<PaymentRequest>) request.getAttribute("payments");
    Integer totalPayments = (Integer) request.getAttribute("totalPayments");
    Integer pendingPayments = (Integer) request.getAttribute("pendingPayments");
    Integer completedPayments = (Integer) request.getAttribute("completedPayments");
    Integer rejectedPayments = (Integer) request.getAttribute("rejectedPayments");
    String currentFilter = (String) request.getAttribute("currentFilter");
    
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("errorMessage");
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Pagos - PawPaw Admin</title>
    
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <div class="dashboard">
        <!-- Sidebar Admin -->
        <aside class="sidebar">
            <div class="logo">
                <h2>👑 Admin PawPaw</h2>
            </div>
            
            <nav class="nav-menu">
                <a href="<%= request.getContextPath() %>/admin/panel" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"></path>
                    </svg>
                    Panel
                </a>
                
                <a href="<%= request.getContextPath() %>/admin/promotions" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                    Promociones
                </a>
                
                <a href="<%= request.getContextPath() %>/admin/payments" class="nav-item active">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z"></path>
                    </svg>
                    Pagos
                </a>
                
                <a href="<%= request.getContextPath() %>/admin/foundations" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"></path>
                    </svg>
                    Fundaciones
                </a>
                
                <a href="<%= request.getContextPath() %>/admin/users" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path>
                    </svg>
                    Usuarios
                </a>
            </nav>
            
            <div class="user-info">
                <div class="user-avatar admin-avatar">
                    <%= userName != null && !userName.isEmpty() ? userName.substring(0, 1).toUpperCase() : "A" %>
                </div>
                <div class="user-details">
                    <p class="user-name"><%= userName %></p>
                    <span class="user-role">Administrador</span>
                    <a href="<%= request.getContextPath() %>/logout" class="logout-link">Cerrar sesión</a>
                </div>
            </div>
        </aside>
        
        <!-- Main Content -->
        <main class="main-content">
            <div class="content-header">
                <h1 class="content-title">Gestión de Pagos</h1>
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
                        <p><%= totalPayments != null ? totalPayments : 0 %></p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #FFA726, #FB8C00);">
                        ⏳
                    </div>
                    <div class="stat-info">
                        <h3>Pendientes</h3>
                        <p><%= pendingPayments != null ? pendingPayments : 0 %></p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #4CAF50, #45a049);">
                        ✅
                    </div>
                    <div class="stat-info">
                        <h3>Completados</h3>
                        <p><%= completedPayments != null ? completedPayments : 0 %></p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #EF5350, #E53935);">
                        ❌
                    </div>
                    <div class="stat-info">
                        <h3>Rechazados</h3>
                        <p><%= rejectedPayments != null ? rejectedPayments : 0 %></p>
                    </div>
                </div>
            </div>
            
            <!-- Filtros -->
            <div class="filter-section">
                <label for="paymentFilter">Filtrar por:</label>
                <select id="paymentFilter" class="filter-select" onchange="window.location.href='<%= request.getContextPath() %>/admin/payments?filter=' + this.value">
                    <option value="" <%= currentFilter == null || currentFilter.isEmpty() ? "selected" : "" %>>Todos</option>
                    <option value="pending" <%= "pending".equals(currentFilter) ? "selected" : "" %>>Pendientes</option>
                    <option value="completed" <%= "completed".equals(currentFilter) ? "selected" : "" %>>Completados</option>
                    <option value="rejected" <%= "rejected".equals(currentFilter) ? "selected" : "" %>>Rechazados</option>
                </select>
            </div>
            
            <!-- Payments Table -->
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Usuario</th>
                            <th>Monto</th>
                            <th>Slots</th>
                            <th>Método</th>
                            <th>Estado</th>
                            <th>Fecha</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (payments != null && !payments.isEmpty()) {
                            for (PaymentRequest payment : payments) { %>
                        <tr>
                            <td>
                                <strong><%= payment.getUserName() %></strong>
                                <br><small><%= payment.getUserEmail() %></small>
                            </td>
                            <td class="amount-cell">$<%= payment.getAmount() %></td>
                            <td><%= payment.getSlotsPurchased() %> slots</td>
                            <td><%= payment.getPaymentMethodText() %></td>
                            <td>
                                <span class="status-badge status-<%= payment.getPaymentStatus() %>">
                                    <%= payment.getStatusText() %>
                                </span>
                            </td>
                            <td><%= payment.getCreatedAt() != null ? dateFormat.format(payment.getCreatedAt()) : "-" %></td>
                            <td class="action-buttons">
                                <% if ("pending".equals(payment.getPaymentStatus())) { %>
                                <form method="post" style="display:inline;" onsubmit="return confirm('¿Aprobar este pago?\n\nSe agregarán <%= payment.getSlotsPurchased() %> slots al usuario.');">
                                    <input type="hidden" name="action" value="approve">
                                    <input type="hidden" name="idPayment" value="<%= payment.getIdPayment() %>">
                                    <button type="submit" class="btn-icon btn-success" title="Aprobar">
                                        ✅
                                    </button>
                                </form>
                                
                                <form method="post" style="display:inline;" onsubmit="return confirm('¿Rechazar este pago?');">
                                    <input type="hidden" name="action" value="reject">
                                    <input type="hidden" name="idPayment" value="<%= payment.getIdPayment() %>">
                                    <input type="hidden" name="reason" value="Comprobante inválido">
                                    <button type="submit" class="btn-icon btn-danger" title="Rechazar">
                                        ❌
                                    </button>
                                </form>
                                <% } else { %>
                                <span style="color: #999;">-</span>
                                <% } %>
                            </td>
                        </tr>
                        <% }
                        } else { %>
                        <tr>
                            <td colspan="7" class="empty-state">
                                <p>📭 No hay pagos registrados</p>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </main>
    </div>
</body>
</html>
