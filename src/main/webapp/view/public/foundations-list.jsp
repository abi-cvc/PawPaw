<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Fundaciones Aliadas - PawPaw</title>
    <meta name="description" content="Conoce las fundaciones y rescatistas aliados de PawPaw. Organizaciones verificadas que protegen animales en Ecuador.">
    <meta name="robots" content="index, follow">
    <link rel="canonical" href="${pageContext.request.contextPath}/foundations">
    <meta property="og:type" content="website">
    <meta property="og:title" content="Fundaciones Aliadas - PawPaw">
    <meta property="og:description" content="Organizaciones verificadas aliadas de PawPaw que protegen animales en Ecuador.">
    <meta property="og:image" content="${pageContext.request.contextPath}/images/logo.png">
    <meta property="og:locale" content="es_EC">
    <meta property="og:site_name" content="PawPaw">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css?v=2">
</head>
<body>

    <!-- ══ NAVBAR ══ -->
    <jsp:include page="/view/components/navbar.jsp" />

    <!-- ══ HERO ══ -->
    <section class="fl-hero">
        <div class="fl-hero-inner">
            <div class="fl-hero-text">
                <span class="fl-hero-tag">🐾 Red de adopción PawPaw</span>
                <h1 class="fl-hero-title">Fundaciones Aliadas</h1>
                <p class="fl-hero-subtitle">
                    Conectamos a animales rescatados con familias que los están esperando.
                    Cada código QR que llevamos es una historia que empieza de nuevo.
                </p>
                <a href="${pageContext.request.contextPath}/foundation/apply" class="btn btn-primario btn-grande">
                    Unir mi fundación →
                </a>
            </div>
            <div class="fl-hero-stats">
                <div class="fl-stat-card">
                    <span class="fl-stat-num"><c:out value="${totalFoundations}" default="0"/></span>
                    <span class="fl-stat-lbl">Fundaciones activas</span>
                </div>
                <div class="fl-stat-card">
                    <span class="fl-stat-num"><c:out value="${totalAvailablePets}" default="0"/></span>
                    <span class="fl-stat-lbl">Mascotas en adopción</span>
                </div>
            </div>
        </div>
    </section>

    <!-- ══ GRID DE FUNDACIONES ══ -->
    <section class="fl-section">
        <div class="fl-container">

            <c:if test="${not empty foundations}">

            <div class="fl-grid">
                <c:forEach var="foundation" items="${foundations}">
                    <c:set var="fname" value="${foundation['foundation_name']}"/>
                    <c:set var="fdesc" value="${foundation['description']}"/>
                    <c:set var="fcontact" value="${foundation['contact_name']}"/>
                    <c:set var="fphone" value="${foundation['phone']}"/>
                    <c:set var="availablePets" value="${foundation['available_pets'] != null ? foundation['available_pets'] : 0}"/>
                    <c:set var="adoptedPets" value="${foundation['adopted_pets'] != null ? foundation['adopted_pets'] : 0}"/>
                    <c:set var="initial" value="${not empty fname ? fn:toUpperCase(fn:substring(fname, 0, 1)) : 'F'}"/>
                    <c:set var="fdescTrunc" value="${not empty fdesc && fn:length(fdesc) > 110 ? fn:substring(fdesc, 0, 110).concat('…') : fdesc}"/>

                <div class="fl-card">

                    <!-- Cabecera -->
                    <div class="fl-card-header">
                        <div class="fl-avatar"><c:out value="${initial}"/></div>
                        <div class="fl-card-meta">
                            <h3 class="fl-card-name"><c:out value="${fname}"/></h3>
                            <span class="fl-badge-partner">✓ Partner PawPaw</span>
                        </div>
                    </div>

                    <!-- Descripción -->
                    <c:if test="${not empty fdesc}">
                    <p class="fl-card-desc">
                        <c:out value="${fdescTrunc}"/>
                    </p>
                    </c:if>

                    <!-- Contactos -->
                    <div class="fl-card-contacts">
                        <c:if test="${not empty fcontact}">
                        <span class="fl-contact-pill">👤 <c:out value="${fcontact}"/></span>
                        </c:if>
                        <c:if test="${not empty fphone}">
                        <a href="tel:${fn:escapeXml(fphone)}" class="fl-contact-pill fl-contact-link">
                            📞 <c:out value="${fphone}"/>
                        </a>
                        </c:if>
                    </div>

                    <!-- Stats -->
                    <div class="fl-card-stats">
                        <div class="fl-mini-stat fl-stat-green">
                            <span class="fl-mini-num"><c:out value="${availablePets}"/></span>
                            <span class="fl-mini-lbl">En adopción</span>
                        </div>
                        <div class="fl-mini-stat fl-stat-blue">
                            <span class="fl-mini-num"><c:out value="${adoptedPets}"/></span>
                            <span class="fl-mini-lbl">Adoptados</span>
                        </div>
                    </div>

                    <!-- Acción -->
                    <div class="fl-card-footer">
                        <a href="${pageContext.request.contextPath}/foundations/${foundation['id_user']}"
                           class="btn btn-primario btn-block">
                            Ver mascotas 🐾
                        </a>
                    </div>
                </div>

                </c:forEach>
            </div>

            </c:if>

            <c:if test="${empty foundations}">

            <div class="fl-empty">
                <div class="fl-empty-icon">🏠</div>
                <h3>No hay fundaciones disponibles aún</h3>
                <p>Sé la primera fundación aliada de PawPaw y dale visibilidad a tus animales.</p>
                <a href="${pageContext.request.contextPath}/foundation/apply" class="btn btn-primario">
                    Unir mi fundación →
                </a>
            </div>

            </c:if>

        </div>
    </section>

    <!-- ══ FOOTER ══ -->
    <jsp:include page="/view/components/footer.jsp" />

</body>
</html>
