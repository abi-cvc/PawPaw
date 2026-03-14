<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PawPaw - La seguridad de tu mascota</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

    <!-- ==================== HEADER ==================== -->
    <header class="header">
        <nav class="navbar">
            <div class="navbar-contenedor">
                <div class="navbar-logo">
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo PawPaw" class="logo-img">
                    <span class="logo-texto">PawPaw</span>
                </div>
                <div class="navbar-botones">
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-secundario">Iniciar sesión</a>
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-primario">Crear cuenta</a>
                </div>
            </div>
        </nav>
    </header>

    <main>

        <!-- ========== HERO ========== -->
        <section class="hero">
            <div class="hero-contenedor">

                <div class="hero-contenido">
                    <h1 class="hero-titulo">La seguridad de tu mascota, a un escaneo de distancia</h1>
                    <p class="hero-descripcion">
                        Crea un perfil digital para tu mejor amigo y ayuda a que siempre encuentre el camino a casa.
                        Con un simple código QR, cualquier persona puede contactarte si tu mascota se pierde.
                    </p>
                    <div class="hero-botones">
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-primario btn-grande">Crear cuenta gratis</a>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-grande">Iniciar sesión</a>
                    </div>
                    <div class="hero-confianza">
                        <div class="confianza-item">
                            <span class="confianza-icono">✓</span>
                            <span class="confianza-texto">100% Seguro</span>
                        </div>
                        <div class="confianza-item">
                            <span class="confianza-icono">✓</span>
                            <span class="confianza-texto">Configuración en minutos</span>
                        </div>
                        <div class="confianza-item">
                            <span class="confianza-icono">✓</span>
                            <span class="confianza-texto">Sin costo mensual</span>
                        </div>
                    </div>
                </div>

                <!-- Imagen placas -->
                <div class="hero-imagen">
                    <img src="${pageContext.request.contextPath}/images/placas.png"
                         alt="Placas QR PawPaw para mascotas"
                         class="hero-placas-img">
                </div>

            </div>
        </section>

        <!-- ========== CÓMO FUNCIONA ========== -->
        <section class="como-funciona">
            <div class="seccion-contenedor">
                <div class="seccion-encabezado">
                    <h2 class="seccion-titulo">¿Cómo funciona?</h2>
                    <p class="seccion-subtitulo">Cuatro pasos simples para proteger a tu mascota</p>
                </div>
                <div class="pasos-grid">
                    <div class="paso-card">
                        <div class="paso-numero">1</div>
                        <div class="paso-icono">
                            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
                                <circle cx="12" cy="7" r="4"></circle>
                            </svg>
                        </div>
                        <h3 class="paso-titulo">Crea el perfil</h3>
                        <p class="paso-descripcion">Registra los datos de tu mascota: nombre, foto, información médica y tus datos de contacto.</p>
                    </div>
                    <div class="paso-card">
                        <div class="paso-numero">2</div>
                        <div class="paso-icono">
                            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <rect x="3" y="3" width="18" height="18" rx="2"></rect>
                                <rect x="7" y="7" width="3" height="3"></rect>
                                <rect x="14" y="7" width="3" height="3"></rect>
                                <rect x="7" y="14" width="3" height="3"></rect>
                                <rect x="14" y="14" width="3" height="3"></rect>
                            </svg>
                        </div>
                        <h3 class="paso-titulo">Genera el QR</h3>
                        <p class="paso-descripcion">El sistema crea automáticamente un código QR único vinculado al perfil de tu mascota.</p>
                    </div>
                    <div class="paso-card">
                        <div class="paso-numero">3</div>
                        <div class="paso-icono">
                            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <circle cx="12" cy="12" r="10"></circle>
                                <path d="M12 6v6l4 2"></path>
                            </svg>
                        </div>
                        <h3 class="paso-titulo">Colócalo en su placa</h3>
                        <p class="paso-descripcion">Descarga e imprime el QR, o solicita una placa física con el código grabado.</p>
                    </div>
                    <div class="paso-card">
                        <div class="paso-numero">4</div>
                        <div class="paso-icono">
                            <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
                            </svg>
                        </div>
                        <h3 class="paso-titulo">Contacto instantáneo</h3>
                        <p class="paso-descripcion">Cuando alguien escanea el QR, puede ver el perfil público y contactarte de inmediato.</p>
                    </div>
                </div>
            </div>
        </section>

        <!-- ========== CARACTERÍSTICAS ========== -->
        <section class="confianza">
            <div class="seccion-contenedor">
                <div class="seccion-encabezado">
                    <h2 class="seccion-titulo">Diseñado pensando en ti y tu mascota</h2>
                </div>
                <div class="caracteristicas-grid">
                    <div class="caracteristica-card">
                        <div class="caracteristica-icono">
                            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
                                <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                            </svg>
                        </div>
                        <h3 class="caracteristica-titulo">Cuentas seguras</h3>
                        <p class="caracteristica-descripcion">Tus datos y los de tu mascota están protegidos con tecnología de encriptación.</p>
                    </div>
                    <div class="caracteristica-card">
                        <div class="caracteristica-icono">
                            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <circle cx="12" cy="12" r="10"></circle>
                                <polyline points="12 6 12 12 16 14"></polyline>
                            </svg>
                        </div>
                        <h3 class="caracteristica-titulo">QR instantáneo</h3>
                        <p class="caracteristica-descripcion">Genera tu código QR en segundos y empieza a proteger a tu mascota hoy mismo.</p>
                    </div>
                    <div class="caracteristica-card">
                        <div class="caracteristica-icono">
                            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M12 2v20M2 12h20"></path>
                                <circle cx="12" cy="12" r="4"></circle>
                            </svg>
                        </div>
                        <h3 class="caracteristica-titulo">Gestión fácil</h3>
                        <p class="caracteristica-descripcion">Actualiza la información de tu mascota cuando quieras desde cualquier dispositivo.</p>
                    </div>
                    <div class="caracteristica-card">
                        <div class="caracteristica-icono">
                            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                                <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
                                <circle cx="9" cy="7" r="4"></circle>
                                <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
                                <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
                            </svg>
                        </div>
                        <h3 class="caracteristica-titulo">Para toda la familia</h3>
                        <p class="caracteristica-descripcion">Crea perfiles para todas tus mascotas.</p>
                    </div>
                </div>
            </div>
        </section>

        <!-- ========== SECCIÓN FUNDACIONES ========== -->
        <section class="fundaciones-seccion">
            <div class="seccion-contenedor">

                <div class="seccion-encabezado">
                    <span class="fundaciones-etiqueta">🏡 Para fundaciones y rescatistas</span>
                    <h2 class="seccion-titulo">Dale visibilidad a tus animales rescatados</h2>
                    <p class="seccion-subtitulo">
                        Si tienes una fundación o rescatas animales, PawPaw te da herramientas especiales
                        para conectar a tus mascotas con familias que las están esperando.
                    </p>
                </div>

                <div class="fundacion-beneficios-grid">

                    <div class="fundacion-beneficio-card">
                        <div class="fundacion-beneficio-icono">♾️</div>
                        <h3 class="fundacion-beneficio-titulo">Slots ilimitados</h3>
                        <p class="fundacion-beneficio-desc">
                            Registra todos los animales que cuidas sin restricciones,
                            sin límite de mascotas y sin costo adicional.
                        </p>
                    </div>

                    <div class="fundacion-beneficio-card">
                        <div class="fundacion-beneficio-icono">🌐</div>
                        <h3 class="fundacion-beneficio-titulo">Página pública propia</h3>
                        <p class="fundacion-beneficio-desc">
                            Tu fundación tendrá una página en PawPaw donde cualquier persona
                            puede ver tus animales disponibles para adopción.
                        </p>
                    </div>

                    <div class="fundacion-beneficio-card">
                        <div class="fundacion-beneficio-icono">📲</div>
                        <h3 class="fundacion-beneficio-titulo">QR que sigue al animal</h3>
                        <p class="fundacion-beneficio-desc">
                            Cuando un animal es adoptado, el QR se transfiere automáticamente
                            al nuevo dueño. La placa no cambia, el perfil sí.
                        </p>
                    </div>

                    <div class="fundacion-beneficio-card">
                        <div class="fundacion-beneficio-icono">🤝</div>
                        <h3 class="fundacion-beneficio-titulo">Badge de partner verificado</h3>
                        <p class="fundacion-beneficio-desc">
                            Apareces verificado en el directorio de aliados PawPaw,
                            generando confianza en los potenciales adoptantes.
                        </p>
                    </div>

                    <div class="fundacion-beneficio-card">
                        <div class="fundacion-beneficio-icono">📬</div>
                        <h3 class="fundacion-beneficio-titulo">Transferencia por email</h3>
                        <p class="fundacion-beneficio-desc">
                            Inicia la adopción con solo el email del adoptante.
                            Ellos reciben un enlace seguro para confirmar.
                        </p>
                    </div>

                    <!-- Tarjeta CTA -->
                    <div class="fundacion-beneficio-card fundacion-beneficio-cta">
                        <div class="fundacion-beneficio-icono">🐾</div>
                        <h3 class="fundacion-beneficio-titulo">¿Tienes una fundación?</h3>
                        <p class="fundacion-beneficio-desc">
                            Únete como fundación aliada y dale visibilidad a tus animales hoy mismo.
                            El proceso es simple y gratuito.
                        </p>
                        <a href="${pageContext.request.contextPath}/foundation/apply" class="btn btn-primario">
                            Enviar solicitud →
                        </a>
                    </div>

                </div>

                <!-- Banner secundario para ver fundaciones existentes -->
                <div class="fundaciones-banner-secundario">
                    <p>¿Quieres conocer las fundaciones que ya forman parte de PawPaw?</p>
                    <a href="${pageContext.request.contextPath}/foundations/public" class="btn btn-secundario btn-sm">
                        Ver fundaciones aliadas →
                    </a>
                </div>

            </div>
        </section>

        <!-- ========== CTA FINAL ========== -->
        <section class="cta-final">
            <div class="cta-contenedor">
                <h2 class="cta-titulo">¿Listo para proteger a tu mejor amigo?</h2>
                <p class="cta-subtitulo">Únete a miles de familias que ya confían en PawPaw</p>
                <div class="cta-botones">
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-primario btn-grande">Comenzar ahora</a>
                    <a href="${pageContext.request.contextPath}/foundations/public" class="btn btn-outline-claro btn-grande">Ver fundaciones aliadas</a>
                </div>
            </div>
        </section>

    </main>

    <!-- ==================== FOOTER ==================== -->
    <footer class="footer">
        <div class="footer-contenedor">
            <div class="footer-contenido">
                <div class="footer-brand">
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo PawPaw" class="footer-logo-img">
                    <span class="footer-logo-texto">PawPaw</span>
                </div>
                <nav class="footer-links">
                    <a href="${pageContext.request.contextPath}/about" class="footer-link">Sobre nosotros</a>
                    <a href="${pageContext.request.contextPath}/contact" class="footer-link">Contacto</a>
                    <a href="${pageContext.request.contextPath}/privacy" class="footer-link">Política de privacidad</a>
                    <a href="${pageContext.request.contextPath}/terms" class="footer-link">Términos de uso</a>
                    <a href="${pageContext.request.contextPath}/foundations/public" class="footer-link">🏡 Fundaciones Aliadas</a>
                    <a href="${pageContext.request.contextPath}/foundation/apply" class="footer-link">Ser fundación aliada</a>
                </nav>
            </div>
            <div class="footer-bottom">
                <p class="footer-copyright">© 2024 PawPaw. Todos los derechos reservados.</p>
            </div>
        </div>
    </footer>

</body>
</html>
