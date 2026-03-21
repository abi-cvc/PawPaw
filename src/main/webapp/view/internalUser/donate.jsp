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
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Donar - PawPaw</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script src="https://www.paypal.com/sdk/js?client-id=<c:out value='${paypalClientId}'/>&currency=USD"></script>
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
                    <div class="user-avatar">
                        ${fn:toUpperCase(fn:substring(userName, 0, 1))}
                    </div>
                    <div class="user-details">
                        <h3><c:out value="${userName}"/></h3>
                        <p>Usuario</p>
                    </div>
                </div>
            </div>

            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/user/panel" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"></path>
                    </svg>
                    Panel Principal
                </a>

                <a href="${pageContext.request.contextPath}/user/pets" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                    Mis Mascotas
                </a>

                <a href="${pageContext.request.contextPath}/user/qr-codes" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 20h4M4 12h4m12 0h.01M5 8h2a1 1 0 001-1V5a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1zm12 0h2a1 1 0 001-1V5a1 1 0 00-1-1h-2a1 1 0 00-1 1v2a1 1 0 001 1zM5 20h2a1 1 0 001-1v-2a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1z"></path>
                    </svg>
                    Códigos QR
                </a>

                <a href="${pageContext.request.contextPath}/user/messages" class="nav-item">
				    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
				        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
				              d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path>
				    </svg>
				    Mis Mensajes
				    <c:if test="${unreadCount != null && unreadCount > 0}">
				    <span class="notification-badge"><c:out value="${unreadCount}"/></span>
				    </c:if>
				</a>

                <div class="nav-divider"></div>

                <a href="${pageContext.request.contextPath}/user/profile" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                    </svg>
                    Mi Perfil
                </a>

                <a href="${pageContext.request.contextPath}/user/send-suggestion" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"></path>
                    </svg>
                    Enviar Sugerencia
                </a>

                <a href="${pageContext.request.contextPath}/user/my-suggestions" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path>
                    </svg>
                    Mis Sugerencias
                </a>

                <a href="${pageContext.request.contextPath}/donate" class="nav-item active">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path>
                    </svg>
                    Donar
                </a>

                <a href="${pageContext.request.contextPath}/logout" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1"></path>
                    </svg>
                    Cerrar Sesión
                </a>
            </nav>
        </aside>

        <!-- Main Content -->
        <div class="main-content">
            <!-- Top Bar -->
            <div class="topbar">
                <div class="topbar-title">
                    <h1>Donar</h1>
                </div>
            </div>

            <!-- Content -->
            <div class="content">
                <div class="welcome-section">
                    <h2>Apoya a PawPaw</h2>
                    <p>Tu donacion nos ayuda a seguir protegiendo mascotas y conectandolas con sus familias.</p>
                </div>

                <div class="donate-form-container">
                    <!-- Montos sugeridos -->
                    <div class="donate-section">
                        <h2>Elige un monto</h2>
                        <div class="donate-amounts">
                            <button type="button" class="donate-amount-btn" data-amount="1">$1</button>
                            <button type="button" class="donate-amount-btn" data-amount="5">$5</button>
                            <button type="button" class="donate-amount-btn active" data-amount="10">$10</button>
                            <button type="button" class="donate-amount-btn" data-amount="25">$25</button>
                        </div>
                        <div class="donate-custom-amount">
                            <label for="customAmount">O ingresa un monto personalizado (USD)</label>
                            <div class="donate-input-wrapper">
                                <span class="donate-currency">$</span>
                                <input type="number" id="customAmount" min="1" max="500" step="0.01" placeholder="10.00">
                            </div>
                            <small class="donate-hint">Minimo $1.00 - Maximo $500.00</small>
                        </div>
                    </div>

                    <!-- Datos del donante -->
                    <div class="donate-section">
                        <h2>Tus datos</h2>
                        <div class="donate-anonymous">
                            <label class="donate-checkbox-label">
                                <input type="checkbox" id="anonymousCheck">
                                <span>Prefiero mantenerme anonimo</span>
                            </label>
                        </div>
                        <div class="donate-fields" id="donorFields">
                            <div class="donate-field">
                                <label for="donorName">Nombre</label>
                                <input type="text" id="donorName" placeholder="Tu nombre"
                                    value="<c:out value='${sessionUserName}' default=''/>">
                            </div>
                            <div class="donate-field">
                                <label for="donorEmail">Email</label>
                                <input type="email" id="donorEmail" placeholder="tu@email.com"
                                    value="<c:out value='${sessionUserEmail}' default=''/>">
                            </div>
                        </div>
                        <div class="donate-field donate-field-full">
                            <label for="donorMessage">Mensaje (opcional)</label>
                            <textarea id="donorMessage" rows="3" placeholder="Escribe un mensaje de apoyo..."></textarea>
                        </div>
                    </div>

                    <!-- Boton PayPal -->
                    <div class="donate-section">
                        <div id="donation-error" class="alert alert-error" style="display:none;"></div>
                        <div id="donation-success" class="alert alert-success" style="display:none;"></div>
                        <div id="paypal-button-container"></div>
                    </div>
                </div>

                <!-- Info adicional -->
                <div class="donate-info-section" style="margin-top: 2rem;">
                    <h3>A donde va tu donacion?</h3>
                    <div class="stats-grid">
                        <div class="stat-card">
                            <div class="stat-icon pets">🏥</div>
                            <div class="stat-info">
                                <h3>Atencion veterinaria</h3>
                                <p>Gastos medicos de mascotas rescatadas</p>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon qr">💻</div>
                            <div class="stat-info">
                                <h3>Plataforma</h3>
                                <p>Mantenimiento y mejoras de PawPaw</p>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon active">🐾</div>
                            <div class="stat-info">
                                <h3>Placas QR gratuitas</h3>
                                <p>Donamos placas a fundaciones aliadas</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
    (function() {
        var selectedAmount = 10;
        var amountBtns = document.querySelectorAll('.donate-amount-btn');
        var customInput = document.getElementById('customAmount');
        var anonymousCheck = document.getElementById('anonymousCheck');
        var donorFields = document.getElementById('donorFields');
        var donorName = document.getElementById('donorName');
        var donorEmail = document.getElementById('donorEmail');
        var errorDiv = document.getElementById('donation-error');
        var successDiv = document.getElementById('donation-success');

        // Seleccion de monto
        amountBtns.forEach(function(btn) {
            btn.addEventListener('click', function() {
                amountBtns.forEach(function(b) { b.classList.remove('active'); });
                btn.classList.add('active');
                selectedAmount = parseFloat(btn.dataset.amount);
                customInput.value = '';
            });
        });

        customInput.addEventListener('input', function() {
            if (customInput.value) {
                amountBtns.forEach(function(b) { b.classList.remove('active'); });
                selectedAmount = parseFloat(customInput.value);
            }
        });

        // Anonimo toggle
        anonymousCheck.addEventListener('change', function() {
            if (anonymousCheck.checked) {
                donorFields.style.opacity = '0.5';
                donorFields.style.pointerEvents = 'none';
                donorName.disabled = true;
                donorEmail.disabled = true;
            } else {
                donorFields.style.opacity = '1';
                donorFields.style.pointerEvents = 'auto';
                donorName.disabled = false;
                donorEmail.disabled = false;
            }
        });

        function getAmount() {
            if (customInput.value) {
                return parseFloat(customInput.value);
            }
            return selectedAmount;
        }

        function showError(msg) {
            errorDiv.textContent = msg;
            errorDiv.style.display = 'block';
            successDiv.style.display = 'none';
        }

        function showSuccess(msg) {
            successDiv.textContent = msg;
            successDiv.style.display = 'block';
            errorDiv.style.display = 'none';
        }

        // PayPal Smart Buttons
        paypal.Buttons({
            createOrder: function(data, actions) {
                var amount = getAmount();
                if (!amount || amount < 1 || amount > 500) {
                    showError('El monto debe estar entre $1.00 y $500.00');
                    return actions.reject();
                }

                errorDiv.style.display = 'none';

                return fetch('${pageContext.request.contextPath}/api/paypal/create-donation', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ amount: amount })
                }).then(function(res) {
                    return res.json();
                }).then(function(data) {
                    if (data.status === 'error') {
                        showError(data.message);
                        return actions.reject();
                    }
                    return data.id;
                });
            },
            onApprove: function(data, actions) {
                var isAnonymous = anonymousCheck.checked;

                var payload = {
                    orderID: data.orderID,
                    isAnonymous: isAnonymous
                };

                if (!isAnonymous) {
                    payload.donorName = donorName.value || '';
                    payload.donorEmail = donorEmail.value || '';
                }

                var messageField = document.getElementById('donorMessage');
                if (messageField.value) {
                    payload.message = messageField.value;
                }

                return fetch('${pageContext.request.contextPath}/api/paypal/capture-donation', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                }).then(function(res) {
                    return res.json();
                }).then(function(result) {
                    if (result.status === 'success') {
                        showSuccess('Gracias por tu donacion! Tu apoyo hace la diferencia.');
                        document.getElementById('paypal-button-container').style.display = 'none';
                    } else {
                        showError(result.message || 'Error al procesar la donacion');
                    }
                });
            },
            onError: function(err) {
                showError('Error al procesar el pago. Intenta de nuevo.');
                console.error('PayPal error:', err);
            }
        }).render('#paypal-button-container');
    })();
    </script>

</body>
</html>
