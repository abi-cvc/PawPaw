package model.entity;

import java.sql.Timestamp;

/**
 * Entidad para auditoría de ajustes de slots
 */
public class SlotAdjustment {
    
    private Integer idAdjustment;
    private Integer idUser;
    private Integer idAdmin;
    private Integer previousLimit;
    private Integer newLimit;
    private String adjustmentReason;
    private Timestamp createdAt;
    
    // Campos de la vista
    private String userName;
    private String userEmail;
    private String adminName;
    
    // Constructores
    
    public SlotAdjustment() {
    }
    
    public SlotAdjustment(Integer idUser, Integer idAdmin, Integer previousLimit, 
                         Integer newLimit, String adjustmentReason) {
        this.idUser = idUser;
        this.idAdmin = idAdmin;
        this.previousLimit = previousLimit;
        this.newLimit = newLimit;
        this.adjustmentReason = adjustmentReason;
    }
    
    // Getters y Setters
    
    public Integer getIdAdjustment() {
        return idAdjustment;
    }
    
    public void setIdAdjustment(Integer idAdjustment) {
        this.idAdjustment = idAdjustment;
    }
    
    public Integer getIdUser() {
        return idUser;
    }
    
    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
    
    public Integer getIdAdmin() {
        return idAdmin;
    }
    
    public void setIdAdmin(Integer idAdmin) {
        this.idAdmin = idAdmin;
    }
    
    public Integer getPreviousLimit() {
        return previousLimit;
    }
    
    public void setPreviousLimit(Integer previousLimit) {
        this.previousLimit = previousLimit;
    }
    
    public Integer getNewLimit() {
        return newLimit;
    }
    
    public void setNewLimit(Integer newLimit) {
        this.newLimit = newLimit;
    }
    
    public String getAdjustmentReason() {
        return adjustmentReason;
    }
    
    public void setAdjustmentReason(String adjustmentReason) {
        this.adjustmentReason = adjustmentReason;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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
    
    public String getAdminName() {
        return adminName;
    }
    
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }
    
    // Métodos útiles
    
    /**
     * Obtener el cambio (positivo o negativo)
     */
    public int getAdjustmentAmount() {
        if (newLimit == null || previousLimit == null) return 0;
        return newLimit - previousLimit;
    }
    
    /**
     * Verificar si fue un aumento
     */
    public boolean isIncrease() {
        return getAdjustmentAmount() > 0;
    }
    
    /**
     * Verificar si fue una disminución
     */
    public boolean isDecrease() {
        return getAdjustmentAmount() < 0;
    }
    
    /**
     * Obtener texto del cambio para mostrar
     */
    public String getAdjustmentText() {
        int amount = getAdjustmentAmount();
        if (amount > 0) {
            return "+" + amount;
        } else if (amount < 0) {
            return String.valueOf(amount);
        }
        return "0";
    }
    
    @Override
    public String toString() {
        return "SlotAdjustment{" +
                "idAdjustment=" + idAdjustment +
                ", userName='" + userName + '\'' +
                ", previous=" + previousLimit +
                ", new=" + newLimit +
                ", change=" + getAdjustmentAmount() +
                ", reason='" + adjustmentReason + '\'' +
                '}';
    }
}
