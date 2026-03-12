package model.entity;

import java.sql.Timestamp;

public class PetTransferRequest {

    private int idTransfer;
    private int idPet;
    private int idFoundation;
    private String adopterEmail;
    private String adopterName;
    private String adopterPhone;
    private String transferToken;
    private String status; // pending, accepted, rejected, expired
    private String message;
    private Timestamp createdAt;
    private Timestamp acceptedAt;
    private Timestamp expiresAt;
    
    // ── Campos transient: los rellena el servlet antes de hacer forward ──
    private transient String petName;
    private transient String foundationName;

    // ─── Constructors ────────────────────────────────────────
    public PetTransferRequest() {}

    // ─── Getters & Setters ───────────────────────────────────
    public int getIdTransfer() { return idTransfer; }
    public void setIdTransfer(int idTransfer) { this.idTransfer = idTransfer; }

    public int getIdPet() { return idPet; }
    public void setIdPet(int idPet) { this.idPet = idPet; }

    public int getIdFoundation() { return idFoundation; }
    public void setIdFoundation(int idFoundation) { this.idFoundation = idFoundation; }

    public String getAdopterEmail() { return adopterEmail; }
    public void setAdopterEmail(String adopterEmail) { this.adopterEmail = adopterEmail; }

    public String getAdopterName() { return adopterName; }
    public void setAdopterName(String adopterName) { this.adopterName = adopterName; }

    public String getAdopterPhone() { return adopterPhone; }
    public void setAdopterPhone(String adopterPhone) { this.adopterPhone = adopterPhone; }

    public String getTransferToken() { return transferToken; }
    public void setTransferToken(String transferToken) { this.transferToken = transferToken; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(Timestamp acceptedAt) { this.acceptedAt = acceptedAt; }

    public Timestamp getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Timestamp expiresAt) { this.expiresAt = expiresAt; }
    
    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }
 
    public String getFoundationName() { return foundationName; }
    public void setFoundationName(String foundationName) { this.foundationName = foundationName; }
    
    public long getDaysRemaining() {
        if (expiresAt == null) return 0;
        long diffMillis = expiresAt.getTime() - System.currentTimeMillis();
        if (diffMillis <= 0) return 0;
        return diffMillis / (1000L * 60 * 60 * 24);
    }
 
    /**
     * Retorna true si quedan 2 días o menos para que expire.
     */
    public boolean isExpiringSoon() {
        return getDaysRemaining() <= 2;
    }
}