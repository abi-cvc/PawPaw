<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
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
    <title>Gestión de Usuarios - PawPaw Admin</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="dashboard">
        <aside class="sidebar">
            <div class="sidebar-header"><a href="${pageContext.request.contextPath}/view/index.jsp" class="sidebar-logo"><img src="${pageContext.request.contextPath}/images/logo.png" alt="PawPaw Logo"><span class="sidebar-logo-text">PawPaw</span></a></div>
            <div class="sidebar-user"><div class="user-info"><div class="user-avatar" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">${fn:toUpperCase(fn:substring(userName, 0, 1))}</div><div class="user-details"><h3><c:out value="${userName}"/></h3><p>Administrador</p></div></div></div>
            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/admin/panel" class="nav-item active"><svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path></svg> Dashboard</a>
                <a href="${pageContext.request.contextPath}/admin/users" class="nav-item"><svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg> Usuarios</a>
                <a href="${pageContext.request.contextPath}/admin/suggestions" class="nav-item"><svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"></path></svg> Sugerencias
                    <c:if test="${pendingSuggestions != null && pendingSuggestions > 0}"><span style="margin-left: auto; background: var(--color-error); color: white; padding: 0.25rem 0.5rem; border-radius: var(--radio-full); font-size: 0.75rem; font-weight: 700;"><c:out value="${pendingSuggestions}"/></span></c:if>
                </a>
                <div class="nav-divider"></div>
		        <a href="${pageContext.request.contextPath}/logout" class="nav-item"><svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path></svg> Cerrar Sesión</a>
            </nav>
        </aside>

        <main class="main-content">
            <div class="content-header"><div><h1 class="content-title">👥 Gestión de Usuarios</h1><p class="content-subtitle">Administrar usuarios y sus límites de slots</p></div></div>

            <c:if test="${not empty successMsg}"><div class="alert alert-success"><c:out value="${successMsg}"/></div></c:if>
            <c:if test="${not empty errorMsg}"><div class="alert alert-error"><c:out value="${errorMsg}"/></div></c:if>

            <div class="stats-grid">
                <div class="stat-card"><div class="stat-icon" style="background: linear-gradient(135deg, var(--color-2), var(--color-1));">👥</div><div class="stat-info"><h3>Total Usuarios</h3><p>${totalUsers != null ? totalUsers : 0}</p></div></div>
                <div class="stat-card"><div class="stat-icon" style="background: linear-gradient(135deg, #4CAF50, #388E3C);">👤</div><div class="stat-info"><h3>Usuarios Normales</h3><p>${normalUsers != null ? normalUsers : 0}</p></div></div>
                <div class="stat-card"><div class="stat-icon" style="background: linear-gradient(135deg, #2196F3, #1976D2);">🛡️</div><div class="stat-info"><h3>Administradores</h3><p>${adminUsers != null ? adminUsers : 0}</p></div></div>
                <div class="stat-card"><div class="stat-icon" style="background: linear-gradient(135deg, #FF9800, #F57C00);">🤝</div><div class="stat-info"><h3>Partners/Fundaciones</h3><p>${partners != null ? partners : 0}</p></div></div>
            </div>

            <div class="quick-actions-section">
                <h3 class="section-title">🚀 Accesos Rápidos</h3>
                <div class="quick-actions-grid">
                    <a href="${pageContext.request.contextPath}/admin/promotions" class="quick-action-card"><div class="quick-action-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">🎁</div><div class="quick-action-info"><h4>Promociones</h4><p>Gestionar ofertas</p></div></a>
                    <a href="${pageContext.request.contextPath}/admin/payments" class="quick-action-card"><div class="quick-action-icon" style="background: linear-gradient(135deg, #4CAF50, #388E3C);">💳</div><div class="quick-action-info"><h4>Pagos</h4><p>Ver transacciones</p></div></a>
                    <a href="${pageContext.request.contextPath}/admin/foundations" class="quick-action-card"><div class="quick-action-icon" style="background: linear-gradient(135deg, #FF9800, #F57C00);">🏢</div><div class="quick-action-info"><h4>Fundaciones</h4><p>Gestionar partners</p></div></a>
                </div>
            </div>

            <div class="filter-section">
                <form method="get" action="${pageContext.request.contextPath}/admin/users" style="display: flex; gap: 1rem; align-items: center; flex-wrap: wrap;">
                    <div style="flex: 1; min-width: 250px;"><input type="text" name="search" class="form-input" placeholder="Buscar por nombre o email..." value="<c:out value="${currentSearch}"/>"></div>
                    <select name="role" class="filter-select">
                        <option value="all" ${empty currentRole || currentRole == 'all' ? 'selected' : ''}>Todos los roles</option>
                        <option value="user" ${currentRole == 'user' ? 'selected' : ''}>Usuarios</option>
                        <option value="admin" ${currentRole == 'admin' ? 'selected' : ''}>Administradores</option>
                    </select>
                    <button type="submit" class="btn btn-primario">🔍 Buscar</button>
                    <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secundario">↻ Limpiar</a>
                </form>
            </div>

            <div class="table-container">
                <c:choose>
                <c:when test="${not empty users}">
                <table class="data-table users-table">
                    <thead><tr><th>Usuario</th><th>Email</th><th>Rol</th><th>Límite Slots</th><th>Mascotas</th><th>Partner</th><th>Visibilidad</th><th>Acciones</th></tr></thead>
                    <tbody>
                        <c:forEach var="u" items="${users}">
                            <c:set var="petCount" value="${petCounts[u.idUser] != null ? petCounts[u.idUser] : 0}"/>
                            <c:set var="limit" value="${u.petLimit != null ? u.petLimit : 2}"/>
                        <tr>
                            <td><div class="user-cell"><div class="admin-avatar">${fn:toUpperCase(fn:substring(u.nameUser, 0, 1))}</div><strong><c:out value="${u.nameUser}"/></strong></div></td>
                            <td><c:out value="${u.email}"/></td>
                            <td><span class="user-role ${u.rol == 'admin' ? 'admin' : 'user'}">${u.rol == 'admin' ? 'Admin' : 'Usuario'}</span></td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.isPartner}"><span class="status-badge status-active">Ilimitado</span></c:when>
                                    <c:otherwise><span class="slots-display ${petCount >= limit ? 'slots-full' : ''}"><c:out value="${petCount}"/>/<c:out value="${limit}"/></span></c:otherwise>
                                </c:choose>
                            </td>
                            <td><c:out value="${petCount}"/></td>
                            <td><c:choose><c:when test="${u.isPartner}"><span class="status-badge status-active">✓ Partner</span></c:when><c:otherwise><span class="status-badge status-inactive">No</span></c:otherwise></c:choose></td>
                            <td>
                                <c:choose>
                                <c:when test="${u.isPartner}">
                                    <c:set var="visible" value="${u.partnerVisible == null || u.partnerVisible}"/>
                                    <button class="btn btn-sm ${visible ? 'btn-success' : 'btn-error'}"
                                            onclick="openVisibilityModal(${u.idUser}, this.dataset.uname, ${visible})"
                                            data-uname="<c:out value="${u.nameUser}"/>"
                                            title="${visible ? 'Visible en página de Partners' : 'Oculta de página de Partners'}">
                                        ${visible ? 'Visible' : 'Oculta'}
                                    </button>
                                </c:when>
                                <c:otherwise><span style="color: #999;">-</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <button class="btn btn-icon btn-primario"
                                            onclick="openAdjustModal(${u.idUser}, this.dataset.username, ${limit}, ${petCount})"
                                            data-username="<c:out value="${u.nameUser}"/>"
                                            title="Ajustar slots">⚙️</button>
                                    <a href="${pageContext.request.contextPath}/admin/users/${u.idUser}/slot-history" class="btn btn-icon btn-secundario" title="Ver historial">📜</a>
                                </div>
                            </td>
                        </tr>
                        </c:forEach>
                    </tbody>
                </table>
                </c:when>
                <c:otherwise>
                <div class="empty-state"><div class="empty-icon">👥</div><h3>No se encontraron usuarios</h3><p>Intenta ajustar los filtros de búsqueda.</p></div>
                </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>

    <!-- Modal de Ajuste de Slots -->
    <div class="modal-overlay" id="adjustSlotsModal">
        <div class="modal-container">
            <div class="modal-header"><h3 id="modalTitle">Ajustar Slots</h3><button class="modal-close" onclick="closeModal()">&times;</button></div>
            <form method="post" action="${pageContext.request.contextPath}/admin/adjust-slots" onsubmit="return validateForm()">
                <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                <div class="modal-body">
                    <input type="hidden" name="userId" id="modalUserId">
                    <div class="modal-info-card"><div class="info-row"><span class="info-label">Límite actual:</span><span class="info-value" id="modalCurrentLimit">-</span></div><div class="info-row"><span class="info-label">Mascotas registradas:</span><span class="info-value" id="modalPetCount">-</span></div></div>
                    <div class="form-group"><label for="newLimit" class="form-label">Nuevo límite *</label><input type="number" id="newLimit" name="newLimit" class="form-input" required min="0" max="100" onchange="validateLimit()"><div class="form-hint" id="limitHint"></div></div>
                    <div class="form-group"><label for="reason" class="form-label">Razón del ajuste * (mínimo 10 caracteres)</label><textarea id="reason" name="reason" class="form-textarea" rows="4" required minlength="10" maxlength="500" placeholder="Ej: Fundación aprobada, ajuste por error, compra adicional..." oninput="updateCharCount()"></textarea><div class="form-hint" id="charCount">0/500 caracteres</div></div>
                </div>
                <div class="modal-footer"><button type="button" class="btn btn-secundario" onclick="closeModal()">Cancelar</button><button type="submit" class="btn btn-primario" id="submitBtn">Guardar cambios</button></div>
            </form>
        </div>
    </div>

    <!-- Modal de Visibilidad -->
    <div class="modal-overlay" id="visibilityModal">
        <div class="modal-container">
            <div class="modal-header"><h3 id="visModalTitle">Cambiar Visibilidad</h3><button class="modal-close" onclick="closeVisibilityModal()">&times;</button></div>
            <form method="post" action="${pageContext.request.contextPath}/admin/toggle-visibility">
                <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                <input type="hidden" name="userId" id="visUserId">
                <input type="hidden" name="visibilityAction" id="visAction">
                <div class="modal-body">
                    <p id="visMessage"></p>
                    <div id="visReasonGroup" class="form-group" style="margin-top: 1.5rem; display: none;"><label for="visReason" class="form-label">Razón (se enviará a la fundación)</label><textarea id="visReason" name="reason" class="form-textarea" rows="4" placeholder="Ej: Perfil incompleto, pendiente de verificación..."></textarea></div>
                </div>
                <div class="modal-footer"><button type="button" class="btn btn-secundario" onclick="closeVisibilityModal()">Cancelar</button><button type="submit" class="btn" id="visSubmitBtn">Confirmar</button></div>
            </form>
        </div>
    </div>

    <script>
        var currentPetCount = 0;
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
        function closeModal() { document.getElementById('adjustSlotsModal').classList.remove('active'); document.body.style.overflow = ''; }
        function validateLimit() {
            var newLimit = parseInt(document.getElementById('newLimit').value), hint = document.getElementById('limitHint');
            if (isNaN(newLimit)) return;
            if (newLimit < currentPetCount) { hint.textContent = '⚠️ No puedes bajar el límite por debajo de ' + currentPetCount; hint.style.color = '#dc3545'; return false; }
            else if (newLimit > 100) { hint.textContent = '⚠️ El límite máximo es 100 slots'; hint.style.color = '#dc3545'; return false; }
            else { hint.textContent = '✓ Límite válido'; hint.style.color = '#28a745'; return true; }
        }
        function updateCharCount() { var r = document.getElementById('reason').value, c = document.getElementById('charCount'); c.textContent = r.length + '/500 caracteres'; c.style.color = r.length < 10 ? '#dc3545' : '#28a745'; }
        function validateForm() { if (document.getElementById('reason').value.trim().length < 10) { alert('La razón debe tener al menos 10 caracteres'); return false; } if (!validateLimit()) { alert('El nuevo límite no es válido'); return false; } document.getElementById('submitBtn').disabled = true; document.getElementById('submitBtn').textContent = 'Guardando...'; return true; }
        document.addEventListener('keydown', function(e) { if (e.key === 'Escape') closeModal(); });
        document.getElementById('adjustSlotsModal').addEventListener('click', function(e) { if (e.target === this) closeModal(); });
        function openVisibilityModal(userId, userName, isCurrentlyVisible) {
            document.getElementById('visUserId').value = userId;
            document.getElementById('visModalTitle').textContent = 'Visibilidad - ' + userName;
            document.getElementById('visReason').value = '';
            if (isCurrentlyVisible) { document.getElementById('visAction').value = 'hide'; document.getElementById('visMessage').textContent = '¿Deseas ocultar a "' + userName + '" de la página pública de Partners?'; document.getElementById('visReasonGroup').style.display = 'block'; document.getElementById('visSubmitBtn').textContent = 'Ocultar'; document.getElementById('visSubmitBtn').className = 'btn btn-error'; }
            else { document.getElementById('visAction').value = 'show'; document.getElementById('visMessage').textContent = '¿Deseas hacer visible a "' + userName + '" en la página pública de Partners?'; document.getElementById('visReasonGroup').style.display = 'none'; document.getElementById('visSubmitBtn').textContent = 'Hacer Visible'; document.getElementById('visSubmitBtn').className = 'btn btn-success'; }
            document.getElementById('visibilityModal').classList.add('active'); document.body.style.overflow = 'hidden';
        }
        function closeVisibilityModal() { document.getElementById('visibilityModal').classList.remove('active'); document.body.style.overflow = ''; }
        document.getElementById('visibilityModal').addEventListener('click', function(e) { if (e.target === this) closeVisibilityModal(); });
    </script>
</body>
</html>
