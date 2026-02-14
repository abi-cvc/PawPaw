package model.dao;

import config.DatabaseConnection;
import model.entity.QRcode;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar códigos QR de mascotas
 */
public class QRCodeDAO {
    
    /**
     * Crea un nuevo código QR para una mascota
     */
    public boolean create(QRcode qrCode) {
        String sql = "INSERT INTO qrcodes (id_pet, url, active) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, qrCode.getIdPet());
            pstmt.setString(2, qrCode.getUrl());
            pstmt.setBoolean(3, qrCode.getActive() != null ? qrCode.getActive() : true);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        qrCode.setIdQR(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear código QR: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Busca un código QR por ID
     */
    public QRcode findById(Integer idQR) {
        String sql = "SELECT * FROM qrcodes WHERE id_qr = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idQR);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractQRCodeFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar código QR: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Busca código QR por ID de mascota
     */
    public QRcode findByPetId(Integer idPet) {
        String sql = "SELECT * FROM qrcodes WHERE id_pet = ? ORDER BY generation_date DESC LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPet);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractQRCodeFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar QR por mascota: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todos los QR de un usuario (a través de sus mascotas)
     */
    public List<QRcode> findByUserId(Integer userId) {
        List<QRcode> qrCodes = new ArrayList<>();
        String sql = "SELECT q.* FROM qrcodes q " +
                     "INNER JOIN pets p ON q.id_pet = p.id_pet " +
                     "WHERE p.id_user = ? " +
                     "ORDER BY q.generation_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    qrCodes.add(extractQRCodeFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener QRs del usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return qrCodes;
    }
    
    /**
     * Actualiza un código QR
     */
    public boolean update(QRcode qrCode) {
        String sql = "UPDATE qrcodes SET url = ?, active = ? WHERE id_qr = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, qrCode.getUrl());
            pstmt.setBoolean(2, qrCode.getActive());
            pstmt.setInt(3, qrCode.getIdQR());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar QR: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Incrementa el contador de escaneos
     */
    public boolean incrementScanCount(Integer idQR) {
        String sql = "UPDATE qrcodes SET scans_count = scans_count + 1, last_scan_date = CURRENT_TIMESTAMP WHERE id_qr = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idQR);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al incrementar contador: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cuenta QRs activos de un usuario
     */
    public int countActiveByUserId(Integer userId) {
        String sql = "SELECT COUNT(*) FROM qrcodes q " +
                     "INNER JOIN pets p ON q.id_pet = p.id_pet " +
                     "WHERE p.id_user = ? AND q.active = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar QRs: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Elimina un código QR
     */
    public boolean delete(Integer idQR) {
        String sql = "DELETE FROM qrcodes WHERE id_qr = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idQR);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar QR: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Extrae un QRcode desde un ResultSet
     */
    private QRcode extractQRCodeFromResultSet(ResultSet rs) throws SQLException {
        QRcode qr = new QRcode();
        qr.setIdQR(rs.getInt("id_qr"));
        qr.setIdPet(rs.getInt("id_pet"));
        qr.setUrl(rs.getString("url"));
        qr.setGenerationDate(rs.getTimestamp("generation_date"));
        qr.setActive(rs.getBoolean("active"));
        
        int scansCount = rs.getInt("scans_count");
        if (!rs.wasNull()) {
            qr.setScansCount(scansCount);
        }
        
        Timestamp lastScan = rs.getTimestamp("last_scan_date");
        if (lastScan != null) {
            qr.setLastScanDate(lastScan);
        }
        
        return qr;
    }
}