package model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Entidad para solicitudes de pago de slots adicionales
 */
public class PaymentRequest {
    
    private Integer idPayment;
    private Integer idUser;
    private Integer idPromotion;
    
    private BigDecimal amount;
    private Integer slotsPurchased;
    private String paymentMethod;      // paypal, deuna, manual
    private String paymentStatus;      // pending, completed, rejected
    
    // PayPal específico
    private String paypalTransactionId;
    private String paypalPayerId;
    private String paypalOrderId;
    
    // Pago manual
    private String paymentProof;
    
    // Auditoría
    private Timestamp createdAt;
    private Timestamp verifiedAt;
    private Integer verifiedBy;
    private String adminNotes;
    
    // Información adicional (para mostrar en admin)
    private String userName;
    private String userEmail;
    private String promoName;
    
    /**
     * Constructor vacío
     */
    public PaymentRequest() {
    }
    
    /**
     * Constructor para pago PayPal
     */
    public PaymentRequest(Integer idUser, BigDecimal amount, Integer slotsPurchased, String paypalOrderId) {
        this.idUser = idUser;
        this.amount = amount;
        this.slotsPurchased = slotsPurchased;
        this.paymentMethod = "paypal";
        this.paymentStatus = "pending";
        this.paypalOrderId = paypalOrderId;
    }
    
    /**
     * Constructor para pago manual
     */
    public PaymentRequest(Integer idUser, BigDecimal amount, Integer slotsPurchased, 
                         String paymentMethod, String paymentProof) {
        this.idUser = idUser;
        this.amount = amount;
        this.slotsPurchased = slotsPurchased;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = "pending";
        this.paymentProof = paymentProof;
    }
    
    // Getters y Setters
    
    public Integer getIdPayment() {
        return idPayment;
    }
    
    public void setIdPayment(Integer idPayment) {
        this.idPayment = idPayment;
    }
    
    public Integer getIdUser() {
        return idUser;
    }
    
    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
    
    public Integer getIdPromotion() {
        return idPromotion;
    }
    
    public void setIdPromotion(Integer idPromotion) {
        this.idPromotion = idPromotion;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Integer getSlotsPurchased() {
        return slotsPurchased;
    }
    
    public void setSlotsPurchased(Integer slotsPurchased) {
        this.slotsPurchased = slotsPurchased;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public String getPaypalTransactionId() {
        return paypalTransactionId;
    }
    
    public void setPaypalTransactionId(String paypalTransactionId) {
        this.paypalTransactionId = paypalTransactionId;
    }
    
    public String getPaypalPayerId() {
        return paypalPayerId;
    }
    
    public void setPaypalPayerId(String paypalPayerId) {
        this.paypalPayerId = paypalPayerId;
    }
    
    public String getPaypalOrderId() {
        return paypalOrderId;
    }
    
    public void setPaypalOrderId(String paypalOrderId) {
        this.paypalOrderId = paypalOrderId;
    }
    
    public String getPaymentProof() {
        return paymentProof;
    }
    
    public void setPaymentProof(String paymentProof) {
        this.paymentProof = paymentProof;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getVerifiedAt() {
        return verifiedAt;
    }
    
    public void setVerifiedAt(Timestamp verifiedAt) {
        this.verifiedAt = verifiedAt;
    }
    
    public Integer getVerifiedBy() {
        return verifiedBy;
    }
    
    public void setVerifiedBy(Integer verifiedBy) {
        this.verifiedBy = verifiedBy;
    }
    
    public String getAdminNotes() {
        return adminNotes;
    }
    
    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public String getPromoName() {
        return promoName;
    }
    
    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }
    
    /**
     * Obtiene texto en español del estado
     */
    public String getStatusText() {
        if (paymentStatus == null) return "Desconocido";
        
        switch (paymentStatus.toLowerCase()) {
            case "pending":
                return "Pendiente";
            case "completed":
                return "Completado";
            case "rejected":
                return "Rechazado";
            default:
                return paymentStatus;
        }
    }
    
    /**
     * Obtiene clase CSS según estado
     */
    public String getStatusClass() {
        if (paymentStatus == null) return "";
        
        switch (paymentStatus.toLowerCase()) {
            case "pending":
                return "status-pending";
            case "completed":
                return "status-completed";
            case "rejected":
                return "status-rejected";
            default:
                return "";
        }
    }
    
    /**
     * Obtiene texto del método de pago
     */
    public String getPaymentMethodText() {
        if (paymentMethod == null) return "Desconocido";
        
        switch (paymentMethod.toLowerCase()) {
            case "paypal":
                return "PayPal";
            case "deuna":
                return "DeUna";
            case "manual":
                return "Manual";
            default:
                return paymentMethod;
        }
    }
}