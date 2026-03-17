package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/about", "/contact", "/privacy", "/terms"})
public class StaticPagesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        String jsp = switch (path) {
            case "/about"   -> "/view/public/about.jsp";
            case "/contact" -> "/view/public/contact.jsp";
            case "/privacy" -> "/view/public/privacy.jsp";
            case "/terms"   -> "/view/public/terms.jsp";
            default         -> "/view/index.jsp";
        };

        request.getRequestDispatcher(jsp).forward(request, response);
    }
}
