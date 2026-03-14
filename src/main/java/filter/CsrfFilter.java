package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Filtro CSRF - Genera y valida tokens anti-CSRF en formularios POST
 * El token se almacena en la sesión y debe incluirse en cada form:
 * <input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
 */
@WebFilter("/*")
public class CsrfFilter implements Filter {

    private static final String CSRF_TOKEN_ATTR = "csrfToken";
    private static final String CSRF_PARAM = "_csrf";
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Generar token si no existe en la sesión
        HttpSession session = httpRequest.getSession(false);
        if (session != null && session.getAttribute(CSRF_TOKEN_ATTR) == null) {
            session.setAttribute(CSRF_TOKEN_ATTR, generateToken());
        }

        // Solo validar en requests POST (excepto APIs y PayPal callbacks)
        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            String path = httpRequest.getRequestURI();

            // Excluir endpoints API que no usan forms HTML
            if (!isExcludedPath(path)) {
                if (session == null) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Sesion invalida");
                    return;
                }

                String sessionToken = (String) session.getAttribute(CSRF_TOKEN_ATTR);
                String requestToken = httpRequest.getParameter(CSRF_PARAM);

                if (sessionToken == null || !sessionToken.equals(requestToken)) {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Token CSRF invalido");
                    return;
                }
            }
        }

        // Asegurar que las nuevas sesiones también tengan token
        session = httpRequest.getSession(false);
        if (session != null && session.getAttribute(CSRF_TOKEN_ATTR) == null) {
            session.setAttribute(CSRF_TOKEN_ATTR, generateToken());
        }

        chain.doFilter(request, response);
    }

    /**
     * Rutas excluidas de validación CSRF (APIs JSON, callbacks externos)
     */
    private boolean isExcludedPath(String path) {
        return path.contains("/api/") ||
               path.contains("/paypal/") ||
               path.contains("/accept-transfer") ||
               path.contains("/health") ||
               path.contains("/diagnostic");
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
