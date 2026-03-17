<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Politica de Privacidad - PawPaw</title>
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
                <span class="static-hero-tag">Tu privacidad importa</span>
                <h1 class="static-hero-title">Politica de Privacidad</h1>
                <p class="static-hero-subtitle">
                    Ultima actualizacion: 16 de marzo de 2026
                </p>
            </div>
        </section>

        <section class="static-content">
            <div class="static-container static-legal">

                <div class="static-section">
                    <h2>1. Introduccion</h2>
                    <p>
                        PawPaw ("nosotros", "nuestro" o "la Plataforma") es una plataforma digital ecuatoriana
                        dedicada a la identificacion y proteccion de mascotas mediante codigos QR y perfiles digitales.
                    </p>
                    <p>
                        La presente Politica de Privacidad describe como recopilamos, usamos, almacenamos y protegemos
                        los datos personales de nuestros usuarios, en cumplimiento con la
                        <strong>Ley Organica de Proteccion de Datos Personales del Ecuador (LOPDP)</strong>,
                        publicada en el Registro Oficial Suplemento No. 459 del 26 de mayo de 2021, y su
                        Reglamento General.
                    </p>
                    <p>
                        Al crear una cuenta o utilizar nuestros servicios, usted declara haber leido, comprendido
                        y aceptado esta politica de forma <strong>libre, voluntaria, especifica e informada</strong>,
                        conforme lo establece el articulo 7 de la LOPDP.
                    </p>
                </div>

                <div class="static-section">
                    <h2>2. Responsable del tratamiento</h2>
                    <p>
                        El responsable del tratamiento de sus datos personales es PawPaw, con domicilio en la
                        Republica del Ecuador. Para cualquier consulta relacionada con el tratamiento de sus datos,
                        puede contactarnos en:
                    </p>
                    <ul>
                        <li><strong>Correo electronico:</strong> pawpawsystem@gmail.com</li>
                    </ul>
                </div>

                <div class="static-section">
                    <h2>3. Datos personales que recopilamos</h2>
                    <p>Recopilamos los siguientes datos en funcion de su interaccion con la Plataforma:</p>

                    <h3>3.1. Datos del usuario (titular de la cuenta)</h3>
                    <ul>
                        <li>Nombre completo</li>
                        <li>Direccion de correo electronico</li>
                        <li>Contrasena (almacenada de forma encriptada mediante BCrypt)</li>
                        <li>Fecha de registro</li>
                    </ul>

                    <h3>3.2. Datos de mascotas</h3>
                    <ul>
                        <li>Nombre de la mascota</li>
                        <li>Edad, raza y sexo</li>
                        <li>Condiciones medicas</li>
                        <li>Telefono de contacto del dueno</li>
                        <li>Fotografia (almacenada en Cloudinary)</li>
                        <li>Comentarios adicionales</li>
                        <li>Estado de adopcion</li>
                    </ul>

                    <h3>3.3. Datos de fundaciones aliadas</h3>
                    <ul>
                        <li>Nombre de la fundacion y persona de contacto</li>
                        <li>Correo electronico, telefono y WhatsApp</li>
                        <li>Sitio web (opcional)</li>
                        <li>Descripcion de actividades y tipos de animales</li>
                    </ul>

                    <h3>3.4. Datos de uso y tecnicos</h3>
                    <ul>
                        <li>Direccion IP y User-Agent al escanear un codigo QR</li>
                        <li>Fecha y hora de escaneos</li>
                        <li>Datos de transacciones de pago (procesados por PayPal; PawPaw no almacena datos de tarjetas)</li>
                    </ul>
                </div>

                <div class="static-section">
                    <h2>4. Finalidad del tratamiento</h2>
                    <p>Sus datos personales son tratados para las siguientes finalidades:</p>
                    <ul>
                        <li>Creacion y gestion de su cuenta de usuario</li>
                        <li>Generacion de perfiles digitales de mascotas y codigos QR de identificacion</li>
                        <li>Facilitar el contacto entre quien encuentra una mascota y su dueno</li>
                        <li>Gestion de solicitudes de fundaciones aliadas</li>
                        <li>Procesamiento de pagos por servicios adicionales (slots)</li>
                        <li>Envio de notificaciones del sistema y correos transaccionales</li>
                        <li>Mejora continua de la Plataforma y sus funcionalidades</li>
                        <li>Cumplimiento de obligaciones legales aplicables</li>
                    </ul>
                </div>

                <div class="static-section">
                    <h2>5. Base legal del tratamiento</h2>
                    <p>
                        El tratamiento de sus datos personales se fundamenta en las siguientes bases legales,
                        conforme a los articulos 7 y 8 de la LOPDP:
                    </p>
                    <ul>
                        <li>
                            <strong>Consentimiento:</strong> Al registrarse y utilizar la Plataforma, usted otorga
                            su consentimiento libre, especifico, informado e inequivoco para el tratamiento de
                            sus datos conforme a esta politica.
                        </li>
                        <li>
                            <strong>Ejecucion contractual:</strong> El tratamiento es necesario para la prestacion
                            del servicio que usted solicita al crear una cuenta.
                        </li>
                        <li>
                            <strong>Interes legitimo:</strong> Para la mejora de la seguridad y funcionalidad
                            de la Plataforma.
                        </li>
                    </ul>
                </div>

                <div class="static-section">
                    <h2>6. Publicidad de datos mediante codigos QR</h2>
                    <p>
                        Al crear un perfil de mascota y generar un codigo QR, usted <strong>acepta y entiende</strong> que
                        la informacion asociada a dicho perfil (nombre de la mascota, fotografia, condiciones medicas,
                        telefono de contacto y comentarios) sera <strong>accesible publicamente</strong> por cualquier persona
                        que escanee el codigo QR.
                    </p>
                    <p>
                        Esta funcionalidad es el proposito central de la Plataforma y su uso es completamente
                        <strong>libre y voluntario</strong>. Usted puede modificar o eliminar la informacion publica
                        en cualquier momento desde su Dashboard.
                    </p>
                </div>

                <div class="static-section">
                    <h2>7. Transferencia internacional de datos</h2>
                    <p>
                        Para la prestacion del servicio, PawPaw utiliza proveedores tecnologicos cuyos servidores
                        pueden estar ubicados fuera de la Republica del Ecuador:
                    </p>
                    <ul>
                        <li><strong>Neon (PostgreSQL):</strong> Almacenamiento de base de datos</li>
                        <li><strong>Railway:</strong> Hospedaje de la aplicacion</li>
                        <li><strong>Cloudinary:</strong> Almacenamiento de imagenes</li>
                        <li><strong>Brevo:</strong> Envio de correos electronicos transaccionales</li>
                        <li><strong>PayPal:</strong> Procesamiento de pagos</li>
                    </ul>
                    <p>
                        Al utilizar la Plataforma, usted <strong>consiente expresamente</strong> la transferencia
                        internacional de sus datos a estos proveedores, conforme al articulo 37 de la LOPDP.
                        Estos proveedores cuentan con politicas de proteccion de datos alineadas con estandares
                        internacionales de seguridad.
                    </p>
                </div>

                <div class="static-section">
                    <h2>8. Derechos del titular de los datos (Derechos ARCO)</h2>
                    <p>
                        De conformidad con los articulos 17 al 22 de la LOPDP, usted tiene los siguientes derechos
                        sobre sus datos personales:
                    </p>
                    <ul>
                        <li><strong>Acceso:</strong> Conocer que datos personales suyos estan siendo tratados por PawPaw.</li>
                        <li><strong>Rectificacion:</strong> Solicitar la correccion de datos inexactos o incompletos.</li>
                        <li><strong>Cancelacion (Eliminacion):</strong> Solicitar la eliminacion de sus datos cuando ya no sean necesarios para la finalidad para la que fueron recopilados.</li>
                        <li><strong>Oposicion:</strong> Oponerse al tratamiento de sus datos en determinadas circunstancias.</li>
                        <li><strong>Portabilidad:</strong> Solicitar la entrega de sus datos en un formato estructurado y de uso comun.</li>
                    </ul>
                    <p>
                        Para ejercer cualquiera de estos derechos, envie un correo electronico a
                        <strong>pawpawsystem@gmail.com</strong> indicando su nombre completo, correo de registro
                        y el derecho que desea ejercer. Responderemos en un plazo maximo de quince (15) dias habiles
                        conforme lo establece la LOPDP.
                    </p>
                </div>

                <div class="static-section">
                    <h2>9. Medidas de seguridad</h2>
                    <p>PawPaw implementa las siguientes medidas tecnicas y organizativas para proteger sus datos:</p>
                    <ul>
                        <li>Contrasenas encriptadas mediante algoritmo BCrypt</li>
                        <li>Proteccion CSRF (Cross-Site Request Forgery) en todos los formularios</li>
                        <li>Cabeceras de seguridad HTTP (X-Content-Type-Options, X-Frame-Options, X-XSS-Protection)</li>
                        <li>Conexiones cifradas mediante HTTPS/SSL</li>
                        <li>Validacion del lado del servidor para todos los datos de entrada</li>
                        <li>Fijacion de sesion prevenida en el proceso de autenticacion</li>
                    </ul>
                </div>

                <div class="static-section">
                    <h2>10. Conservacion de datos</h2>
                    <p>
                        Sus datos personales seran conservados mientras su cuenta permanezca activa y sean necesarios
                        para cumplir con las finalidades descritas en esta politica. Si solicita la eliminacion de su
                        cuenta, procederemos a eliminar o anonimizar sus datos en un plazo razonable, salvo que exista
                        una obligacion legal que requiera su conservacion.
                    </p>
                </div>

                <div class="static-section">
                    <h2>11. Datos de menores de edad</h2>
                    <p>
                        PawPaw esta dirigido a personas mayores de dieciocho (18) anos. Si un menor de edad desea
                        utilizar la Plataforma, debera hacerlo bajo la supervision y con el consentimiento de su
                        representante legal, quien sera responsable del uso de la cuenta y del tratamiento de los
                        datos asociados.
                    </p>
                </div>

                <div class="static-section">
                    <h2>12. Exencion de responsabilidad</h2>
                    <p>
                        PawPaw no es responsable por el uso indebido que terceros puedan hacer de la informacion
                        publica accesible mediante el escaneo de codigos QR. La decision de hacer publica la
                        informacion de contacto a traves del perfil de la mascota es <strong>exclusiva y voluntaria
                        del usuario</strong>.
                    </p>
                    <p>
                        Asimismo, PawPaw no garantiza la recuperacion de mascotas perdidas. La Plataforma es una
                        herramienta de identificacion digital que facilita el contacto, pero no constituye un
                        seguro ni un servicio de busqueda activa.
                    </p>
                </div>

                <div class="static-section">
                    <h2>13. Modificaciones a esta politica</h2>
                    <p>
                        PawPaw se reserva el derecho de actualizar esta Politica de Privacidad en cualquier momento.
                        Las modificaciones seran publicadas en esta misma pagina con la fecha de ultima actualizacion.
                        El uso continuado de la Plataforma despues de la publicacion de cambios constituye su
                        aceptacion de los mismos.
                    </p>
                </div>

                <div class="static-section">
                    <h2>14. Legislacion aplicable y jurisdiccion</h2>
                    <p>
                        Esta Politica de Privacidad se rige por las leyes de la Republica del Ecuador, en particular
                        por la <strong>Ley Organica de Proteccion de Datos Personales (LOPDP)</strong> y su
                        Reglamento General. Cualquier controversia sera sometida a los jueces y tribunales
                        competentes del Ecuador.
                    </p>
                </div>

                <div class="static-section">
                    <h2>15. Contacto</h2>
                    <p>
                        Para cualquier consulta sobre esta Politica de Privacidad o sobre el tratamiento de sus
                        datos personales, puede escribirnos a:
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
