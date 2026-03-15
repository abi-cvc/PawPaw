package model.entity;

import java.sql.Timestamp;

public class SystemMessage {

    private Integer idMessage;
    private Integer idUser;
    private String subject;
    private String message;
    private Boolean isRead;
    private Timestamp createdAt;

    public SystemMessage() {}

    public SystemMessage(Integer idUser, String subject, String message) {
        this.idUser = idUser;
        this.subject = subject;
        this.message = message;
        this.isRead = false;
    }

    public Integer getIdMessage() { return idMessage; }
    public void setIdMessage(Integer idMessage) { this.idMessage = idMessage; }

    public Integer getIdUser() { return idUser; }
    public void setIdUser(Integer idUser) { this.idUser = idUser; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public boolean isUnread() { return isRead == null || !isRead; }

    public String getTimeAgo() {
        if (createdAt == null) return "";
        long diffMs = System.currentTimeMillis() - createdAt.getTime();
        long minutes = diffMs / 60000;
        if (minutes < 1) return "Hace un momento";
        if (minutes < 60) return "Hace " + minutes + " min";
        long hours = minutes / 60;
        if (hours < 24) return "Hace " + hours + "h";
        long days = hours / 24;
        return "Hace " + days + " día" + (days > 1 ? "s" : "");
    }
}
