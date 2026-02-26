package controller;

import model.dao.FoundationRequestDAO;
import model.dao.UserDAO;
import model.entity.FoundationRequest;
import model.entity.User;
import service.EmailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet para gestionar solicitudes de fundaciones (Admin)
 */
@WebServlet("/admin/foundations")
public class ManageFoundationsServlet extends HttpServlet {
    
    private FoundationRequestDAO foundationDAO = new FoundationRequestDAO();
    private UserDAO userDAO = new UserDAO();
    private EmailService emailService = new EmailService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar sesión y rol
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String userRole = (String) session.getAttribute("userRole");
        if (!"admin".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/user/panel");
            return;
        }
        
        Integer adminId = (Integer) session.getAttribute("userId");
        User admin = userDAO.findById(adminId);
        if (admin != null) {
            request.setAttribute("user", admin);
        }
        
        // Obtener filtro
        String filter = request.getParameter("filter");
        
        List<FoundationRequest> requests;
        if (filter != null && !filter.isEmpty()) {
            requests = foundationDAO.findByStatus(filter);
        } else {
            requests = foundationDAO.findAll();
        }
        
        request.setAttribute("requests", requests);
        
        // Estadísticas
        int pendingCount = foundationDAO.countByStatus("pending");
        int approvedCount = foundationDAO.countByStatus("approved");
        int rejectedCount = foundationDAO.countByStatus("rejected");
        
        request.setAttribute("totalRequests", requests.size());
        request.setAttribute("pendingRequests", pendingCount);
        request.setAttribute("approvedRequests", approvedCount);
        request.setAttribute("rejectedRequests", rejectedCount);
        request.setAttribute("currentFilter", filter);
        
        // Mensajes
        String successMessage = (String) session.getAttribute("successMessage");
        String errorMessage = (String) session.getAttribute("errorMessage");
        session.removeAttribute("successMessage");
        session.removeAttribute("errorMessage");
        
        request.setAttribute("successMessage", successMessage);
        request.setAttribute("errorMessage", errorMessage);
        
        System.out.println("🏢 Admin viendo solicitudes de fundaciones. Total: " + requests.size());
        
        request.getRequestDispatcher("/view/admin/manage-foundations.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Verificar sesión y rol
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String userRole = (String) session.getAttribute("userRole");
        if (!"admin".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/user/panel");
            return;
        }
        
        Integer adminId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");
        
        try {
            if ("approve".equals(action)) {
                approveFoundation(request, adminId, session);
            } else if ("reject".equals(action)) {
                rejectFoundation(request, adminId, session);
            }
        } catch (Exception e) {
            System.err.println("❌ Error en acción de fundación: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error al procesar la solicitud");
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/foundations");
    }
    
    /**
     * Aprobar solicitud de fundación
     */
    private void approveFoundation(HttpServletRequest request, Integer adminId, HttpSession session) {
        try {
            Integer idRequest = Integer.parseInt(request.getParameter("idRequest"));
            FoundationRequest foundationReq = foundationDAO.findById(idRequest);
            
            if (foundationReq == null) {
                session.setAttribute("errorMessage", "Solicitud no encontrada");
                return;
            }
            
            // Aprobar solicitud (genera token automáticamente)
            if (foundationDAO.approve(idRequest, adminId)) {
                // Obtener el token generado
                foundationReq = foundationDAO.findById(idRequest);
                String token = foundationReq.getRegistrationToken();
                
                // Construir link de registro
                String registrationLink = request.getScheme() + "://" + 
                                        request.getServerName() + 
                                        (request.getServerPort() != 80 && request.getServerPort() != 443 
                                            ? ":" + request.getServerPort() : "") +
                                        request.getContextPath() + 
                                        "/register?token=" + token;
                
                // ✅ CORREGIDO: Usar sendNotificationEmail con 4 parámetros
                String subject = "¡Solicitud Aprobada - Bienvenido a PawPaw! 🤝";
                String message = "Estimado/a " + foundationReq.getContactName() + ",\n\n" +
                                 "¡Excelentes noticias! Tu solicitud para " + foundationReq.getFoundationName() + 
                                 " ha sido APROBADA.\n\n" +
                                 "Como Aliado PawPaw 🤝, tendrás acceso ilimitado para registrar todas tus mascotas en adopción.\n\n" +
                                 "Para completar tu registro, haz click en el siguiente enlace:\n" +
                                 registrationLink + "\n\n" +
                                 "Este enlace es único y personal. Úsalo para crear tu cuenta con el email: " + 
                                 foundationReq.getEmail() + "\n\n" +
                                 "¡Bienvenidos al equipo PawPaw!\n\n" +
                                 "Saludos,\nEquipo PawPaw";
                
                emailService.sendNotificationEmail(
                    foundationReq.getEmail(),
                    foundationReq.getContactName(),
                    subject,
                    message
                );
                
                session.setAttribute("successMessage", 
                    "Solicitud aprobada. Email enviado a " + foundationReq.getEmail());
                System.out.println("✅ Fundación " + idRequest + " aprobada. Email enviado.");
                
            } else {
                session.setAttribute("errorMessage", "Error al aprobar la solicitud");
            }
            
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al aprobar la solicitud");
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Rechazar solicitud de fundación
     */
    private void rejectFoundation(HttpServletRequest request, Integer adminId, HttpSession session) {
        try {
            Integer idRequest = Integer.parseInt(request.getParameter("idRequest"));
            String reason = request.getParameter("reason");
            
            FoundationRequest foundationReq = foundationDAO.findById(idRequest);
            
            if (foundationReq == null) {
                session.setAttribute("errorMessage", "Solicitud no encontrada");
                return;
            }
            
            if (foundationDAO.reject(idRequest, adminId, reason)) {
                // ✅ CORREGIDO: Usar sendNotificationEmail con 4 parámetros
                String subject = "Actualización de tu solicitud - PawPaw";
                String message = "Estimado/a " + foundationReq.getContactName() + ",\n\n" +
                                 "Gracias por tu interés en " + foundationReq.getFoundationName() + 
                                 " para formar parte de PawPaw.\n\n" +
                                 "Lamentablemente, en este momento no podemos aprobar tu solicitud.\n\n" +
                                 (reason != null && !reason.isEmpty() ? "Motivo: " + reason + "\n\n" : "") +
                                 "Si tienes preguntas, no dudes en contactarnos.\n\n" +
                                 "Saludos,\nEquipo PawPaw";
                
                emailService.sendNotificationEmail(
                    foundationReq.getEmail(),
                    foundationReq.getContactName(),
                    subject,
                    message
                );
                
                session.setAttribute("successMessage", "Solicitud rechazada. Email enviado.");
                System.out.println("✅ Fundación " + idRequest + " rechazada");
                
            } else {
                session.setAttribute("errorMessage", "Error al rechazar la solicitud");
            }
            
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al rechazar la solicitud");
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}