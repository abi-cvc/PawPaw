package model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Entidad para promociones de slots de mascotas
 */
public class Promotion {
    
    private Integer idPromotion;
    private String promoName;
    private String promoDescription;
    
    // Configuración de la oferta
    private Integer slotsQuantity;
    private BigDecimal promoPrice;
    private BigDecimal regularPrice;
    
    // Código promocional opcional
    private String promoCode;
    
    // Fechas de validez
    private Timestamp startDate;
    private Timestamp endDate;
    
    // Control
    private Boolean isActive;
    private Integer maxUses;
    private Integer currentUses;
    
    // Auditoría
    private Timestamp createdAt;
    private Integer createdBy;
    private Timestamp updatedAt;
    private Integer updatedBy;
    
    // Campos calculados (no en BD)
    private BigDecimal savings; // Ahorro calculado
    private String validityStatus; // Estado de validez
    
    /**
     * Constructor vacío
     */
    public Promotion() {
    }
    
    /**
     * Constructor para crear promoción simple
     */
    public Promotion(String promoName, Integer slotsQuantity, BigDecimal promoPrice, BigDecimal regularPrice) {
        this.promoName = promoName;
        this.slotsQuantity = slotsQuantity;
        this.promoPrice = promoPrice;
        this.regularPrice = regularPrice;
        this.isActive = true;
        this.currentUses = 0;
    }
    
    // Getters y Setters
    
    public Integer getIdPromotion() {
        return idPromotion;
    }
    
    public void setIdPromotion(Integer idPromotion) {
        this.idPromotion = idPromotion;
    }
    
    public String getPromoName() {
        return promoName;
    }
    
    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }
    
    public String getPromoDescription() {
        return promoDescription;
    }
    
    public void setPromoDescription(String promoDescription) {
        this.promoDescription = promoDescription;
    }
    
    public Integer getSlotsQuantity() {
        return slotsQuantity;
    }
    
    public void setSlotsQuantity(Integer slotsQuantity) {
        this.slotsQuantity = slotsQuantity;
    }
    
    public BigDecimal getPromoPrice() {
        return promoPrice;
    }
    
    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }
    
    public BigDecimal getRegularPrice() {
        return regularPrice;
    }
    
    public void setRegularPrice(BigDecimal regularPrice) {
        this.regularPrice = regularPrice;
    }
    
    public String getPromoCode() {
        return promoCode;
    }
    
    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
    
    public Timestamp getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
    
    public Timestamp getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Integer getMaxUses() {
        return maxUses;
    }
    
    public void setMaxUses(Integer maxUses) {
        this.maxUses = maxUses;
    }
    
    public Integer getCurrentUses() {
        return currentUses;
    }
    
    public void setCurrentUses(Integer currentUses) {
        this.currentUses = currentUses;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Integer getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Integer getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public BigDecimal getSavings() {
        return savings;
    }
    
    public void setSavings(BigDecimal savings) {
        this.savings = savings;
    }
    
    public String getValidityStatus() {
        return validityStatus;
    }
    
    public void setValidityStatus(String validityStatus) {
        this.validityStatus = validityStatus;
    }
    
    /**
     * Calcula el ahorro de la promoción
     */
    public BigDecimal calculateSavings() {
        if (regularPrice != null && promoPrice != null) {
            return regularPrice.subtract(promoPrice);
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Verifica si la promoción está vigente
     */
    public boolean isCurrentlyValid() {
        if (!isActive) return false;
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        
        // Verificar fecha de inicio
        if (startDate != null && now.before(startDate)) {
            return false;
        }
        
        // Verificar fecha de fin
        if (endDate != null && now.after(endDate)) {
            return false;
        }
        
        // Verificar límite de usos
        if (maxUses != null && currentUses != null && currentUses >= maxUses) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Obtiene texto de estado de validez
     */
    public String getValidityStatusText() {
        if (!isActive) {
            return "Desactivada";
        }
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        
        if (startDate != null && now.before(startDate)) {
            return "Programada";
        }
        
        if (endDate != null && now.after(endDate)) {
            return "Expirada";
        }
        
        if (maxUses != null && currentUses != null && currentUses >= maxUses) {
            return "Límite alcanzado";
        }
        
        return "Activa";
    }
    
    /**
     * Obtiene usos restantes
     */
    public String getUsesRemainingText() {
        if (maxUses == null) {
            return "Ilimitado";
        }
        
        int remaining = maxUses - (currentUses != null ? currentUses : 0);
        return remaining + " restante" + (remaining != 1 ? "s" : "");
    }
}