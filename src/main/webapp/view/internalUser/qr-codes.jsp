<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.entity.User" %>
<%@ page import="controller.QRCodeController.QRCodeData" %>
<%@ page import="model.entity.Pet" %>
<%@ page import="model.entity.QRcode" %>
<%
    // Verificar sesión
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    User user = (User) request.getAttribute("user");
    String userName = user != null ? user.getNameUser() : (String) session.getAttribute("userName");
    
    @SuppressWarnings("unchecked")
    List<QRCodeData> qrDataList = (List<QRCodeData>) request.getAttribute("qrDataList");
    Integer totalQRs = (Integer) request.getAttribute("totalQRs");
    
    if (qrDataList == null) {
        qrDataList = new java.util.ArrayList<>();
    }
    if (totalQRs == null) {
        totalQRs = 0;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Códigos QR - PawPaw</title>
    
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
    
    <!-- QR Code Generator Library -->
    <script src="https://cdn.jsdelivr.net/npm/qrcodejs@1.0.0/qrcode.min.js"></script>
</head>
<body>
    <div class="dashboard">
        
        <!-- Sidebar -->
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
                
                <a href="<%= request.getContextPath() %>/user/qr-codes" class="nav-item active">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 20h4M4 12h4m12 0h.01M5 8h2a1 1 0 001-1V5a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1zm12 0h2a1 1 0 001-1V5a1 1 0 00-1-1h-2a1 1 0 00-1 1v2a1 1 0 001 1zM5 20h2a1 1 0 001-1v-2a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1z"></path>
                    </svg>
                    Códigos QR
                </a>
                
                <div class="nav-divider"></div>
                
                <a href="<%= request.getContextPath() %>/user/profile" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                    </svg>
                    Mi Perfil
                </a>
                
                <a href="<%= request.getContextPath() %>/user/send-suggestion" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 8h10M7 12h4m1 8l-4-4H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-3l-4 4z"></path>
                    </svg>
                    Enviar Sugerencia
                </a>
                
                <a href="<%= request.getContextPath() %>/user/my-suggestions" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path>
                    </svg>
                    Mis Sugerencias
                </a>
                
                <a href="<%= request.getContextPath() %>/logout" class="nav-item">
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
                    <h1>Mis Códigos QR</h1>
                </div>
                <div class="topbar-actions">
                    <a href="<%= request.getContextPath() %>/user/pets/new" class="btn btn-primario">
                        + Nueva Mascota
                    </a>
                </div>
            </div>
            
            <!-- Content -->
            <div class="content">
                
                <% if (qrDataList.isEmpty()) { %>
                    <!-- Empty State -->
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
                        <a href="<%= request.getContextPath() %>/user/pets/new" class="btn btn-primario btn-grande">
                            Registrar Mi Primera Mascota
                        </a>
                    </div>
                <% } else { %>
                    
                    <!-- QR Section -->
                    <div class="qr-section">
                        <div class="qr-header">
                            <h2>Todos tus códigos QR (<%= qrDataList.size() %>)</h2>
                            <p>Descarga, comparte o visualiza los códigos QR de tus mascotas</p>
                        </div>
                        
                        <div class="qr-grid">
                            <% for (QRCodeData qrData : qrDataList) { 
                                Pet pet = qrData.getPet();
                                QRcode qr = qrData.getQrCode();
                                
                                // URL pública de la mascota
                                String publicUrl = request.getScheme() + "://" + request.getServerName() + 
                                                 (request.getServerPort() != 80 && request.getServerPort() != 443 ? ":" + request.getServerPort() : "") +
                                                 request.getContextPath() + "/pet/" + pet.getIdPet();
                            %>
                            
                            <div class="qr-card">
                                <!-- Info de la mascota -->
                                <div class="qr-pet-info">
                                    <% if (pet.getPhoto() != null && !pet.getPhoto().isEmpty()) { %>
                                        <img src="<%= pet.getPhoto() %>" alt="<%= pet.getNamePet() %>" class="qr-pet-photo">
                                    <% } else { %>
                                        <div class="qr-pet-photo-placeholder">🐾</div>
                                    <% } %>
                                    <div>
                                        <h3><%= pet.getNamePet() %></h3>
                                        <p><%= pet.getBreed() != null ? pet.getBreed() : "Sin raza especificada" %></p>
                                    </div>
                                </div>
                                
                                <!-- QR Code generado dinámicamente -->
                                <div class="qr-code-container">
                                    <div id="qrcode-<%= pet.getIdPet() %>" class="qr-code"></div>
                                </div>
                                
                                <!-- Estadísticas -->
                                <div class="qr-stats">
                                    <div class="qr-stat">
                                        <span class="qr-stat-label">Escaneos</span>
                                        <span class="qr-stat-value"><%= qr.getScansCount() != null ? qr.getScansCount() : 0 %></span>
                                    </div>
                                    <div class="qr-stat">
                                        <span class="qr-stat-label">Estado</span>
                                        <span class="qr-stat-value <%= qr.getActive() ? "text-success" : "text-error" %>">
                                            <%= qr.getActive() ? "✓ Activo" : "⚠ Inactivo" %>
                                        </span>
                                    </div>
                                </div>
                                
                                <!-- URL del QR (para copiar) -->
                                <div class="qr-url">
                                    <input type="text" 
                                           value="<%= publicUrl %>" 
                                           readonly 
                                           class="qr-url-input"
                                           id="url-<%= pet.getIdPet() %>">
                                    <button onclick="copyURL(<%= pet.getIdPet() %>)" class="btn-copy">
                                        📋 Copiar
                                    </button>
                                </div>
                                
                                <!-- Acciones -->
                                <div class="qr-actions">
                                    <!-- Ver página pública -->
                                    <a href="<%= request.getContextPath() %>/pet/<%= pet.getIdPet() %>" 
                                       target="_blank" 
                                       class="btn btn-secundario">
                                        👁️ Vista previa
                                    </a>
                                    
                                    <!-- Descargar QR -->
                                    <button onclick="downloadQR(<%= pet.getIdPet() %>, '<%= pet.getNamePet() %>')" 
                                            class="btn btn-primario">
                                        ⬇️ Descargar
                                    </button>
                                </div>
                                
                                <!-- Script para generar el QR -->
                                <script>
                                    (function() {
                                        const qrDiv = document.getElementById('qrcode-<%= pet.getIdPet() %>');
                                        if (qrDiv && typeof QRCode !== 'undefined') {
                                            new QRCode(qrDiv, {
                                                text: '<%= publicUrl %>',
                                                width: 200,
                                                height: 200,
                                                colorDark: '#884A39',
                                                colorLight: '#ffffff',
                                                correctLevel: QRCode.CorrectLevel.H
                                            });
                                        }
                                    })();
                                </script>
                            </div>
                            
                            <% } %>
                        </div>
                    </div>
                    
                <% } %>
                
            </div>
        </div>
    </div>
    
    <!-- Scripts -->
    <script>
        // Copiar URL al portapapeles
        function copyURL(petId) {
            const input = document.getElementById('url-' + petId);
            input.select();
            input.setSelectionRange(0, 99999);
            
            navigator.clipboard.writeText(input.value).then(function() {
                alert('✅ URL copiada al portapapeles');
            }).catch(function() {
                document.execCommand('copy');
                alert('✅ URL copiada');
            });
        }
        
        // Descargar QR como imagen
        function downloadQR(petId, petName) {
            const qrDiv = document.getElementById('qrcode-' + petId);
            const canvas = qrDiv.querySelector('canvas');
            
            if (canvas) {
                const tmpCanvas = document.createElement('canvas');
                tmpCanvas.width = canvas.width;
                tmpCanvas.height = canvas.height;
                const ctx = tmpCanvas.getContext('2d');
                
                ctx.fillStyle = '#ffffff';
                ctx.fillRect(0, 0, tmpCanvas.width, tmpCanvas.height);
                ctx.drawImage(canvas, 0, 0);
                
                const link = document.createElement('a');
                link.download = 'QR-' + petName.replace(/\s+/g, '-') + '.png';
                link.href = tmpCanvas.toDataURL('image/png');
                link.click();
            } else {
                alert('⚠️ Error al generar la imagen');
            }
        }
    </script>
    
</body>
</html>
