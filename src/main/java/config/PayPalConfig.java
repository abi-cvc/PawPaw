package config;

/**
 * Configuración de PayPal
 * Lee credenciales de variables de entorno
 * Env vars: PAYPAL_MODE, PAYPAL_CLIENT_ID, PAYPAL_SECRET
 */
public class PayPalConfig {

    // URLs de PayPal API
    private static final String SANDBOX_API_BASE = "https://api-m.sandbox.paypal.com";
    private static final String LIVE_API_BASE = "https://api-m.paypal.com";

    /**
     * Obtener modo actual (sandbox o live)
     */
    public static String getMode() {
        String mode = System.getenv("PAYPAL_MODE");
        return (mode != null && !mode.isEmpty()) ? mode : "sandbox";
    }

    /**
     * Obtener Client ID desde variable de entorno
     */
    public static String getClientId() {
        String clientId = System.getenv("PAYPAL_CLIENT_ID");
        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalStateException("Variable de entorno PAYPAL_CLIENT_ID no configurada");
        }
        return clientId;
    }

    /**
     * Obtener Secret desde variable de entorno
     */
    public static String getSecret() {
        String secret = System.getenv("PAYPAL_SECRET");
        if (secret == null || secret.isEmpty()) {
            throw new IllegalStateException("Variable de entorno PAYPAL_SECRET no configurada");
        }
        return secret;
    }

    /**
     * Obtener base URL de API según el modo
     */
    public static String getApiBase() {
        return isSandbox() ? SANDBOX_API_BASE : LIVE_API_BASE;
    }

    /**
     * Verificar si está en modo sandbox
     */
    public static boolean isSandbox() {
        return "sandbox".equals(getMode());
    }
}
