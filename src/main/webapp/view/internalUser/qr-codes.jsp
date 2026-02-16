<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="controller.QRCodeController.QRCodeData" %>
<%@ page import="model.entity.Pet" %>
<%@ page import="model.entity.QRcode" %>
<%
    // Verificar sesión
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
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
    
    <!-- Header con navegación -->
    <header class="header-interno">
        <nav class="navbar-interno">
            <div class="navbar-contenedor">
                <a href="<%= request.getContextPath() %>/" class="navbar-logo">
                    <img src="<%= request.getContextPath() %>/images/logo.png" alt="PawPaw" class="logo-img">
                    <span class="logo-texto">PawPaw</span>
                </a>
                
                <div class="navbar-menu">
                    <a href="<%= request.getContextPath() %>/user/panel" class="navbar-link">
                        🏠 Panel
                    </a>
                    <a href="<%= request.getContextPath() %>/user/pets" class="navbar-link">
                        🐾 Mis Mascotas
                    </a>
                    <a href="<%= request.getContextPath() %>/user/qr-codes" class="navbar-link active">
                        📱 Códigos QR
                    </a>
                    <a href="<%= request.getContextPath() %>/user/profile" class="navbar-link">
                        👤 Perfil
                    </a>
                    <a href="<%= request.getContextPath() %>/logout" class="navbar-link">
                        🚪 Salir
                    </a>
                </div>
            </div>
        </nav>
    </header>
    
    <main class="main-content">
        <div class="container">
            
            <!-- Header de página -->
            <div class="page-header">
                <div>
                    <h1>📱 Mis Códigos QR</h1>
                    <p class="page-subtitle">Gestiona los códigos QR de todas tus mascotas</p>
                </div>
            </div>
            
            <!-- Lista de QR Codes -->
            <% if (qrDataList.isEmpty()) { %>
                <div class="empty-state">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <rect x="3" y="3" width="18" height="18" rx="2"></rect>
                        <rect x="7" y="7" width="3" height="3"></rect>
                        <rect x="14" y="7" width="3" height="3"></rect>
                        <rect x="7" y="14" width="3" height="3"></rect>
                        <rect x="14" y="14" width="3" height="3"></rect>
                    </svg>
                    <h3>No tienes mascotas registradas</h3>
                    <p>Primero debes registrar una mascota para poder generar su código QR</p>
                    <a href="<%= request.getContextPath() %>/user/pets/new" class="btn btn-primario btn-grande">
                        ➕ Registrar primera mascota
                    </a>
                </div>
            <% } else { %>
                
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
                        <!-- Foto de la mascota -->
                        <div class="qr-card-header">
                            <div class="qr-pet-photo">
                                <% if (pet.getPhoto() != null && !pet.getPhoto().isEmpty()) { %>
                                    <img src="<%= pet.getPhoto() %>" alt="<%= pet.getNamePet() %>">
                                <% } else { %>
                                    <div class="pet-photo-placeholder">🐾</div>
                                <% } %>
                            </div>
                            <div class="qr-pet-info">
                                <h3><%= pet.getNamePet() %></h3>
                                <p class="qr-pet-breed"><%= pet.getBreed() != null ? pet.getBreed() : "Sin raza" %></p>
                                
                                <!-- Badge de estado -->
                                <% if ("lost".equals(pet.getStatusPet())) { %>
                                    <span class="pet-status-badge lost">🚨 Perdida</span>
                                <% } else if ("found".equals(pet.getStatusPet())) { %>
                                    <span class="pet-status-badge found">✅ Encontrada</span>
                                <% } else { %>
                                    <span class="pet-status-badge safe">🏠 Segura</span>
                                <% } %>
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
                                <span class="qr-stat-value">
                                    <%= qr.getActive() ? "✅ Activo" : "⚠️ Inactivo" %>
                                </span>
                            </div>
                        </div>
                        
                        <!-- Acciones -->
                        <div class="qr-actions">
                            <!-- Ver página pública -->
                            <a href="<%= request.getContextPath() %>/pet/<%= pet.getIdPet() %>" 
                               target="_blank" 
                               class="btn btn-secundario btn-small">
                                👁️ Vista previa
                            </a>
                            
                            <!-- Descargar QR -->
                            <button onclick="downloadQR(<%= pet.getIdPet() %>, '<%= pet.getNamePet() %>')" 
                                    class="btn btn-primario btn-small">
                                ⬇️ Descargar QR
                            </button>
                            
                            <!-- Compartir -->
                            <button onclick="shareQR('<%= publicUrl %>', '<%= pet.getNamePet() %>')" 
                                    class="btn btn-outline btn-small">
                                🔗 Compartir
                            </button>
                        </div>
                        
                        <!-- URL del QR (para copiar) -->
                        <div class="qr-url">
                            <input type="text" 
                                   value="<%= publicUrl %>" 
                                   readonly 
                                   class="qr-url-input"
                                   id="url-<%= pet.getIdPet() %>">
                            <button onclick="copyURL(<%= pet.getIdPet() %>)" 
                                    class="btn-copy">
                                📋 Copiar
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
                
            <% } %>
            
            <!-- Información adicional -->
            <div class="qr-info-section">
                <h2>💡 ¿Cómo usar tus códigos QR?</h2>
                <div class="info-grid">
                    <div class="info-card">
                        <div class="info-icon">1️⃣</div>
                        <h3>Descarga el QR</h3>
                        <p>Haz clic en "Descargar QR" para guardar la imagen en tu dispositivo</p>
                    </div>
                    <div class="info-card">
                        <div class="info-icon">2️⃣</div>
                        <h3>Imprime o graba</h3>
                        <p>Puedes imprimir el QR en papel o grabarlo en una placa para el collar</p>
                    </div>
                    <div class="info-card">
                        <div class="info-icon">3️⃣</div>
                        <h3>Colócalo en el collar</h3>
                        <p>Asegúrate de que el QR esté siempre visible en el collar de tu mascota</p>
                    </div>
                    <div class="info-card">
                        <div class="info-icon">4️⃣</div>
                        <h3>¡Listo para escanear!</h3>
                        <p>Cualquier persona con un celular puede escanear el QR y contactarte</p>
                    </div>
                </div>
            </div>
            
        </div>
    </main>
    
    <!-- Scripts -->
    <script>
        // Copiar URL al portapapeles
        function copyURL(petId) {
            const input = document.getElementById('url-' + petId);
            input.select();
            input.setSelectionRange(0, 99999); // Para móviles
            
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
                // Crear un canvas temporal con fondo blanco
                const tmpCanvas = document.createElement('canvas');
                tmpCanvas.width = canvas.width;
                tmpCanvas.height = canvas.height;
                const ctx = tmpCanvas.getContext('2d');
                
                // Fondo blanco
                ctx.fillStyle = '#ffffff';
                ctx.fillRect(0, 0, tmpCanvas.width, tmpCanvas.height);
                
                // Dibujar el QR encima
                ctx.drawImage(canvas, 0, 0);
                
                // Descargar
                const link = document.createElement('a');
                link.download = 'QR-' + petName.replace(/\s+/g, '-') + '.png';
                link.href = tmpCanvas.toDataURL('image/png');
                link.click();
            } else {
                alert('⚠️ Error al generar la imagen');
            }
        }
        
        // Compartir QR (Web Share API)
        function shareQR(url, petName) {
            if (navigator.share) {
                navigator.share({
                    title: 'Perfil de ' + petName + ' en PawPaw',
                    text: 'Escanea este QR para ver el perfil de ' + petName,
                    url: url
                }).catch(function(error) {
                    console.log('Error sharing:', error);
                    fallbackShare(url);
                });
            } else {
                fallbackShare(url);
            }
        }
        
        // Fallback para navegadores sin Web Share API
        function fallbackShare(url) {
            const input = document.createElement('input');
            input.value = url;
            document.body.appendChild(input);
            input.select();
            document.execCommand('copy');
            document.body.removeChild(input);
            alert('✅ URL copiada al portapapeles. Puedes pegarla donde quieras compartirla.');
        }
    </script>
    
</body>
</html>
