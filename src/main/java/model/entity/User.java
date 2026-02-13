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
    }
    
    // Getters y Setters
    
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
    
    // Métodos útiles
    
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.rol);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", nameUser='" + nameUser + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                ", active=" + active +
                ", registrationDate=" + registrationDate +
                '}';
    }
}