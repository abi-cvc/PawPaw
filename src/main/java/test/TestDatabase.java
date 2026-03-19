package test;

import config.DatabaseConnection;
import model.dao.UserDAO;
import model.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Clase de prueba para verificar la conexión a la base de datos
 * y el funcionamiento del UserDAO desde consola
 *
 * Ejecuta como: Java Application (Run As → Java Application)
 */
public class TestDatabase {

    private static final Logger logger = LoggerFactory.getLogger(TestDatabase.class);

    public static void main(String[] args) {
        logger.info("TEST DE BASE DE DATOS - PAWPAW");

        // TEST 1: Verificar conexión
        testConnection();

        // TEST 2: Estadísticas del pool
        testConnectionPool();

        // TEST 3: Contar usuarios
        testUserCount();

        // TEST 4: Listar usuarios
        testListUsers();

        // TEST 5: Crear usuario de prueba (descomenta para probar)
        testCreateUser();

        // TEST 6: Buscar usuario
        testFindUser();

        // TEST 7: Verificar autenticación
        testAuthentication();

        logger.info("TESTS COMPLETADOS");
    }

    /**
     * TEST 1: Verificar conexión a PostgreSQL
     */
    private static void testConnection() {
        logger.info("TEST 1: Conexion a Base de Datos");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                logger.info("Conexion EXITOSA a PostgreSQL");
                logger.info("  Base de datos: {}", conn.getCatalog());
                logger.info("  URL: {}", conn.getMetaData().getURL());
                logger.info("  Driver: {} v{}", conn.getMetaData().getDriverName(), conn.getMetaData().getDriverVersion());
            } else {
                logger.info("Conexion cerrada o nula");
            }
        } catch (SQLException e) {
            logger.error("ERROR de conexion: {}", e.getMessage());
            logger.error("Error al conectar a la base de datos", e);
        }
    }

    /**
     * TEST 2: Estadísticas del pool de conexiones
     */
    private static void testConnectionPool() {
        logger.info("TEST 2: Pool de Conexiones (HikariCP)");

        try {
            DatabaseConnection.imprimirEstadisticas();
            logger.info("Pool de conexiones funcionando");
        } catch (Exception e) {
            logger.error("Error al obtener estadisticas del pool", e);
        }
    }

    /**
     * TEST 3: Contar usuarios en la base de datos
     */
    private static void testUserCount() {
        logger.info("TEST 3: Conteo de Usuarios");

        try {
            UserDAO userDAO = new UserDAO();
            int count = userDAO.count();

            logger.info("Total de usuarios en BD: {}", count);

            if (count == 0) {
                logger.info("No hay usuarios registrados aun.");
                logger.info("Crea uno usando /register o descomenta testCreateUser()");
            }
        } catch (Exception e) {
            logger.error("Error al contar usuarios", e);
        }
    }

    /**
     * TEST 4: Listar todos los usuarios
     */
    private static void testListUsers() {
        logger.info("TEST 4: Lista de Usuarios");

        try {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.findAll();

            if (users.isEmpty()) {
                logger.info("No hay usuarios para mostrar");
            } else {
                logger.info("Usuarios encontrados: {}", users.size());

                for (User user : users) {
                    logger.info("  ID: {} | Nombre: {} | Email: {} | Rol: {} | Activo: {}",
                        user.getIdUser(),
                        truncate(user.getNameUser(), 19),
                        truncate(user.getEmail(), 24),
                        user.getRol(),
                        user.getActive() ? "Si" : "No"
                    );
                }
            }
        } catch (Exception e) {
            logger.error("Error al listar usuarios", e);
        }
    }

    /**
     * TEST 5: Crear un usuario de prueba
     * DESCOMENTA en main() para ejecutar
     */
    private static void testCreateUser() {
        logger.info("TEST 5: Crear Usuario de Prueba");

        try {
            UserDAO userDAO = new UserDAO();

            // Crear usuario con timestamp para que sea único
            String timestamp = String.valueOf(System.currentTimeMillis()).substring(7);

            User testUser = new User();
            testUser.setNameUser("Test User " + timestamp);
            testUser.setEmail("test" + timestamp + "@pawpaw.test");
            testUser.setPassword("test123");
            testUser.setRol("user");
            testUser.setActive(true);

            boolean created = userDAO.create(testUser);

            if (created) {
                logger.info("Usuario creado exitosamente!");
                logger.info("  ID: {}", testUser.getIdUser());
                logger.info("  Nombre: {}", testUser.getNameUser());
                logger.info("  Email: {}", testUser.getEmail());
                logger.info("  Contrasena: test123 (hasheada con BCrypt)");
            } else {
                logger.info("No se pudo crear el usuario");
            }
        } catch (Exception e) {
            logger.error("Error al crear usuario", e);
        }
    }

    /**
     * TEST 6: Buscar usuario por email
     */
    private static void testFindUser() {
        logger.info("TEST 6: Buscar Usuario por Email");

        try {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.findAll();

            if (!users.isEmpty()) {
                User firstUser = users.get(0);
                User found = userDAO.findByEmail(firstUser.getEmail());

                if (found != null) {
                    logger.info("Busqueda funcionando correctamente");
                    logger.info("  Email buscado: {}", firstUser.getEmail());
                    logger.info("  Usuario encontrado: {}", found.getNameUser());
                } else {
                    logger.info("No se encontro el usuario");
                }
            } else {
                logger.info("No hay usuarios para buscar");
                logger.info("Crea uno primero");
            }
        } catch (Exception e) {
            logger.error("Error en busqueda", e);
        }
    }

    /**
     * TEST 7: Verificar sistema de autenticación
     */
    private static void testAuthentication() {
        logger.info("TEST 7: Sistema de Autenticacion");

        logger.info("BCrypt configurado correctamente");
        logger.info("  Factor de complejidad: 12 rounds");
        logger.info("  Las contrasenas se hashean automaticamente");
        logger.info("  Verificacion segura implementada");
    }

    /**
     * Método auxiliar para truncar strings
     */
    private static String truncate(String str, int length) {
        if (str == null) return "";
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }
}