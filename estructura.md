# Estructura del Proyecto PawPaw

```
PawPaw/
├── pom.xml                          # Maven config (Java 21, WAR packaging)
├── Dockerfile / Procfile            # Railway deployment
├── estructura.md                    # Este archivo
│
├── docs/                            # Documentación del proyecto
│   ├── auditoria.md                 # Auditoría de seguridad completa
│   ├── plan-correcciones.md         # Plan de correcciones con verificación
│   └── tests.md                     # Plan de tests
│
├── src/main/java/
│   ├── config/
│   │   ├── DatabaseConnection.java  # HikariCP pool (env vars: DB_URL, DB_USER, DB_PASSWORD)
│   │   └── PayPalConfig.java        # PayPal config (env vars: PAYPAL_MODE, PAYPAL_CLIENT_ID, PAYPAL_SECRET)
│   │
│   ├── controller/                  # 33 Servlets
│   │   ├── AuthenticationController.java   # POST /login
│   │   ├── LogoutServlet.java              # GET /logout
│   │   ├── UserController.java             # /register
│   │   ├── UserPanelController.java        # /user/panel
│   │   ├── UserProfileController.java      # /user/profile
│   │   ├── PetController.java              # /user/pets/* (CRUD)
│   │   ├── QRCodeController.java           # /user/qr/*
│   │   ├── PublicPetController.java        # /pet/* (perfil publico QR)
│   │   ├── AdminPanelServlet.java          # /admin/panel
│   │   ├── ManageUsersServlet.java         # /admin/users
│   │   ├── ManagePaymentsServlet.java      # /admin/payments
│   │   ├── ManagePromotionsServlet.java    # /admin/promotions
│   │   ├── ManageSuggestionsServlet.java   # /admin/suggestions
│   │   ├── ManageFoundationsServlet.java   # /admin/foundations
│   │   ├── AdjustUserSlotsServlet.java     # /admin/adjust-slots
│   │   ├── UserSlotHistoryServlet.java     # /admin/slot-history
│   │   ├── CreatePayPalOrderServlet.java   # POST /api/paypal/create-order
│   │   ├── CapturePayPalOrderServlet.java  # POST /api/paypal/capture-order
│   │   ├── PurchaseSlotsServlet.java       # /user/purchase-slots
│   │   ├── ForgotPasswordServlet.java      # /forgot-password
│   │   ├── ResetPasswordServlet.java       # /reset-password
│   │   ├── ResendPasswordResetServlet.java # /resend-password-reset
│   │   ├── SendSuggestionServlet.java      # /user/send-suggestion
│   │   ├── MySuggestionsServlet.java       # /user/my-suggestions
│   │   ├── MyMessagesServlet.java          # /user/my-messages
│   │   ├── FoundationFormServlet.java      # /foundation-form
│   │   ├── FoundationProfileServlet.java   # /foundation/*
│   │   ├── PublicFoundationsServlet.java   # /foundations
│   │   ├── UpdateAdoptionStatusServlet.java # /user/update-adoption-status
│   │   ├── InitiateTransferServlet.java    # /user/initiate-transfer
│   │   ├── AcceptTransferServlet.java      # /accept-transfer
│   │   ├── DiagnosticServlet.java          # /diagnostic
│   │   └── HealthCheckServlet.java         # /health
│   │
│   ├── filter/                      # Servlet Filters
│   │   ├── StaticResourceFilter.java       # Content-Type para recursos estáticos
│   │   ├── CharacterEncodingFilter.java    # UTF-8
│   │   ├── SecurityHeadersFilter.java      # X-Content-Type-Options, X-Frame-Options
│   │   ├── AuthFilter.java                 # Autorización /user/*, /admin/*
│   │   └── CsrfFilter.java                # Protección CSRF en POSTs
│   │
│   ├── model/
│   │   ├── entity/                  # 13 Entidades (mapean a tablas BD)
│   │   │   ├── User.java
│   │   │   ├── Pet.java
│   │   │   ├── QRcode.java
│   │   │   ├── ScanLog.java
│   │   │   ├── PasswordResetToken.java
│   │   │   ├── PetContactMessage.java
│   │   │   ├── Suggestion.java
│   │   │   ├── PaymentRequest.java
│   │   │   ├── Promotion.java
│   │   │   ├── AdminAuditLog.java
│   │   │   ├── SlotAdjustment.java
│   │   │   ├── FoundationRequest.java
│   │   │   └── PetTransferRequest.java
│   │   │
│   │   └── dao/                     # 13 DAOs (JDBC + PreparedStatements)
│   │       ├── UserDAO.java
│   │       ├── PetDAO.java
│   │       ├── QRCodeDAO.java
│   │       ├── ScanLogDAO.java
│   │       ├── PasswordResetTokenDAO.java
│   │       ├── PetContactMessageDAO.java
│   │       ├── SuggestionDAO.java
│   │       ├── PaymentRequestDAO.java
│   │       ├── PromotionDAO.java
│   │       ├── AdminAuditLogDAO.java
│   │       ├── SlotAdjustmentDAO.java
│   │       ├── FoundationRequestDAO.java
│   │       └── PetTransferRequestDAO.java
│   │
│   └── service/
│       └── EmailService.java        # Brevo HTTP API (env vars: BREVO_API_KEY, BREVO_FROM_EMAIL, etc.)
│
├── src/main/webapp/
│   ├── WEB-INF/
│   │   └── web.xml                  # Servlet config, filters, session, security
│   │
│   ├── css/                         # Estilos
│   ├── js/
│   │   └── main.js                  # Cloudinary upload, PayPal SDK
│   ├── img/                         # Imágenes estáticas
│   │
│   └── view/                        # 32 JSPs
│       ├── index.jsp                # Landing page
│       ├── admin/                   # 7 vistas admin
│       ├── internalUser/            # 13 vistas usuario autenticado
│       ├── externalUser/            # 2 vistas perfil público mascota
│       ├── public/                  # 6 vistas fundaciones y transferencias
│       ├── foundations/             # 2 vistas formulario fundaciones
│       └── error/                   # 404.jsp, 500.jsp
│
└── src/main/resources/
    └── logback.xml                  # Logging config (SLF4J/Logback)
```

## Variables de Entorno Requeridas

| Variable | Descripción |
|----------|-------------|
| `DB_URL` | JDBC URL de PostgreSQL (Neon) |
| `DB_USER` | Usuario de BD |
| `DB_PASSWORD` | Contraseña de BD |
| `PAYPAL_MODE` | sandbox / live |
| `PAYPAL_CLIENT_ID` | PayPal Client ID |
| `PAYPAL_SECRET` | PayPal Secret |
| `BREVO_API_KEY` | API key de Brevo para emails |
| `BREVO_FROM_EMAIL` | Email remitente |
| `BREVO_FROM_NAME` | Nombre remitente |
| `APP_BASE_URL` | URL base de la app (ej: https://pawpaw.up.railway.app) |

## Stack Tecnológico
- **Backend**: Java 21, Jakarta EE (Servlets/JSP), DAO pattern
- **BD**: PostgreSQL (Neon), HikariCP pool
- **Frontend**: JSP + JSTL, CSS, JavaScript vanilla
- **Email**: Brevo HTTP API
- **Pagos**: PayPal REST API
- **Imágenes**: Cloudinary (unsigned uploads)
- **Deploy**: Railway (Docker/Tomcat embebido)
- **Seguridad**: BCrypt (costo 12), CSRF tokens, session management
