package model.dao;

import config.DatabaseConnection;
import model.entity.ScanLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad ScanLog
 * Maneja registros de escaneo de QR codes
 * Tabla BD: scan_logs
 */
public class ScanLogDAO {

    /**
     * Registra un nuevo escaneo de QR
     */
    public boolean create(ScanLog scanLog) {
        String sql = "INSERT INTO scan_logs (id_qr, ip_address, user_agent, location_data) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, scanLog.getIdQr());
            pstmt.setString(2, scanLog.getIpAddress());
            pstmt.setString(3, scanLog.getUserAgent());
            pstmt.setString(4, scanLog.getLocationData());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        scanLog.setIdScan(generatedKeys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar escaneo: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Obtiene los escaneos de un QR code específico
     */
    public List<ScanLog> findByQrId(Integer idQr) {
        List<ScanLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM scan_logs WHERE id_qr = ? ORDER BY scan_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idQr);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(extractScanLogFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener escaneos: " + e.getMessage());
            e.printStackTrace();
        }

        return logs;
    }

    /**
     * Cuenta los escaneos de un QR code
     */
    public int countByQrId(Integer idQr) {
        String sql = "SELECT COUNT(*) FROM scan_logs WHERE id_qr = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idQr);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al contar escaneos: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Cuenta el total de escaneos en el sistema
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM scan_logs";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al contar escaneos totales: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    private ScanLog extractScanLogFromResultSet(ResultSet rs) throws SQLException {
        ScanLog log = new ScanLog();
        log.setIdScan(rs.getInt("id_scan"));
        log.setIdQr(rs.getInt("id_qr"));
        log.setScanDate(rs.getTimestamp("scan_date"));
        log.setIpAddress(rs.getString("ip_address"));
        log.setUserAgent(rs.getString("user_agent"));
        log.setLocationData(rs.getString("location_data"));
        return log;
    }
}
