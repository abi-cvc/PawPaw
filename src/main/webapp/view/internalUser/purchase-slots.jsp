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
    BigDecimal singleSlotPrice = (BigDecimal) request.getAttribute("singleSlotPrice");
    BigDecimal savings = (BigDecimal) request.getAttribute("savings");
    
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
    
    <style>
        .pricing-container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 1rem;
        }
        
        .pricing-header {
            text-align: center;
            margin-bottom: 3rem;
        }
        
        .pricing-header h1 {
            color: var(--color-1);
            font-size: 2.5rem;
            margin-bottom: 0.5rem;
        }
        
        .pricing-header p {
            color: var(--color-2);
            font-size: 1.1rem;
        }
        
        .pricing-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 2rem;
            margin-bottom: 3rem;
        }
        
        .pricing-card {
            background: white;
            border-radius: var(--radio-lg);
            padding: 2rem;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            transition: all 0.3s ease;
            border: 2px solid transparent;
            position: relative;
        }
        
        .pricing-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 24px rgba(0,0,0,0.15);
        }
        
        .pricing-card.featured {
            border-color: var(--color-1);
            transform: scale(1.05);
        }
        
        .pricing-card.featured::before {
            content: "🎉 OFERTA ESPECIAL";
            position: absolute;
            top: -15px;
            left: 50%;
            transform: translateX(-50%);
            background: linear-gradient(135deg, var(--color-1), var(--color-2));
            color: white;
            padding: 0.5rem 1.5rem;
            border-radius: var(--radio-full);
            font-size: 0.85rem;
            font-weight: 600;
        }
        
        .pricing-card h3 {
            color: var(--color-1);
            font-size: 1.5rem;
            margin-bottom: 1rem;
        }
        
        .pricing-price {
            font-size: 3rem;
            font-weight: 700;
            color: var(--color-2);
            margin-bottom: 0.5rem;
        }
        
        .pricing-price small {
            font-size: 1.2rem;
            color: #999;
        }
        
        .pricing-savings {
            background: #e8f5e9;
            color: #2e7d32;
            padding: 0.5rem 1rem;
            border-radius: var(--radio-md);
            font-weight: 600;
            margin-bottom: 1.5rem;
            display: inline-block;
        }
        
        .pricing-features {
            list-style: none;
            padding: 0;
            margin: 1.5rem 0;
        }
        
        .pricing-features li {
            padding: 0.75rem 0;
            border-bottom: 1px solid #f0f0f0;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        
        .pricing-features li:last-child {
            border-bottom: none;
        }
        
        .pricing-features li::before {
            content: "✓";
            color: var(--color-1);
            font-weight: 700;
            font-size: 1.2rem;
        }
        
        .btn-purchase {
            width: 100%;
            padding: 1rem;
            background: linear-gradient(135deg, var(--color-1), var(--color-2));
            color: white;
            border: none;
            border-radius: var(--radio-md);
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .btn-purchase:hover {
            transform: scale(1.05);
            box-shadow: 0 4px 12px rgba(136, 74, 57, 0.3);
        }
        
        .payment-methods {
            background: #f9f9f9;
            border-radius: var(--radio-lg);
            padding: 2rem;
            margin-top: 3rem;
        }
        
        .payment-methods h3 {
            color: var(--color-1);
            margin-bottom: 1.5rem;
            text-align: center;
        }
        
        .payment-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1rem;
        }
        
        .payment-option {
            background: white;
            padding: 1.5rem;
            border-radius: var(--radio-md);
            text-align: center;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
        }
        
        .payment-option h4 {
            color: var(--color-2);
            margin-bottom: 0.5rem;
        }
        
        .payment-option p {
            font-size: 0.9rem;
            color: #666;
        }
    </style>
</head>
<body>
    <div class="dashboard">
        <!-- Sidebar -->
        <aside class="sidebar">
            <div class="logo">
                <h2>🐾 PawPaw</h2>
            </div>
            
            <nav class="nav-menu">
                <a href="<%= request.getContextPath() %>/user/panel" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"></path>
                    </svg>
                    Panel
                </a>
                
                <a href="<%= request.getContextPath() %>/user/pets" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 10l-2 1m0 0l-2-1m2 1v2.5M20 7l-2 1m2-1l-2-1m2 1v2.5M14 4l-2-1-2 1M4 7l2-1M4 7l2 1M4 7v2.5M12 21l-2-1m2 1l2-1m-2 1v-2.5M6 18l-2-1v-2.5M18 18l2-1v-2.5"></path>
                    </svg>
                    Mis Mascotas
                </a>
                
                <a href="<%= request.getContextPath() %>/user/purchase-slots" class="nav-item active">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
                    </svg>
                    Comprar Slots
                </a>
            </nav>
            
            <div class="user-info">
                <div class="user-avatar">
                    <%= userName != null && !userName.isEmpty() ? userName.substring(0, 1).toUpperCase() : "U" %>
                </div>
                <div class="user-details">
                    <p class="user-name"><%= userName %></p>
                    <a href="<%= request.getContextPath() %>/logout" class="logout-link">Cerrar sesión</a>
                </div>
            </div>
        </aside>
        
        <!-- Main Content -->
        <main class="main-content">
            <div class="pricing-container">
                <!-- Header -->
                <div class="pricing-header">
                    <h1>🐾 Expande tu límite de mascotas</h1>
                    <p>Elige el plan que mejor se adapte a tus necesidades</p>
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
                    <!-- Slot Individual -->
                    <div class="pricing-card">
                        <h3>Slot Individual</h3>
                        <div class="pricing-price">
                            $5<small> USD</small>
                        </div>
                        <p style="color: #666; margin-bottom: 1.5rem;">Pago único</p>
                        
                        <ul class="pricing-features">
                            <li>1 espacio adicional</li>
                            <li>Permanente</li>
                            <li>No caduca</li>
                        </ul>
                        
                        <button class="btn-purchase" onclick="selectPlan(1, 5.00)">
                            Comprar ahora
                        </button>
                    </div>
                    
                    <!-- Promoción (si existe) -->
                    <% if (activePromo != null) { %>
                    <div class="pricing-card featured">
                        <h3><%= activePromo.getPromoName() %></h3>
                        <div class="pricing-price">
                            $<%= activePromo.getPromoPrice() %><small> USD</small>
                        </div>
                        
                        <% if (savings != null && savings.compareTo(BigDecimal.ZERO) > 0) { %>
                        <div class="pricing-savings">
                            ¡Ahorra $<%= savings %>!
                        </div>
                        <% } %>
                        
                        <ul class="pricing-features">
                            <li><%= activePromo.getSlotsQuantity() %> espacios adicionales</li>
                            <li>Permanentes</li>
                            <li>No caducan</li>
                            <% if (activePromo.getPromoDescription() != null) { %>
                            <li><%= activePromo.getPromoDescription() %></li>
                            <% } %>
                        </ul>
                        
                        <button class="btn-purchase" onclick="selectPlan(<%= activePromo.getSlotsQuantity() %>, <%= activePromo.getPromoPrice() %>)">
                            Aprovechar oferta
                        </button>
                    </div>
                    <% } %>
                </div>
                
                <!-- Métodos de Pago -->
                <div class="payment-methods">
                    <h3>💳 Métodos de pago disponibles</h3>
                    <div class="payment-grid">
                        <div class="payment-option">
                            <h4>PayPal</h4>
                            <p>Activación inmediata</p>
                        </div>
                        <div class="payment-option">
                            <h4>DeUna QR</h4>
                            <p>Verificación en 24h</p>
                        </div>
                        <div class="payment-option">
                            <h4>Transferencia</h4>
                            <p>Verificación en 24h</p>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
    
    <script>
        function selectPlan(slots, price) {
            // Por ahora solo alerta, después integraremos PayPal
            alert('Has seleccionado ' + slots + ' slot(s) por $' + price + ' USD\n\nPróximamente: Integración con PayPal');
        }
    </script>
</body>
</html>
