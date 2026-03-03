<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Solicitud Enviada - PawPaw</title>
    
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <!-- Header -->
    <header class="header">
        <nav class="navbar">
            <div class="navbar-contenedor">
                <a href="<%= request.getContextPath() %>/" class="navbar-logo">
                    <img src="<%= request.getContextPath() %>/images/logo.png" alt="PawPaw Logo" class="logo-img">
                    <span class="logo-texto">PawPaw</span>
                </a>
            </div>
        </nav>
    </header>
    
    <!-- Success Message -->
    <div class="success-container">
        <div class="success-card">
            <div class="success-icon">✅</div>
            <h1 class="success-title">¡Solicitud Enviada Exitosamente!</h1>
            <p class="success-message">
                Hemos recibido tu solicitud para convertirte en Aliado PawPaw. 
                Nuestro equipo la revisará y te contactaremos pronto al email proporcionado.
            </p>
            
            <div class="success-steps">
                <h3>Próximos pasos:</h3>
                <ol class="steps-list">
                    <li>Revisaremos tu solicitud (usualmente 24-48 horas)</li>
                    <li>Te enviaremos un email con el resultado</li>
                    <li>Si es aprobada, recibirás un link único de registro</li>
                    <li>Podrás crear tu cuenta con acceso ilimitado</li>
                </ol>
            </div>
            
            <div class="success-actions">
                <a href="<%= request.getContextPath() %>/" class="btn btn-primario">
                    Volver al Inicio
                </a>
            </div>
        </div>
    </div>
    
    <!-- Footer -->
    <footer class="footer">
        <div class="footer-contenido">
            <p class="footer-copyright">&copy; 2024 PawPaw. Todos los derechos reservados.</p>
        </div>
    </footer>
</body>
</html>
