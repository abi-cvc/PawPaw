import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Context;
import org.apache.catalina.Container;
import org.apache.catalina.Wrapper;
import java.io.File;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) throws Exception {
        String port = System.getenv("PORT");
        if (port == null) port = "8080";
        
        System.out.println("=".repeat(60));
        System.out.println("PawPaw starting on port: " + port);
        System.out.println("=".repeat(60));
        
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
        
        // Listar archivos .class del WAR
        System.out.println("\n" + "=".repeat(60));
        System.out.println("WAR CONTENTS (classes only):");
        System.out.println("=".repeat(60));
        try (JarFile jar = new JarFile(warFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            int classCount = 0;
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class")) {
                    System.out.println("  " + name);
                    classCount++;
                }
            }
            System.out.println("Total classes: " + classCount);
        } catch (Exception e) {
            System.err.println("Could not read WAR: " + e.getMessage());
        }
        System.out.println("=".repeat(60));
        
        System.out.println("\nDeploying WAR: " + warFile.getAbsolutePath());
        
        // Desplegar WAR
        Context context = tomcat.addWebapp("", warFile.getAbsolutePath());
        
        // Reducir logging
        context.getJarScanner().setJarScanFilter((type, name) -> 
            !name.contains("jaxb") && !name.contains("activation")
        );
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Starting Tomcat...");
        System.out.println("=".repeat(60));
        
        tomcat.start();
        
        // DEBUGGING: Listar servlets registrados
        System.out.println("\n" + "=".repeat(60));
        System.out.println("REGISTERED SERVLETS:");
        System.out.println("=".repeat(60));
        Container[] children = context.findChildren();
        if (children.length == 0) {
            System.out.println("  *** NO SERVLETS REGISTERED *** ");
            System.out.println("  This means web.xml was not processed or annotations not scanned.");
        } else {
            for (Container child : children) {
                if (child instanceof Wrapper) {
                    Wrapper wrapper = (Wrapper) child;
                    System.out.println("  Servlet: " + wrapper.getName());
                    System.out.println("    Class: " + wrapper.getServletClass());
                    String[] mappings = context.findServletMappings();
                    for (String mapping : mappings) {
                        if (mapping.equals(wrapper.getName())) {
                            System.out.println("    URL: " + context.findServletMapping(mapping));
                        }
                    }
                }
            }
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("CONTEXT INFO:");
        System.out.println("=".repeat(60));
        System.out.println("  Context Path: " + context.getPath());
        System.out.println("  Doc Base: " + context.getDocBase());
        System.out.println("  State: " + context.getState());
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println(" PawPaw is RUNNING on port " + port);
        System.out.println(" Try: https://web-production-e92f4.up.railway.app/");
        System.out.println(" Try: https://web-production-e92f4.up.railway.app/health");
        System.out.println("=".repeat(60) + "\n");
        
        tomcat.getServer().await();
    }
}