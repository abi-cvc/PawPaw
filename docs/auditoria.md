# Auditoría de Seguridad - PawPaw

**Fecha**: 2026-03-13
**Estado general**: CRITICO → EN CORRECCIÓN

## Vulnerabilidades Encontradas y Estado

### Criticas (Corregidas)
| ID | Descripción | Archivo | Estado |
|----|-------------|---------|--------|
| SEC-001 | Credenciales BD hardcodeadas | DatabaseConnection.java | CORREGIDO - Fail-fast con env vars |
| SEC-002 | Credenciales PayPal hardcodeadas | PayPalConfig.java | CORREGIDO - Env vars |
| SEC-004 | Zero protección CSRF | Todos los forms | CORREGIDO - CsrfFilter.java |
| SEC-006 | Cookies inseguras | web.xml | CORREGIDO - secure=true, HTTPS |
| SEC-007 | Delete de mascotas via GET | PetController.java | CORREGIDO - Solo POST |
| SEC-008 | Sin protección session fixation | AuthenticationController.java | CORREGIDO - Invalidar sesión |
| SEC-009 | Sin filtro centralizado auth | — | CORREGIDO - AuthFilter.java |
| SEC-010 | Tampering precios PayPal | CreatePayPalOrderServlet.java | CORREGIDO - Precio server-side |
| SEC-011 | Sin validación URLs Cloudinary | PetController.java | CORREGIDO - Regex validation |
| SEC-012 | Inyección JSON en EmailService | EmailService.java | CORREGIDO - Gson |

### Bugs Críticos (Corregidos)
| ID | Descripción | Archivo | Estado |
|----|-------------|---------|--------|
| BUG-001 | PetDAO.create() SQL con columnas inexistentes | PetDAO.java | CORREGIDO |
| BUG-002 | PetDAO.update() mismo problema | PetDAO.java | CORREGIDO |
| BUG-003 | Tabla scan_logs sin DAO/Entity | — | CORREGIDO - ScanLog + ScanLogDAO |
| BUG-004 | available_for_adoption sin uso en código | Pet.java, PetDAO.java | CORREGIDO |

### Pendientes
| ID | Descripción | Prioridad |
|----|-------------|-----------|
| SEC-003 | Cloudinary upload preset público en JS | Media - Configurar en dashboard |
| SEC-005 | XSS en JSPs (scriptlets sin escapar) | Alta - Migrar a JSTL `<c:out>` |
| SEC-013 | Rate limiting password reset bypasseable | Media |
| SEC-014 | DiagnosticServlet sin autenticación | Media |
| SEC-015 | HealthCheck revela versión Tomcat | Baja |
| DB-002 | Sin transacciones en AcceptTransfer | Alta |
| DB-003 | N+1 queries en ManageUsers | Media |
| CQ-001 | System.out en vez de SLF4J (353 ocurrencias) | Media |
| CQ-002 | Dependencias sin usar en pom.xml | Baja |

## Aspectos Positivos
- BCrypt con costo 12 para passwords
- PreparedStatements consistentes (sin SQL injection)
- HikariCP bien configurado
- Páginas de error configuradas (404, 500)
- Env vars para Brevo (email service)

## Acciones Pendientes Post-Auditoría
1. **URGENTE**: Rotar contraseña de Neon (la vieja está en el historial de git)
2. **URGENTE**: Limpiar historial de git con `git filter-repo`
3. Configurar variables de entorno en Railway para PayPal
4. Migrar todos los JSPs de scriptlets a JSTL
5. Agregar Flyway para migraciones de BD
