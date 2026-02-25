package model.entity;

import java.sql.Timestamp;

/**
 * Entidad para solicitudes de fundaciones
 */
public class FoundationRequest {
    
    private Integer idRequest;
    
    // Información de la fundación
    private String foundationName;
    private String contactName;
    private String email;
    private String phone;
    private String whatsapp;
    
    // Detalles
    private String animalTypes;
    private Integer currentAnimals;
    private String description;
    private String website;
    
    // Estado
    private String status;  // pending, approved, rejected
    private String registrationToken;
    
    // Auditoría
    private Timestamp createdAt;
    private Timestamp reviewedAt;
    private Integer reviewedBy;
    private String rejectionReason;
    
    // Info adicional (para mostrar en admin)
    private String reviewerName;
    
    /**
     * Constructor vacío
     */
    public FoundationRequest() {
    }
    
    /**
     * Constructor básico
     */
    public FoundationRequest(String foundationName, String contactName, String email, 
                           String phone, String animalTypes, Integer currentAnimals) {
        this.foundationName = foundationName;
        this.contactName = contactName;
        this.email = email;
        this.phone = phone;
        this.animalTypes = animalTypes;
        this.currentAnimals = currentAnimals;
        this.status = "pending";
    }
    
    // Getters y Setters
    
    public Integer getIdRequest() {
        return idRequest;
    }
    
    public void setIdRequest(Integer idRequest) {
        this.idRequest = idRequest;
    }
    
    public String getFoundationName() {
        return foundationName;
    }
    
    public void setFoundationName(String foundationName) {
        this.foundationName = foundationName;
    }
    
    public String getContactName() {
        return contactName;
    }
    
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getWhatsapp() {
        return whatsapp;
    }
    
    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }
    
    public String getAnimalTypes() {
        return animalTypes;
    }
    
    public void setAnimalTypes(String animalTypes) {
        this.animalTypes = animalTypes;
    }
    
    public Integer getCurrentAnimals() {
        return currentAnimals;
    }
    
    public void setCurrentAnimals(Integer currentAnimals) {
        this.currentAnimals = currentAnimals;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getRegistrationToken() {
        return registrationToken;
    }
    
    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getReviewedAt() {
        return reviewedAt;
    }
    
    public void setReviewedAt(Timestamp reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
    
    public Integer getReviewedBy() {
        return reviewedBy;
    }
    
    public void setReviewedBy(Integer reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
    
    public String getRejectionReason() {
        return rejectionReason;
    }
    
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    
    public String getReviewerName() {
        return reviewerName;
    }
    
    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }
    
    /**
     * Obtiene texto del estado en español
     */
    public String getStatusText() {
        if (status == null) return "Desconocido";
        
        switch (status.toLowerCase()) {
            case "pending":
                return "Pendiente";
            case "approved":
                return "Aprobada";
            case "rejected":
                return "Rechazada";
            default:
                return status;
        }
    }
    
    /**
     * Obtiene clase CSS según estado
     */
    public String getStatusClass() {
        if (status == null) return "";
        
        switch (status.toLowerCase()) {
            case "pending":
                return "status-pending";
            case "approved":
                return "status-approved";
            case "rejected":
                return "status-rejected";
            default:
                return "";
        }
    }
    
    /**
     * Verifica si está pendiente
     */
    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }
    
    /**
     * Verifica si está aprobada
     */
    public boolean isApproved() {
        return "approved".equalsIgnoreCase(status);
    }
    
    /**
     * Verifica si está rechazada
     */
    public boolean isRejected() {
        return "rejected".equalsIgnoreCase(status);
    }
}