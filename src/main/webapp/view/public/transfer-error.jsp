<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String error = (String) request.getAttribute("error");
    if (error == null) error = "Ha ocurrido un error con esta transferencia";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Transferencia</title>
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <!-- CSS — usar SIEMPRE contextPath, nunca ruta relativa -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
    
</head>
<body>
    <div class="transfer-page">
        <div class="transfer-container">
            <div class="transfer-header">
                <img src="<%= request.getContextPath() %>/images/logo.png" alt="PawPaw Logo" class="transfer-logo">
            </div>
            
            <div class="transfer-card transfer-error">
                <div class="status-icon error-icon">❌</div>
                <h2>Error en la Transferencia</h2>
                <p class="status-message"><%= error %></p>
                
                <div class="status-actions">
                    <a href="<%= request.getContextPath() %>/view/index.jsp" class="btn btn-primario">
                        Volver al Inicio
                    </a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
