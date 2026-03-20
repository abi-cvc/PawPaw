<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page import="model.entity.User" %>
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
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Donaciones - Admin PawPaw</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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

                <a href="${pageContext.request.contextPath}/admin/donations" class="nav-item active">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path>
                    </svg>
                    Donaciones
                </a>

                <div class="nav-divider"></div>

                <a href="${pageContext.request.contextPath}/logout" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
                    </svg>
                    Cerrar Sesion
                </a>
            </nav>
        </aside>

        <!-- Main Content -->
        <div class="main-content">
            <div class="topbar">
                <div class="topbar-title">
                    <h1>Donaciones</h1>
                </div>
            </div>

            <div class="content">
                <!-- Stats Cards -->
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #4CAF50 0%, #45a049 100%);">
                            💰
                        </div>
                        <div class="stat-info">
                            <h3>Total Recaudado</h3>
                            <p>$<fmt:formatNumber value="${totalAmount}" pattern="#,##0.00"/></p>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                            ❤️
                        </div>
                        <div class="stat-info">
                            <h3>Total Donaciones</h3>
                            <p><c:out value="${totalDonations}"/></p>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                            📊
                        </div>
                        <div class="stat-info">
                            <h3>Promedio por Donacion</h3>
                            <p>$<fmt:formatNumber value="${avgDonation}" pattern="#,##0.00"/></p>
                        </div>
                    </div>
                </div>

                <!-- Grafica -->
                <div style="margin-top: 2rem; padding: 1.5rem; background: white; border-radius: var(--radio-lg); box-shadow: var(--sombra-sm);">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1rem;">
                        <h3 style="margin: 0; color: var(--color-2);">Donaciones por Mes</h3>
                        <div>
                            <c:forEach var="y" begin="${currentYear - 2}" end="${currentYear}">
                                <a href="${pageContext.request.contextPath}/admin/donations?year=${y}"
                                   style="padding: 0.4rem 0.8rem; margin-left: 0.25rem; border-radius: var(--radio-sm); text-decoration: none; font-size: 0.85rem;
                                   ${y == selectedYear ? 'background: var(--color-2); color: white;' : 'background: var(--color-5); color: var(--color-1);'}">
                                    <c:out value="${y}"/>
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="donate-chart-container">
                        <canvas id="donationsChart"></canvas>
                    </div>
                </div>

                <!-- Tabla de donaciones -->
                <div style="margin-top: 2rem; padding: 1.5rem; background: white; border-radius: var(--radio-lg); box-shadow: var(--sombra-sm);">
                    <h3 style="margin: 0 0 1rem 0; color: var(--color-2);">Donaciones Recientes</h3>

                    <c:choose>
                        <c:when test="${empty donations}">
                            <p style="color: #666; text-align: center; padding: 2rem 0;">No hay donaciones registradas.</p>
                        </c:when>
                        <c:otherwise>
                            <div style="overflow-x: auto;">
                                <table class="users-table">
                                    <thead>
                                        <tr>
                                            <th>Fecha</th>
                                            <th>Donante</th>
                                            <th>Monto</th>
                                            <th>Estado</th>
                                            <th>Mensaje</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="donation" items="${donations}">
                                            <tr>
                                                <td>
                                                    <fmt:formatDate value="${donation.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                </td>
                                                <td>
                                                    <c:out value="${donation.displayName}"/>
                                                    <c:if test="${not empty donation.donorEmail}">
                                                        <br><small style="color: #999;"><c:out value="${donation.donorEmail}"/></small>
                                                    </c:if>
                                                </td>
                                                <td style="font-weight: 600; color: var(--color-success);">
                                                    $<fmt:formatNumber value="${donation.amount}" pattern="#,##0.00"/>
                                                </td>
                                                <td>
                                                    <span class="status-badge <c:out value='${donation.statusClass}'/>">
                                                        <c:out value="${donation.statusText}"/>
                                                    </span>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty donation.message}">
                                                            <c:out value="${donation.message}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span style="color: #ccc;">-</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

            </div>
        </div>

    </div>

    <script>
    (function() {
        var monthlyData = ${monthlyData};
        var months = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];

        var ctx = document.getElementById('donationsChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: months,
                datasets: [{
                    label: 'Donaciones (USD)',
                    data: monthlyData,
                    backgroundColor: 'rgba(195, 129, 84, 0.7)',
                    borderColor: '#C38154',
                    borderWidth: 1,
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) { return '$' + value; }
                        }
                    }
                }
            }
        });
    })();
    </script>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
