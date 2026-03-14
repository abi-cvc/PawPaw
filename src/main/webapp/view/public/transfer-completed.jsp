<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    String message = (String) request.getAttribute("message");
    if (message == null) message = "Esta transferencia ya fue completada";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transferencia Completada</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <!-- CSS — usar SIEMPRE contextPath, nunca ruta relativa -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">

</head>
<body>
    <div class="transfer-page">
        <div class="transfer-container">
            <div class="transfer-header">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="PawPaw Logo" class="transfer-logo">
            </div>

            <div class="transfer-card transfer-completed">
                <div class="status-icon success-icon">✅</div>
                <h2>Transferencia Completada</h2>
                <p class="status-message"><c:out value="${message}"/></p>

                <div class="info-box">
                    <p>Esta transferencia ya fue procesada anteriormente.
                       Si necesitas ayuda, contacta a soporte.</p>
                </div>

                <div class="status-actions">
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-primario">
                        Iniciar Sesión
                    </a>
                    <a href="${pageContext.request.contextPath}/view/index.jsp" class="btn btn-secundario">
                        Volver al Inicio
                    </a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
