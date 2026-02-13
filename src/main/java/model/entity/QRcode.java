package model.entity;

import java.sql.Timestamp;

/**
 * Modelo de entidad QRCode
 * Representa un código QR generado para una mascota
 */
public class QRcode {
    
    private Integer idQR;
    private Integer idPet;
    private String url;
    private Timestamp generationDate;
    private Boolean active;
    private Integer scansCount;
    private Timestamp lastScanDate;
    
    // Constructores
    
    /**
     * Constructor vacío
     */
    public QRcode() {
    }
    
    /**
     * Constructor para generación de nuevo QR
     */
    public QRcode(Integer idPet, String url) {
        this.idPet = idPet;
        this.url = url;
        this.active = true;
        this.scansCount = 0;
    }
    
    /**
     * Constructor completo
     */
    public QRcode(Integer idQR, Integer idPet, String url, Timestamp generationDate, 
                  Boolean active, Integer scansCount, Timestamp lastScanDate) {
        this.idQR = idQR;
        this.idPet = idPet;
        this.url = url;
        this.generationDate = generationDate;
        this.active = active;
        this.scansCount = scansCount;
        this.lastScanDate = lastScanDate;
    }
    
    // Getters y Setters
    
    public Integer getIdQR() {
        return idQR;
    }
    
    public void setIdQR(Integer idQR) {
        this.idQR = idQR;
    }
    
    public Integer getIdPet() {
        return idPet;
    }
    
    public void setIdPet(Integer idPet) {
        this.idPet = idPet;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Timestamp getGenerationDate() {
        return generationDate;
    }
    
    public void setGenerationDate(Timestamp generationDate) {
        this.generationDate = generationDate;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public Integer getScansCount() {
        return scansCount;
    }
    
    public void setScansCount(Integer scansCount) {
        this.scansCount = scansCount;
    }
    
    public Timestamp getLastScanDate() {
        return lastScanDate;
    }
    
    public void setLastScanDate(Timestamp lastScanDate) {
        this.lastScanDate = lastScanDate;
    }
    
    // Métodos útiles
    
    public void incrementScanCount() {
        if (this.scansCount == null) {
            this.scansCount = 0;
        }
        this.scansCount++;
    }
    
    @Override
    public String toString() {
        return "QRCode{" +
                "idQR=" + idQR +
                ", idPet=" + idPet +
                ", url='" + url + '\'' +
                ", active=" + active +
                ", scansCount=" + scansCount +
                ", generationDate=" + generationDate +
                ", lastScanDate=" + lastScanDate +
                '}';
    }
}