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

    // Compute base URL for QR codes (needs full absolute URL)
    String baseUrl = request.getScheme() + "://" + request.getServerName() +
        (request.getServerPort() != 80 && request.getServerPort() != 443 ? ":" + request.getServerPort() : "") +
        request.getContextPath();
    pageContext.setAttribute("baseUrl", baseUrl);
%>
<c:set var="userName" value="${not empty user ? user.nameUser : sessionScope.userName}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Códigos QR - PawPaw</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script src="https://cdn.jsdelivr.net/npm/qrcodejs@1.0.0/qrcode.min.js"></script>
</head>
<body>
    <div class="dashboard">
        <aside class="sidebar">
            <div class="sidebar-header">
                <a href="${pageContext.request.contextPath}/view/index.jsp" class="sidebar-logo">
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="PawPaw Logo">
                    <span class="sidebar-logo-text">PawPaw</span>
                </a>
            </div>
            <div class="sidebar-user">
                <div class="user-info">
                    <div class="user-avatar">${fn:toUpperCase(fn:substring(userName, 0, 1))}</div>
                    <div class="user-details">
                        <h3><c:out value="${userName}"/></h3>
                        <p>Usuario</p>
                    </div>
                </div>
            </div>
            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/user/panel" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"></path></svg>
                    Panel Principal
                </a>
                <a href="${pageContext.request.contextPath}/user/pets" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                    Mis Mascotas
                </a>
                <a href="${pageContext.request.contextPath}/user/qr-codes" class="nav-item active">
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
                <a href="${pageContext.request.contextPath}/logout" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path></svg>
                    Cerrar Sesión
                </a>
            </nav>
        </aside>

        <div class="main-content">
            <div class="topbar">
                <div class="topbar-title"><h1>Mis Códigos QR</h1></div>
                <div class="topbar-actions">
                    <a href="${pageContext.request.contextPath}/user/pets/new" class="btn btn-primario">+ Nueva Mascota</a>
                </div>
            </div>

            <div class="content">
                <c:choose>
                <c:when test="${empty qrDataList}">
                    <div class="empty-state">
                        <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <rect x="3" y="3" width="18" height="18" rx="2"></rect>
                            <rect x="7" y="7" width="3" height="3"></rect>
                            <rect x="14" y="7" width="3" height="3"></rect>
                            <rect x="7" y="14" width="3" height="3"></rect>
                            <rect x="14" y="14" width="3" height="3"></rect>
                        </svg>
                        <h3>¡Aún no tienes mascotas registradas!</h3>
                        <p>Comienza registrando a tu primera mascota para generar su código QR único</p>
                        <a href="${pageContext.request.contextPath}/user/pets/new" class="btn btn-primario btn-grande">Registrar Mi Primera Mascota</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="qr-section">
                        <div class="qr-header">
                            <h2>Todos tus códigos QR (${fn:length(qrDataList)})</h2>
                            <p>Descarga, comparte o visualiza los códigos QR de tus mascotas</p>
                        </div>

                        <div class="qr-grid">
                            <c:forEach var="qrData" items="${qrDataList}">
                                <c:set var="pet" value="${qrData.pet}"/>
                                <c:set var="qr" value="${qrData.qrCode}"/>
                                <c:set var="publicUrl" value="${baseUrl}/pet/${pet.idPet}"/>

                            <div class="qr-card">
                                <div class="qr-pet-info">
                                    <c:choose>
                                        <c:when test="${not empty pet.photo}">
                                        <img src="${fn:escapeXml(pet.photo)}" alt="<c:out value="${pet.namePet}"/>" class="qr-pet-photo">
                                        </c:when>
                                        <c:otherwise>
                                        <div class="qr-pet-photo-placeholder">🐾</div>
                                        </c:otherwise>
                                    </c:choose>
                                    <div>
                                        <h3><c:out value="${pet.namePet}"/></h3>
                                        <p><c:out value="${not empty pet.breed ? pet.breed : 'Sin raza especificada'}"/></p>
                                    </div>
                                </div>

                                <div class="qr-code-container">
                                    <div id="qrcode-${pet.idPet}" class="qr-code"></div>
                                </div>

                                <div class="qr-stats">
                                    <div class="qr-stat">
                                        <span class="qr-stat-label">Escaneos</span>
                                        <span class="qr-stat-value"><c:out value="${qr.scansCount != null ? qr.scansCount : 0}"/></span>
                                    </div>
                                    <div class="qr-stat">
                                        <span class="qr-stat-label">Estado</span>
                                        <span class="qr-stat-value ${qr.active ? 'text-success' : 'text-error'}">
                                            ${qr.active ? '✓ Activo' : '⚠ Inactivo'}
                                        </span>
                                    </div>
                                </div>

                                <div class="qr-url">
                                    <input type="text" value="${fn:escapeXml(publicUrl)}" readonly class="qr-url-input" id="url-${pet.idPet}">
                                    <button onclick="copyURL(${pet.idPet})" class="btn-copy">📋 Copiar</button>
                                </div>

                                <div class="qr-actions">
                                    <a href="${pageContext.request.contextPath}/pet/${pet.idPet}" target="_blank" class="btn btn-secundario">👁️ Vista previa</a>
                                    <button onclick="downloadQR(${pet.idPet}, '${fn:replace(pet.namePet, "'", "\\'")}')" class="btn btn-primario">⬇️ Descargar</button>
                                </div>

                                <script>
                                    (function() {
                                        var qrDiv = document.getElementById('qrcode-${pet.idPet}');
                                        if (qrDiv && typeof QRCode !== 'undefined') {
                                            new QRCode(qrDiv, {
                                                text: '${fn:escapeXml(publicUrl)}',
                                                width: 200, height: 200,
                                                colorDark: '#884A39', colorLight: '#ffffff',
                                                correctLevel: QRCode.CorrectLevel.H
                                            });
                                        }
                                    })();
                                </script>
                            </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <script>
        function copyURL(petId) {
            var input = document.getElementById('url-' + petId);
            input.select();
            input.setSelectionRange(0, 99999);
            navigator.clipboard.writeText(input.value).then(function() { alert('✅ URL copiada al portapapeles'); }).catch(function() { document.execCommand('copy'); alert('✅ URL copiada'); });
        }
        function downloadQR(petId, petName) {
            var qrDiv = document.getElementById('qrcode-' + petId);
            var canvas = qrDiv.querySelector('canvas');
            if (canvas) {
                var tmpCanvas = document.createElement('canvas');
                tmpCanvas.width = canvas.width; tmpCanvas.height = canvas.height;
                var ctx = tmpCanvas.getContext('2d');
                ctx.fillStyle = '#ffffff'; ctx.fillRect(0, 0, tmpCanvas.width, tmpCanvas.height);
                ctx.drawImage(canvas, 0, 0);
                var link = document.createElement('a');
                link.download = 'QR-' + petName.replace(/\s+/g, '-') + '.png';
                link.href = tmpCanvas.toDataURL('image/png');
                link.click();
            } else { alert('⚠️ Error al generar la imagen'); }
        }
    </script>
</body>
</html>
