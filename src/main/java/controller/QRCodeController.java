package controller;

import model.dao.QRCodeDAO;
import model.dao.PetDAO;
import model.entity.QRcode;
import model.entity.Pet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Controlador para gestionar códigos QR
 */
@WebServlet("/user/qr-codes")
public class QRCodeController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private QRCodeDAO qrCodeDAO = new QRCodeDAO();
    private PetDAO petDAO = new PetDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Verificar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        Integer userId = (Integer) session.getAttribute("userId");
        
        System.out.println("🔵 Cargando códigos QR para usuario: " + userId);
        
        // Obtener todas las mascotas del usuario con sus QR
        List<Pet> pets = petDAO.findByUserId(userId);
        
        System.out.println("   Mascotas encontradas: " + pets.size());
        
        // Para cada mascota, obtener o generar su QR
        List<QRCodeData> qrDataList = new ArrayList<>();
        
        for (Pet pet : pets) {
            QRcode qr = qrCodeDAO.findByPetId(pet.getIdPet());
            
            // Si no existe QR, generar uno automáticamente
            if (qr == null) {
                System.out.println("   📝 Generando QR para: " + pet.getNamePet());
                qr = generateQRForPet(pet);
            } else {
                System.out.println("   ✅ QR existente para: " + pet.getNamePet());
            }
            
            // Agregar a la lista con los datos combinados
            qrDataList.add(new QRCodeData(pet, qr));
        }
        
        System.out.println("✅ Total QRs procesados: " + qrDataList.size());
        
        // Pasar datos a la vista
        request.setAttribute("qrDataList", qrDataList);
        request.setAttribute("totalQRs", qrDataList.size());
        
        request.getRequestDispatcher("/view/internalUser/qr-codes.jsp").forward(request, response);
    }
    
    /**
     * Genera un código QR para una mascota
     */
    private QRcode generateQRForPet(Pet pet) {
        // URL base - cambiar por tu dominio real en producción
        String baseUrl = "https://pawpaw.app/pet/";
        String qrUrl = baseUrl + pet.getIdPet();
        
        QRcode qr = new QRcode();
        qr.setIdPet(pet.getIdPet());
        qr.setUrl(qrUrl);
        qr.setActive(true);
        qr.setScansCount(0);
        
        // Guardar en BD
        if (qrCodeDAO.create(qr)) {
            System.out.println("   ✅ QR guardado en BD - ID: " + qr.getIdQR() + " - URL: " + qrUrl);
        } else {
            System.out.println("   ❌ Error al guardar QR en BD");
        }
        
        return qr;
    }
    
    /**
     * Clase interna para combinar datos de mascota y QR
     */
    public static class QRCodeData {
        private Pet pet;
        private QRcode qrCode;
        
        public QRCodeData(Pet pet, QRcode qrCode) {
            this.pet = pet;
            this.qrCode = qrCode;
        }
        
        public Pet getPet() {
            return pet;
        }
        
        public QRcode getQrCode() {
            return qrCode;
        }
    }
}