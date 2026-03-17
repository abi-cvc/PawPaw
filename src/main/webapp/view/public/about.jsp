<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sobre Nosotros - PawPaw</title>
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
                <span class="static-hero-tag">Nuestra historia</span>
                <h1 class="static-hero-title">Sobre PawPaw</h1>
                <p class="static-hero-subtitle">
                    Conectamos mascotas con sus familias a traves de tecnologia simple y accesible.
                </p>
            </div>
        </section>

        <section class="static-content">
            <div class="static-container">

                <div class="static-section">
                    <h2>Quienes somos</h2>
                    <p>
                        PawPaw es una plataforma ecuatoriana creada con el proposito de proteger a las mascotas
                        mediante identificacion digital. Nacimos de una idea simple: que cada mascota pueda
                        llevar consigo la informacion que necesita para volver a casa si se pierde.
                    </p>
                    <p>
                        A traves de codigos QR vinculados a perfiles digitales, cualquier persona que encuentre
                        una mascota perdida puede acceder a sus datos y contactar a su familia de forma inmediata,
                        sin necesidad de descargar aplicaciones ni registrarse.
                    </p>
                </div>

                <div class="static-section">
                    <h2>Nuestra mision</h2>
                    <p>
                        Reducir el numero de mascotas perdidas que no logran regresar a casa, brindando a cada
                        familia una herramienta digital gratuita, facil de usar y siempre disponible.
                    </p>
                </div>

                <div class="static-section">
                    <h2>Nuestra vision</h2>
                    <p>
                        Ser la plataforma de referencia en Ecuador y Latinoamerica para la identificacion
                        y proteccion digital de mascotas, construyendo una comunidad solidaria entre
                        duenos, fundaciones y ciudadanos comprometidos con el bienestar animal.
                    </p>
                </div>

                <div class="static-cards-grid">
                    <div class="static-card">
                        <div class="static-card-icon">🐾</div>
                        <h3>Identificacion QR</h3>
                        <p>Cada mascota tiene un perfil digital unico accesible con un simple escaneo desde cualquier telefono.</p>
                    </div>
                    <div class="static-card">
                        <div class="static-card-icon">🏡</div>
                        <h3>Red de fundaciones</h3>
                        <p>Fundaciones aliadas usan PawPaw para dar visibilidad a sus animales en adopcion y gestionar transferencias.</p>
                    </div>
                    <div class="static-card">
                        <div class="static-card-icon">🔒</div>
                        <h3>Seguridad y privacidad</h3>
                        <p>Datos protegidos con encriptacion. Tu decides que informacion es publica y que permanece privada.</p>
                    </div>
                    <div class="static-card">
                        <div class="static-card-icon">💚</div>
                        <h3>Gratuito</h3>
                        <p>Crear tu cuenta y registrar a tu primera mascota es completamente gratis. Sin costos ocultos.</p>
                    </div>
                </div>

                <div class="static-section">
                    <h2>Nuestro equipo</h2>
                    <p>
                        PawPaw fue desarrollado en Ecuador por un equipo comprometido con la tecnologia
                        y el bienestar animal. Creemos que la innovacion puede y debe estar al servicio
                        de quienes no pueden hablar por si mismos.
                    </p>
                </div>

            </div>
        </section>
    </main>

    <jsp:include page="/view/components/footer.jsp" />

</body>
</html>
