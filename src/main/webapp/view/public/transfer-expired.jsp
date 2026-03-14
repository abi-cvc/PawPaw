<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.entity.PetTransferRequest" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    PetTransferRequest transfer = (PetTransferRequest) request.getAttribute("transfer");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transferencia Expirada</title>
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

            <div class="transfer-card transfer-expired">
                <div class="status-icon warning-icon">⏰</div>
                <h2>Transferencia Expirada</h2>
                <% if (transfer != null) { %>
                <p class="status-message">
                    La transferencia de <strong><c:out value="${transfer.petName}"/></strong> ha expirado.
                </p>
                <p>Esta transferencia estaba destinada a <strong><c:out value="${transfer.adopterName}"/></strong>.</p>
                <% } else { %>
                <p class="status-message">Esta transferencia ha expirado y ya no está disponible.</p>
                <% } %>

                <div class="info-box">
                    <p>Las transferencias tienen una validez de 7 días.
                       Si aún deseas adoptar esta mascota, contacta directamente con la fundación.</p>
                </div>

                <div class="status-actions">
                    <a href="${pageContext.request.contextPath}/foundations/public" class="btn btn-primario">
                        Ver Fundaciones
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
