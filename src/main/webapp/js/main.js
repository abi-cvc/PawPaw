// ==================== NAVEGACIÓN Y REDIRECCIONES ====================

// Función para redirigir a login
function irALogin() {
    window.location.href = contextPath + '/views/login.jsp';
}

// Función para redirigir a registro
function irARegistro() {
    window.location.href = contextPath + '/views/register.jsp';
}

// Función para redirigir al index
function irAIndex() {
    window.location.href = contextPath + '/views/index.jsp';
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
        // Aquí enviarías el formulario al servidor
        document.getElementById('loginForm').submit();
    }
}

// Validar formulario de registro
function validarRegistro(event) {
    event.preventDefault();
    
    const nombre = document.getElementById('nombre').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();
    const confirmarPassword = document.getElementById('confirmar-password').value.trim();
    
    // Limpiar mensajes de error previos
    limpiarErrores();
    
    let esValido = true;
    
    // Validar nombre
    if (nombre === '') {
        mostrarError('nombre', 'Por favor ingresa tu nombre');
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
        mostrarError('confirmar-password', 'Por favor confirma tu contraseña');
        esValido = false;
    } else if (password !== confirmarPassword) {
        mostrarError('confirmar-password', 'Las contraseñas no coinciden');
        esValido = false;
    }
    
    if (esValido) {
        // Aquí enviarías el formulario al servidor
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
    errorDiv.className = 'mensaje-error';
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