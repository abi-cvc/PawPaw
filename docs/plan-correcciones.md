# Plan de Correcciones - PawPaw

## Qué se hizo

### Fase 0: Documentación
- Creado `memory/database-schema.md` con schema completo de Neon (13 tablas)
- Creado `estructura.md` con estructura limpia del proyecto
- Creado `docs/` con documentación de auditoría y planes

### Fase 1: Emergencia
- **DatabaseConnection.java**: Eliminadas credenciales hardcodeadas, fail-fast si no hay env vars
- **PayPalConfig.java**: Refactorizado para leer de env vars (PAYPAL_MODE, PAYPAL_CLIENT_ID, PAYPAL_SECRET)
- **PetDAO.java**: Reescrito create() y update() con columnas reales de BD (name_pet, age_pet, breed, sex_pet, medical_conditions, contact_phone, photo, status_pet, extra_comments, available_for_adoption, adoption_description, adoption_status)
- **Pet.java**: Agregados campos availableForAdoption y adoptionDescription
- **ScanLog.java + ScanLogDAO.java**: Creados para tabla scan_logs existente

### Fase 2: Seguridad Crítica
- **CsrfFilter.java**: Token CSRF por sesión, validación en POSTs
- **AuthFilter.java**: Autorización centralizada para /user/* y /admin/*
- **SecurityHeadersFilter.java**: Headers X-Content-Type-Options, X-Frame-Options, X-XSS-Protection
- **AuthenticationController.java**: Protección contra session fixation
- **PetController.java**: Delete solo via POST, validación Cloudinary URL
- **web.xml**: Cookie secure=true, HTTPS constraint habilitada

### Fase 3: Correcciones adicionales
- **CreatePayPalOrderServlet.java**: Precio calculado server-side
- **EmailService.java**: JSON construido con Gson en vez de concatenación
- **UserDAO.java + ResetPasswordServlet.java**: Corregido typo ressetPassword → resetPassword
- **PetDAO.java + QRCodeDAO.java**: Métodos count() agregados
- **pom.xml**: Maven compiler release 17 → 21
- **.gitignore**: Expandido con IDE, OS, env files

## Cómo verificar

### Credenciales (SEC-001/002)
1. Verificar que `DatabaseConnection.java` no contiene contraseñas hardcodeadas
2. Verificar que `PayPalConfig.java` lee de `System.getenv()`
3. La app debe fallar con error claro si env vars no están configuradas

### PetDAO (BUG-001/002)
1. Crear una mascota nueva → verificar que se guarda en BD
2. Editar la mascota → verificar que la actualización es correcta
3. Verificar que las columnas en el SQL coinciden con las de la tabla `pets`

### CSRF (SEC-004)
1. Verificar que cada `<form>` en JSPs incluye `<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">`
2. Enviar POST sin token → debe recibir 403
3. Enviar POST con token válido → debe funcionar normalmente

### Session Fixation (SEC-008)
1. Obtener session ID antes de login
2. Hacer login
3. Verificar que el session ID cambió

### Delete via POST (SEC-007)
1. Intentar GET `/user/pets/delete?id=1` → debe recibir 405
2. Enviar POST con form → debe funcionar
