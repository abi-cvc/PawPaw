<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.PetTransferRequest" %>
<%@ page import="model.entity.User" %>
<%
    PetTransferRequest transfer = (PetTransferRequest) request.getAttribute("transfer");
    User user = (User) request.getAttribute("user");
    String token = (String) request.getAttribute("token");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Aceptar Transferencia - PawPaw</title>
    
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <!-- CSS — usar SIEMPRE contextPath, nunca ruta relativa -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <div class="transfer-page">
        <div class="transfer-container">
            <div class="transfer-header">
                <img src="<%= request.getContextPath() %>/images/logo.png" alt="PawPaw Logo" class="transfer-logo">
                <h1>🎉 ¡Felicidades por tu Adopción!</h1>
            </div>
            
            <div class="transfer-card">
                <div class="transfer-pet-info">
                    <div class="pet-icon">🐾</div>
                    <h2><%= transfer.getPetName() %></h2>
                    <p class="foundation-name">De: <%= transfer.getFoundationName() %></p>
                </div>
                
                <div class="transfer-details">
                    <div class="detail-row">
                        <span class="detail-label">Para:</span>
                        <span class="detail-value"><%= transfer.getAdopterName() %></span>
                    </div>
                    <div class="detail-row">
                        <span class="detail-label">Email:</span>
                        <span class="detail-value"><%= transfer.getAdopterEmail() %></span>
                    </div>
                    <div class="detail-row">
                        <span class="detail-label">Válido hasta:</span>
                        <span class="detail-value">
                            <%= transfer.getDaysRemaining() %> días restantes
                            <% if (transfer.isExpiringSoon()) { %>
                            <span class="badge-warning">⚠️ Expira pronto</span>
                            <% } %>
                        </span>
                    </div>
                </div>
                
                <% if (transfer.getMessage() != null && !transfer.getMessage().trim().isEmpty()) { %>
                <div class="transfer-message">
                    <h3>Mensaje de la fundación:</h3>
                    <p>"<%= transfer.getMessage() %>"</p>
                </div>
                <% } %>
                
                <div class="transfer-info-box">
                    <h3>¿Qué sucederá al aceptar?</h3>
                    <ul>
                        <li>✅ <%= transfer.getPetName() %> será oficialmente tuyo/a</li>
                        <li>✅ Podrás editar su perfil y actualizar información</li>
                        <li>✅ El código QR seguirá funcionando con tus datos</li>
                        <li>✅ Recibirás notificaciones sobre <%= transfer.getPetName() %></li>
                    </ul>
                </div>
                
                <div class="transfer-user-confirm">
                    <p class="confirm-text">
                        Estás iniciando sesión como: <strong><%= user.getNameUser() %></strong> 
                        (<%= user.getEmail() %>)
                    </p>
                    <% if (!user.getEmail().equalsIgnoreCase(transfer.getAdopterEmail())) { %>
                    <p class="warning-text">
                        ⚠️ Nota: El email de tu cuenta (<%= user.getEmail() %>) 
                        es diferente al email del adoptante (<%= transfer.getAdopterEmail() %>).
                        Asegúrate de ser la persona correcta.
                    </p>
                    <% } %>
                </div>
                
                <form method="post" action="<%= request.getContextPath() %>/accept-transfer" 
                      onsubmit="return confirmAccept()">
                    <input type="hidden" name="token" value="<%= token %>">
                    <input type="hidden" name="action" value="accept">
                    
                    <div class="transfer-actions">
                        <button type="submit" class="btn btn-lg btn-success">
                            ✓ Aceptar Transferencia
                        </button>
                        <button type="button" class="btn btn-lg btn-secundario" 
                                onclick="showRejectModal()">
                            ✗ Rechazar
                        </button>
                    </div>
                </form>
            </div>
            
            <div class="transfer-footer">
                <p>¿No eres <%= transfer.getAdopterName() %>? 
                   <a href="<%= request.getContextPath() %>/logout">Cerrar sesión</a>
                </p>
            </div>
        </div>
    </div>
    
    <!-- Modal de Rechazo -->
    <div class="modal-overlay" id="rejectModal">
        <div class="modal-container">
            <div class="modal-header">
                <h3>¿Rechazar Transferencia?</h3>
                <button class="modal-close" onclick="closeRejectModal()">&times;</button>
            </div>
            <form method="post" action="<%= request.getContextPath() %>/accept-transfer">
                <input type="hidden" name="token" value="<%= token %>">
                <input type="hidden" name="action" value="reject">
                
                <div class="modal-body">
                    <p>¿Estás seguro de que deseas rechazar la transferencia de <strong><%= transfer.getPetName() %></strong>?</p>
                    <p class="warning-text">Esta acción notificará a la fundación y no podrá ser revertida.</p>
                </div>
                
                <div class="modal-footer">
                    <button type="button" class="btn btn-secundario" onclick="closeRejectModal()">
                        Cancelar
                    </button>
                    <button type="submit" class="btn btn-error">
                        ✗ Rechazar Transferencia
                    </button>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        function confirmAccept() {
            return confirm('¿Confirmas que deseas aceptar la transferencia de <%= transfer.getPetName() %>?\n\nAl aceptar, te convertirás en el dueño oficial.');
        }
        
        function showRejectModal() {
            document.getElementById('rejectModal').classList.add('active');
            document.body.style.overflow = 'hidden';
        }
        
        function closeRejectModal() {
            document.getElementById('rejectModal').classList.remove('active');
            document.body.style.overflow = '';
        }
        
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                closeRejectModal();
            }
        });
        
        document.getElementById('rejectModal').addEventListener('click', function(e) {
            if (e.target === this) {
                closeRejectModal();
            }
        });
    </script>
    
</body>
</html>
