package controller;

import model.dao.PaymentRequestDAO;
import model.dao.UserDAO;
import model.entity.PaymentRequest;
import model.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet para gestionar pagos de slots (Admin)
 */
@WebServlet("/admin/payments")
public class ManagePaymentsServlet extends HttpServlet {
    
    private PaymentRequestDAO paymentDAO = new PaymentRequestDAO();
    private UserDAO userDAO = new UserDAO();
    
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
        
        List<PaymentRequest> payments;
        if (filter != null && !filter.isEmpty()) {
            payments = paymentDAO.findByStatus(filter);
        } else {
            payments = paymentDAO.findAll();
        }
        
        request.setAttribute("payments", payments);
        
        // Estadísticas
        int pendingCount = paymentDAO.countByStatus("pending");
        int completedCount = paymentDAO.countByStatus("completed");
        int rejectedCount = paymentDAO.countByStatus("rejected");
        
        request.setAttribute("totalPayments", payments.size());
        request.setAttribute("pendingPayments", pendingCount);
        request.setAttribute("completedPayments", completedCount);
        request.setAttribute("rejectedPayments", rejectedCount);
        request.setAttribute("currentFilter", filter);
        
        // Mensajes
        String successMessage = (String) session.getAttribute("successMessage");
        String errorMessage = (String) session.getAttribute("errorMessage");
        session.removeAttribute("successMessage");
        session.removeAttribute("errorMessage");
        
        request.setAttribute("successMessage", successMessage);
        request.setAttribute("errorMessage", errorMessage);
        
        System.out.println("💰 Admin viendo pagos. Total: " + payments.size());
        
        request.getRequestDispatcher("/view/admin/manage-payments.jsp").forward(request, response);
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
                approvePayment(request, adminId, session);
            } else if ("reject".equals(action)) {
                rejectPayment(request, adminId, session);
            } else if ("addNotes".equals(action)) {
                addNotes(request, session);
            }
        } catch (Exception e) {
            System.err.println("❌ Error en acción de pago: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error al procesar la solicitud");
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/payments");
    }
    
    /**
     * Aprobar pago manual
     */
    private void approvePayment(HttpServletRequest request, Integer adminId, HttpSession session) {
        try {
            Integer idPayment = Integer.parseInt(request.getParameter("idPayment"));
            PaymentRequest payment = paymentDAO.findById(idPayment);
            
            if (payment == null) {
                session.setAttribute("errorMessage", "Pago no encontrado");
                return;
            }
            
            // Actualizar estado del pago
            if (paymentDAO.updateStatus(idPayment, "completed", adminId)) {
                // Incrementar el límite del usuario
                if (userDAO.incrementPetLimit(payment.getIdUser(), payment.getSlotsPurchased())) {
                    session.setAttribute("successMessage", 
                        "Pago aprobado. Se agregaron " + payment.getSlotsPurchased() + " slots al usuario");
                    System.out.println("✅ Pago " + idPayment + " aprobado. Slots agregados al usuario " + payment.getIdUser());
                } else {
                    session.setAttribute("errorMessage", "Pago aprobado pero error al actualizar límite");
                }
            } else {
                session.setAttribute("errorMessage", "Error al aprobar el pago");
            }
            
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al aprobar el pago");
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Rechazar pago manual
     */
    private void rejectPayment(HttpServletRequest request, Integer adminId, HttpSession session) {
        try {
            Integer idPayment = Integer.parseInt(request.getParameter("idPayment"));
            String reason = request.getParameter("reason");
            
            if (paymentDAO.updateStatus(idPayment, "rejected", adminId)) {
                // Agregar razón como nota
                if (reason != null && !reason.isEmpty()) {
                    paymentDAO.updateAdminNotes(idPayment, "Rechazado: " + reason);
                }
                session.setAttribute("successMessage", "Pago rechazado");
                System.out.println("✅ Pago " + idPayment + " rechazado");
            } else {
                session.setAttribute("errorMessage", "Error al rechazar el pago");
            }
            
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al rechazar el pago");
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    /**
     * Agregar notas administrativas
     */
    private void addNotes(HttpServletRequest request, HttpSession session) {
        try {
            Integer idPayment = Integer.parseInt(request.getParameter("idPayment"));
            String notes = request.getParameter("notes");
            
            if (paymentDAO.updateAdminNotes(idPayment, notes)) {
                session.setAttribute("successMessage", "Notas agregadas");
                System.out.println("✅ Notas agregadas al pago " + idPayment);
            } else {
                session.setAttribute("errorMessage", "Error al agregar notas");
            }
            
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error al agregar notas");
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}