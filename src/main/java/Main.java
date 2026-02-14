import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Context;
import java.io.File;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) throws Exception {
        String port = System.getenv("PORT");
        if (port == null) port = "8080";
        
        System.out.println("PawPaw starting on port: " + port);
        
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(port));
        
        // Crear estructura de directorios completa
        File workDir = new File(System.getProperty("java.io.tmpdir"), "tomcat-" + System.currentTimeMillis());
        File webappsDir = new File(workDir, "webapps");
        File rootDir = new File(webappsDir, "ROOT");
        
        rootDir.mkdirs();
        System.out.println("Created directories:");
        System.out.println("  Work: " + workDir.getAbsolutePath());
        System.out.println("  Webapps: " + webappsDir.getAbsolutePath());
        System.out.println("  ROOT: " + rootDir.getAbsolutePath());
        
        tomcat.setBaseDir(workDir.getAbsolutePath());
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
            System.err.println("ERROR: No WAR file found");
            System.exit(1);
        }
        
        // NUEVO: Listar contenido del WAR para debug
        System.out.println("\n=== WAR CONTENTS ===");
        try (JarFile jar = new JarFile(warFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            int count = 0;
            while (entries.hasMoreElements() && count < 50) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                // Solo mostrar archivos importantes
                if (name.endsWith(".jsp") || name.endsWith(".html") || 
                    name.equals("WEB-INF/web.xml") || name.startsWith("view/")) {
                    System.out.println("  " + name);
                    count++;
                }
            }
        } catch (Exception e) {
            System.err.println("Could not read WAR: " + e.getMessage());
        }
        System.out.println("===================\n");
        
        System.out.println("Deploying: " + warFile.getAbsolutePath());
        
        // Desplegar WAR
        Context context = tomcat.addWebapp("", warFile.getAbsolutePath());
        
        // Reducir logging
        context.getJarScanner().setJarScanFilter((type, name) -> 
            !name.contains("jaxb") && !name.contains("activation")
        );
        
        System.out.println("Starting Tomcat...");
        tomcat.start();
        
        System.out.println("==========================================");
        System.out.println(" PawPaw is RUNNING on port " + port);
        System.out.println(" Context path: " + context.getPath());
        System.out.println(" Try: https://your-domain.railway.app/");
        System.out.println("==========================================");
        
        tomcat.getServer().await();
    }
}