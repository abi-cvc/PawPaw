<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Terminos de Uso - PawPaw</title>
    <meta name="description" content="Terminos y condiciones de uso de PawPaw. Reglas para el uso de la plataforma de identificacion de mascotas.">
    <meta name="robots" content="index, follow">
    <link rel="canonical" href="${pageContext.request.contextPath}/terms">
    <meta property="og:type" content="website">
    <meta property="og:title" content="Terminos de Uso - PawPaw">
    <meta property="og:description" content="Terminos y condiciones de uso de la plataforma PawPaw.">
    <meta property="og:image" content="${pageContext.request.contextPath}/images/logo.png">
    <meta property="og:locale" content="es_EC">
    <meta property="og:site_name" content="PawPaw">
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
                <span class="static-hero-tag">Condiciones del servicio</span>
                <h1 class="static-hero-title">Terminos de Uso</h1>
                <p class="static-hero-subtitle">
                    Ultima actualizacion: 16 de marzo de 2026
                </p>
            </div>
        </section>

        <section class="static-content">
            <div class="static-container static-legal">

                <div class="static-section">
                    <h2>1. Aceptacion de los terminos</h2>
                    <p>
                        Al acceder, registrarse o utilizar la plataforma PawPaw ("la Plataforma"), usted acepta
                        de forma <strong>libre, voluntaria e informada</strong> quedar vinculado por los presentes
                        Terminos de Uso. Si no esta de acuerdo con alguna de estas condiciones, le solicitamos
                        que no utilice la Plataforma.
                    </p>
                    <p>
                        El uso de la Plataforma implica la aceptacion integra de estos terminos, asi como de
                        nuestra <a href="${pageContext.request.contextPath}/privacy">Politica de Privacidad</a>,
                        que forma parte integrante de estos Terminos de Uso.
                    </p>
                </div>

                <div class="static-section">
                    <h2>2. Descripcion del servicio</h2>
                    <p>
                        PawPaw es una plataforma digital que permite a los usuarios:
                    </p>
                    <ul>
                        <li>Crear perfiles digitales para sus mascotas</li>
                        <li>Generar codigos QR unicos vinculados a dichos perfiles</li>
                        <li>Facilitar el contacto entre quien encuentra una mascota y su dueno</li>
                        <li>Gestionar la visibilidad y adopcion de animales (para fundaciones aliadas)</li>
                        <li>Adquirir slots adicionales para registrar mas mascotas</li>
                    </ul>
                    <p>
                        PawPaw es una <strong>herramienta de identificacion digital</strong>. No constituye un
                        servicio de busqueda activa, seguro, ni garantia de recuperacion de mascotas perdidas.
                    </p>
                </div>

                <div class="static-section">
                    <h2>3. Requisitos para el uso</h2>
                    <ul>
                        <li>
                            <strong>Edad minima:</strong> Debe ser mayor de dieciocho (18) anos para crear una cuenta.
                            Los menores de edad podran usar la Plataforma unicamente bajo la supervision y con el
                            consentimiento expreso de su representante legal, quien asumira toda responsabilidad
                            derivada del uso.
                        </li>
                        <li>
                            <strong>Informacion veraz:</strong> Usted se compromete a proporcionar informacion
                            verdadera, completa y actualizada al registrarse y al crear perfiles de mascotas.
                        </li>
                        <li>
                            <strong>Seguridad de la cuenta:</strong> Usted es responsable de mantener la
                            confidencialidad de sus credenciales de acceso. PawPaw no sera responsable por
                            accesos no autorizados derivados de la negligencia del usuario en la custodia
                            de su contrasena.
                        </li>
                    </ul>
                </div>

                <div class="static-section">
                    <h2>4. Uso permitido</h2>
                    <p>La Plataforma debe ser utilizada unicamente para:</p>
                    <ul>
                        <li>La gestion de mascotas propias o bajo custodia legitima del usuario</li>
                        <li>La comunicacion de buena fe entre usuarios respecto a mascotas perdidas o en adopcion</li>
                        <li>Las actividades de fundaciones aliadas debidamente aprobadas por PawPaw</li>
                    </ul>

                    <h3>Queda expresamente prohibido:</h3>
                    <ul>
                        <li>Registrar mascotas que no sean de su propiedad o custodia</li>
                        <li>Proporcionar informacion falsa o engaanosa en los perfiles</li>
                        <li>Utilizar la Plataforma para actividades ilegales, fraudulentas o que contravengan las leyes ecuatorianas</li>
                        <li>Intentar acceder a cuentas ajenas o a areas restringidas de la Plataforma</li>
                        <li>Subir contenido ofensivo, violento, inapropiado o que vulnere derechos de terceros</li>
                        <li>Utilizar la informacion de contacto obtenida via QR para fines distintos a la devolucion o contacto respecto a la mascota</li>
                        <li>Realizar scraping, ingenieria inversa o cualquier actividad que comprometa la integridad tecnica de la Plataforma</li>
                    </ul>
                </div>

                <div class="static-section">
                    <h2>5. Contenido del usuario</h2>
                    <p>
                        Usted es el unico responsable del contenido que publica en la Plataforma, incluyendo
                        fotografias, datos de mascotas e informacion de contacto.
                    </p>
                    <p>
                        PawPaw se reserva el derecho de <strong>eliminar, suspender o modificar</strong> cualquier
                        contenido que considere inapropiado, falso, ofensivo o que incumpla estos Terminos de Uso,
                        sin necesidad de notificacion previa.
                    </p>
                    <p>
                        Al publicar contenido en la Plataforma, usted otorga a PawPaw una licencia no exclusiva,
                        gratuita y mundial para mostrar dicho contenido en el contexto del servicio (perfiles
                        publicos, pagina de fundaciones, codigos QR).
                    </p>
                </div>

                <div class="static-section">
                    <h2>6. Fundaciones aliadas</h2>
                    <p>
                        Las fundaciones que soliciten ser aliadas de PawPaw estan sujetas a un proceso de revision
                        y aprobacion por parte de nuestro equipo. PawPaw se reserva el derecho de:
                    </p>
                    <ul>
                        <li>Aprobar o rechazar solicitudes de fundaciones a su discrecion</li>
                        <li>Ocultar el perfil publico de una fundacion si se detectan irregularidades, con la debida notificacion</li>
                        <li>Revocar el estatus de fundacion aliada en caso de incumplimiento de estos terminos</li>
                    </ul>
                </div>

                <div class="static-section">
                    <h2>7. Pagos y slots adicionales</h2>
                    <p>
                        PawPaw ofrece la posibilidad de adquirir slots adicionales para registrar mas mascotas.
                        Los pagos son procesados a traves de <strong>PayPal</strong>. PawPaw no almacena datos
                        de tarjetas de credito o debito.
                    </p>
                    <ul>
                        <li>Los precios estan expresados en dolares estadounidenses (USD)</li>
                        <li>Los pagos realizados no son reembolsables, salvo en casos excepcionales evaluados por nuestro equipo</li>
                        <li>Los slots adquiridos son de uso personal e intransferible</li>
                    </ul>
                </div>

                <div class="static-section">
                    <h2>8. Limitacion de responsabilidad</h2>
                    <p>
                        PawPaw proporciona la Plataforma <strong>"tal como esta"</strong> y <strong>"segun
                        disponibilidad"</strong>. En la maxima medida permitida por la legislacion ecuatoriana:
                    </p>
                    <ul>
                        <li>
                            <strong>No garantizamos</strong> que la Plataforma estara disponible de forma
                            ininterrumpida, libre de errores o completamente segura. Pueden existir periodos
                            de mantenimiento o interrupciones tecnicas.
                        </li>
                        <li>
                            <strong>No somos responsables</strong> por la perdida o no recuperacion de mascotas.
                            PawPaw es una herramienta de identificacion, no un servicio de busqueda ni un seguro.
                        </li>
                        <li>
                            <strong>No somos responsables</strong> por el uso indebido que terceros hagan de la
                            informacion publica accesible mediante el escaneo de codigos QR.
                        </li>
                        <li>
                            <strong>No somos responsables</strong> por danos indirectos, incidentales o
                            consecuentes derivados del uso o la imposibilidad de uso de la Plataforma.
                        </li>
                        <li>
                            <strong>No somos responsables</strong> por la veracidad de la informacion proporcionada
                            por los usuarios o fundaciones en sus perfiles.
                        </li>
                    </ul>
                </div>

                <div class="static-section">
                    <h2>9. Suspension y terminacion de cuentas</h2>
                    <p>
                        PawPaw se reserva el derecho de suspender o eliminar cuentas de usuario en los
                        siguientes casos:
                    </p>
                    <ul>
                        <li>Incumplimiento de estos Terminos de Uso</li>
                        <li>Publicacion de informacion falsa o contenido inapropiado</li>
                        <li>Uso fraudulento o malintencionado de la Plataforma</li>
                        <li>Solicitud del propio usuario</li>
                        <li>Requerimiento de autoridad competente</li>
                    </ul>
                    <p>
                        En caso de suspension o eliminacion, PawPaw notificara al usuario al correo
                        electronico registrado, salvo en casos que requieran accion inmediata por razones
                        de seguridad o por orden judicial.
                    </p>
                </div>

                <div class="static-section">
                    <h2>10. Propiedad intelectual</h2>
                    <p>
                        Todo el contenido de la Plataforma (diseno, logotipos, textos, codigo fuente, graficos)
                        es propiedad de PawPaw o se utiliza con la debida autorizacion, y esta protegido por
                        las leyes de propiedad intelectual de la Republica del Ecuador y tratados internacionales
                        aplicables.
                    </p>
                    <p>
                        Queda prohibida la reproduccion, distribucion o modificacion total o parcial del contenido
                        de la Plataforma sin autorizacion expresa y por escrito de PawPaw.
                    </p>
                </div>

                <div class="static-section">
                    <h2>11. Modificaciones a los terminos</h2>
                    <p>
                        PawPaw se reserva el derecho de modificar estos Terminos de Uso en cualquier momento.
                        Las modificaciones seran publicadas en esta pagina con la fecha de ultima actualizacion.
                    </p>
                    <p>
                        El uso continuado de la Plataforma despues de la publicacion de cambios constituye su
                        aceptacion de los terminos modificados. Si no esta de acuerdo con las modificaciones,
                        debera dejar de utilizar la Plataforma y solicitar la eliminacion de su cuenta.
                    </p>
                </div>

                <div class="static-section">
                    <h2>12. Legislacion aplicable y resolucion de controversias</h2>
                    <p>
                        Los presentes Terminos de Uso se rigen por las leyes de la Republica del Ecuador.
                    </p>
                    <p>
                        En caso de controversia, las partes se comprometen a intentar resolverla de forma amistosa
                        en primera instancia. De no lograrse un acuerdo, las controversias seran sometidas a los
                        jueces y tribunales competentes del domicilio de PawPaw en la Republica del Ecuador,
                        conforme a la legislacion vigente.
                    </p>
                </div>

                <div class="static-section">
                    <h2>13. Contacto</h2>
                    <p>
                        Para consultas sobre estos Terminos de Uso, puede escribirnos a:
                    </p>
                    <ul>
                        <li><strong>Correo:</strong> pawpawsystem@gmail.com</li>
                    </ul>
                </div>

            </div>
        </section>
    </main>

    <jsp:include page="/view/components/footer.jsp" />

</body>
</html>
