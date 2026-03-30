<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - PawPaw</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/logo.png">
    <link href="https://fonts.googleapis.com/css2?family=Fredoka:wght@400;600&display=swap" rel="stylesheet">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Fredoka', sans-serif; background: #f8f9fa; display: flex; align-items: center; justify-content: center; min-height: 100vh; text-align: center; padding: 2rem; }
        .error-container { max-width: 500px; }
        .error-icon { font-size: 5rem; margin-bottom: 1rem; }
        h1 { font-size: 2rem; color: #333; margin-bottom: 0.5rem; }
        p { color: #666; margin-bottom: 1.5rem; }
        a { display: inline-block; padding: 0.75rem 2rem; background: #ff6b35; color: white; text-decoration: none; border-radius: 8px; font-weight: 600; }
        a:hover { background: #e55a2b; }
        .error-detail { background: #fff3f3; border: 1px solid #ffcdd2; border-radius: 8px; padding: 1rem; margin-top: 1rem; text-align: left; font-size: 0.85rem; color: #c62828; word-break: break-all; }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-icon">😿</div>
        <h1>Algo salio mal</h1>
        <p>Ocurrio un error inesperado. Por favor intenta de nuevo.</p>
        <a href="${pageContext.request.contextPath}/">Volver al inicio</a>
        <% if (exception != null) { %>
        <div class="error-detail">
            <strong>Error:</strong> <%= exception.getMessage() %>
        </div>
        <% } %>
    </div>
</body>
</html>
