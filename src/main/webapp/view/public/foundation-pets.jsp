<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%
    // TODO: Move unread count to servlet
    Integer unreadCount = null;
    try {
        model.dao.PetContactMessageDAO msgDAO = new model.dao.PetContactMessageDAO();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            unreadCount = msgDAO.countUnreadByUserId(userId);
        }
    } catch (Exception e) { /* silent */ }
    pageContext.setAttribute("unreadCount", unreadCount);
%>
<c:set var="userName" value="${not empty requestScope.user ? requestScope.user.nameUser : sessionScope.userName}"/>
<c:set var="isPartner" value="${sessionScope.isPartner == true}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mascotas de la Fundación - PawPaw</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="dashboard">
        <aside class="sidebar">
            <div class="sidebar-header">
                <a href="${pageContext.request.contextPath}/" class="sidebar-logo">
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="PawPaw Logo">
                    <span class="sidebar-logo-text">PawPaw</span>
                </a>
            </div>
            <div class="sidebar-user">
                <div class="user-info">
                    <div class="user-avatar">${fn:toUpperCase(fn:substring(userName, 0, 1))}</div>
                    <div class="user-details">
                        <h3><c:out value="${userName}"/></h3>
                        <p>${isPartner ? 'Fundación Partner' : 'Usuario'}</p>
                    </div>
                </div>
            </div>
            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/user/panel" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"></path></svg>
                    Panel Principal
                </a>
                <a href="${pageContext.request.contextPath}/user/pets" class="nav-item active">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                    ${isPartner ? 'Mascotas Fundación' : 'Mis Mascotas'}
                </a>
                <a href="${pageContext.request.contextPath}/user/qr-codes" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 20h4M4 12h4m12 0h.01M5 8h2a1 1 0 001-1V5a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1zm12 0h2a1 1 0 001-1V5a1 1 0 00-1-1h-2a1 1 0 00-1 1v2a1 1 0 001 1zM5 20h2a1 1 0 001-1v-2a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1z"></path></svg>
                    Códigos QR
                </a>
                <a href="${pageContext.request.contextPath}/user/messages" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path></svg>
                    Mis Mensajes
                    <c:if test="${unreadCount != null && unreadCount > 0}">
                    <span class="notification-badge"><c:out value="${unreadCount}"/></span>
                    </c:if>
                </a>
                <c:if test="${isPartner}">
                <a href="${pageContext.request.contextPath}/foundations/${sessionScope.userId}" target="_blank" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-2 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"></path></svg>
                    Mi Página Pública
                </a>
                </c:if>
                <div class="nav-divider"></div>
                <a href="${pageContext.request.contextPath}/user/profile" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path></svg>
                    Mi Perfil
                </a>
                <a href="${pageContext.request.contextPath}/user/send-suggestion" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"></path></svg>
                    Enviar Sugerencia
                </a>
                <a href="${pageContext.request.contextPath}/user/my-suggestions" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path></svg>
                    Mis Sugerencias
                </a>
                <a href="${pageContext.request.contextPath}/donate" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path></svg>
                    Donar
                </a>
                <a href="${pageContext.request.contextPath}/logout" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path></svg>
                    Cerrar Sesión
                </a>
            </nav>
        </aside>

        <div class="main-content">
            <div class="topbar">
                <div class="topbar-title"><h1>${isPartner ? 'Mascotas de la Fundación' : 'Mis Mascotas'}</h1></div>
                <div class="topbar-actions">
                    <a href="${pageContext.request.contextPath}/user/pets/new" class="btn btn-primario">+ Registrar Nueva Mascota</a>
                </div>
            </div>

            <div class="content">
                <!-- Alertas flash -->
                <c:if test="${param.success == 'transferInitiated'}">
                    <div class="alert alert-success">
                        ✅ Transferencia iniciada para <strong><c:out value="${not empty param.petName ? param.petName : 'la mascota'}"/></strong>.
                        Se envió un email al adoptante con el enlace de aceptación.
                    </div>
                </c:if>
                <c:if test="${param.success == 'statusUpdated'}">
                    <div class="alert alert-success">✅ Estado de adopción actualizado correctamente.</div>
                </c:if>
                <c:if test="${not empty param.error}">
                    <div class="alert alert-error">
                        ⚠️ <c:choose>
                            <c:when test="${param.error == 'missingFields'}">Completa todos los campos requeridos.</c:when>
                            <c:when test="${param.error == 'petNotAvailable'}">La mascota no está disponible para transferencia.</c:when>
                            <c:when test="${param.error == 'transferFailed'}">Error al iniciar la transferencia. Intenta de nuevo.</c:when>
                            <c:otherwise>Ocurrió un error. Intenta de nuevo.</c:otherwise>
                        </c:choose>
                    </div>
                </c:if>

                <!-- Banner de fundación -->
                <c:if test="${isPartner}">
                <div class="partner-banner">
                    <div class="partner-banner-icon">🏡</div>
                    <div class="partner-banner-text">
                        <strong>Modo Fundación</strong> — Puedes marcar mascotas como disponibles para adopción
                        e iniciar transferencias directamente al adoptante. El QR se mantiene intacto.
                    </div>
                    <a href="${pageContext.request.contextPath}/foundations/${sessionScope.userId}" target="_blank" class="btn btn-secundario btn-sm">Ver página pública</a>
                </div>
                </c:if>

                <!-- Lista de mascotas -->
                <c:choose>
                <c:when test="${empty pets}">
                    <div class="empty-state">
                        <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                        <h3>¡Aún no hay mascotas registradas!</h3>
                        <p>${isPartner ? 'Registra los animales que cuidas para publicarlos en adopción.' : 'Comienza registrando tu primera mascota.'}</p>
                        <a href="${pageContext.request.contextPath}/user/pets/new" class="btn btn-primario btn-grande">
                            ${isPartner ? 'Registrar Primera Mascota' : 'Registrar Mi Primera Mascota'}
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="pets-section">
                        <div class="pets-header">
                            <h2>Todas las mascotas (${fn:length(pets)})</h2>
                            <p>${isPartner ? 'Gestiona el estado de adopción y transfiere mascotas a sus nuevos dueños.' : 'Gestiona la información y códigos QR de tus mascotas.'}</p>
                        </div>

                        <div class="pets-grid">
                            <c:forEach var="pet" items="${pets}">
                                <%-- Extract adoption status via scriptlet since getAdoptionStatus() may throw --%>
                                <%
                                    model.entity.Pet currentPet = (model.entity.Pet) pageContext.getAttribute("pet");
                                    String adoptionStatus = "owned";
                                    try { if (currentPet.getAdoptionStatus() != null) adoptionStatus = currentPet.getAdoptionStatus(); } catch (Exception ignored) {}
                                    pageContext.setAttribute("adoptionStatus", adoptionStatus);
                                %>
                            <div class="pet-card">
                                <div class="pet-photo">
                                    <c:choose>
                                        <c:when test="${not empty pet.photo}">
                                        <img src="${fn:escapeXml(pet.photo)}" alt="<c:out value="${pet.namePet}"/>">
                                        </c:when>
                                        <c:otherwise><span class="pet-photo-placeholder">🐾</span></c:otherwise>
                                    </c:choose>
                                    <span class="pet-status-badge pet-status-${pet.statusPet}">
                                        <c:choose>
                                            <c:when test="${pet.statusPet == 'active'}">Activa</c:when>
                                            <c:when test="${pet.statusPet == 'lost'}">Perdida</c:when>
                                            <c:when test="${pet.statusPet == 'found'}">Encontrada</c:when>
                                            <c:otherwise>Inactiva</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>

                                <div class="pet-info">
                                    <h4><c:out value="${pet.namePet}"/></h4>
                                    <div class="pet-details">
                                        <div class="detail-item"><span class="detail-label">🐕 Raza:</span><span class="detail-value"><c:out value="${not empty pet.breed ? pet.breed : 'No especificada'}"/></span></div>
                                        <div class="detail-item"><span class="detail-label">🎂 Edad:</span><span class="detail-value"><c:choose><c:when test="${pet.agePet != null}"><c:out value="${pet.agePet}"/> años</c:when><c:otherwise>No especificada</c:otherwise></c:choose></span></div>
                                        <div class="detail-item"><span class="detail-label">⚧ Sexo:</span><span class="detail-value"><c:out value="${not empty pet.sexPet ? pet.sexPet : 'No especificado'}"/></span></div>
                                        <c:if test="${not empty pet.contactPhone}">
                                        <div class="detail-item"><span class="detail-label">📞 Contacto:</span><span class="detail-value"><c:out value="${pet.contactPhone}"/></span></div>
                                        </c:if>
                                    </div>

                                    <div class="adoption-status-row">
                                        <span class="adoption-status-badge adoption-${fn:replace(adoptionStatus, '_', '-')}">
                                            <c:choose>
                                                <c:when test="${adoptionStatus == 'available'}">🟢 En adopción</c:when>
                                                <c:when test="${adoptionStatus == 'adopted_pending'}">🟡 Transferencia pendiente</c:when>
                                                <c:when test="${adoptionStatus == 'adopted_transferred'}">🏠 Adoptado</c:when>
                                                <c:otherwise>🔵 Propio</c:otherwise>
                                            </c:choose>
                                        </span>
                                    </div>
                                </div>

                                <div class="pet-actions">
                                    <a href="${pageContext.request.contextPath}/user/pets/edit?id=${pet.idPet}" class="btn btn-secundario">✏️ Editar</a>
                                    <a href="${pageContext.request.contextPath}/pet/${pet.idPet}" target="_blank" class="btn btn-primario">📱 Ver Perfil</a>
                                </div>

                                <c:if test="${isPartner && adoptionStatus != 'adopted_transferred'}">
                                <div class="partner-actions">
                                    <form method="POST" action="${pageContext.request.contextPath}/pet/adoption-status" class="partner-status-form">
                                        <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                                        <input type="hidden" name="petId" value="${pet.idPet}">
                                        <select name="adoptionStatus" class="form-select form-select-sm">
                                            <option value="owned" ${adoptionStatus == 'owned' ? 'selected' : ''}>Propio</option>
                                            <option value="available" ${adoptionStatus == 'available' ? 'selected' : ''}>En adopción</option>
                                        </select>
                                        <button type="submit" class="btn btn-secundario btn-sm">Guardar</button>
                                    </form>

                                    <c:if test="${adoptionStatus == 'available'}">
                                    <button class="btn btn-success btn-sm btn-block"
                                            onclick="openTransferModal(${pet.idPet}, '${fn:replace(pet.namePet, "'", "\\'")}')"
                                    >🤝 Transferir a Adoptante</button>
                                    </c:if>

                                    <c:if test="${adoptionStatus == 'adopted_pending'}">
                                    <div class="alert alert-info adoption-pending-notice">⏳ Esperando confirmación del adoptante por email.</div>
                                    </c:if>
                                </div>
                                </c:if>

                                <c:if test="${adoptionStatus != 'adopted_pending' && adoptionStatus != 'adopted_transferred'}">
                                <div class="pet-delete">
                                    <form method="POST" action="${pageContext.request.contextPath}/user/pets/delete" style="display:inline;">
                                        <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                                        <input type="hidden" name="id" value="${pet.idPet}">
                                        <button type="submit" class="btn-delete" onclick="return confirm('¿Eliminar a ${fn:replace(pet.namePet, "'", "\\'")}? Esta acción no se puede deshacer.');">🗑️ Eliminar</button>
                                    </form>
                                </div>
                                </c:if>
                            </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Modal Transferencia -->
    <c:if test="${isPartner}">
    <div id="transferModal" class="transfer-modal" onclick="if(event.target===this) closeTransferModal()">
        <div class="transfer-modal-content">
            <div class="transfer-modal-header">
                <h2>🤝 Transferir mascota a adoptante</h2>
                <button class="transfer-modal-close" onclick="closeTransferModal()">✕</button>
            </div>
            <p class="transfer-modal-subtitle">Se enviará un email al adoptante con un enlace seguro. Una vez que lo acepte, el perfil QR se transfiere automáticamente a su cuenta.</p>
            <form method="POST" action="${pageContext.request.contextPath}/pet/transfer/initiate" class="transfer-modal-form">
                <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                <input type="hidden" id="modalPetId" name="petId" value="">
                <div class="transfer-modal-pet-name">Mascota: <strong id="modalPetName"></strong></div>
                <div class="form-group">
                    <label class="form-label" for="adopterName">Nombre del Adoptante *</label>
                    <input type="text" id="adopterName" name="adopterName" class="form-input" placeholder="Ej: María García" required>
                </div>
                <div class="form-group">
                    <label class="form-label" for="adopterEmail">Email del Adoptante *</label>
                    <input type="email" id="adopterEmail" name="adopterEmail" class="form-input" placeholder="adoptante@email.com" required>
                    <small class="form-hint">Se le enviará el enlace de aceptación a este correo.</small>
                </div>
                <div class="form-group">
                    <label class="form-label" for="adopterPhone">Teléfono (opcional)</label>
                    <input type="tel" id="adopterPhone" name="adopterPhone" class="form-input" placeholder="+593 99 123 4567">
                </div>
                <div class="form-group">
                    <label class="form-label" for="transferMessage">Mensaje personal (opcional)</label>
                    <textarea id="transferMessage" name="message" class="form-input form-textarea" placeholder="Ej: Ha sido una mascota muy cariñosa. ¡Cuídala mucho!"></textarea>
                </div>
                <div class="transfer-modal-actions">
                    <button type="submit" class="btn btn-primario btn-block">📧 Enviar invitación de adopción</button>
                    <button type="button" class="btn btn-secundario btn-block" onclick="closeTransferModal()">Cancelar</button>
                </div>
            </form>
        </div>
    </div>
    </c:if>

    <script>
        function openTransferModal(petId, petName) {
            document.getElementById('modalPetId').value = petId;
            document.getElementById('modalPetName').textContent = petName;
            document.getElementById('transferModal').classList.add('transfer-modal-open');
            document.body.style.overflow = 'hidden';
        }
        function closeTransferModal() {
            document.getElementById('transferModal').classList.remove('transfer-modal-open');
            document.body.style.overflow = '';
        }
        setTimeout(function() {
            var alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(a) { a.style.opacity = '0'; a.style.transition = 'opacity 0.5s'; });
        }, 5000);
    </script>

    <jsp:include page="/view/components/footer.jsp" />

</body>
</html>
