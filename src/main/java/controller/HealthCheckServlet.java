package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// SOLO /health - NO mapear a /
@WebServlet(name = "HealthCheckServlet", urlPatterns = {"/health"})
public class HealthCheckServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>PawPaw Health Check</title>");
            out.println("<style>");
            out.println("body { font-family: Arial; padding: 50px; background: #f0f0f0; }");
            out.println(".container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
            out.println("h1 { color: #4CAF50; }");
            out.println(".info { margin: 10px 0; padding: 10px; background: #f9f9f9; border-left: 4px solid #4CAF50; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>");
            out.println("<h1>PawPaw is RUNNING!</h1>");
            out.println("<div class='info'><strong>Status:</strong> ONLINE</div>");
            out.println("<div class='info'><strong>Context Path:</strong> " + request.getContextPath() + "</div>");
            out.println("<div class='info'><strong>Servlet Path:</strong> " + request.getServletPath() + "</div>");
            out.println("<div class='info'><strong>Server Info:</strong> " + getServletContext().getServerInfo() + "</div>");
            out.println("<hr>");
            out.println("<p>PawPaw application deployed successfully on Railway!</p>");
            out.println("<p><a href='/view/index.jsp'>Go to Index.jsp</a> | ");
            out.println("<a href='/view/internalUser/login.jsp'>Go to Login</a></p>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}