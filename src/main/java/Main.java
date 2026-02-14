import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Context;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        String port = System.getenv("PORT");
        if (port == null) port = "8080";
        
        System.out.println("🚀 PawPaw starting on port: " + port);
        
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(port));
        tomcat.getConnector();
        
        // Buscar cualquier archivo WAR en target/
        File targetDir = new File("target");
        File warFile = null;
        
        if (targetDir.exists() && targetDir.isDirectory()) {
            File[] files = targetDir.listFiles((dir, name) -> name.endsWith(".war"));
            if (files != null && files.length > 0) {
                warFile = files[0];
                System.out.println("📦 Found WAR: " + warFile.getName());
            }
        }
        
        if (warFile == null || !warFile.exists()) {
            System.err.println("❌ No WAR file found in target/ directory");
            System.err.println("📂 Listing target/ contents:");
            if (targetDir.exists()) {
                File[] allFiles = targetDir.listFiles();
                if (allFiles != null) {
                    for (File f : allFiles) {
                        System.err.println("   - " + f.getName());
                    }
                }
            }
            System.exit(1);
        }
        
        System.out.println("✅ Loading WAR: " + warFile.getAbsolutePath());
        Context context = tomcat.addWebapp("", warFile.getAbsolutePath());
        
        tomcat.start();
        System.out.println("✅ PawPaw is running at http://localhost:" + port);
        tomcat.getServer().await();
    }
}