package model.entity;

import java.sql.Timestamp;

/**
 * Entidad para mensajes de contacto recibidos desde vista pública
 */
public class PetContactMessage {
    
    private Integer idMessage;
    private Integer idPet;
    private Integer idUser;
    private String senderName;
    private String senderPhone;
    private String message;
    private Timestamp createdAt;
    private Boolean isRead;
    
    // Campos adicionales de la vista
    private String petName;
    private String petPhoto;
    private String petBreed;
    private String ownerName;
    
    // Constructores
    
    public PetContactMessage() {
    }
    
    public PetContactMessage(Integer idPet, Integer idUser, String senderName, 
                           String senderPhone, String message) {
        this.idPet = idPet;
        this.idUser = idUser;
        this.senderName = senderName;
        this.senderPhone = senderPhone;
        this.message = message;
        this.isRead = false;
    }
    
    // Getters y Setters
    
    public Integer getIdMessage() {
        return idMessage;
    }
    
    public void setIdMessage(Integer idMessage) {
        this.idMessage = idMessage;
    }
    
    public Integer getIdPet() {
        return idPet;
    }
    
    public void setIdPet(Integer idPet) {
        this.idPet = idPet;
    }
    
    public Integer getIdUser() {
        return idUser;
    }
    
    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
    
    public String getSenderName() {
        return senderName;
    }
    
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    
    public String getSenderPhone() {
        return senderPhone;
    }
    
    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
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
    
    public Boolean getIsRead() {
        return isRead;
    }
    
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
    
    public String getPetName() {
        return petName;
    }
    
    public void setPetName(String petName) {
        this.petName = petName;
    }
    
    public String getPetPhoto() {
        return petPhoto;
    }
    
    public void setPetPhoto(String petPhoto) {
        this.petPhoto = petPhoto;
    }
    
    public String getPetBreed() {
        return petBreed;
    }
    
    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }
    
    public String getOwnerName() {
        return ownerName;
    }
    
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    
    // Métodos útiles
    
    /**
     * Verifica si el mensaje no ha sido leído
     */
    public boolean isUnread() {
        return isRead != null && !isRead;
    }
    
    /**
     * Obtiene el tiempo transcurrido desde que se envió
     */
    public String getTimeAgo() {
        if (createdAt == null) return "";
        
        long diff = System.currentTimeMillis() - createdAt.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return days == 1 ? "Hace 1 día" : "Hace " + days + " días";
        } else if (hours > 0) {
            return hours == 1 ? "Hace 1 hora" : "Hace " + hours + " horas";
        } else if (minutes > 0) {
            return minutes == 1 ? "Hace 1 minuto" : "Hace " + minutes + " minutos";
        } else {
            return "Hace unos segundos";
        }
    }
    
    /**
     * Preview corto del mensaje (primeros 100 caracteres)
     */
    public String getMessagePreview() {
        if (message == null) return "";
        if (message.length() <= 100) return message;
        return message.substring(0, 97) + "...";
    }
    
    @Override
    public String toString() {
        return "PetContactMessage{" +
                "idMessage=" + idMessage +
                ", petName='" + petName + '\'' +
                ", senderName='" + senderName + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}
