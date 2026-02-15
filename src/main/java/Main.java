import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Context;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        String port = System.getenv("PORT");
        if (port == null) port = "8080";
        
        System.out.println("PawPaw starting on port: " + port);
        
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(port));
        
        // Crear directorios
        File workDir = new File(System.getProperty("java.io.tmpdir"), "tomcat-" + System.currentTimeMillis());
        File webappsDir = new File(workDir, "webapps");
        File rootDir = new File(webappsDir, "ROOT");
        rootDir.mkdirs();
        
        tomcat.setBaseDir(workDir.getAbsolutePath());
        tomcat.getConnector();
        
        // Buscar WAR
        File targetDir = new File("target");
        File warFile = null;
        
        if (targetDir.exists() && targetDir.isDirectory()) {
            File[] files = targetDir.listFiles((dir, name) -> name.endsWith(".war"));
            if (files != null && files.length > 0) {
                warFile = files[0];
            }
        }
        
        if (warFile == null || !warFile.exists()) {
            System.err.println("ERROR: No WAR file found");
            System.exit(1);
        }
        
        System.out.println("Deploying: " + warFile.getName());
        
        // Desplegar WAR
        Context context = tomcat.addWebapp("", warFile.getAbsolutePath());
        
        // MANUAL SERVLET REGISTRATION
        System.out.println("Registering servlets manually...");
        
        // Health check servlet
        Tomcat.addServlet(context, "HealthCheckServlet", "controller.HealthCheckServlet");
        context.addServletMappingDecoded("/health", "HealthCheckServlet");
        
        // Authentication
        Tomcat.addServlet(context, "AuthenticationController", "controller.AuthenticationController");
        context.addServletMappingDecoded("/login", "AuthenticationController");
        
        // Logout
        Tomcat.addServlet(context, "LogoutServlet", "controller.LogoutServlet");
        context.addServletMappingDecoded("/logout", "LogoutServlet");
        
        // User panel
        Tomcat.addServlet(context, "UserPanelController", "controller.UserPanelController");
        context.addServletMappingDecoded("/user/panel", "UserPanelController");
        context.addServletMappingDecoded("/admin/panel", "UserPanelController");
        
        // User profile
        Tomcat.addServlet(context, "UserProfileController", "controller.UserProfileController");
        context.addServletMappingDecoded("/user/profile", "UserProfileController");
        
        // User controller
        Tomcat.addServlet(context, "UserController", "controller.UserController");
        context.addServletMappingDecoded("/user/register", "UserController");
        
        // Pet controller
        Tomcat.addServlet(context, "PetController", "controller.PetController");
        context.addServletMappingDecoded("/pet/*", "PetController");
        
        // QR Code controller
        Tomcat.addServlet(context, "QRCodeController", "controller.QRCodeController");
        context.addServletMappingDecoded("/qr/*", "QRCodeController");
        
        // Public pet controller
        Tomcat.addServlet(context, "PublicPetController", "controller.PublicPetController");
        context.addServletMappingDecoded("/pet/*", "PublicPetController");
        
        System.out.println("✓ Servlets registered");
        
        // Reducir logging
        context.getJarScanner().setJarScanFilter((type, name) -> 
            !name.contains("jaxb") && !name.contains("activation")
        );
        
        tomcat.start();
        
        System.out.println("=".repeat(50));
        System.out.println("PawPaw is RUNNING on port " + port);
        System.out.println("Test: https://web-production-e92f4.up.railway.app/health");
        System.out.println("=".repeat(50));
        
        tomcat.getServer().await();
    }
}