// ==================== NAVEGACIÓN Y REDIRECCIONES ====================

// Función para redirigir a login
function irALogin() {
    window.location.href = contextPath + '/login';
}

// Función para redirigir a registro
function irARegistro() {
    window.location.href = contextPath + '/register';
}

// Función para redirigir al index
function irAIndex() {
    window.location.href = contextPath + '/';
}

// ==================== VALIDACIONES DE FORMULARIOS ====================

// Validar formulario de login
function validarLogin(event) {
    event.preventDefault();
    
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    
    // Limpiar mensajes de error previos
    limpiarErrores();
    
    let esValido = true;
    
    // Validar email
    if (email === '') {
        mostrarError('email', 'Por favor ingresa tu correo electrónico');
        esValido = false;
    } else if (!validarEmail(email)) {
        mostrarError('email', 'Por favor ingresa un correo válido');
        esValido = false;
    }
    
    // Validar contraseña
    if (password === '') {
        mostrarError('password', 'Por favor ingresa tu contraseña');
        esValido = false;
    }
    
    if (esValido) {
        document.getElementById('loginForm').submit();
    }
}

// Validar formulario de registro
function validarRegistro(event) {
    event.preventDefault();
    
    const nombre = document.getElementById('name').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const confirmarPassword = document.getElementById('confirmPassword').value.trim();
    
    // Limpiar mensajes de error previos
    limpiarErrores();
    
    let esValido = true;
    
    // Validar nombre
    if (nombre === '') {
        mostrarError('name', 'Por favor ingresa tu nombre');
        esValido = false;
    }
    
    // Validar email
    if (email === '') {
        mostrarError('email', 'Por favor ingresa tu correo electrónico');
        esValido = false;
    } else if (!validarEmail(email)) {
        mostrarError('email', 'Por favor ingresa un correo válido');
        esValido = false;
    }
    
    // Validar contraseña
    if (password === '') {
        mostrarError('password', 'Por favor ingresa una contraseña');
        esValido = false;
    } else if (password.length < 6) {
        mostrarError('password', 'La contraseña debe tener al menos 6 caracteres');
        esValido = false;
    }
    
    // Validar confirmación de contraseña
    if (confirmarPassword === '') {
        mostrarError('confirmPassword', 'Por favor confirma tu contraseña');
        esValido = false;
    } else if (password !== confirmarPassword) {
        mostrarError('confirmPassword', 'Las contraseñas no coinciden');
        esValido = false;
    }
    
    if (esValido) {
        document.getElementById('registerForm').submit();
    }
}

// ==================== FUNCIONES AUXILIARES ====================

// Validar formato de email
function validarEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
}

// Mostrar mensaje de error
function mostrarError(campoId, mensaje) {
    const campo = document.getElementById(campoId);
    const errorDiv = document.createElement('div');
    errorDiv.className = 'mensaje mensaje-error';
    errorDiv.textContent = mensaje;
    campo.parentElement.appendChild(errorDiv);
    campo.classList.add('input-error');
}

// Limpiar todos los mensajes de error
function limpiarErrores() {
    const mensajesError = document.querySelectorAll('.mensaje-error');
    mensajesError.forEach(mensaje => mensaje.remove());
    
    const camposConError = document.querySelectorAll('.input-error');
    camposConError.forEach(campo => campo.classList.remove('input-error'));
}

// Mostrar/ocultar contraseña
function togglePassword(inputId, iconId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(iconId);
    
    if (input.type === 'password') {
        input.type = 'text';
        icon.textContent = '🙈';
    } else {
        input.type = 'password';
        icon.textContent = '👁️';
    }
}

// ==================== UPLOAD DE IMÁGENES ====================

// Configuración de Imgur
const IMGUR_CLIENT_ID = 'df36f9a0bb16cee';

// Inicializar eventos de drag & drop
function inicializarUpload() {
    const uploadArea = document.getElementById('uploadArea');
    const fileInput = document.getElementById('fileInput');
    
    if (!uploadArea || !fileInput) return;
    
    // Prevenir comportamiento por defecto
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
        uploadArea.addEventListener(eventName, preventDefaults, false);
    });
    
    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }
    
    // Efectos visuales
    ['dragenter', 'dragover'].forEach(eventName => {
        uploadArea.addEventListener(eventName, () => {
            uploadArea.classList.add('dragover');
        }, false);
    });
    
    ['dragleave', 'drop'].forEach(eventName => {
        uploadArea.addEventListener(eventName, () => {
            uploadArea.classList.remove('dragover');
        }, false);
    });
    
    // Manejar drop
    uploadArea.addEventListener('drop', (e) => {
        const files = e.dataTransfer.files;
        if (files.length > 0) {
            handleFile(files[0]);
        }
    }, false);
    
    // Click en área
    uploadArea.addEventListener('click', () => {
        fileInput.click();
    });
    
    // Cambio en input
    fileInput.addEventListener('change', (e) => {
        if (e.target.files.length > 0) {
            handleFile(e.target.files[0]);
        }
    });
}

// Procesar archivo
function handleFile(file) {
    // Validar tipo
    if (!file.type.match('image.*')) {
        alert('⚠️ Por favor selecciona una imagen válida');
        return;
    }
    
    // Validar tamaño (10MB)
    if (file.size > 10 * 1024 * 1024) {
        alert('⚠️ La imagen es muy grande. Máximo 10MB');
        return;
    }
    
    // Mostrar preview local inmediato
    const reader = new FileReader();
    reader.onload = (e) => {
        mostrarPreview(e.target.result);
    };
    reader.readAsDataURL(file);
    
    // Subir a Imgur
    uploadToImgur(file);
}

// Subir imagen a Imgur
async function uploadToImgur(file) {
    const submitBtn = document.getElementById('submitBtn');
    const progress = document.getElementById('uploadProgress');
    const progressFill = document.getElementById('progressFill');
    const status = document.getElementById('uploadStatus');
    
    if (!progress || !progressFill || !status || !submitBtn) return;
    
    // Deshabilitar botón
    submitBtn.disabled = true;
    
    // Mostrar progreso
    progress.style.display = 'block';
    status.textContent = '📤 Subiendo imagen...';
    progressFill.style.width = '30%';
    
    const formData = new FormData();
    formData.append('image', file);
    
    try {
        const response = await fetch('https://api.imgur.com/3/image', {
            method: 'POST',
            headers: {
                'Authorization': `Client-ID ${IMGUR_CLIENT_ID}`
            },
            body: formData
        });
        
        progressFill.style.width = '70%';
        
        const data = await response.json();
        
        if (data.success) {
            const imageUrl = data.data.link;
            document.getElementById('photoUrl').value = imageUrl;
            
            progressFill.style.width = '100%';
            status.textContent = '✅ Imagen subida correctamente';
            
            setTimeout(() => {
                progress.style.display = 'none';
                submitBtn.disabled = false;
            }, 1500);
            
            console.log('✅ Imagen subida a Imgur:', imageUrl);
        } else {
            throw new Error('Error al subir la imagen');
        }
        
    } catch (error) {
        console.error('Error:', error);
        progressFill.style.width = '0%';
        status.textContent = '❌ Error al subir. Intenta nuevamente.';
        submitBtn.disabled = false;
        
        setTimeout(() => {
            progress.style.display = 'none';
        }, 3000);
    }
}

// Mostrar preview
function mostrarPreview(url) {
    const preview = document.getElementById('imagePreview');
    const container = document.getElementById('previewContainer');
    const uploadArea = document.getElementById('uploadArea');
    
    if (!preview || !container || !uploadArea) return;
    
    preview.src = url;
    container.style.display = 'block';
    uploadArea.style.display = 'none';
}

// Quitar imagen
function quitarImagen() {
    const preview = document.getElementById('imagePreview');
    const container = document.getElementById('previewContainer');
    const uploadArea = document.getElementById('uploadArea');
    const photoInput = document.getElementById('photoUrl');
    const fileInput = document.getElementById('fileInput');
    
    if (preview) preview.src = '';
    if (photoInput) photoInput.value = '';
    if (fileInput) fileInput.value = '';
    if (container) container.style.display = 'none';
    if (uploadArea) uploadArea.style.display = 'block';
}

// Inicializar al cargar la página
document.addEventListener('DOMContentLoaded', function() {
    inicializarUpload();
    
    // Mostrar preview si ya hay URL
    const photoUrl = document.getElementById('photoUrl');
    if (photoUrl && photoUrl.value) {
        mostrarPreview(photoUrl.value);
    }
});