package model.entity;

import java.sql.Timestamp;

/**
 * Entidad ScanLog
 * Representa un registro de escaneo de QR en el sistema PawPaw
 * Tabla BD: scan_logs
 */
public class ScanLog {

    private Integer idScan;
    private Integer idQr;
    private Timestamp scanDate;
    private String ipAddress;
    private String userAgent;
    private String locationData;

    public ScanLog() {
    }

    public ScanLog(Integer idQr, String ipAddress, String userAgent, String locationData) {
        this.idQr = idQr;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.locationData = locationData;
    }

    // Getters y Setters

    public Integer getIdScan() {
        return idScan;
    }

    public void setIdScan(Integer idScan) {
        this.idScan = idScan;
    }

    public Integer getIdQr() {
        return idQr;
    }

    public void setIdQr(Integer idQr) {
        this.idQr = idQr;
    }

    public Timestamp getScanDate() {
        return scanDate;
    }

    public void setScanDate(Timestamp scanDate) {
        this.scanDate = scanDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getLocationData() {
        return locationData;
    }

    public void setLocationData(String locationData) {
        this.locationData = locationData;
    }

    @Override
    public String toString() {
        return "ScanLog{" +
                "idScan=" + idScan +
                ", idQr=" + idQr +
                ", scanDate=" + scanDate +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
