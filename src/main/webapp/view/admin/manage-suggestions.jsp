<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    // Session flash messages require removeAttribute
    String successMessage = (String) session.getAttribute("successMessage");
    String errorMessage = (String) session.getAttribute("errorMessage");
    session.removeAttribute("successMessage");
    session.removeAttribute("errorMessage");
    if (successMessage != null) pageContext.setAttribute("successMsg", successMessage);
    if (errorMessage != null) pageContext.setAttribute("errorMsg", errorMessage);
%>
<c:set var="userName" value="${not empty requestScope.user ? requestScope.user.nameUser : sessionScope.userName}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestionar Sugerencias - PawPaw</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .custom-select { transition: all 0.3s ease; }
        .custom-select:hover { border-color: var(--color-2) !important; box-shadow: 0 2px 8px rgba(136, 74, 57, 0.15); }
        .custom-select:focus { outline: none; border-color: var(--color-1) !important; box-shadow: 0 0 0 3px rgba(136, 74, 57, 0.1); }
        .custom-select option { padding: 0.75rem; font-size: 1rem; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(-5px); } to { opacity: 1; transform: translateY(0); } }
        .custom-select-wrapper { animation: fadeIn 0.3s ease; }
    </style>
</head>
<body>
    <div class="dashboard">
        <aside class="sidebar">
		    <div class="sidebar-header"><a href="${pageContext.request.contextPath}/admin/panel" class="sidebar-logo"><img src="${pageContext.request.contextPath}/images/logo.png" alt="PawPaw Logo"><span class="sidebar-logo-text">PawPaw</span></a></div>
		    <div class="sidebar-user"><div class="user-info"><div class="user-avatar" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">${fn:toUpperCase(fn:substring(userName, 0, 1))}</div><div class="user-details"><h3><c:out value="${userName}"/></h3><p>Administrador</p></div></div></div>
		    <nav class="sidebar-nav">
		        <a href="${pageContext.request.contextPath}/admin/panel" class="nav-item"><svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path></svg> Dashboard</a>
		        <a href="${pageContext.request.contextPath}/admin/users" class="nav-item"><svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg> Usuarios</a>
		        <a href="${pageContext.request.contextPath}/admin/suggestions" class="nav-item"><svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"></path></svg> Sugerencias</a>
		        <div class="nav-divider"></div>
		        <a href="${pageContext.request.contextPath}/logout" class="nav-item"><svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path></svg> Cerrar Sesión</a>
		    </nav>
		</aside>

        <div class="main-content">
            <div class="topbar"><div class="topbar-title"><h1>Gestionar Sugerencias</h1></div></div>
            <div class="content">
                <c:if test="${not empty successMsg}"><div class="mensaje mensaje-exito" style="margin-bottom: 1.5rem;"><c:out value="${successMsg}"/></div></c:if>
                <c:if test="${not empty errorMsg}"><div class="mensaje mensaje-error" style="margin-bottom: 1.5rem;"><c:out value="${errorMsg}"/></div></c:if>

                <div class="stats-grid" style="margin-bottom: 2rem;">
                    <div class="stat-card"><div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">📊</div><div class="stat-info"><h3>Total</h3><p><c:out value="${totalSuggestions != null ? totalSuggestions : 0}"/></p></div></div>
                    <div class="stat-card"><div class="stat-icon" style="background: linear-gradient(135deg, #ffd700 0%, #ff8c00 100%);">⏳</div><div class="stat-info"><h3>Pendientes</h3><p><c:out value="${pendingSuggestions != null ? pendingSuggestions : 0}"/></p></div></div>
                    <div class="stat-card"><div class="stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">👁️</div><div class="stat-info"><h3>Revisadas</h3><p><c:out value="${reviewedSuggestions != null ? reviewedSuggestions : 0}"/></p></div></div>
                    <div class="stat-card"><div class="stat-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">✅</div><div class="stat-info"><h3>Resueltas</h3><p><c:out value="${resolvedSuggestions != null ? resolvedSuggestions : 0}"/></p></div></div>
                </div>

                <div style="margin-bottom: 2rem; display: flex; align-items: center; gap: 1rem;">
                    <label for="suggestionFilter" style="font-weight: 600; color: var(--color-2); font-size: 1rem;">Filtrar por:</label>
                    <div class="custom-select-wrapper" style="position: relative; min-width: 250px;">
                        <select id="suggestionFilter" class="custom-select" onchange="window.location.href='${pageContext.request.contextPath}/admin/suggestions?filter=' + this.value"
                                style="width: 100%; padding: 0.75rem 1rem; border: 2px solid #e0e0e0; border-radius: var(--radio-md); background: white; font-size: 1rem; cursor: pointer; appearance: none; background-image: url('data:image/svg+xml;utf8,<svg fill=\"%23884A39\" viewBox=\"0 0 24 24\" xmlns=\"http://www.w3.org/2000/svg\"><path d=\"M7 10l5 5 5-5z\"/></svg>'); background-repeat: no-repeat; background-position: right 0.75rem center; background-size: 1.5rem; transition: all 0.3s ease;">
                            <option value="" ${empty currentFilter ? 'selected' : ''}>✓ Todas (${totalSuggestions != null ? totalSuggestions : 0})</option>
                            <option value="pending" ${currentFilter == 'pending' ? 'selected' : ''}>⏳ Pendientes (${pendingSuggestions != null ? pendingSuggestions : 0})</option>
                            <option value="reviewed" ${currentFilter == 'reviewed' ? 'selected' : ''}>👁️ Revisadas (${reviewedSuggestions != null ? reviewedSuggestions : 0})</option>
                            <option value="resolved" ${currentFilter == 'resolved' ? 'selected' : ''}>✅ Resueltas (${resolvedSuggestions != null ? resolvedSuggestions : 0})</option>
                            <option value="rejected" ${currentFilter == 'rejected' ? 'selected' : ''}>❌ Rechazadas</option>
                        </select>
                    </div>
                </div>

                <c:choose>
                <c:when test="${not empty suggestions}">
                    <c:forEach var="suggestion" items="${suggestions}">
                        <div class="suggestion-card">
                            <div class="suggestion-header">
                                <div>
                                    <span class="suggestion-user"><c:out value="${not empty suggestion.userName ? suggestion.userName : 'Usuario'}"/></span>
                                    <span style="color: #999; margin-left: 0.5rem;">(<c:out value="${suggestion.userEmail}"/>)</span>
                                </div>
                                <div style="display: flex; align-items: center; gap: 0.5rem;">
                                    <span class="status-badge ${suggestion.statusClass}"><c:out value="${suggestion.statusText}"/></span>
                                    <span class="suggestion-date"><c:if test="${suggestion.submissionDate != null}"><fmt:formatDate value="${suggestion.submissionDate}" pattern="dd/MM/yyyy HH:mm"/></c:if></span>
                                </div>
                            </div>
                            <div class="suggestion-message" style="white-space: pre-wrap;"><c:out value="${suggestion.message}"/></div>

                            <c:if test="${not empty suggestion.adminResponse}">
                                <div class="admin-response">
                                    <strong style="color: var(--color-2);">Respuesta del administrador:</strong><br>
                                    <span style="white-space: pre-wrap;"><c:out value="${suggestion.adminResponse}"/></span>
                                    <c:if test="${suggestion.responseDate != null}">
                                        <div style="font-size: 0.85rem; color: #999; margin-top: 0.5rem;">Respondido: <fmt:formatDate value="${suggestion.responseDate}" pattern="dd/MM/yyyy HH:mm"/></div>
                                    </c:if>
                                </div>
                            </c:if>

                            <div class="suggestion-actions" style="margin-top: 1rem; padding-top: 1rem; border-top: 1px solid #e0e0e0;">
                                <form method="post" action="${pageContext.request.contextPath}/admin/suggestions" style="width: 100%;">
                                    <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                                    <input type="hidden" name="action" value="updateStatus">
                                    <input type="hidden" name="suggestionId" value="${suggestion.idSuggestion}">
                                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem;">
                                        <div class="form-group" style="margin: 0;">
                                            <label for="status-${suggestion.idSuggestion}" class="form-label">Cambiar estado:</label>
                                            <select id="status-${suggestion.idSuggestion}" name="status" class="form-input">
                                                <option value="pending" ${suggestion.statusSuggestion == 'pending' ? 'selected' : ''}>Pendiente</option>
                                                <option value="reviewed" ${suggestion.statusSuggestion == 'reviewed' ? 'selected' : ''}>Revisada</option>
                                                <option value="resolved" ${suggestion.statusSuggestion == 'resolved' ? 'selected' : ''}>Resuelta</option>
                                                <option value="rejected" ${suggestion.statusSuggestion == 'rejected' ? 'selected' : ''}>Rechazada</option>
                                            </select>
                                        </div>
                                        <div style="display: flex; align-items: flex-end; gap: 0.5rem;">
                                            <button type="submit" class="btn btn-primario" style="flex: 1;">Actualizar Estado</button>
                                            <button type="button" onclick="toggleResponse(${suggestion.idSuggestion})" class="btn btn-secundario">💬</button>
                                        </div>
                                    </div>
                                    <div id="response-${suggestion.idSuggestion}" style="display: none;">
                                        <div class="form-group" style="margin: 0;">
                                            <label class="form-label">Respuesta del administrador (opcional):</label>
                                            <textarea name="adminResponse" class="form-input" rows="3" placeholder="Escribe una respuesta..."><c:out value="${suggestion.adminResponse}"/></textarea>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <div style="font-size: 4rem; margin-bottom: 1rem;">📭</div>
                        <h3 style="margin: 0 0 0.5rem 0; color: var(--color-2);">No hay sugerencias</h3>
                        <p style="color: #999; margin: 0;">
                            <c:choose>
                                <c:when test="${not empty currentFilter}">No hay sugerencias con el estado "<c:out value="${currentFilter}"/>"</c:when>
                                <c:otherwise>Aún no se han recibido sugerencias</c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <script>
        function toggleResponse(id) {
            var responseDiv = document.getElementById('response-' + id);
            responseDiv.style.display = responseDiv.style.display === 'none' ? 'block' : 'none';
        }
    </script>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
