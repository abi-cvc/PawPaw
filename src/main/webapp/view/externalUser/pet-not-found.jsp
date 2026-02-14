<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String error = (String) request.getAttribute("error");
    if (error == null) error = "Mascota no encontrada";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mascota no encontrada - PawPaw</title>
    
    <link rel="icon" type="image/png" href="<%= request.getContextPath() %>/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@300;400;500;600;700&family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body class="public-page">
    
    <header class="public-header">
        <div class="public-header-content">
            <a href="<%= request.getContextPath() %>/" class="public-logo">
                <img src="<%= request.getContextPath() %>/images/logo.png" alt="PawPaw">
                <span>PawPaw</span>
            </a>
        </div>
    </header>
    
    <main class="public-main">
        <div class="public-container">
            <div class="empty-state">
                <svg fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                </svg>
                <h3><%= error %></h3>
                <p>El código QR que escaneaste no corresponde a ninguna mascota registrada.</p>
                <p>Puede que el perfil haya sido eliminado o el enlace sea incorrecto.</p>
                <a href="<%= request.getContextPath() %>/" class="btn btn-primario btn-grande">
                    Ir a PawPaw
                </a>
            </div>
        </div>
    </main>
    
</body>
</html>
