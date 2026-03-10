package model.entity;

import java.sql.Timestamp;

/**
 * Entidad para solicitudes de transferencia de mascotas
 * Usado cuando una fundación quiere transferir una mascota adoptada a su nuevo dueño
 */
public class PetTransferRequest {
    
    private Integer idTransfer;
    private Integer idPet;
    private Integer idFoundation;
    private String adopterEmail;
    private String adopterName;
    private String adopterPhone;
    private String transferToken;
    private String status; // pending, accepted, rejected, expired
    private String message;
    private Timestamp createdAt;
    private Timestamp acceptedAt;
    private Timestamp expiresAt;
    
    // Info adicional para mostrar en vistas
    private String petName;
    private String foundationName;
    
    /**
     * Constructor vacío
     */
    public PetTransferRequest() {
    }
    
    /**
     * Constructor para crear nueva solicitud
     */
    public PetTransferRequest(Integer idPet, Integer idFoundation, String adopterEmail, 
                             String adopterName, String transferToken) {
        this.idPet = idPet;
        this.idFoundation = idFoundation;
        this.adopterEmail = adopterEmail;
        this.adopterName = adopterName;
        this.transferToken = transferToken;
        this.status = "pending";
    }
    
    // Getters y Setters
    
    public Integer getIdTransfer() {
        return idTransfer;
    }
    
    public void setIdTransfer(Integer idTransfer) {
        this.idTransfer = idTransfer;
    }
    
    public Integer getIdPet() {
        return idPet;
    }
    
    public void setIdPet(Integer idPet) {
        this.idPet = idPet;
    }
    
    public Integer getIdFoundation() {
        return idFoundation;
    }
    
    public void setIdFoundation(Integer idFoundation) {
        this.idFoundation = idFoundation;
    }
    
    public String getAdopterEmail() {
        return adopterEmail;
    }
    
    public void setAdopterEmail(String adopterEmail) {
        this.adopterEmail = adopterEmail;
    }
    
    public String getAdopterName() {
        return adopterName;
    }
    
    public void setAdopterName(String adopterName) {
        this.adopterName = adopterName;
    }
    
    public String getAdopterPhone() {
        return adopterPhone;
    }
    
    public void setAdopterPhone(String adopterPhone) {
        this.adopterPhone = adopterPhone;
    }
    
    public String getTransferToken() {
        return transferToken;
    }
    
    public void setTransferToken(String transferToken) {
        this.transferToken = transferToken;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getAcceptedAt() {
        return acceptedAt;
    }
    
    public void setAcceptedAt(Timestamp acceptedAt) {
        this.acceptedAt = acceptedAt;
    }
    
    public Timestamp getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public String getPetName() {
        return petName;
    }
    
    public void setPetName(String petName) {
        this.petName = petName;
    }
    
    public String getFoundationName() {
        return foundationName;
    }
    
    public void setFoundationName(String foundationName) {
        this.foundationName = foundationName;
    }
    
    // Métodos útiles
    
    /**
     * Verifica si la transferencia está pendiente
     */
    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }
    
    /**
     * Verifica si la transferencia fue aceptada
     */
    public boolean isAccepted() {
        return "accepted".equalsIgnoreCase(status);
    }
    
    /**
     * Verifica si la transferencia fue rechazada
     */
    public boolean isRejected() {
        return "rejected".equalsIgnoreCase(status);
    }
    
    /**
     * Verifica si la transferencia expiró
     */
    public boolean isExpired() {
        if ("expired".equalsIgnoreCase(status)) {
            return true;
        }
        
        if (expiresAt != null && isPending()) {
            return expiresAt.before(new Timestamp(System.currentTimeMillis()));
        }
        
        return false;
    }
    
    /**
     * Obtiene el texto del estado en español
     */
    public String getStatusText() {
        if (status == null) return "Desconocido";
        
        switch (status.toLowerCase()) {
            case "pending":
                return isExpired() ? "Expirado" : "Pendiente";
            case "accepted":
                return "Aceptado";
            case "rejected":
                return "Rechazado";
            case "expired":
                return "Expirado";
            default:
                return status;
        }
    }
    
    /**
     * Obtiene la clase CSS según el estado
     */
    public String getStatusClass() {
        if (status == null) return "";
        
        if (isExpired()) {
            return "status-expired";
        }
        
        switch (status.toLowerCase()) {
            case "pending":
                return "status-pending";
            case "accepted":
                return "status-active";
            case "rejected":
                return "status-inactive";
            case "expired":
                return "status-expired";
            default:
                return "";
        }
    }
    
    /**
     * Calcula días restantes para aceptar
     */
    public long getDaysRemaining() {
        if (expiresAt == null || !isPending()) {
            return 0;
        }
        
        long diff = expiresAt.getTime() - System.currentTimeMillis();
        return diff / (1000 * 60 * 60 * 24);
    }
    
    /**
     * Verifica si está próximo a expirar (menos de 2 días)
     */
    public boolean isExpiringSoon() {
        return isPending() && getDaysRemaining() <= 2 && getDaysRemaining() > 0;
    }
    
    @Override
    public String toString() {
        return "PetTransferRequest{" +
                "idTransfer=" + idTransfer +
                ", idPet=" + idPet +
                ", adopterName='" + adopterName + '\'' +
                ", adopterEmail='" + adopterEmail + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
