# PawPaw - Sistema de Identificacion y Proteccion de Mascotas

PawPaw es una plataforma web que permite a los duenos de mascotas crear perfiles digitales con codigos QR. Si una mascota se pierde, cualquier persona puede escanear el codigo QR y contactar al dueno al instante, sin necesidad de descargar aplicaciones ni registrarse.

**URL:** [pawpaw-wmwb.onrender.com](https://pawpaw-wmwb.onrender.com)

---

## Funcionalidades Principales

### Para usuarios
- Registro e inicio de sesion (email/password o Google OAuth 2.0)
- Crear perfiles digitales para mascotas con foto, raza, edad, condiciones medicas
- Generacion automatica de codigos QR unicos por mascota
- 2 slots gratuitos para registrar mascotas, slots adicionales por $5 USD via PayPal
- Panel de usuario con gestion de mascotas, codigos QR y mensajes recibidos
- Reportar mascota como perdida/encontrada
- Transferencia de mascotas entre usuarios (adopcion)

### Para fundaciones y rescatistas
- Formulario de solicitud para ser fundacion aliada
- Aprobacion por administrador con token de registro unico
- Slots ilimitados para registro de mascotas
- Pagina publica de la fundacion con badge "Aliados PawPaw"
- Transferencia de mascotas a adoptantes via email

### Para administradores
- Dashboard con estadisticas (usuarios, mascotas, codigos QR, donaciones)
- Gestion de usuarios (ajustar slots, toggle visibilidad de fundaciones)
- Gestion de solicitudes de fundaciones (aprobar/rechazar)
- Panel de donaciones con graficos mensuales (Chart.js)
- Gestion de sugerencias y pagos

### Pagina publica de mascota (QR)
- Perfil visible sin registro: nombre, foto, raza, edad, condiciones medicas
- Formulario de contacto para enviar mensaje al dueno
- Boton de llamada directa al dueno
- Notificacion por email al dueno cuando alguien escanea el QR
- Boton "Encontre a esta mascota" que notifica al dueno

### Donaciones
- Sistema de donaciones via PayPal (montos predefinidos o personalizados)
- Panel administrativo con estadisticas y graficos de donaciones mensuales

---

## Tecnologias

### Backend
| Tecnologia | Uso |
|------------|-----|
| Java 21 | Lenguaje principal |
| Jakarta EE 6.0 (Servlets) | Controladores y logica de negocio |
| JSP + JSTL/EL | Vistas del servidor |
| Apache Tomcat 10.1 | Servidor de aplicaciones |
| PostgreSQL | Base de datos relacional |
| HikariCP | Connection pooling |
| jBCrypt | Hashing de contrasenas |
| SLF4J + Logback | Logging |
| Gson | Procesamiento JSON |

### Frontend
| Tecnologia | Uso |
|------------|-----|
| HTML5 + CSS3 | Estructura y estilos |
| JavaScript (vanilla) | Interactividad |
| Google Fonts (Fredoka + Inter) | Tipografia |
| Chart.js | Graficos de donaciones |
| Responsive Design | Adaptable a moviles, tablets y desktop |

### Integraciones
| Servicio | Uso |
|----------|-----|
| Google OAuth 2.0 | Inicio de sesion con Google |
| PayPal SDK | Pagos (slots) y donaciones |
| Brevo SMTP | Envio de emails transaccionales |
| Cloudinary | Almacenamiento de imagenes |
| Google Search Console | SEO e indexacion |

### DevOps y Deploy
| Tecnologia | Uso |
|------------|-----|
| Docker | Contenedorizacion (multi-stage build) |
| Render | Hosting de produccion |
| Neon | PostgreSQL en la nube |
| GitHub Actions | CI/CD (build + package WAR) |
| Maven | Gestion de dependencias y build |

---

## Seguridad

- Contrasenas hasheadas con BCrypt (costo 12)
- Proteccion CSRF con tokens por sesion
- Proteccion contra Session Fixation
- Filtro de autorizacion para rutas protegidas (/user/*, /admin/*)
- Headers de seguridad (X-Frame-Options, X-Content-Type-Options, etc.)
- Rate limiting en recuperacion de contrasena
- Validacion de precios en servidor (no confiar en el cliente)
- Variables de entorno para credenciales (nunca hardcodeadas)
- Cookies HttpOnly + Secure

---

## SEO

- Meta descriptions y Open Graph en todas las paginas publicas
- Twitter Cards para compartir en redes sociales
- JSON-LD (schema.org) con datos de la organizacion
- Sitemap XML y robots.txt
- Verificacion en Google Search Console
- URLs limpias y semanticas

---

## Arquitectura

```
PawPaw/
├── src/main/java/
│   ├── config/          # DatabaseConnection, PayPalConfig
│   ├── controller/      # Servlets (Auth, Pets, QR, Donations, Google OAuth...)
│   ├── filter/          # AuthFilter, CsrfFilter, SecurityHeadersFilter...
│   ├── model/
│   │   ├── dao/         # Data Access Objects (UserDAO, PetDAO, QRCodeDAO...)
│   │   └── entity/      # Entidades (User, Pet, QRcode, Donation...)
│   └── service/         # EmailService
├── src/main/webapp/
│   ├── css/             # styles.css (responsive design)
│   ├── images/          # Logo, iconos
│   ├── js/              # JavaScript
│   ├── view/
│   │   ├── admin/       # Panel de administrador
│   │   ├── components/  # navbar.jsp, footer.jsp (reutilizables)
│   │   ├── error/       # 404.jsp, 500.jsp
│   │   ├── externalUser/# Pagina publica de mascota (QR scan)
│   │   ├── internalUser/# Login, registro, panel de usuario
│   │   └── public/      # Paginas estaticas, fundaciones, donaciones
│   ├── robots.txt
│   └── sitemap.xml
├── Dockerfile           # Multi-stage build (Maven + Tomcat)
├── pom.xml
└── .github/workflows/   # CI pipeline
```

---

## Base de Datos

PostgreSQL con 14 tablas principales:
- `users` — Usuarios (normales, fundaciones, admin)
- `pets` — Mascotas con perfil completo
- `qr_codes` — Codigos QR vinculados a mascotas
- `scan_logs` — Registro de escaneos de QR
- `pet_contact_messages` — Mensajes recibidos via QR
- `pet_transfer_requests` — Solicitudes de transferencia/adopcion
- `foundation_requests` — Solicitudes de fundaciones aliadas
- `donations` — Donaciones via PayPal
- `payments` — Pagos de slots adicionales
- `password_reset_tokens` — Tokens de recuperacion de contrasena
- `system_messages` — Mensajes del sistema a fundaciones
- Y mas...

---

## Como Ejecutar Localmente

### Requisitos
- Docker Desktop
- Cuenta en Neon (PostgreSQL) o instancia local de PostgreSQL

### Pasos

1. Clonar el repositorio:
```bash
git clone https://github.com/abi-cvc/PawPaw.git
cd PawPaw
```

2. Construir la imagen Docker:
```bash
docker build -t pawpaw .
```

3. Ejecutar con variables de entorno:
```bash
docker run -d --name pawpaw -p 9090:8080 \
  -e DB_URL="tu_jdbc_url" \
  -e DB_USER="tu_usuario" \
  -e DB_PASSWORD="tu_password" \
  -e APP_BASE_URL="http://localhost:9090/" \
  -e GOOGLE_CLIENT_ID="tu_google_client_id" \
  -e BREVO_API_KEY="tu_brevo_api_key" \
  -e BREVO_FROM_EMAIL="tu_email" \
  -e BREVO_FROM_NAME="PawPaw" \
  -e BREVO_SMTP_USERNAME="tu_email" \
  -e PAYPAL_MODE="sandbox" \
  -e PAYPAL_CLIENT_ID="tu_paypal_client_id" \
  -e PAYPAL_SECRET="tu_paypal_secret" \
  pawpaw
```

4. Abrir en el navegador: [http://localhost:9090](http://localhost:9090)

---

## Autora

**Carol Velasquez**

---

*Desarrollado con Java, desplegado con Docker en Render.*
