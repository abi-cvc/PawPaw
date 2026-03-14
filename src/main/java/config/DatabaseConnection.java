package config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Configuración de conexión a la base de datos PostgreSQL (Neon)
 * Utiliza HikariCP para pool de conexiones eficiente
 * Funciona en desarrollo (localhost) y producción (Railway)
 */
public class DatabaseConnection {
    
    private static HikariDataSource dataSource;
    
    // Configuración de conexión - DEBE configurarse via variables de entorno
    // DB_URL, DB_USER, DB_PASSWORD son OBLIGATORIAS
    
    // Configuración del pool de conexiones
    private static final int MAX_POOL_SIZE = 10;
    private static final int MIN_IDLE = 2;
    private static final long CONNECTION_TIMEOUT = 30000; // 30 segundos
    private static final long IDLE_TIMEOUT = 600000; // 10 minutos
    private static final long MAX_LIFETIME = 1800000; // 30 minutos
    
    static {
        try {
            inicializarDataSource();
        } catch (Exception e) {
            System.err.println("Error al inicializar el DataSource: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Inicializa el DataSource con HikariCP
     * Lee configuración de variables de entorno (Railway) o usa valores locales
     */
    private static void inicializarDataSource() {
        HikariConfig config = new HikariConfig();
        
        // Leer variables de entorno (OBLIGATORIO)
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        // Fail-fast si no hay variables de entorno configuradas
        if (dbUrl == null || dbUrl.isEmpty() ||
            dbUser == null || dbUser.isEmpty() ||
            dbPassword == null || dbPassword.isEmpty()) {
            throw new IllegalStateException(
                "Variables de entorno de BD no configuradas. " +
                "Requeridas: DB_URL, DB_USER, DB_PASSWORD");
        }

        System.out.println("✅ Variables de entorno de BD detectadas");
        
        // Configuración básica
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);
        
        // Configuración del pool
        config.setMaximumPoolSize(MAX_POOL_SIZE);
        config.setMinimumIdle(MIN_IDLE);
        config.setConnectionTimeout(CONNECTION_TIMEOUT);
        config.setIdleTimeout(IDLE_TIMEOUT);
        config.setMaxLifetime(MAX_LIFETIME);
        
        // Configuración adicional para PostgreSQL
        config.setDriverClassName("org.postgresql.Driver");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        // Pool name para debugging
        config.setPoolName("PawPaw-HikariCP");
        
        // Crear el DataSource
        dataSource = new HikariDataSource(config);
        
        System.out.println("✅ Pool de conexiones inicializado correctamente");
    }
    
    /**
     * Obtiene una conexión del pool
     * @return Connection objeto de conexión
     * @throws SQLException si hay error al obtener la conexión
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource no inicializado");
        }
        return dataSource.getConnection();
    }
    
    /**
     * Cierra el pool de conexiones
     * Llamar solo al cerrar la aplicación
     */
    public static void cerrarDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("✅ Pool de conexiones cerrado");
        }
    }
    
    /**
     * Verifica si la conexión está funcionando
     * @return true si la conexión es exitosa
     */
    public static boolean verificarConexion() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar conexión: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene estadísticas del pool de conexiones
     */
    public static void imprimirEstadisticas() {
        if (dataSource != null) {
            System.out.println("📊 Estadísticas del Pool de Conexiones:");
            System.out.println("   - Conexiones activas: " + dataSource.getHikariPoolMXBean().getActiveConnections());
            System.out.println("   - Conexiones inactivas: " + dataSource.getHikariPoolMXBean().getIdleConnections());
            System.out.println("   - Total conexiones: " + dataSource.getHikariPoolMXBean().getTotalConnections());
            System.out.println("   - Hilos esperando: " + dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
    }
}