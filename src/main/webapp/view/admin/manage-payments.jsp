<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
    
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="dashboard">
        <!-- Sidebar Admin -->
        <aside class="sidebar">
		    <div class="sidebar-header">
		        <a href="${pageContext.request.contextPath}/admin/panel" class="sidebar-logo">
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
		        <a href="${pageContext.request.contextPath}/admin/panel" class="nav-item">
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
        <main class="main-content">
            <div class="content-header">
                <h1 class="content-title">Gestión de Pagos</h1>
            </div>
            
            <!-- Mensajes -->
            <% if (successMessage != null) { %>
                <div class="alert alert-success">
                    <c:out value="${successMessage}"/>
                </div>
            <% } %>

            <% if (errorMessage != null) { %>
                <div class="alert alert-error">
                    <c:out value="${errorMessage}"/>
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
                        <p>${totalPayments != null ? totalPayments : 0}</p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #FFA726, #FB8C00);">
                        ⏳
                    </div>
                    <div class="stat-info">
                        <h3>Pendientes</h3>
                        <p>${pendingPayments != null ? pendingPayments : 0}</p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #4CAF50, #45a049);">
                        ✅
                    </div>
                    <div class="stat-info">
                        <h3>Completados</h3>
                        <p>${completedPayments != null ? completedPayments : 0}</p>
                    </div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #EF5350, #E53935);">
                        ❌
                    </div>
                    <div class="stat-info">
                        <h3>Rechazados</h3>
                        <p>${rejectedPayments != null ? rejectedPayments : 0}</p>
                    </div>
                </div>
            </div>
            
            <!-- Filtros -->
            <div class="filter-section">
                <label for="paymentFilter">Filtrar por:</label>
                <select id="paymentFilter" class="filter-select" onchange="window.location.href='${pageContext.request.contextPath}/admin/payments?filter=' + this.value">
                    <option value="" ${currentFilter == null || currentFilter.isEmpty() ? "selected" : ""}>Todos</option>
                    <option value="pending" ${"pending".equals(currentFilter) ? "selected" : ""}>Pendientes</option>
                    <option value="completed" ${"completed".equals(currentFilter) ? "selected" : ""}>Completados</option>
                    <option value="rejected" ${"rejected".equals(currentFilter) ? "selected" : ""}>Rechazados</option>
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
                                <strong><c:out value="${payment.getUserName()}"/></strong>
                                <br><small><c:out value="${payment.getUserEmail()}"/></small>
                            </td>
                            <td class="amount-cell">$<c:out value="${payment.getAmount()}"/></td>
                            <td><c:out value="${payment.getSlotsPurchased()}"/> slots</td>
                            <td><c:out value="${payment.getPaymentMethodText()}"/></td>
                            <td>
                                <span class="status-badge status-${payment.getPaymentStatus()}">
                                    <c:out value="${payment.getStatusText()}"/>
                                </span>
                            </td>
                            <td><%= payment.getCreatedAt() != null ? dateFormat.format(payment.getCreatedAt()) : "-" %></td>

                            <td class="action-buttons">
                                <% if ("pending".equals(payment.getPaymentStatus())) { %>
                                <form method="post" style="display:inline;" onsubmit="return confirm('¿Aprobar este pago?\n\nSe agregarán <%= payment.getSlotsPurchased() %> slots al usuario.');">
                                    <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                                    <input type="hidden" name="action" value="approve">
                                    <input type="hidden" name="idPayment" value="<%= payment.getIdPayment() %>">
                                    <button type="submit" class="btn-icon btn-success" title="Aprobar">
                                        ✅
                                    </button>
                                </form>
                                
                                <form method="post" style="display:inline;" onsubmit="return confirm('¿Rechazar este pago?');">
                                    <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
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
