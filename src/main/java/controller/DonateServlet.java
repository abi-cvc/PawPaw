package controller;

import config.PayPalConfig;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/donate")
public class DonateServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DonateServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("userId") != null;

        if (loggedIn) {
            request.setAttribute("sessionUserName", session.getAttribute("userName"));
            request.setAttribute("sessionUserEmail", session.getAttribute("user"));
        }

        request.setAttribute("paypalClientId", PayPalConfig.getClientId());

        logger.info("Acceso a pagina de donaciones - usuario logueado: {}", loggedIn);

        if (loggedIn) {
            request.getRequestDispatcher("/view/internalUser/donate.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/view/public/donate.jsp").forward(request, response);
        }
    }
}
