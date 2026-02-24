package model.entity;

import java.sql.Timestamp;

/**
 * Entidad que representa una sugerencia enviada por un usuario
 */
public class Suggestion {
    
    private Integer idSuggestion;
    private Integer idUser;
    private String message;
    private Timestamp submissionDate;
    private String statusSuggestion; // pending, reviewed, resolved, rejected
    private String adminResponse;
    private Timestamp responseDate;
    
    // Información adicional del usuario (para mostrar en listados)
    private String userName;
    private String userEmail;
    
    /**
     * Constructor vacío
     */
    public Suggestion() {
    }
    
    /**
     * Constructor para crear nueva sugerencia
     */
    public Suggestion(Integer idUser, String message) {
        this.idUser = idUser;
        this.message = message;
        this.statusSuggestion = "pending";
    }
    
    // Getters y Setters
    
    public Integer getIdSuggestion() {
        return idSuggestion;
    }
    
    public void setIdSuggestion(Integer idSuggestion) {
        this.idSuggestion = idSuggestion;
    }
    
    public Integer getIdUser() {
        return idUser;
    }
    
    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Timestamp getSubmissionDate() {
        return submissionDate;
    }
    
    public void setSubmissionDate(Timestamp submissionDate) {
        this.submissionDate = submissionDate;
    }
    
    public String getStatusSuggestion() {
        return statusSuggestion;
    }
    
    public void setStatusSuggestion(String statusSuggestion) {
        this.statusSuggestion = statusSuggestion;
    }
    
    public String getAdminResponse() {
        return adminResponse;
    }
    
    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }
    
    public Timestamp getResponseDate() {
        return responseDate;
    }
    
    public void setResponseDate(Timestamp responseDate) {
        this.responseDate = responseDate;
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
    
    /**
     * Obtiene el texto en español del estado
     */
    public String getStatusText() {
        if (statusSuggestion == null) return "Desconocido";
        
        switch (statusSuggestion.toLowerCase()) {
            case "pending":
                return "Pendiente";
            case "reviewed":
                return "Revisada";
            case "resolved":
                return "Resuelta";
            case "rejected":
                return "Rechazada";
            default:
                return statusSuggestion;
        }
    }
    
    /**
     * Obtiene la clase CSS según el estado
     */
    public String getStatusClass() {
        if (statusSuggestion == null) return "status-default";
        
        switch (statusSuggestion.toLowerCase()) {
            case "pending":
                return "status-pending";
            case "reviewed":
                return "status-reviewed";
            case "resolved":
                return "status-resolved";
            case "rejected":
                return "status-rejected";
            default:
                return "status-default";
        }
    }
}