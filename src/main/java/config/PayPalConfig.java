package config;

/**
 * Configuración de PayPal
 * IMPORTANTE: En producción, mover credenciales a variables de entorno
 */
public class PayPalConfig {
    
    // Modo: "sandbox" para pruebas, "live" para producción
    private static final String MODE = "sandbox";
    
    // Credenciales SANDBOX (para testing)
    private static final String SANDBOX_CLIENT_ID = "AWl8G_qfgo0lmbDgplDHcFzdSu8I5HrWtnDoRkgxmZx6Bx9-jOMQHXrE0xqY1RPuSlTpfv9X5aeYThKf";
    private static final String SANDBOX_SECRET = "EJxF1JBXYzf5em8bhvNLGzlllvRo5B2QIt4KC93sDof7Grkq4HWn52ge27ZrH_wo1hEadr_oNxtClUEf";
    
    // Credenciales LIVE (producción - agregar cuando estés listo)
    private static final String LIVE_CLIENT_ID = "TU_CLIENT_ID_PRODUCCION";
    private static final String LIVE_SECRET = "TU_SECRET_PRODUCCION";
    
    // URLs de PayPal API
    private static final String SANDBOX_API_BASE = "https://api-m.sandbox.paypal.com";
    private static final String LIVE_API_BASE = "https://api-m.paypal.com";
    
    /**
     * Obtener Client ID según el modo
     */
    public static String getClientId() {
        return MODE.equals("sandbox") ? SANDBOX_CLIENT_ID : LIVE_CLIENT_ID;
    }
    
    /**
     * Obtener Secret según el modo
     */
    public static String getSecret() {
        return MODE.equals("sandbox") ? SANDBOX_SECRET : LIVE_SECRET;
    }
    
    /**
     * Obtener base URL de API según el modo
     */
    public static String getApiBase() {
        return MODE.equals("sandbox") ? SANDBOX_API_BASE : LIVE_API_BASE;
    }
    
    /**
     * Verificar si está en modo sandbox
     */
    public static boolean isSandbox() {
        return MODE.equals("sandbox");
    }
    
    /**
     * Obtener modo actual
     */
    public static String getMode() {
        return MODE;
    }
}
