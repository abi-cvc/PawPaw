package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet de diagnóstico para verificar la configuración de MIME types
 * y servido de archivos estáticos
 */
@WebServlet("/diagnostic")
public class DiagnosticServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.printWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>PawPaw - Diagnóstico</title>");
        out.println("<style>");
        out.println("body { font-family: monospace; padding: 20px; background: #f5f5f5; }");
        out.println("h1 { color: #333; }");
        out.println(".section { background: white; padding: 15px; margin: 10px 0; border-radius: 5px; }");
        out.println(".ok { color: green; }");
        out.println(".error { color: red; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        
        out.println("<h1>🔍 PawPaw - Diagnóstico del Sistema</h1>");
        
        // Context Path
        out.println("<div class='section'>");
        out.println("<h2>📍 Context Path</h2>");
        out.println("<p>Context Path: <strong>" + request.getContextPath() + "</strong></p>");
        out.println("<p>Servlet Context Path: <strong>" + getServletContext().getContextPath() + "</strong></p>");
        out.println("</div>");
        
        // MIME Types
        out.println("<div class='section'>");
        out.println("<h2>📄 MIME Types Configurados</h2>");
        String cssMime = getServletContext().getMimeType("styles.css");
        String jsMime = getServletContext().getMimeType("script.js");
        String pngMime = getServletContext().getMimeType("image.png");
        
        out.println("<p>CSS (.css): <strong>" + (cssMime != null ? cssMime : "NO CONFIGURADO") + "</strong> " + 
                   (cssMime != null && cssMime.equals("text/css") ? "<span class='ok'>✓</span>" : "<span class='error'>✗</span>") + "</p>");
        out.println("<p>JavaScript (.js): <strong>" + (jsMime != null ? jsMime : "NO CONFIGURADO") + "</strong></p>");
        out.println("<p>PNG (.png): <strong>" + (pngMime != null ? pngMime : "NO CONFIGURADO") + "</strong></p>");
        out.println("</div>");
        
        // Rutas de recursos
        out.println("<div class='section'>");
        out.println("<h2>🗂️ Recursos Estáticos</h2>");
        
        String cssPath = getServletContext().getRealPath("/css/styles.css");
        String jsPath = getServletContext().getRealPath("/js/main.js");
        String logoPath = getServletContext().getRealPath("/images/logo.png");
        
        out.println("<p>styles.css: <strong>" + (cssPath != null ? cssPath : "NO ENCONTRADO") + "</strong></p>");
        out.println("<p>main.js: <strong>" + (jsPath != null ? jsPath : "NO ENCONTRADO") + "</strong></p>");
        out.println("<p>logo.png: <strong>" + (logoPath != null ? logoPath : "NO ENCONTRADO") + "</strong></p>");
        
        // Verificar existencia de archivos
        if (cssPath != null) {
            java.io.File cssFile = new java.io.File(cssPath);
            out.println("<p>CSS existe: <strong>" + cssFile.exists() + "</strong> " + 
                       (cssFile.exists() ? "<span class='ok'>✓</span>" : "<span class='error'>✗</span>") + "</p>");
            if (cssFile.exists()) {
                out.println("<p>CSS size: <strong>" + cssFile.length() + " bytes</strong></p>");
            }
        }
        out.println("</div>");
        
        // URLs de prueba
        out.println("<div class='section'>");
        out.println("<h2>🔗 URLs de Prueba</h2>");
        String baseUrl = request.getScheme() + "://" + request.getServerName() + 
                        (request.getServerPort() != 80 && request.getServerPort() != 443 ? ":" + request.getServerPort() : "") +
                        request.getContextPath();
        
        out.println("<p><a href='" + baseUrl + "/css/styles.css' target='_blank'>Abrir CSS</a></p>");
        out.println("<p><a href='" + baseUrl + "/js/main.js' target='_blank'>Abrir JS</a></p>");
        out.println("<p><a href='" + baseUrl + "/images/logo.png' target='_blank'>Abrir Logo</a></p>");
        out.println("<p><a href='" + baseUrl + "/' target='_blank'>Ir a Home</a></p>");
        out.println("</div>");
        
        // Variables de entorno (sin mostrar valores sensibles)
        out.println("<div class='section'>");
        out.println("<h2>🔧 Configuración</h2>");
        out.println("<p>DB_URL configurado: <strong>" + (System.getenv("DB_URL") != null ? "SÍ ✓" : "NO ✗") + "</strong></p>");
        out.println("<p>CLOUDINARY_CLOUD_NAME configurado: <strong>" + (System.getenv("CLOUDINARY_CLOUD_NAME") != null ? "SÍ ✓" : "NO ✗") + "</strong></p>");
        out.println("</div>");
        
        // Test de carga de CSS inline
        out.println("<div class='section'>");
        out.println("<h2>🎨 Test de CSS</h2>");
        out.println("<p>Si el siguiente cuadro tiene fondo verde, el CSS inline funciona:</p>");
        out.println("<div style='background: #4CAF50; color: white; padding: 10px; border-radius: 5px;'>✓ CSS Inline OK</div>");
        out.println("</div>");
        
        out.println("</body>");
        out.println("</html>");
    }
}