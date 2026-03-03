<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.Pet" %>
<%@ page import="model.entity.User" %>
<%
    Pet pet = (Pet) request.getAttribute("pet");
    User owner = (User) request.getAttribute("owner");
    String ownerName = (String) request.getAttribute("ownerName");
    
    // Mensajes de sesión (después de POST)
    String successMessage = (String) session.getAttribute("successMessage");
    String errorMessage = (String) session.getAttribute("errorMessage");
    session.removeAttribute("successMessage");
    session.removeAttribute("errorMessage");
    
    if (pet == null) {
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= pet.getNamePet() %> - PawPaw</title>
    
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body class="public-page">
    
    <!-- Header simple -->
    <header class="public-header">
        <div class="public-header-content">
            <a href="<%= request.getContextPath() %>/" class="public-logo">
                <img src="<%= request.getContextPath() %>/images/logo.png" alt="PawPaw">
                <span>PawPaw</span>
            </a>
        </div>
    </header>
    
    <!-- Contenido principal -->
    <main class="public-main">
        <div class="public-container">
            
            <!-- Mensajes -->
            <% if (successMessage != null) { %>
                <div class="alert alert-success">
                    ✅ <%= successMessage %>
                </div>
            <% } %>
            
            <% if (errorMessage != null) { %>
                <div class="alert alert-error">
                    ⚠️ <%= errorMessage %>
                </div>
            <% } %>
            
            <!-- Card de la mascota -->
            <div class="pet-public-card">
                
                <!-- Foto grande -->
                <div class="pet-public-photo">
                    <% if (pet.getPhoto() != null && !pet.getPhoto().isEmpty()) { %>
                        <img src="<%= pet.getPhoto() %>" alt="<%= pet.getNamePet() %>">
                    <% } else { %>
                        <div class="pet-photo-placeholder-large">
                            🐾
                        </div>
                    <% } %>
                    
                    <!-- Badge de estado -->
                    <% if ("lost".equals(pet.getStatusPet())) { %>
                        <div class="pet-public-status lost">
                            🚨 MASCOTA PERDIDA
                        </div>
                    <% } else if ("found".equals(pet.getStatusPet())) { %>
                        <div class="pet-public-status found">
                            ✅ MASCOTA ENCONTRADA
                        </div>
                    <% } %>
                </div>
                
                <!-- Información -->
                <div class="pet-public-info">
                    <h1>¡Hola! Soy <%= pet.getNamePet() %></h1>
                    
                    <div class="pet-public-details">
                        <div class="detail-row">
                            <span class="detail-icon">🐕</span>
                            <div class="detail-content">
                                <span class="detail-label">Raza</span>
                                <span class="detail-value"><%= pet.getBreed() != null ? pet.getBreed() : "No especificada" %></span>
                            </div>
                        </div>
                        
                        <div class="detail-row">
                            <span class="detail-icon">🎂</span>
                            <div class="detail-content">
                                <span class="detail-label">Edad</span>
                                <span class="detail-value"><%= pet.getAgePet() != null ? pet.getAgePet() + " años" : "No especificada" %></span>
                            </div>
                        </div>
                        
                        <div class="detail-row">
                            <span class="detail-icon">⚧</span>
                            <div class="detail-content">
                                <span class="detail-label">Sexo</span>
                                <span class="detail-value"><%= pet.getSexPet() != null ? pet.getSexPet() : "No especificado" %></span>
                            </div>
                        </div>
                        
                        <% if (pet.getMedicalConditions() != null && !pet.getMedicalConditions().isEmpty()) { %>
                        <div class="detail-row important">
                            <span class="detail-icon">⚕️</span>
                            <div class="detail-content">
                                <span class="detail-label">Condiciones médicas</span>
                                <span class="detail-value"><%= pet.getMedicalConditions() %></span>
                            </div>
                        </div>
                        <% } %>
                        
                        <% if (pet.getExtraComments() != null && !pet.getExtraComments().isEmpty()) { %>
                        <div class="detail-row important">
                            <span class="detail-icon">💬</span>
                            <div class="detail-content">
                                <span class="detail-label">Comentarios Extras</span>
                                <span class="detail-value"><%= pet.getExtraComments() %></span>
                            </div>
                        </div>
                        <% } %>
                    </div>
                    
                    <!-- Información del dueño -->
                    <div class="owner-info">
                        <h3>👤 Mi dueño es <%= ownerName %></h3>
                        <% if (pet.getContactPhone() != null && !pet.getContactPhone().isEmpty()) { %>
                        <a href="tel:<%= pet.getContactPhone() %>" class="contact-phone">
                            📞 <%= pet.getContactPhone() %>
                        </a>
                        <% } %>
                    </div>
                    
                    <!-- Acciones principales -->
                    <div class="public-actions">
                        
                        <!-- Reportar encontrada (solo si está perdida) -->
                        <% if ("lost".equals(pet.getStatusPet())) { %>
                        <form method="POST" action="<%= request.getContextPath() %>/pet/<%= pet.getIdPet() %>" class="inline-form">
                            <input type="hidden" name="action" value="report-found">
                            <input type="hidden" name="petId" value="<%= pet.getIdPet() %>">
                            <button type="submit" class="btn btn-primario btn-grande" onclick="return confirm('¿Confirmas que encontraste a <%= pet.getNamePet() %>?');">
                                🏠 ¡Encontré a <%= pet.getNamePet() %>!
                            </button>
                        </form>
                        <% } %>
                        
                        <!-- Botón para llamar -->
                        <% if (pet.getContactPhone() != null && !pet.getContactPhone().isEmpty()) { %>
                        <a href="tel:<%= pet.getContactPhone() %>" class="btn btn-secundario btn-grande">
                            📞 Llamar al dueño
                        </a>
                        <% } %>
                    </div>
                    
                    <!-- Formulario de contacto -->
                    <div class="contact-form-section">
                        <h3>💬 Enviar mensaje al dueño</h3>
                        <p class="form-description">Déjale un mensaje al dueño de <%= pet.getNamePet() %></p>
                        
                        <form method="POST" action="<%= request.getContextPath() %>/pet/<%= pet.getIdPet() %>" class="contact-form">
                            <input type="hidden" name="action" value="send-message">
                            <input type="hidden" name="petId" value="<%= pet.getIdPet() %>">
                            
                            <div class="form-group">
                                <label for="senderName" class="form-label required">Tu nombre *</label>
                                <input type="text" 
                                       id="senderName" 
                                       name="senderName" 
                                       class="form-input" 
                                       required
                                       maxlength="100"
                                       placeholder="Ej: Juan Pérez">
                            </div>
                            
                            <div class="form-group">
                                <label for="senderPhone" class="form-label required">Tu teléfono *</label>
                                <input type="tel" 
                                       id="senderPhone" 
                                       name="senderPhone" 
                                       class="form-input" 
                                       required
                                       maxlength="20"
                                       placeholder="Ej: 099 123 456">
                            </div>
                            
                            <div class="form-group">
                                <label for="message" class="form-label required">Mensaje *</label>
                                <textarea id="message" 
                                          name="message" 
                                          class="form-textarea" 
                                          rows="4" 
                                          required
                                          maxlength="500"
                                          placeholder="Cuéntale al dueño dónde viste a <%= pet.getNamePet() %> o cómo pueden contactarte..."></textarea>
                            </div>
                            
                            <button type="submit" class="btn btn-primario btn-grande">
                                ✉️ Enviar mensaje
                            </button>
                        </form>
                    </div>
                    
                </div>
            </div>
            
            <!-- Footer informativo -->
            <div class="public-footer">
                <p>Esta mascota está protegida con <strong>PawPaw</strong></p>
                <p>¿Quieres proteger a tu mascota también? <a href="<%= request.getContextPath() %>/register">Crea tu cuenta gratis</a></p>
            </div>
            
        </div>
    </main>
    
</body>
</html>
