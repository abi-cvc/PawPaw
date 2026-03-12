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

    @SuppressWarnings("unchecked")
    List<Pet> pets = (List<Pet>) request.getAttribute("pets");

    Boolean isPartner = (Boolean) session.getAttribute("isPartner");
    if (isPartner == null) isPartner = false;

    // Mensajes flash de redirección
    String successParam = request.getParameter("success");
    String errorParam   = request.getParameter("error");
    String petNameParam = request.getParameter("petName");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mascotas de la Fundación - PawPaw</title>

    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <div class="dashboard">

        <!-- ════════════════════════════════ SIDEBAR ════════════════════════════════ -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <a href="<%= request.getContextPath() %>/" class="sidebar-logo">
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
                        <p><%= isPartner ? "Fundación Partner" : "Usuario" %></p>
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

                <a href="<%= request.getContextPath() %>/user/pets" class="nav-item active">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                    <%= isPartner ? "Mascotas Fundación" : "Mis Mascotas" %>
                </a>

                <a href="<%= request.getContextPath() %>/user/qr-codes" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v1m6 11h2m-6 0h-2v4m0-11v3m0 0h.01M12 12h4.01M16 20h4M4 12h4m12 0h.01M5 8h2a1 1 0 001-1V5a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1zm12 0h2a1 1 0 001-1V5a1 1 0 00-1-1h-2a1 1 0 00-1 1v2a1 1 0 001 1zM5 20h2a1 1 0 001-1v-2a1 1 0 00-1-1H5a1 1 0 00-1 1v2a1 1 0 001 1z"></path>
                    </svg>
                    Códigos QR
                </a>

                <a href="<%= request.getContextPath() %>/user/messages" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z"></path>
                    </svg>
                    Mis Mensajes
                    <%
                    Integer unreadCount = null;
                    try {
                        model.dao.PetContactMessageDAO msgDAO = new model.dao.PetContactMessageDAO();
                        Integer userId = (Integer) session.getAttribute("userId");
                        if (userId != null) unreadCount = msgDAO.countUnreadByUserId(userId);
                    } catch (Exception e) { /* silenciar */ }
                    if (unreadCount != null && unreadCount > 0) { %>
                    <span class="notification-badge"><%= unreadCount %></span>
                    <% } %>
                </a>

                <% if (isPartner) { %>
                <a href="<%= request.getContextPath() %>/foundations/<%= session.getAttribute("userId") %>"
                   target="_blank" class="nav-item">
                    <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-2 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"></path>
                    </svg>
                    Mi Página Pública
                </a>
                <% } %>

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

        <!-- ════════════════════════════════ MAIN CONTENT ════════════════════════════ -->
        <div class="main-content">

            <!-- Top Bar -->
            <div class="topbar">
                <div class="topbar-title">
                    <h1><%= isPartner ? "Mascotas de la Fundación" : "Mis Mascotas" %></h1>
                </div>
                <div class="topbar-actions">
                    <a href="<%= request.getContextPath() %>/user/pets/new" class="btn btn-primario">
                        + Registrar Nueva Mascota
                    </a>
                </div>
            </div>

            <!-- Content -->
            <div class="content">

                <!-- ── Alertas flash ─────────────────────────────────────────── -->
                <% if ("transferInitiated".equals(successParam)) { %>
                    <div class="alert alert-success">
                        ✅ Transferencia iniciada para <strong><%= petNameParam != null ? petNameParam : "la mascota" %></strong>.
                        Se envió un email al adoptante con el enlace de aceptación.
                    </div>
                <% } else if ("statusUpdated".equals(successParam)) { %>
                    <div class="alert alert-success">✅ Estado de adopción actualizado correctamente.</div>
                <% } else if (errorParam != null) { %>
                    <div class="alert alert-error">
                        ⚠️ <%= "missingFields".equals(errorParam) ? "Completa todos los campos requeridos." :
                               "petNotAvailable".equals(errorParam) ? "La mascota no está disponible para transferencia." :
                               "transferFailed".equals(errorParam) ? "Error al iniciar la transferencia. Intenta de nuevo." :
                               "Ocurrió un error. Intenta de nuevo." %>
                    </div>
                <% } %>

                <!-- ── Banner de fundación ───────────────────────────────────── -->
                <% if (isPartner) { %>
                <div class="partner-banner">
                    <div class="partner-banner-icon">🏡</div>
                    <div class="partner-banner-text">
                        <strong>Modo Fundación</strong> — Puedes marcar mascotas como disponibles para adopción
                        e iniciar transferencias directamente al adoptante. El QR se mantiene intacto.
                    </div>
                    <a href="<%= request.getContextPath() %>/foundations/<%= session.getAttribute("userId") %>"
                       target="_blank" class="btn btn-secundario btn-sm">
                        Ver página pública
                    </a>
                </div>
                <% } %>

                <!-- ── Lista de mascotas ──────────────────────────────────────── -->
                <% if (pets == null || pets.isEmpty()) { %>
                    <div class="empty-state">
                        <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                        </svg>
                        <h3>¡Aún no hay mascotas registradas!</h3>
                        <p><%= isPartner ? "Registra los animales que cuidas para publicarlos en adopción." : "Comienza registrando tu primera mascota." %></p>
                        <a href="<%= request.getContextPath() %>/user/pets/new" class="btn btn-primario btn-grande">
                            <%= isPartner ? "Registrar Primera Mascota" : "Registrar Mi Primera Mascota" %>
                        </a>
                    </div>
                <% } else { %>

                    <div class="pets-section">
                        <div class="pets-header">
                            <h2>Todas las mascotas (<%= pets.size() %>)</h2>
                            <p><%= isPartner ? "Gestiona el estado de adopción y transfiere mascotas a sus nuevos dueños." : "Gestiona la información y códigos QR de tus mascotas." %></p>
                        </div>

                        <div class="pets-grid">
                            <% for (Pet pet : pets) {
                                String adoptionStatus = null;
                                try { adoptionStatus = pet.getAdoptionStatus(); } catch (Exception ignored) {}
                                if (adoptionStatus == null) adoptionStatus = "owned";
                            %>
                            <div class="pet-card">

                                <!-- Foto -->
                                <div class="pet-photo">
                                    <% if (pet.getPhoto() != null && !pet.getPhoto().isEmpty()) { %>
                                        <img src="<%= pet.getPhoto() %>" alt="<%= pet.getNamePet() %>">
                                    <% } else { %>
                                        <span class="pet-photo-placeholder">🐾</span>
                                    <% } %>
                                    <!-- Badge de estado general -->
                                    <span class="pet-status-badge pet-status-<%= pet.getStatusPet() %>">
                                        <%
                                            if ("active".equals(pet.getStatusPet()))      out.print("Activa");
                                            else if ("lost".equals(pet.getStatusPet()))   out.print("Perdida");
                                            else if ("found".equals(pet.getStatusPet()))  out.print("Encontrada");
                                            else                                           out.print("Inactiva");
                                        %>
                                    </span>
                                </div>

                                <!-- Info -->
                                <div class="pet-info">
                                    <h4><%= pet.getNamePet() %></h4>
                                    <div class="pet-details">
                                        <div class="detail-item">
                                            <span class="detail-label">🐕 Raza:</span>
                                            <span class="detail-value"><%= pet.getBreed() != null ? pet.getBreed() : "No especificada" %></span>
                                        </div>
                                        <div class="detail-item">
                                            <span class="detail-label">🎂 Edad:</span>
                                            <span class="detail-value"><%= pet.getAgePet() != null ? pet.getAgePet() + " años" : "No especificada" %></span>
                                        </div>
                                        <div class="detail-item">
                                            <span class="detail-label">⚧ Sexo:</span>
                                            <span class="detail-value"><%= pet.getSexPet() != null ? pet.getSexPet() : "No especificado" %></span>
                                        </div>
                                        <% if (pet.getContactPhone() != null && !pet.getContactPhone().isEmpty()) { %>
                                        <div class="detail-item">
                                            <span class="detail-label">📞 Contacto:</span>
                                            <span class="detail-value"><%= pet.getContactPhone() %></span>
                                        </div>
                                        <% } %>
                                    </div>

                                    <!-- Badge de adopción (siempre visible) -->
                                    <div class="adoption-status-row">
                                        <span class="adoption-status-badge adoption-<%= adoptionStatus.replace("_", "-") %>">
                                            <%
                                                if ("available".equals(adoptionStatus))           out.print("🟢 En adopción");
                                                else if ("adopted_pending".equals(adoptionStatus)) out.print("🟡 Transferencia pendiente");
                                                else if ("adopted_transferred".equals(adoptionStatus)) out.print("🏠 Adoptado");
                                                else                                               out.print("🔵 Propio");
                                            %>
                                        </span>
                                    </div>
                                </div>

                                <!-- Acciones estándar -->
                                <div class="pet-actions">
                                    <a href="<%= request.getContextPath() %>/user/pets/edit?id=<%= pet.getIdPet() %>" class="btn btn-secundario">
                                        ✏️ Editar
                                    </a>
                                    <a href="<%= request.getContextPath() %>/pet/<%= pet.getIdPet() %>" target="_blank" class="btn btn-primario">
                                        📱 Ver Perfil
                                    </a>
                                </div>

                                <!-- ── Acciones de adopción (solo fundaciones) ────────── -->
                                <% if (isPartner && !"adopted_transferred".equals(adoptionStatus)) { %>
                                <div class="partner-actions">

                                    <!-- Cambiar estado de adopción -->
                                    <form method="POST" action="<%= request.getContextPath() %>/pet/adoption-status"
                                          class="partner-status-form">
                                        <input type="hidden" name="petId" value="<%= pet.getIdPet() %>">
                                        <select name="adoptionStatus" class="form-select form-select-sm">
                                            <option value="owned"           <%= "owned".equals(adoptionStatus)           ? "selected" : "" %>>Propio</option>
                                            <option value="available"       <%= "available".equals(adoptionStatus)       ? "selected" : "" %>>En adopción</option>
                                        </select>
                                        <button type="submit" class="btn btn-secundario btn-sm">Guardar</button>
                                    </form>

                                    <!-- Botón de transferencia (solo si está disponible y no pendiente) -->
                                    <% if ("available".equals(adoptionStatus)) { %>
                                    <button class="btn btn-success btn-sm btn-block"
                                            onclick="openTransferModal(<%= pet.getIdPet() %>, '<%= pet.getNamePet().replace("'", "\\'") %>')">
                                        🤝 Transferir a Adoptante
                                    </button>
                                    <% } %>

                                    <% if ("adopted_pending".equals(adoptionStatus)) { %>
                                    <div class="alert alert-info adoption-pending-notice">
                                        ⏳ Esperando confirmación del adoptante por email.
                                    </div>
                                    <% } %>

                                </div>
                                <% } %>

                                <!-- Botón eliminar -->
                                <% if (!"adopted_pending".equals(adoptionStatus) && !"adopted_transferred".equals(adoptionStatus)) { %>
                                <div class="pet-delete">
                                    <a href="<%= request.getContextPath() %>/user/pets/delete?id=<%= pet.getIdPet() %>"
                                       class="btn-delete"
                                       onclick="return confirm('¿Eliminar a <%= pet.getNamePet() %>? Esta acción no se puede deshacer.');">
                                        🗑️ Eliminar
                                    </a>
                                </div>
                                <% } %>

                            </div>
                            <% } // end for %>
                        </div>
                    </div>
                <% } %>

            </div>
        </div>
    </div>

    <!-- ════════════════════════════════ MODAL TRANSFERENCIA ════════════════════════ -->
    <% if (isPartner) { %>
    <div id="transferModal" class="transfer-modal" onclick="if(event.target===this) closeTransferModal()">
        <div class="transfer-modal-content">
            <div class="transfer-modal-header">
                <h2>🤝 Transferir mascota a adoptante</h2>
                <button class="transfer-modal-close" onclick="closeTransferModal()">✕</button>
            </div>

            <p class="transfer-modal-subtitle">
                Se enviará un email al adoptante con un enlace seguro. Una vez que lo acepte,
                el perfil QR se transfiere automáticamente a su cuenta.
            </p>

            <form method="POST" action="<%= request.getContextPath() %>/pet/transfer/initiate"
                  class="transfer-modal-form">
                <input type="hidden" id="modalPetId" name="petId" value="">

                <div class="transfer-modal-pet-name">
                    Mascota: <strong id="modalPetName"></strong>
                </div>

                <div class="form-group">
                    <label class="form-label" for="adopterName">Nombre del Adoptante *</label>
                    <input type="text" id="adopterName" name="adopterName"
                           class="form-input" placeholder="Ej: María García" required>
                </div>

                <div class="form-group">
                    <label class="form-label" for="adopterEmail">Email del Adoptante *</label>
                    <input type="email" id="adopterEmail" name="adopterEmail"
                           class="form-input" placeholder="adoptante@email.com" required>
                    <small class="form-hint">Se le enviará el enlace de aceptación a este correo.</small>
                </div>

                <div class="form-group">
                    <label class="form-label" for="adopterPhone">Teléfono (opcional)</label>
                    <input type="tel" id="adopterPhone" name="adopterPhone"
                           class="form-input" placeholder="+593 99 123 4567">
                </div>

                <div class="form-group">
                    <label class="form-label" for="transferMessage">Mensaje personal (opcional)</label>
                    <textarea id="transferMessage" name="message" class="form-input form-textarea"
                              placeholder="Ej: Ha sido una mascota muy cariñosa. ¡Cuídala mucho!"></textarea>
                </div>

                <div class="transfer-modal-actions">
                    <button type="submit" class="btn btn-primario btn-block">
                        📧 Enviar invitación de adopción
                    </button>
                    <button type="button" class="btn btn-secundario btn-block" onclick="closeTransferModal()">
                        Cancelar
                    </button>
                </div>
            </form>
        </div>
    </div>
    <% } %>

    <script>
        function openTransferModal(petId, petName) {
            document.getElementById('modalPetId').value = petId;
            document.getElementById('modalPetName').textContent = petName;
            document.getElementById('transferModal').classList.add('transfer-modal-open');
            document.body.style.overflow = 'hidden';
        }

        function closeTransferModal() {
            document.getElementById('transferModal').classList.remove('transfer-modal-open');
            document.body.style.overflow = '';
        }

        // Auto-ocultar alertas flash después de 5 segundos
        setTimeout(function() {
            var alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(a) { a.style.opacity = '0'; a.style.transition = 'opacity 0.5s'; });
        }, 5000);
    </script>

</body>
</html>
