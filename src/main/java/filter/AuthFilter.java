package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filtro centralizado de autorización
 * Protege rutas /user/* y /admin/* verificando sesión y rol
 */
@WebFilter({"/user/*", "/admin/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        // Verificar que hay sesión activa
        if (session == null || session.getAttribute("userId") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        String path = httpRequest.getRequestURI();
        String userRole = (String) session.getAttribute("userRole");

        // Rutas /admin/* requieren rol admin
        if (path.contains("/admin/") && !"admin".equals(userRole)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
