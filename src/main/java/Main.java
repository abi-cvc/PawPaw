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
        
        // Configurar directorio base con permisos de escritura
        String baseDir = System.getProperty("java.io.tmpdir");
        tomcat.setBaseDir(baseDir);
        System.out.println("📁 Base directory: " + baseDir);
        
        tomcat.getConnector();
        
        // Buscar archivo WAR
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
            System.err.println("❌ No WAR file found in target/");
            System.exit(1);
        }
        
        System.out.println("✅ Loading WAR: " + warFile.getAbsolutePath());
        
        // Desplegar WAR con context path vacío (root)
        Context context = tomcat.addWebapp("", warFile.getAbsolutePath());
        
        // Deshabilitar escaneo de JARs innecesarios para evitar errores
        context.getJarScanner().setJarScanFilter((jarScanType, jarName) -> {
            // Solo escanear JARs de tu aplicación, ignorar jaxb y otros opcionales
            return !jarName.contains("jaxb") && !jarName.contains("activation");
        });
        
        System.out.println("✅ Context configured: " + context.getPath());
        
        tomcat.start();
        System.out.println("✅ PawPaw is RUNNING!");
        System.out.println("🌐 Access at: http://localhost:" + port);
        
        tomcat.getServer().await();
    }
}