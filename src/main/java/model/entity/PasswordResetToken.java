package model.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Entidad para tokens de recuperación de contraseña
 */
public class PasswordResetToken {
    
    private Integer idToken;
    private Integer idUser;
    private String token;
    private Timestamp expirationDate;
    private Boolean used;
    private Timestamp createdAt;
    
    // Constructores
    
    public PasswordResetToken() {
    }
    
    /**
     * Constructor para crear nuevo token
     * @param idUser ID del usuario
     * @param token Token generado
     * @param expirationDate Fecha de expiración
     */
    public PasswordResetToken(Integer idUser, String token, Timestamp expirationDate) {
        this.idUser = idUser;
        this.token = token;
        this.expirationDate = expirationDate;
        this.used = false;
    }
    
    // Getters y Setters
    
    public Integer getIdToken() {
        return idToken;
    }
    
    public void setIdToken(Integer idToken) {
        this.idToken = idToken;
    }
    
    public Integer getIdUser() {
        return idUser;
    }
    
    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public Timestamp getExpirationDate() {
        return expirationDate;
    }
    
    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    public Boolean getUsed() {
        return used;
    }
    
    public void setUsed(Boolean used) {
        this.used = used;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // Métodos útiles
    
    /**
     * Verifica si el token ha expirado
     */
    public boolean isExpired() {
        return expirationDate.before(new Timestamp(System.currentTimeMillis()));
    }
    
    /**
     * Verifica si el token es válido (no usado y no expirado)
     */
    public boolean isValid() {
        return !used && !isExpired();
    }
    
    @Override
    public String toString() {
        return "PasswordResetToken{" +
                "idToken=" + idToken +
                ", idUser=" + idUser +
                ", token='" + token + '\'' +
                ", expirationDate=" + expirationDate +
                ", used=" + used +
                ", createdAt=" + createdAt +
                '}';
    }
}