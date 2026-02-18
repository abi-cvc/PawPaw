<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.User" %>
<%@ page import="model.entity.Pet" %>
<%@ page import="java.util.List" %>
<%
    // Verificar sesión
    if (session == null || session.getAttribute("userId") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    User user = (User) request.getAttribute("user");
    String userName = user != null ? user.getNameUser() : (String) session.getAttribute("userName");
    
    // Estadísticas
    Integer totalPets = (Integer) request.getAttribute("totalPets");
    Integer activePets = (Integer) request.getAttribute("activePets");
    Integer totalQRCodes = (Integer) request.getAttribute("totalQRCodes");
    
    if (totalPets == null) totalPets = 0;
    if (activePets == null) activePets = 0;
    if (totalQRCodes == null) totalQRCodes = 0;
    
    @SuppressWarnings("unchecked")
    List<Pet> pets = (List<Pet>) request.getAttribute("pets");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Usuario - PawPaw</title>
    
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
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
                <a href="<%= request.getContextPath() %>/user/panel" class="nav-item active">
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
                
                <div class="nav-divider"></div>
                
                <a href="<%= request.getContextPath() %>/user/profile" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"></path>
                    </svg>
                    Mi Perfil
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
                    <h1>Panel Principal</h1>
                </div>
                <div class="topbar-actions">
                    <a href="<%= request.getContextPath() %>/user/pets/new" class="btn btn-primario">
                        + Nueva Mascota
                    </a>
                </div>
            </div>
            
            <!-- Content -->
            <div class="content">
                <!-- Welcome Section -->
                <div class="welcome-section">
                    <h2>¡Bienvenido, <%= userName %>! 👋</h2>
                    <p>Gestiona tus mascotas y códigos QR desde aquí</p>
                </div>
                
                <!-- Stats Cards -->
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon pets">🐾</div>
                        <div class="stat-info">
                            <h3>Mascotas Registradas</h3>
                            <p><%= totalPets %></p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon qr">📱</div>
                        <div class="stat-info">
                            <h3>Códigos QR Activos</h3>
                            <p><%= totalQRCodes %></p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon active">✓</div>
                        <div class="stat-info">
                            <h3>Mascotas Activas</h3>
                            <p><%= activePets %></p>
                        </div>
                    </div>
                </div>
                
                <!-- Quick Actions -->
                <div class="quick-actions">
                    <h3>Acciones Rápidas</h3>
                    <div class="actions-grid">
                        <a href="<%= request.getContextPath() %>/user/pets/new" class="action-btn">
                            <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
                            </svg>
                            Registrar Mascota
                        </a>
                        
                        <a href="<%= request.getContextPath() %>/user/qr-codes" class="action-btn">
                            <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 20h4M4 12h4m12 0h.01M5 8h2a1 1 0 001-1V5a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1zm12 0h2a1 1 0 001-1V5a1 1 0 00-1-1h-2a1 1 0 00-1 1v2a1 1 0 001 1zM5 20h2a1 1 0 001-1v-2a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1z"></path>
                            </svg>
                            Generar Código QR
                        </a>
                        
                        <a href="<%= request.getContextPath() %>/user/profile" class="action-btn">
                            <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"></path>
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
                            </svg>
                            Configuración
                        </a>
                    </div>
                </div>
                
                <!-- Lista de Mascotas o Empty State -->
                <% if (pets == null || pets.isEmpty()) { %>
                    <div class="empty-state">
                        <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                        </svg>
                        <h3>¡Aún no tienes mascotas registradas!</h3>
                        <p>Comienza registrando a tu primera mascota para generar su código QR único</p>
                        <a href="<%= request.getContextPath() %>/user/pets/new" class="btn btn-primario btn-grande">
                            Registrar Mi Primera Mascota
                        </a>
                    </div>
                <% } else { %>
                    <div class="pets-section">
                        <h3>Mis Mascotas Registradas</h3>
                        
                        <div class="pets-grid">
                            <% for (Pet pet : pets) { %>
                                <div class="pet-card">
                                    <div class="pet-photo">
                                        <% if (pet.getPhoto() != null && !pet.getPhoto().isEmpty()) { %>
                                            <img src="<%= pet.getPhoto() %>" alt="<%= pet.getNamePet() %>">
                                        <% } else { %>
                                            <span class="pet-photo-placeholder">🐾</span>
                                        <% } %>
                                    </div>
                                    
                                    <div class="pet-info">
                                        <h4><%= pet.getNamePet() %></h4>
                                        <p><strong>Raza:</strong> <%= pet.getBreed() != null ? pet.getBreed() : "No especificada" %></p>
                                        <p><strong>Edad:</strong> <%= pet.getAgePet() != null ? pet.getAgePet() + " años" : "No especificada" %></p>
                                        <p><strong>Sexo:</strong> <%= pet.getSexPet() != null ? pet.getSexPet() : "No especificado" %></p>
                                        
                                        <span class="pet-status pet-status-<%= pet.getStatusPet() %>">
                                            <% 
                                                if ("active".equals(pet.getStatusPet())) {
                                                    out.print("✓ Activa");
                                                } else if ("lost".equals(pet.getStatusPet())) {
                                                    out.print("⚠ Perdida");
                                                } else if ("found".equals(pet.getStatusPet())) {
                                                    out.print("✓ Encontrada");
                                                } else {
                                                    out.print("○ Inactiva");
                                                }
                                            %>
                                        </span>
                                    </div>
                                    
                                    <div class="pet-actions">
                                        <a href="<%= request.getContextPath() %>/user/pets/edit?id=<%= pet.getIdPet() %>" class="btn btn-secundario">
                                            Editar
                                        </a>
                                        <a href="<%= request.getContextPath() %>/pet/<%= pet.getIdPet() %>" target="_blank" class="btn btn-primario">
                                            Ver QR
                                        </a>
                                    </div>
                                </div>
                            <% } %>
                        </div>
                    </div>
                <% } %>
            </div>
        </div>
    </div>
</body>
</html>
