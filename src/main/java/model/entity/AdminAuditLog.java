package model.entity;

import java.sql.Timestamp;

/**
 * Entidad para el registro de auditoría de acciones administrativas
 * Permite rastrear TODOS los cambios que hacen los administradores
 */
public class AdminAuditLog {
    
    private Integer idAudit;
    private Integer idAdmin;
    private String action;          // LOGIN, UPDATE_STATUS, ADD_RESPONSE, DELETE_SUGGESTION
    private String targetType;      // suggestion, user, pet
    private Integer targetId;       // ID del registro afectado
    private String oldValue;        // Valor anterior
    private String newValue;        // Valor nuevo
    private String ipAddress;       // IP del admin
    private String userAgent;       // Navegador usado
    private Timestamp timestamp;
    private String details;         // Información adicional
    
    // Información adicional del admin (para mostrar en vistas)
    private String adminName;
    private String adminEmail;
    
    /**
     * Constructor vacío
     */
    public AdminAuditLog() {
    }
    
    /**
     * Constructor para crear un log básico
     */
    public AdminAuditLog(Integer idAdmin, String action) {
        this.idAdmin = idAdmin;
        this.action = action;
    }
    
    /**
     * Constructor completo para cambios en sugerencias
     */
    public AdminAuditLog(Integer idAdmin, String action, String targetType, Integer targetId, 
                         String oldValue, String newValue, String ipAddress) {
        this.idAdmin = idAdmin;
        this.action = action;
        this.targetType = targetType;
        this.targetId = targetId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.ipAddress = ipAddress;
    }
    
    // Getters y Setters
    
    public Integer getIdAudit() {
        return idAudit;
    }
    
    public void setIdAudit(Integer idAudit) {
        this.idAudit = idAudit;
    }
    
    public Integer getIdAdmin() {
        return idAdmin;
    }
    
    public void setIdAdmin(Integer idAdmin) {
        this.idAdmin = idAdmin;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getTargetType() {
        return targetType;
    }
    
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
    
    public Integer getTargetId() {
        return targetId;
    }
    
    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }
    
    public String getOldValue() {
        return oldValue;
    }
    
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
    
    public String getNewValue() {
        return newValue;
    }
    
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public Timestamp getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
    
    public String getAdminName() {
        return adminName;
    }
    
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
    
    public String getAdminEmail() {
        return adminEmail;
    }
    
    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
    
    /**
     * Obtiene el texto en español de la acción
     */
    public String getActionText() {
        if (action == null) return "Acción desconocida";
        
        switch (action.toUpperCase()) {
            case "LOGIN":
                return "Inicio de sesión";
            case "UPDATE_STATUS":
                return "Actualización de estado";
            case "ADD_RESPONSE":
                return "Respuesta agregada";
            case "DELETE_SUGGESTION":
                return "Sugerencia eliminada";
            case "UPDATE_SUGGESTION":
                return "Sugerencia actualizada";
            default:
                return action;
        }
    }
    
    /**
     * Obtiene descripción legible del cambio
     */
    public String getChangeDescription() {
        if (oldValue == null && newValue == null) {
            return "Sin cambios específicos";
        }
        
        if (oldValue == null) {
            return "Nuevo valor: " + newValue;
        }
        
        if (newValue == null) {
            return "Valor anterior: " + oldValue;
        }
        
        return oldValue + " → " + newValue;
    }
}