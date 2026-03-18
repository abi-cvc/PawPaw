<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="isEdit" value="${action == 'edit'}"/>
<c:set var="pageTitle" value="${isEdit ? 'Editar Mascota' : 'Registrar Nueva Mascota'}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle}"/> - PawPaw</title>

    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <div class="contenedor-formulario">
        <div class="tarjeta-formulario" style="max-width: 800px;">

            <div class="formulario-encabezado">
                <h1>🐾 <c:out value="${pageTitle}"/></h1>
                <p>Completa la información de tu mascota</p>
            </div>

            <c:if test="${not empty error}">
                <div class="mensaje mensaje-error">
                    ⚠️ <c:out value="${error}"/>
                </div>
            </c:if>

            <form method="POST" action="${pageContext.request.contextPath}/user/pets/${isEdit ? 'edit' : 'new'}" class="form-grid" id="petForm">
                <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">

                <c:if test="${isEdit && pet != null}">
                    <input type="hidden" name="id" value="${pet.idPet}">
                </c:if>

                <input type="hidden" name="photo" id="photoUrl" value="${fn:escapeXml(pet.photo)}">

                <!-- Nombre de la mascota -->
                <div class="form-group form-group-full">
                    <label for="name" class="form-label required">Nombre de la mascota</label>
                    <input type="text"
                           id="name"
                           name="name"
                           class="form-input"
                           value="<c:out value="${pet.namePet}"/>"
                           required
                           placeholder="Ej: Max, Luna, Rocky">
                </div>

                <!-- Raza -->
                <div class="form-group">
                    <label for="breed" class="form-label required">Raza</label>
                    <input type="text"
                           id="breed"
                           name="breed"
                           class="form-input"
                           value="<c:out value="${pet.breed}"/>"
                           required
                           placeholder="Ej: Labrador, Mestizo">
                </div>

                <!-- Edad -->
                <div class="form-group">
                    <label for="age" class="form-label required">Edad (años)</label>
                    <input type="number"
                           id="age"
                           name="age"
                           class="form-input"
                           value="${pet.agePet != null ? pet.agePet : ''}"
                           min="0"
                           max="50"
                           required
                           placeholder="Ej: 3">
                </div>

                <!-- Sexo -->
                <div class="form-group">
                    <label for="sex" class="form-label">Sexo</label>
                    <select id="sex" name="sex" class="form-select form-input">
                        <option value="">Selecciona...</option>
                        <option value="Macho" ${pet.sexPet == 'Macho' ? 'selected' : ''}>🦁 Macho</option>
                        <option value="Hembra" ${pet.sexPet == 'Hembra' ? 'selected' : ''}>🦄 Hembra</option>
                        <option value="Otro" ${pet.sexPet == 'Otro' ? 'selected' : ''}>⭐ Otro</option>
                    </select>
                </div>

                <!-- Teléfono de contacto -->
                <div class="form-group">
                    <label for="contactPhone" class="form-label">Teléfono de Contacto</label>
                    <input type="tel"
                           id="contactPhone"
                           name="contactPhone"
                           class="form-input"
                           value="<c:out value="${pet.contactPhone}"/>"
                           placeholder="Ej: 0999999999">
                </div>

                <!-- Estado (solo en edición) -->
                <c:if test="${isEdit}">
                <div class="form-group">
                    <label for="status" class="form-label">Estado</label>
                    <c:set var="petStatus" value="${not empty pet.statusPet ? pet.statusPet : 'active'}"/>
                    <select id="status" name="status" class="form-select form-input">
                        <option value="active" ${petStatus == 'active' ? 'selected' : ''}>✅ Activa</option>
                        <option value="lost" ${petStatus == 'lost' ? 'selected' : ''}>⚠️ Perdida</option>
                        <option value="found" ${petStatus == 'found' ? 'selected' : ''}>🎉 Encontrada</option>
                        <option value="inactive" ${petStatus == 'inactive' ? 'selected' : ''}>💤 Inactiva</option>
                    </select>
                </div>
                </c:if>

                <!-- Condiciones médicas -->
                <div class="form-group form-group-full">
                    <label for="medicalConditions" class="form-label">Condiciones Médicas o Alergias</label>
                    <textarea id="medicalConditions"
                              name="medicalConditions"
                              class="form-textarea form-input"
                              placeholder="Ej: Alérgico al pollo, artritis, toma medicamento X"><c:out value="${pet.medicalConditions}"/></textarea>
                </div>

                <!-- Upload de foto -->
                <div class="form-group form-group-full">
                    <label class="form-label">Foto de la mascota</label>
                    <div class="upload-area" id="uploadArea">
                        <div class="upload-icon">📷</div>
                        <div class="upload-text"><strong>Haz clic o arrastra una imagen aquí</strong></div>
                        <div class="upload-hint">Formatos: JPG, PNG, GIF (Máx. 10MB)</div>
                    </div>
                    <input type="file"
                           id="fileInput"
                           accept="image/jpeg,image/png,image/gif,image/webp"
                           style="display: none;">

                    <div class="upload-progress" id="uploadProgress">
                        <div class="progress-bar">
                            <div class="progress-fill" id="progressFill"></div>
                        </div>
                        <div class="upload-status" id="uploadStatus"></div>
                    </div>

                    <div class="preview-container" id="previewContainer">
                        <img id="imagePreview" class="preview-image" alt="Vista previa">
                        <button type="button" class="remove-image" onclick="quitarImagen()">❌ Quitar</button>
                    </div>
                </div>

                <!-- Comentarios adicionales -->
                <div class="form-group form-group-full">
                    <label for="extraComments" class="form-label">Comentarios Adicionales</label>
                    <textarea id="extraComments"
                              name="extraComments"
                              class="form-textarea form-input"
                              placeholder="Información adicional sobre tu mascota (comportamiento, preferencias, etc.)"><c:out value="${pet.extraComments}"/></textarea>
                </div>

                <!-- Botones de acción -->
                <div class="form-actions">
                    <button type="submit" class="btn btn-primario" id="submitBtn">
                        ${isEdit ? '💾 Guardar Cambios' : '✅ Registrar Mascota'}
                    </button>
                    <a href="${pageContext.request.contextPath}/user/panel" class="btn btn-secundario">
                        ← Cancelar
                    </a>
                </div>

            </form>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
