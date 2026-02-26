package model.entity;

import java.sql.Timestamp;

/**
 * Modelo de entidad User
 * Representa un usuario registrado en el sistema PawPaw
 */
public class User {
    
    private Integer idUser;
    private String nameUser;
    private String email;
    private String password;
    private Timestamp registrationDate;
    private String rol;
    private Boolean active;
    
    // ✅ NUEVOS CAMPOS para sistema de límites y partners
    private Integer petLimit;
    private Boolean isPartner;
    private String partnerBadge;
    
    // Constructores
    
    /**
     * Constructor vacío
     */
    public User() {
    }
    
    /**
     * Constructor para registro de nuevo usuario
     */
    public User(String nameUser, String email, String password) {
        this.nameUser = nameUser;
        this.email = email;
        this.password = password;
        this.rol = "user"; // Rol por defecto
        this.active = true; // Activo por defecto
        this.petLimit = 2; // ✅ Límite por defecto: 2 mascotas gratis
        this.isPartner = false; // ✅ Por defecto no es partner
    }
    
    /**
     * Constructor completo
     */
    public User(Integer idUser, String nameUser, String email, String password, 
                Timestamp registrationDate, String rol, Boolean active) {
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.rol = rol;
        this.active = active;
        this.petLimit = 2; // ✅ Default
        this.isPartner = false; // ✅ Default
    }
    
    // Getters y Setters ORIGINALES
    
    public Integer getIdUser() {
        return idUser;
    }
    
    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
    
    public String getNameUser() {
        return nameUser;
    }
    
    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Timestamp getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    // ✅ NUEVOS Getters y Setters para límites y partners
    
    public Integer getPetLimit() {
        return petLimit;
    }
    
    public void setPetLimit(Integer petLimit) {
        this.petLimit = petLimit;
    }
    
    public Boolean getIsPartner() {
        return isPartner;
    }
    
    public void setIsPartner(Boolean isPartner) {
        this.isPartner = isPartner;
    }
    
    public String getPartnerBadge() {
        return partnerBadge;
    }
    
    public void setPartnerBadge(String partnerBadge) {
        this.partnerBadge = partnerBadge;
    }
    
    // Métodos útiles ORIGINALES
    
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.rol);
    }
    
    // ✅ NUEVOS Métodos útiles para límites y partners
    
    /**
     * Verifica si el usuario es un partner/aliado
     */
    public boolean isPartnerUser() {
        return isPartner != null && isPartner;
    }
    
    /**
     * Obtiene el límite de mascotas (default: 2 si es null)
     */
    public int getPetLimitValue() {
        return petLimit != null ? petLimit : 2;
    }
    
    /**
     * Verifica si el usuario ha alcanzado su límite de mascotas
     * (Necesita la cantidad actual de mascotas como parámetro)
     */
    public boolean hasReachedLimit(int currentPets) {
        return currentPets >= getPetLimitValue();
    }
    
    /**
     * Calcula cuántos slots disponibles tiene el usuario
     */
    public int getAvailableSlots(int currentPets) {
        return getPetLimitValue() - currentPets;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", nameUser='" + nameUser + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                ", active=" + active +
                ", petLimit=" + petLimit +
                ", isPartner=" + isPartner +
                ", partnerBadge='" + partnerBadge + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}