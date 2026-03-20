package model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Donation {

    private Integer idDonation;
    private Integer idUser;
    private String donorName;
    private String donorEmail;
    private BigDecimal amount;
    private String message;
    private boolean anonymous;
    private String paymentStatus;
    private String paypalOrderId;
    private String paypalCaptureId;
    private Timestamp createdAt;

    public Donation() {
    }

    // Getters y Setters

    public Integer getIdDonation() {
        return idDonation;
    }

    public void setIdDonation(Integer idDonation) {
        this.idDonation = idDonation;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorEmail() {
        return donorEmail;
    }

    public void setDonorEmail(String donorEmail) {
        this.donorEmail = donorEmail;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaypalOrderId() {
        return paypalOrderId;
    }

    public void setPaypalOrderId(String paypalOrderId) {
        this.paypalOrderId = paypalOrderId;
    }

    public String getPaypalCaptureId() {
        return paypalCaptureId;
    }

    public void setPaypalCaptureId(String paypalCaptureId) {
        this.paypalCaptureId = paypalCaptureId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatusText() {
        if (paymentStatus == null) return "Desconocido";
        switch (paymentStatus.toLowerCase()) {
            case "pending": return "Pendiente";
            case "completed": return "Completado";
            case "failed": return "Fallido";
            default: return paymentStatus;
        }
    }

    public String getStatusClass() {
        if (paymentStatus == null) return "";
        switch (paymentStatus.toLowerCase()) {
            case "pending": return "status-pending";
            case "completed": return "status-completed";
            case "failed": return "status-rejected";
            default: return "";
        }
    }

    public String getDisplayName() {
        if (anonymous || donorName == null || donorName.isEmpty()) {
            return "Anonimo";
        }
        return donorName;
    }
}
