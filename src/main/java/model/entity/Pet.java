package model.entity;

import java.sql.Timestamp;

/**
 * Modelo de entidad Pet
 * Representa una mascota registrada en el sistema PawPaw
 */
public class Pet {
    
    private Integer idPet;
    private Integer idUser;
    private String namePet;
    private Integer agePet;
    private String breed;
    private String sexPet;
    private String medicalConditions;
    private String contactPhone;
    private String photo; // URL de la imagen
    private String statusPet;
    private Timestamp creationDate;
    private String extraComments;
    private String adoptionStatus; // owned, available, adopted_pending, adopted_transferred
    
    // Constructores
    
    /**
     * Constructor vacío
     */
    public Pet() {
    }
    
    /**
     * Constructor para creación de nueva mascota
     */
    public Pet(Integer idUser, String namePet, Integer agePet, String breed, 
               String sexPet, String contactPhone) {
        this.idUser = idUser;
        this.namePet = namePet;
        this.agePet = agePet;
        this.breed = breed;
        this.sexPet = sexPet;
        this.contactPhone = contactPhone;
        this.statusPet = "active"; // Estado por defecto
        this.adoptionStatus = "owned";
    }
    
    /**
     * Constructor completo
     */
    public Pet(Integer idPet, Integer idUser, String namePet, Integer agePet, 
               String breed, String sexPet, String medicalConditions, String contactPhone, 
               String photo, String statusPet, Timestamp creationDate, String extraComments) {
        this.idPet = idPet;
        this.idUser = idUser;
        this.namePet = namePet;
        this.agePet = agePet;
        this.breed = breed;
        this.sexPet = sexPet;
        this.medicalConditions = medicalConditions;
        this.contactPhone = contactPhone;
        this.photo = photo;
        this.statusPet = statusPet;
        this.creationDate = creationDate;
        this.extraComments = extraComments;
    }
    
    // Getters y Setters
    
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
    
    public String getNamePet() {
        return namePet;
    }
    
    public void setNamePet(String namePet) {
        this.namePet = namePet;
    }
    
    public Integer getAgePet() {
        return agePet;
    }
    
    public void setAgePet(Integer agePet) {
        this.agePet = agePet;
    }
    
    public String getBreed() {
        return breed;
    }
    
    public void setBreed(String breed) {
        this.breed = breed;
    }
    
    public String getSexPet() {
        return sexPet;
    }
    
    public void setSexPet(String sexPet) {
        this.sexPet = sexPet;
    }
    
    public String getMedicalConditions() {
        return medicalConditions;
    }
    
    public void setMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
    }
    
    public String getContactPhone() {
        return contactPhone;
    }
    
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    
    public String getPhoto() {
        return photo;
    }
    
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    
    public String getStatusPet() {
        return statusPet;
    }
    
    public void setStatusPet(String statusPet) {
        this.statusPet = statusPet;
    }
    
    public Timestamp getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }
    
    public String getExtraComments() {
        return extraComments;
    }
    
    public void setExtraComments(String extraComments) {
        this.extraComments = extraComments;
    }
    
    public String getAdoptionStatus() {
        return adoptionStatus;
    }

    public void setAdoptionStatus(String adoptionStatus) {
        this.adoptionStatus = adoptionStatus;
    }
    
    // Métodos útiles
    
    public boolean isActive() {
        return "active".equalsIgnoreCase(this.statusPet);
    }
    
    public boolean isLost() {
        return "lost".equalsIgnoreCase(this.statusPet);
    }
    
    @Override
    public String toString() {
        return "Pet{" +
                "idPet=" + idPet +
                ", namePet='" + namePet + '\'' +
                ", agePet=" + agePet +
                ", breed='" + breed + '\'' +
                ", sexPet='" + sexPet + '\'' +
                ", statusPet='" + statusPet + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
    
 // ✅ MÉTODOS ÚTILES para adopciones

    /**
     * Verifica si la mascota está disponible para adopción
     */
    public boolean isAvailableForAdoption() {
        return "available".equalsIgnoreCase(adoptionStatus);
    }

    /**
     * Verifica si la mascota fue adoptada
     */
    public boolean isAdopted() {
        return adoptionStatus != null && adoptionStatus.toLowerCase().startsWith("adopted");
    }

    /**
     * Verifica si la transferencia está pendiente
     */
    public boolean isAdoptionPending() {
        return "adopted_pending".equalsIgnoreCase(adoptionStatus);
    }

    /**
     * Verifica si la transferencia fue completada
     */
    public boolean isAdoptionTransferred() {
        return "adopted_transferred".equalsIgnoreCase(adoptionStatus);
    }

    /**
     * Obtiene el texto del estado de adopción en español
     */
    public String getAdoptionStatusText() {
        if (adoptionStatus == null || "owned".equalsIgnoreCase(adoptionStatus)) {
            return "Propia";
        }
        
        switch (adoptionStatus.toLowerCase()) {
            case "available":
                return "🏠 En Adopción";
            case "adopted_pending":
                return "🔄 Adoptado (Pendiente)";
            case "adopted_transferred":
                return "✅ Adoptado (Transferido)";
            default:
                return adoptionStatus;
        }
    }

    /**
     * Obtiene la clase CSS del badge según estado
     */
    public String getAdoptionStatusClass() {
        if (adoptionStatus == null || "owned".equalsIgnoreCase(adoptionStatus)) {
            return "adoption-owned";
        }
        
        switch (adoptionStatus.toLowerCase()) {
            case "available":
                return "adoption-available";
            case "adopted_pending":
                return "adoption-pending";
            case "adopted_transferred":
                return "adoption-transferred";
            default:
                return "";
        }
    }
}