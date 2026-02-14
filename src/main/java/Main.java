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
        
        // Usar directorio writable
        File workDir = new File(System.getProperty("java.io.tmpdir"), "tomcat-work-" + System.currentTimeMillis());
        workDir.mkdirs();
        tomcat.setBaseDir(workDir.getAbsolutePath());
        System.out.println("Work directory: " + workDir.getAbsolutePath());
        
        tomcat.getConnector();
        
        // Buscar WAR
        File targetDir = new File("target");
        File warFile = null;
        
        if (targetDir.exists() && targetDir.isDirectory()) {
            File[] files = targetDir.listFiles((dir, name) -> name.endsWith(".war"));
            if (files != null && files.length > 0) {
                warFile = files[0];
                System.out.println("Found WAR: " + warFile.getName());
            }
        }
        
        if (warFile == null || !warFile.exists()) {
            System.err.println("No WAR file found");
            System.exit(1);
        }
        
        System.out.println("Deploying: " + warFile.getAbsolutePath());
        
        // Desplegar WAR
        Context context = tomcat.addWebapp("", warFile.getAbsolutePath());
        
        // Reducir logging verboso
        context.getJarScanner().setJarScanFilter((type, name) -> 
            !name.contains("jaxb") && !name.contains("activation")
        );
        
        System.out.println("Starting Tomcat...");
        tomcat.start();
        
        System.out.println("========================================");
        System.out.println("PawPaw is RUNNING on port " + port);
        System.out.println("========================================");
        
        tomcat.getServer().await();
    }
}