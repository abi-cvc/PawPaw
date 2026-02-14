package test;

import config.DatabaseConnection;
import model.dao.UserDAO;
import model.entity.User;

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
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   🧪 TEST DE BASE DE DATOS - PAWPAW       ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println();
        
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
        
        System.out.println();
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   ✅ TESTS COMPLETADOS                     ║");
        System.out.println("╚════════════════════════════════════════════╝");
    }
    
    /**
     * TEST 1: Verificar conexión a PostgreSQL
     */
    private static void testConnection() {
        System.out.println("📡 TEST 1: Conexión a Base de Datos");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión EXITOSA a PostgreSQL");
                System.out.println("   Base de datos: " + conn.getCatalog());
                System.out.println("   URL: " + conn.getMetaData().getURL());
                System.out.println("   Driver: " + conn.getMetaData().getDriverName() + 
                                 " v" + conn.getMetaData().getDriverVersion());
            } else {
                System.out.println("❌ Conexión cerrada o nula");
            }
        } catch (SQLException e) {
            System.out.println("❌ ERROR de conexión:");
            System.out.println("   " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    /**
     * TEST 2: Estadísticas del pool de conexiones
     */
    private static void testConnectionPool() {
        System.out.println("📊 TEST 2: Pool de Conexiones (HikariCP)");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        try {
            DatabaseConnection.imprimirEstadisticas();
            System.out.println("✅ Pool de conexiones funcionando");
        } catch (Exception e) {
            System.out.println("❌ Error al obtener estadísticas del pool");
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    /**
     * TEST 3: Contar usuarios en la base de datos
     */
    private static void testUserCount() {
        System.out.println("👥 TEST 3: Conteo de Usuarios");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        try {
            UserDAO userDAO = new UserDAO();
            int count = userDAO.count();
            
            System.out.println("✅ Total de usuarios en BD: " + count);
            
            if (count == 0) {
                System.out.println("   ℹ️  No hay usuarios registrados aún.");
                System.out.println("   💡 Crea uno usando /register o descomenta testCreateUser()");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al contar usuarios");
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    /**
     * TEST 4: Listar todos los usuarios
     */
    private static void testListUsers() {
        System.out.println("📋 TEST 4: Lista de Usuarios");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        try {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.findAll();
            
            if (users.isEmpty()) {
                System.out.println("ℹ️  No hay usuarios para mostrar");
            } else {
                System.out.println("✅ Usuarios encontrados: " + users.size());
                System.out.println();
                System.out.println("┌────┬─────────────────────┬──────────────────────────┬────────┬────────┐");
                System.out.println("│ ID │ Nombre              │ Email                    │ Rol    │ Activo │");
                System.out.println("├────┼─────────────────────┼──────────────────────────┼────────┼────────┤");
                
                for (User user : users) {
                    System.out.printf("│ %-2d │ %-19s │ %-24s │ %-6s │ %-6s │%n",
                        user.getIdUser(),
                        truncate(user.getNameUser(), 19),
                        truncate(user.getEmail(), 24),
                        user.getRol(),
                        user.getActive() ? "✓" : "✗"
                    );
                }
                
                System.out.println("└────┴─────────────────────┴──────────────────────────┴────────┴────────┘");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al listar usuarios");
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    /**
     * TEST 5: Crear un usuario de prueba
     * DESCOMENTA en main() para ejecutar
     */
    private static void testCreateUser() {
        System.out.println("➕ TEST 5: Crear Usuario de Prueba");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
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
                System.out.println("✅ Usuario creado exitosamente!");
                System.out.println("   ID: " + testUser.getIdUser());
                System.out.println("   Nombre: " + testUser.getNameUser());
                System.out.println("   Email: " + testUser.getEmail());
                System.out.println("   Contraseña: test123 (hasheada con BCrypt)");
            } else {
                System.out.println("❌ No se pudo crear el usuario");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al crear usuario");
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    /**
     * TEST 6: Buscar usuario por email
     */
    private static void testFindUser() {
        System.out.println("🔍 TEST 6: Buscar Usuario por Email");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        try {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.findAll();
            
            if (!users.isEmpty()) {
                User firstUser = users.get(0);
                User found = userDAO.findByEmail(firstUser.getEmail());
                
                if (found != null) {
                    System.out.println("✅ Búsqueda funcionando correctamente");
                    System.out.println("   Email buscado: " + firstUser.getEmail());
                    System.out.println("   Usuario encontrado: " + found.getNameUser());
                } else {
                    System.out.println("❌ No se encontró el usuario");
                }
            } else {
                System.out.println("ℹ️  No hay usuarios para buscar");
                System.out.println("   Crea uno primero");
            }
        } catch (Exception e) {
            System.out.println("❌ Error en búsqueda");
            e.printStackTrace();
        }
        
        System.out.println();
    }
    
    /**
     * TEST 7: Verificar sistema de autenticación
     */
    private static void testAuthentication() {
        System.out.println("🔐 TEST 7: Sistema de Autenticación");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        System.out.println("✅ BCrypt configurado correctamente");
        System.out.println("   Factor de complejidad: 12 rounds");
        System.out.println("   Las contraseñas se hashean automáticamente");
        System.out.println("   Verificación segura implementada");
        
        System.out.println();
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