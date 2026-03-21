<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="userName" value="${not empty requestScope.user ? requestScope.user.nameUser : sessionScope.userName}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Mensajes - PawPaw</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
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
                <a href="${pageContext.request.contextPath}/user/qr-codes" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 20h4M4 12h4m12 0h.01M5 8h2a1 1 0 001-1V5a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1zm12 0h2a1 1 0 001-1V5a1 1 0 00-1-1h-2a1 1 0 00-1 1v2a1 1 0 001 1zM5 20h2a1 1 0 001-1v-2a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1z"></path></svg>
                    Códigos QR
                </a>
                <a href="${pageContext.request.contextPath}/user/messages" class="nav-item active">
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

        <main class="main-content">
            <div class="content-header">
                <div>
                    <h1 class="content-title">📬 Mis Mensajes</h1>
                    <p class="content-subtitle">Mensajes recibidos sobre tus mascotas</p>
                </div>
                <c:if test="${unreadCount != null && unreadCount > 0}">
                <form method="post" style="display:inline;">
                    <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                    <input type="hidden" name="action" value="mark-all-read">
                    <button type="submit" class="btn btn-secundario">Marcar todos como leídos</button>
                </form>
                </c:if>
            </div>

            <c:if test="${not empty successMessage}">
                <div class="alert alert-success"><c:out value="${successMessage}"/></div>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-error"><c:out value="${errorMessage}"/></div>
            </c:if>

            <div class="messages-stats">
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, var(--color-2), var(--color-1));">📊</div>
                    <div class="stat-info">
                        <h3>Total</h3>
                        <p><c:out value="${not empty messages ? fn:length(messages) : 0}"/></p>
                    </div>
                </div>
                <div class="stat-card">
                    <div class="stat-icon" style="background: linear-gradient(135deg, #FFA726, #FB8C00);">📬</div>
                    <div class="stat-info">
                        <h3>Sin leer</h3>
                        <p><c:out value="${unreadCount != null ? unreadCount : 0}"/></p>
                    </div>
                </div>
            </div>

            <div class="filter-section">
                <label>Filtrar:</label>
                <div class="filter-buttons">
                    <a href="${pageContext.request.contextPath}/user/messages"
                       class="filter-btn ${empty currentFilter ? 'active' : ''}">Todos</a>
                    <a href="${pageContext.request.contextPath}/user/messages?filter=unread"
                       class="filter-btn ${currentFilter == 'unread' ? 'active' : ''}">
                        Sin leer
                        <c:if test="${unreadCount != null && unreadCount > 0}">
                        <span class="badge-small"><c:out value="${unreadCount}"/></span>
                        </c:if>
                    </a>
                </div>
            </div>

            <!-- Mensajes del Sistema -->
            <c:if test="${not empty systemMessages}">
            <div style="margin-bottom: 2rem;">
                <h3 style="margin-bottom: 1rem; color: #333;">Notificaciones del Sistema</h3>
                <div class="messages-container">
                    <c:forEach var="sysMsg" items="${systemMessages}">
                    <div class="message-card ${sysMsg.unread ? 'unread' : ''}" style="border-left: 4px solid #FF9800;">
                        <div class="message-header">
                            <div class="message-pet-info">
                                <div class="message-pet-photo-placeholder" style="background: linear-gradient(135deg, #FF9800, #F57C00);">🔔</div>
                                <div><h3><c:out value="${sysMsg.subject}"/></h3></div>
                            </div>
                            <c:if test="${sysMsg.unread}">
                            <span class="unread-badge">Sin leer</span>
                            </c:if>
                        </div>
                        <div class="message-body">
                            <div class="message-time"><c:out value="${sysMsg.timeAgo}"/></div>
                            <div class="message-text" style="white-space: pre-wrap;"><c:out value="${sysMsg.message}"/></div>
                        </div>
                        <div class="message-actions">
                            <c:if test="${sysMsg.unread}">
                            <form method="post" style="display:inline;">
                                <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                                <input type="hidden" name="action" value="mark-sys-read">
                                <input type="hidden" name="messageId" value="${sysMsg.idMessage}">
                                <button type="submit" class="btn btn-secundario btn-small">✓ Marcar leído</button>
                            </form>
                            </c:if>
                            <form method="post" style="display:inline;" onsubmit="return confirm('¿Eliminar esta notificación?');">
                                <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                                <input type="hidden" name="action" value="delete-sys">
                                <input type="hidden" name="messageId" value="${sysMsg.idMessage}">
                                <button type="submit" class="btn btn-outline btn-small">🗑️ Eliminar</button>
                            </form>
                        </div>
                    </div>
                    </c:forEach>
                </div>
            </div>
            </c:if>

            <!-- Lista de Mensajes -->
            <div class="messages-container">
                <c:choose>
                <c:when test="${not empty messages}">
                    <c:forEach var="msg" items="${messages}">
                <div class="message-card ${msg.unread ? 'unread' : ''}">
                    <div class="message-header">
                        <div class="message-pet-info">
                            <c:choose>
                                <c:when test="${not empty msg.petPhoto}">
                            <img src="${fn:escapeXml(msg.petPhoto)}" alt="<c:out value="${msg.petName}"/>" class="message-pet-photo">
                                </c:when>
                                <c:otherwise>
                            <div class="message-pet-photo-placeholder">🐾</div>
                                </c:otherwise>
                            </c:choose>
                            <div>
                                <h3>Sobre <c:out value="${msg.petName}"/></h3>
                                <c:if test="${not empty msg.petBreed}">
                                <span class="message-pet-breed"><c:out value="${msg.petBreed}"/></span>
                                </c:if>
                            </div>
                        </div>
                        <c:if test="${msg.unread}">
                        <span class="unread-badge">Sin leer</span>
                        </c:if>
                    </div>
                    <div class="message-body">
                        <div class="message-sender"><strong>De:</strong> <c:out value="${msg.senderName}"/></div>
                        <div class="message-phone"><strong>Teléfono:</strong> <a href="tel:${fn:escapeXml(msg.senderPhone)}"><c:out value="${msg.senderPhone}"/></a></div>
                        <div class="message-time"><c:out value="${msg.timeAgo}"/></div>
                        <div class="message-text"><c:out value="${msg.message}"/></div>
                    </div>
                    <div class="message-actions">
                        <a href="tel:${fn:escapeXml(msg.senderPhone)}" class="btn btn-primario btn-small">📞 Llamar</a>
                        <c:if test="${msg.unread}">
                        <form method="post" style="display:inline;">
                            <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                            <input type="hidden" name="action" value="mark-read">
                            <input type="hidden" name="messageId" value="${msg.idMessage}">
                            <button type="submit" class="btn btn-secundario btn-small">✓ Marcar leído</button>
                        </form>
                        </c:if>
                        <form method="post" style="display:inline;" onsubmit="return confirm('¿Eliminar este mensaje?');">
                            <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="messageId" value="${msg.idMessage}">
                            <button type="submit" class="btn btn-outline btn-small">🗑️ Eliminar</button>
                        </form>
                    </div>
                </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                <div class="empty-state">
                    <div class="empty-icon">📭</div>
                    <h3>No hay mensajes</h3>
                    <p>Cuando alguien encuentre a tus mascotas y te contacte, verás los mensajes aquí.</p>
                </div>
                </c:otherwise>
                </c:choose>
            </div>
        </main>
    </div>
</body>
</html>
