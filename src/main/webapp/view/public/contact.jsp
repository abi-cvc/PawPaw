<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contacto - PawPaw</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css?v=2">
</head>
<body>

    <jsp:include page="/view/components/navbar.jsp" />

    <main>
        <section class="static-hero">
            <div class="static-hero-inner">
                <span class="static-hero-tag">Estamos para ayudarte</span>
                <h1 class="static-hero-title">Contacto</h1>
                <p class="static-hero-subtitle">
                    ¿Tienes preguntas, sugerencias o necesitas ayuda? Escribenos y te responderemos lo antes posible.
                </p>
            </div>
        </section>

        <section class="static-content">
            <div class="static-container">

                <div class="contact-grid">

                    <div class="contact-info-panel">
                        <h2>Informacion de contacto</h2>
                        <p>Puedes comunicarte con nosotros a traves de los siguientes canales:</p>

                        <div class="contact-item">
                            <div class="contact-item-icon">
                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path><polyline points="22,6 12,13 2,6"></polyline></svg>
                            </div>
                            <div>
                                <h3>Correo electronico</h3>
                                <a href="mailto:pawpawsystem@gmail.com">pawpawsystem@gmail.com</a>
                            </div>
                        </div>

                        <div class="contact-item">
                            <div class="contact-item-icon">
                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path><circle cx="12" cy="10" r="3"></circle></svg>
                            </div>
                            <div>
                                <h3>Ubicacion</h3>
                                <p>Ecuador</p>
                            </div>
                        </div>

                        <div class="contact-item">
                            <div class="contact-item-icon">
                                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg>
                            </div>
                            <div>
                                <h3>Horario de atencion</h3>
                                <p>Lunes a Viernes: 9:00 AM - 6:00 PM (ECT)</p>
                            </div>
                        </div>

                        <div class="contact-note">
                            <p>
                                Si ya tienes una cuenta, tambien puedes enviarnos sugerencias
                                directamente desde tu Dashboard en la seccion
                                <strong>"Sugerencias"</strong>.
                            </p>
                        </div>
                    </div>

                    <div class="contact-faq-panel">
                        <h2>Preguntas frecuentes</h2>

                        <div class="faq-item">
                            <h3>¿Como registro a mi mascota?</h3>
                            <p>Crea una cuenta gratuita, accede a tu Dashboard y haz clic en "Nueva Mascota". Completa los datos y el sistema generara automaticamente un codigo QR.</p>
                        </div>

                        <div class="faq-item">
                            <h3>¿El servicio es gratuito?</h3>
                            <p>Si. Crear tu cuenta y registrar a tu primera mascota es completamente gratis. Puedes adquirir slots adicionales si necesitas registrar mas mascotas.</p>
                        </div>

                        <div class="faq-item">
                            <h3>¿Como funciona el codigo QR?</h3>
                            <p>Cualquier persona puede escanear el QR de la placa de tu mascota con la camara de su telefono. Vera el perfil publico con la informacion de contacto que hayas configurado.</p>
                        </div>

                        <div class="faq-item">
                            <h3>¿Como me uno como fundacion?</h3>
                            <p>Visita la seccion <a href="${pageContext.request.contextPath}/foundation/apply">"Unirme como fundacion"</a> y completa el formulario. Nuestro equipo revisara tu solicitud y te enviara un enlace de registro.</p>
                        </div>

                        <div class="faq-item">
                            <h3>¿Que hago si olvide mi contrasena?</h3>
                            <p>En la pagina de inicio de sesion, haz clic en "¿Olvidaste tu contrasena?" e ingresa tu correo. Recibiras un enlace para restablecerla.</p>
                        </div>
                    </div>

                </div>

            </div>
        </section>
    </main>

    <jsp:include page="/view/components/footer.jsp" />

</body>
</html>
