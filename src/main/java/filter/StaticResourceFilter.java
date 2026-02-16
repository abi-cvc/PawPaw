package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Filtro para asegurar que los archivos estáticos se sirvan con el Content-Type correcto.
 * Este filtro intercepta las peticiones a CSS, JS, imágenes y fuentes.
 */
@WebFilter(urlPatterns = {"*.css", "*.js", "*.png", "*.jpg", "*.jpeg", "*.svg", "*.woff", "*.woff2", "*.ttf", "*.otf"})
public class StaticResourceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String uri = httpRequest.getRequestURI();
        
        // Determinar el Content-Type correcto basado en la extensión
        String contentType = getContentType(uri);
        
        if (contentType != null) {
            // Crear un wrapper que fuerce el Content-Type
            HttpServletResponse wrappedResponse = new ContentTypeResponseWrapper(httpResponse, contentType);
            chain.doFilter(request, wrappedResponse);
        } else {
            // Si no reconocemos el tipo, continuar sin modificar
            chain.doFilter(request, response);
        }
    }
    
    /**
     * Determina el Content-Type basado en la extensión del archivo
     */
    private String getContentType(String uri) {
        uri = uri.toLowerCase();
        
        // CSS
        if (uri.endsWith(".css")) {
            return "text/css; charset=UTF-8";
        }
        
        // JavaScript
        if (uri.endsWith(".js") || uri.endsWith(".mjs")) {
            return "application/javascript; charset=UTF-8";
        }
        
        // Images
        if (uri.endsWith(".png")) {
            return "image/png";
        }
        if (uri.endsWith(".jpg") || uri.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (uri.endsWith(".gif")) {
            return "image/gif";
        }
        if (uri.endsWith(".svg")) {
            return "image/svg+xml";
        }
        if (uri.endsWith(".webp")) {
            return "image/webp";
        }
        if (uri.endsWith(".ico")) {
            return "image/x-icon";
        }
        
        // Fonts
        if (uri.endsWith(".woff")) {
            return "font/woff";
        }
        if (uri.endsWith(".woff2")) {
            return "font/woff2";
        }
        if (uri.endsWith(".ttf")) {
            return "font/ttf";
        }
        if (uri.endsWith(".otf")) {
            return "font/otf";
        }
        if (uri.endsWith(".eot")) {
            return "application/vnd.ms-fontobject";
        }
        
        return null;
    }
    
    /**
     * Wrapper que fuerza el Content-Type en la respuesta
     */
    private static class ContentTypeResponseWrapper extends HttpServletResponseWrapper {
        private final String contentType;
        
        public ContentTypeResponseWrapper(HttpServletResponse response, String contentType) {
            super(response);
            this.contentType = contentType;
        }
        
        @Override
        public void setContentType(String type) {
            // Forzar nuestro Content-Type, ignorando cualquier otro
            super.setContentType(this.contentType);
        }
        
        @Override
        public void setHeader(String name, String value) {
            if ("Content-Type".equalsIgnoreCase(name)) {
                // Forzar nuestro Content-Type
                super.setHeader(name, this.contentType);
            } else {
                super.setHeader(name, value);
            }
        }
        
        @Override
        public void addHeader(String name, String value) {
            if ("Content-Type".equalsIgnoreCase(name)) {
                // Forzar nuestro Content-Type
                super.setHeader(name, this.contentType);
            } else {
                super.addHeader(name, value);
            }
        }
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No se requiere inicialización
    }
    
    @Override
    public void destroy() {
        // No se requiere limpieza
    }
}