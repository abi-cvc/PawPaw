<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Donar - PawPaw</title>
    <meta name="description" content="Apoya a PawPaw con una donacion. Tu contribucion nos ayuda a proteger mas mascotas en Ecuador con tecnologia QR.">
    <meta name="robots" content="index, follow">
    <link rel="canonical" href="${pageContext.request.contextPath}/donate">
    <meta property="og:type" content="website">
    <meta property="og:title" content="Donar - PawPaw">
    <meta property="og:description" content="Apoya a PawPaw con una donacion para proteger mas mascotas en Ecuador.">
    <meta property="og:image" content="${pageContext.request.contextPath}/images/logo.png">
    <meta property="og:locale" content="es_EC">
    <meta property="og:site_name" content="PawPaw">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css?v=3">
    <script src="https://www.paypal.com/sdk/js?client-id=<c:out value='${paypalClientId}'/>&currency=USD"></script>
</head>
<body>

    <jsp:include page="/view/components/navbar.jsp" />

    <main>
        <section class="static-hero">
            <div class="static-hero-inner">
                <span class="static-hero-tag">Haz la diferencia</span>
                <h1 class="static-hero-title">Apoya a PawPaw</h1>
                <p class="static-hero-subtitle">
                    Tu donacion nos ayuda a seguir protegiendo mascotas y conectandolas con sus familias.
                </p>
            </div>
        </section>

        <section class="static-content">
            <div class="static-container">

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
                <div class="static-section" style="margin-top: 3rem;">
                    <h2>A donde va tu donacion?</h2>
                    <div class="static-cards-grid">
                        <div class="static-card">
                            <div class="static-card-icon">🏥</div>
                            <h3>Atencion veterinaria</h3>
                            <p>Ayudamos a cubrir gastos medicos de mascotas rescatadas por fundaciones aliadas.</p>
                        </div>
                        <div class="static-card">
                            <div class="static-card-icon">💻</div>
                            <h3>Desarrollo de la plataforma</h3>
                            <p>Mantenemos y mejoramos PawPaw para que siga siendo gratuito y accesible.</p>
                        </div>
                        <div class="static-card">
                            <div class="static-card-icon">📢</div>
                            <h3>Difusion y alcance</h3>
                            <p>Llegamos a mas personas y mascotas que necesitan nuestra ayuda.</p>
                        </div>
                        <div class="static-card">
                            <div class="static-card-icon">🐾</div>
                            <h3>Placas QR gratuitas</h3>
                            <p>Donamos placas QR a fundaciones para que mas mascotas puedan ser identificadas.</p>
                        </div>
                    </div>
                </div>

            </div>
        </section>
    </main>

    <jsp:include page="/view/components/footer.jsp" />

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
