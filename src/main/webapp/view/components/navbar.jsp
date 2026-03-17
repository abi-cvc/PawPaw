<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    String navUserName = null;
    String navUserRole = null;
    boolean navLoggedIn = false;

    if (session != null && session.getAttribute("userId") != null) {
        navLoggedIn = true;
        navUserName = (String) session.getAttribute("userName");
        navUserRole = (String) session.getAttribute("userRole");
    }

    String navInitial = (navUserName != null && !navUserName.isEmpty())
        ? String.valueOf(navUserName.charAt(0)).toUpperCase() : "U";
    boolean navIsAdmin = "admin".equalsIgnoreCase(navUserRole);
    String navDashboardUrl = navIsAdmin
        ? request.getContextPath() + "/admin/panel"
        : request.getContextPath() + "/user/panel";

    pageContext.setAttribute("navLoggedIn", navLoggedIn);
    pageContext.setAttribute("navUserName", navUserName);
    pageContext.setAttribute("navInitial", navInitial);
    pageContext.setAttribute("navDashboardUrl", navDashboardUrl);
%>
<header class="header">
    <nav class="navbar">
        <div class="navbar-contenedor">
            <a href="${pageContext.request.contextPath}/" class="navbar-logo" style="text-decoration:none;">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="PawPaw" class="logo-img">
                <span class="logo-texto">PawPaw</span>
            </a>

            <div class="navbar-botones">
                <c:choose>
                    <c:when test="${navLoggedIn}">
                        <div class="nav-user-dropdown" id="navUserDropdown">
                            <button class="nav-user-toggle" id="navUserToggle" type="button">
                                <span class="nav-user-avatar"><c:out value="${navInitial}"/></span>
                                <span class="nav-user-name"><c:out value="${navUserName}"/></span>
                                <svg class="nav-chevron" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                                    <polyline points="6 9 12 15 18 9"></polyline>
                                </svg>
                            </button>
                            <div class="nav-dropdown-panel" id="navDropdownPanel">
                                <div class="nav-dropdown-header">
                                    <span class="nav-dropdown-avatar"><c:out value="${navInitial}"/></span>
                                    <div class="nav-dropdown-info">
                                        <span class="nav-dropdown-name"><c:out value="${navUserName}"/></span>
                                    </div>
                                </div>
                                <div class="nav-dropdown-body">
                                    <a href="${navDashboardUrl}" class="nav-dropdown-link">
                                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="7" height="7" rx="1"></rect><rect x="14" y="3" width="7" height="7" rx="1"></rect><rect x="3" y="14" width="7" height="7" rx="1"></rect><rect x="14" y="14" width="7" height="7" rx="1"></rect></svg>
                                        Dashboard
                                    </a>
                                </div>
                                <div class="nav-dropdown-footer">
                                    <a href="${pageContext.request.contextPath}/logout" class="nav-dropdown-link nav-dropdown-logout">
                                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" y1="12" x2="9" y2="12"></line></svg>
                                        Cerrar sesion
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-secundario">Iniciar sesion</a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-primario">Crear cuenta</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </nav>
</header>

<script>
(function() {
    var toggle = document.getElementById('navUserToggle');
    var panel = document.getElementById('navDropdownPanel');
    if (!toggle || !panel) return;

    toggle.addEventListener('click', function(e) {
        e.stopPropagation();
        panel.classList.toggle('open');
        toggle.classList.toggle('active');
    });

    document.addEventListener('click', function(e) {
        if (!document.getElementById('navUserDropdown').contains(e.target)) {
            panel.classList.remove('open');
            toggle.classList.remove('active');
        }
    });
})();
</script>
