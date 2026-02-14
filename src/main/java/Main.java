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
        
        // Usar el WAR compilado en lugar de src/main/webapp
        String warFile = "target/PawPaw.war";
        File war = new File(warFile);
        
        if (!war.exists()) {
            System.err.println("❌ WAR file not found: " + war.getAbsolutePath());
            System.exit(1);
        }
        
        System.out.println("📦 Loading WAR: " + war.getAbsolutePath());
        Context context = tomcat.addWebapp("", war.getAbsolutePath());
        
        tomcat.start();
        System.out.println("✅ PawPaw is running at http://localhost:" + port);
        tomcat.getServer().await();
    }
}