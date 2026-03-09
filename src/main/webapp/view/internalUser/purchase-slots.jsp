<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.User" %>
<%@ page import="model.entity.Promotion" %>
<%@ page import="java.math.BigDecimal" %>
<%
    // Verificar sesión
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    User user = (User) request.getAttribute("user");
    Promotion activePromo = (Promotion) request.getAttribute("activePromotion");
    
    String userName = user != null ? user.getNameUser() : (String) session.getAttribute("userName");
    
    // Mensajes
    String successMessage = (String) session.getAttribute("successMessage");
    String errorMessage = (String) session.getAttribute("errorMessage");
    session.removeAttribute("successMessage");
    session.removeAttribute("errorMessage");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Comprar Slots - PawPaw</title>
    
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
    
    <!-- PayPal SDK -->
    <script src="https://www.paypal.com/sdk/js?client-id=AWl8G_qfgo0lmbDgplDHcFzdSu8I5HrWtnDoRkgxmZx6Bx9-jOMQHXrE0xqY1RPuSlTpfv9X5aeYThKf&currency=USD"></script>
</head>
<body>
    <div class="dashboard">
        <!-- Sidebar (mismo que antes) -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <a href="<%= request.getContextPath() %>/view/index.jsp" class="sidebar-logo">
                    <img src="<%= request.getContextPath() %>/images/logo.png" alt="PawPaw Logo">
                    <span class="sidebar-logo-text">PawPaw</span>
                </a>
            </div>
            
            <div class="sidebar-user">
                <div class="user-info">
                    <div class="user-avatar">
                        <%= userName != null ? userName.substring(0, 1).toUpperCase() : "U" %>
                    </div>
                    <div class="user-details">
                        <h3><%= userName %></h3>
                        <p>Usuario</p>
                    </div>
                </div>
            </div>
            
            <nav class="sidebar-nav">
                <a href="<%= request.getContextPath() %>/user/panel" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"></path>
                    </svg>
                    Panel Principal
                </a>
                
                <a href="<%= request.getContextPath() %>/user/pets" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                    Mis Mascotas
                </a>
                
                <a href="<%= request.getContextPath() %>/user/qr-codes" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 20h4M4 12h4m12 0h.01M5 8h2a1 1 0 001-1V5a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1zm12 0h2a1 1 0 001-1V5a1 1 0 00-1-1h-2a1 1 0 00-1 1v2a1 1 0 001 1zM5 20h2a1 1 0 001-1v-2a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1z"></path>
                    </svg>
                    Códigos QR
                </a>
                
                <a href="<%= request.getContextPath() %>/user/purchase-slots" class="nav-item active">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
                    </svg>
                    Comprar Slots
                </a>
                
                <div class="nav-divider"></div>
                
                <a href="<%= request.getContextPath() %>/logout" class="nav-item">
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
                <div>
                    <h1 class="content-title">🐾 Expande tu límite de mascotas</h1>
                    <p class="content-subtitle">Elige el plan que mejor se adapte a tus necesidades</p>
                </div>
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
            
            <!-- Pricing Cards -->
            <div class="pricing-grid">
                
                <!-- 1 SLOT -->
                <div class="pricing-card">
                    <h3 class="pricing-title">Slot Individual</h3>
                    <div class="pricing-price">
                        $5<span class="price-currency">USD</span>
                    </div>
                    <p class="pricing-period">Pago único</p>
                    
                    <ul class="pricing-features">
                        <li>✅ 1 espacio adicional</li>
                        <li>✅ Permanente</li>
                        <li>✅ No caduca</li>
                    </ul>
                    
                    <div id="paypal-button-1" class="paypal-button-container"></div>
                </div>
                
                <!-- 5 SLOTS -->
                <div class="pricing-card pricing-featured">
                    <div class="featured-badge">🎉 POPULAR</div>
                    
                    <h3 class="pricing-title">Paquete 5 Slots</h3>
                    <div class="pricing-price">
                        $20<span class="price-currency">USD</span>
                    </div>
                    <p class="pricing-period">Pago único</p>
                    
                    <div class="pricing-savings">
                        ¡Ahorra $5!
                    </div>
                    
                    <ul class="pricing-features">
                        <li>✅ 5 espacios adicionales</li>
                        <li>✅ Permanentes</li>
                        <li>✅ No caducan</li>
                        <li>✅ Mejor relación precio/valor</li>
                    </ul>
                    
                    <div id="paypal-button-5" class="paypal-button-container"></div>
                </div>
                
                <!-- 10 SLOTS -->
                <div class="pricing-card">
                    <h3 class="pricing-title">Paquete 10 Slots</h3>
                    <div class="pricing-price">
                        $35<span class="price-currency">USD</span>
                    </div>
                    <p class="pricing-period">Pago único</p>
                    
                    <div class="pricing-savings">
                        ¡Ahorra $15!
                    </div>
                    
                    <ul class="pricing-features">
                        <li>✅ 10 espacios adicionales</li>
                        <li>✅ Permanentes</li>
                        <li>✅ No caducan</li>
                        <li>✅ Máximo ahorro</li>
                    </ul>
                    
                    <div id="paypal-button-10" class="paypal-button-container"></div>
                </div>
                
            </div>
            
            <!-- Información adicional -->
            <div class="payment-info">
                <h3>ℹ️ Información importante</h3>
                <ul>
                    <li>✅ Los slots son <strong>permanentes</strong> y nunca caducan</li>
                    <li>✅ Pago seguro procesado por PayPal</li>
                    <li>✅ Activación <strong>inmediata</strong> tras confirmar el pago</li>
                    <li>✅ Recibirás un email de confirmación</li>
                    <li>✅ Puedes pagar con tarjeta o saldo PayPal</li>
                </ul>
            </div>
        </main>
    </div>
    
    <script>
        const contextPath = '<%= request.getContextPath() %>';
        
        // Configuración de precios
        const packages = {
            1: { slots: 1, amount: 5.00 },
            5: { slots: 5, amount: 20.00 },
            10: { slots: 10, amount: 35.00 }
        };
        
        // Crear botón PayPal para cada paquete
        Object.keys(packages).forEach(key => {
            const pkg = packages[key];
            
            paypal.Buttons({
                style: {
                    layout: 'vertical',
                    color: 'gold',
                    shape: 'rect',
                    label: 'pay'
                },
                
                createOrder: function(data, actions) {
                    console.log('Creando orden para ' + pkg.slots + ' slots...');
                    
                    return fetch(contextPath + '/api/paypal/create-order', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            slots: pkg.slots,
                            amount: pkg.amount
                        })
                    })
                    .then(response => response.json())
                    .then(order => {
                        console.log('Orden creada:', order.id);
                        return order.id;
                    });
                },
                
                onApprove: function(data, actions) {
                    console.log('Pago aprobado. Capturando...');
                    
                    return fetch(contextPath + '/api/paypal/capture-order', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            orderID: data.orderID,
                            slots: pkg.slots
                        })
                    })
                    .then(response => response.json())
                    .then(result => {
                        console.log('Pago capturado:', result);
                        
                        if (result.status === 'success') {
                            alert('¡Pago exitoso! ' + pkg.slots + ' slot(s) agregado(s) a tu cuenta. ✅');
                            window.location.href = contextPath + '/user/panel';
                        } else {
                            alert('Error al procesar el pago: ' + result.message);
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('Error al procesar el pago. Por favor intenta de nuevo.');
                    });
                },
                
                onCancel: function(data) {
                    console.log('Pago cancelado por el usuario');
                    alert('Pago cancelado. Puedes intentar de nuevo cuando quieras.');
                },
                
                onError: function(err) {
                    console.error('Error en PayPal:', err);
                    alert('Ocurrió un error con PayPal. Por favor intenta de nuevo.');
                }
                
            }).render('#paypal-button-' + key);
        });
    </script>
</body>
</html>
