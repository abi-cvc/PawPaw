package model.entity;

import java.sql.Timestamp;

/**
 * Modelo de entidad Suggestion
 * Representa una sugerencia enviada por un usuario
 */
public class Suggestion {
    
    private Integer idSuggestion;
    private Integer idUser;
    private String message;
    private Timestamp submissionDate;
    private String statusSuggestion;
    private String adminResponse;
    private Timestamp responseDate;
    
    // Constructores
    
    /**
     * Constructor vacío
     */
    public Suggestion() {
    }
    
    /**
     * Constructor para nueva sugerencia
     */
    public Suggestion(Integer idUser, String message) {
        this.idUser = idUser;
        this.message = message;
        this.statusSuggestion = "pending"; // Estado por defecto
    }
    
    /**
     * Constructor completo
     */
    public Suggestion(Integer idSuggestion, Integer idUser, String message, 
                     Timestamp submissionDate, String statusSuggestion, 
                     String adminResponse, Timestamp responseDate) {
        this.idSuggestion = idSuggestion;
        this.idUser = idUser;
        this.message = message;
        this.submissionDate = submissionDate;
        this.statusSuggestion = statusSuggestion;
        this.adminResponse = adminResponse;
        this.responseDate = responseDate;
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
    
    // Métodos útiles
    
    public boolean isPending() {
        return "pending".equalsIgnoreCase(this.statusSuggestion);
    }
    
    public boolean isResolved() {
        return "resolved".equalsIgnoreCase(this.statusSuggestion);
    }
    
    public boolean hasResponse() {
        return adminResponse != null && !adminResponse.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return "Suggestion{" +
                "idSuggestion=" + idSuggestion +
                ", idUser=" + idUser +
                ", message='" + message + '\'' +
                ", submissionDate=" + submissionDate +
                ", statusSuggestion='" + statusSuggestion + '\'' +
                ", hasResponse=" + hasResponse() +
                '}';
    }
}