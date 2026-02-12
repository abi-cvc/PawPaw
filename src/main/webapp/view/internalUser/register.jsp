<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Cuenta - PawPaw</title>

    <!-- Favicon -->
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <!-- CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">

    <style>
        /* Estilos específicos para el formulario de registro */
        .auth-container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 2rem 1rem;
            background: linear-gradient(135deg, #FFF9F0 0%, #FFF 100%);
        }

        .auth-card {
            background: var(--color-5);
            border-radius: var(--radio-lg);
            box-shadow: var(--sombra-lg);
            padding: 3rem;
            max-width: 450px;
            width: 100%;
        }

        .auth-header {
            text-align: center;
            margin-bottom: 2rem;
        }

        .auth-logo {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 0.5rem;
            margin-bottom: 1rem;
        }

        .auth-logo-img {
            width: 60px;
            height: 60px;
            object-fit: contain;
        }

        .auth-logo-texto {
            font-family: var(--fuente-titulos);
            font-size: 2rem;
            font-weight: 600;
            color: var(--color-2);
        }

        .auth-titulo {
            font-family: var(--fuente-titulos);
            font-size: 1.8rem;
            font-weight: 700;
            color: var(--color-2);
            margin-bottom: 0.5rem;
        }

        .auth-subtitulo {
            color: var(--color-1);
            font-size: 0.95rem;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-label {
            display: block;
            margin-bottom: 0.5rem;
            color: var(--color-2);
            font-weight: 500;
            font-size: 0.95rem;
        }

        .form-input {
            width: 100%;
            padding: 0.75rem 1rem;
            border: 2px solid rgba(136, 74, 57, 0.2);
            border-radius: var(--radio-sm);
            font-family: var(--fuente-texto);
            font-size: 1rem;
            color: var(--color-1);
            transition: var(--transicion);
        }

        .form-input:focus {
            outline: none;
            border-color: var(--color-3);
            box-shadow: 0 0 0 3px rgba(255, 194, 111, 0.1);
        }

        .form-input::placeholder {
            color: rgba(92, 51, 10, 0.5);
        }

        .alert {
            padding: 0.75rem 1rem;
            border-radius: var(--radio-sm);
            margin-bottom: 1.5rem;
            font-size: 0.9rem;
        }

        .alert-error {
            background-color: #fee;
            border: 1px solid #fcc;
            color: #c33;
        }

        .password-hint {
            font-size: 0.85rem;
            color: var(--color-1);
            opacity: 0.7;
            margin-top: 0.25rem;
        }

        .form-footer {
            margin-top: 1.5rem;
            text-align: center;
        }

        .form-link {
            color: var(--color-2);
            text-decoration: none;
            font-weight: 500;
            transition: var(--transicion);
        }

        .form-link:hover {
            color: var(--color-3);
        }

        .divider {
            text-align: center;
            margin: 1.5rem 0;
            color: var(--color-1);
            opacity: 0.6;
            font-size: 0.9rem;
        }

        .btn-full {
            width: 100%;
        }

        .terms-text {
            font-size: 0.85rem;
            color: var(--color-1);
            opacity: 0.8;
            text-align: center;
            margin-top: 1rem;
            line-height: 1.5;
        }

        @media (max-width: 640px) {
            .auth-card {
                padding: 2rem 1.5rem;
            }

            .auth-titulo {
                font-size: 1.5rem;
            }
        }
    </style>
</head>
<body>

    <div class="auth-container">
        <div class="auth-card">
            
            <!-- Header -->
            <div class="auth-header">
                <a href="<%= request.getContextPath() %>/view/index.jsp" class="auth-logo">
                    <img src="<%= request.getContextPath() %>/images/logo.png" alt="Logo PawPaw" class="auth-logo-img">
                    <span class="auth-logo-texto">PawPaw</span>
                </a>
                <h1 class="auth-titulo">Crear Cuenta</h1>
                <p class="auth-subtitulo">Comienza a proteger a tu mascota hoy</p>
            </div>

            <!-- Mensaje de error -->
            <% 
                String error = (String) request.getAttribute("error");
                if (error != null && !error.isEmpty()) {
            %>
                <div class="alert alert-error">
                    <%= error %>
                </div>
            <% } %>

            <!-- Formulario -->
            <form action="<%= request.getContextPath() %>/register" method="post">
                
                <!-- Nombre -->
                <div class="form-group">
                    <label for="name" class="form-label">Nombre completo</label>
                    <input 
                        type="text" 
                        id="name" 
                        name="name" 
                        class="form-input" 
                        placeholder="Juan Pérez"
                        value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : "" %>"
                        required
                        autocomplete="name">
                </div>

                <!-- Email -->
                <div class="form-group">
                    <label for="email" class="form-label">Email</label>
                    <input 
                        type="email" 
                        id="email" 
                        name="email" 
                        class="form-input" 
                        placeholder="tucorreo@ejemplo.com"
                        value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>"
                        required
                        autocomplete="email">
                </div>

                <!-- Contraseña -->
                <div class="form-group">
                    <label for="password" class="form-label">Contraseña</label>
                    <input 
                        type="password" 
                        id="password" 
                        name="password" 
                        class="form-input" 
                        placeholder="••••••••"
                        required
                        autocomplete="new-password"
                        minlength="6">
                    <div class="password-hint">Mínimo 6 caracteres</div>
                </div>

                <!-- Confirmar contraseña -->
                <div class="form-group">
                    <label for="confirmPassword" class="form-label">Confirmar contraseña</label>
                    <input 
                        type="password" 
                        id="confirmPassword" 
                        name="confirmPassword" 
                        class="form-input" 
                        placeholder="••••••••"
                        required
                        autocomplete="new-password"
                        minlength="6">
                </div>

                <!-- Botón de envío -->
                <button type="submit" class="btn btn-primario btn-full btn-grande">
                    Crear Cuenta
                </button>

                <!-- Términos y condiciones -->
                <p class="terms-text">
                    Al crear una cuenta, aceptas nuestros 
                    <a href="${pageContext.request.contextPath}/terms" class="form-link">Términos de uso</a> 
                    y 
                    <a href="${pageContext.request.contextPath}/privacy" class="form-link">Política de privacidad</a>
                </p>

            </form>

            <!-- Footer del formulario -->
            <div class="form-footer">
                <div class="divider">¿Ya tienes una cuenta?</div>
                <a href="<%= request.getContextPath() %>/login" class="btn btn-outline btn-full">
                    Iniciar Sesión
                </a>
            </div>

        </div>
    </div>

</body>
</html>
