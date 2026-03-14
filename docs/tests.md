# Plan de Tests - PawPaw

## Estado Actual
- No hay tests automatizados (solo `src/main/java/test/TestDatabase.java` que prueba conexión)
- No existe `src/test/java/`

## Plan de Implementación

### 1. Configurar Infraestructura de Tests
```xml
<!-- Agregar a pom.xml -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.1</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.8.0</version>
    <scope>test</scope>
</dependency>
```

### 2. Tests Prioritarios

#### Tests de DAOs (integración con BD de test)
- `PetDAOTest` — Verificar create/update/findById con columnas reales
- `UserDAOTest` — CRUD + BCrypt password verification
- `QRCodeDAOTest` — create/findByPetId
- `ScanLogDAOTest` — create/findByQrId

#### Tests de Seguridad
- `CsrfFilterTest` — POST sin token → 403, POST con token → OK
- `AuthFilterTest` — Sin sesión → redirect, con sesión → pass, admin path sin admin → 403
- `SessionFixationTest` — Session ID cambia después de login

#### Tests de Validación
- `PetControllerTest` — Cloudinary URL validation, campos obligatorios
- `CreatePayPalOrderTest` — Precio server-side vs slots

#### Tests de Servicio
- `EmailServiceTest` — JSON payload construido correctamente con Gson

### 3. Cómo Ejecutar Tests
```bash
# Ejecutar todos los tests
mvn test

# Ejecutar un test específico
mvn test -Dtest=PetDAOTest

# Ejecutar con cobertura
mvn test jacoco:report
```

### 4. Convenciones
- Directorio: `src/test/java/` (misma estructura de packages)
- Naming: `ClaseTest.java` (ej: `PetDAOTest.java`)
- Cada test es independiente (setup/teardown propios)
- Tests de BD usan base de datos de test separada (env var `DB_URL_TEST`)
