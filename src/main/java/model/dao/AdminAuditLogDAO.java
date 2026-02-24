package model.dao;

import config.DatabaseConnection;
import model.entity.AdminAuditLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar el registro de auditoría administrativa
 */
public class AdminAuditLogDAO {
    
    /**
     * Crea un nuevo registro de auditoría
     */
    public boolean create(AdminAuditLog log) {
        String sql = "INSERT INTO admin_audit_log (id_admin, action, target_type, target_id, " +
                     "old_value, new_value, ip_address, user_agent, details) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, log.getIdAdmin());
            pstmt.setString(2, log.getAction());
            pstmt.setString(3, log.getTargetType());
            pstmt.setObject(4, log.getTargetId(), Types.INTEGER);
            pstmt.setString(5, log.getOldValue());
            pstmt.setString(6, log.getNewValue());
            pstmt.setString(7, log.getIpAddress());
            pstmt.setString(8, log.getUserAgent());
            pstmt.setString(9, log.getDetails());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        log.setIdAudit(generatedKeys.getInt(1));
                    }
                }
                System.out.println("📝 Audit log created: " + log.getAction() + " by admin " + log.getIdAdmin());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al crear audit log: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Obtiene todos los logs de auditoría con información del admin
     */
    public List<AdminAuditLog> findAll() {
        List<AdminAuditLog> logs = new ArrayList<>();
        String sql = "SELECT l.*, u.name_user, u.email " +
                     "FROM admin_audit_log l " +
                     "INNER JOIN users u ON l.id_admin = u.id_user " +
                     "ORDER BY l.timestamp DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                logs.add(extractLogFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener audit logs: " + e.getMessage());
            e.printStackTrace();
        }
        
        return logs;
    }
    
    /**
     * Obtiene logs filtrados por acción
     */
    public List<AdminAuditLog> findByAction(String action) {
        List<AdminAuditLog> logs = new ArrayList<>();
        String sql = "SELECT l.*, u.name_user, u.email " +
                     "FROM admin_audit_log l " +
                     "INNER JOIN users u ON l.id_admin = u.id_user " +
                     "WHERE l.action = ? " +
                     "ORDER BY l.timestamp DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, action);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(extractLogFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener logs por acción: " + e.getMessage());
            e.printStackTrace();
        }
        
        return logs;
    }
    
    /**
     * Obtiene logs de un administrador específico
     */
    public List<AdminAuditLog> findByAdminId(Integer idAdmin) {
        List<AdminAuditLog> logs = new ArrayList<>();
        String sql = "SELECT l.*, u.name_user, u.email " +
                     "FROM admin_audit_log l " +
                     "INNER JOIN users u ON l.id_admin = u.id_user " +
                     "WHERE l.id_admin = ? " +
                     "ORDER BY l.timestamp DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idAdmin);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(extractLogFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener logs del admin: " + e.getMessage());
            e.printStackTrace();
        }
        
        return logs;
    }
    
    /**
     * Obtiene logs relacionados con un registro específico
     */
    public List<AdminAuditLog> findByTarget(String targetType, Integer targetId) {
        List<AdminAuditLog> logs = new ArrayList<>();
        String sql = "SELECT l.*, u.name_user, u.email " +
                     "FROM admin_audit_log l " +
                     "INNER JOIN users u ON l.id_admin = u.id_user " +
                     "WHERE l.target_type = ? AND l.target_id = ? " +
                     "ORDER BY l.timestamp DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, targetType);
            pstmt.setInt(2, targetId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(extractLogFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener logs por target: " + e.getMessage());
            e.printStackTrace();
        }
        
        return logs;
    }
    
    /**
     * Obtiene logs recientes (últimos N registros)
     */
    public List<AdminAuditLog> findRecent(int limit) {
        List<AdminAuditLog> logs = new ArrayList<>();
        String sql = "SELECT l.*, u.name_user, u.email " +
                     "FROM admin_audit_log l " +
                     "INNER JOIN users u ON l.id_admin = u.id_user " +
                     "ORDER BY l.timestamp DESC " +
                     "LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(extractLogFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener logs recientes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return logs;
    }
    
    /**
     * Cuenta el total de logs
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM admin_audit_log";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al contar logs: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Cuenta logs por acción
     */
    public int countByAction(String action) {
        String sql = "SELECT COUNT(*) FROM admin_audit_log WHERE action = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, action);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al contar logs por acción: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Elimina logs antiguos (limpieza de BD)
     * Útil para mantener la tabla bajo control
     */
    public int deleteOlderThan(int days) {
        String sql = "DELETE FROM admin_audit_log WHERE timestamp < CURRENT_TIMESTAMP - INTERVAL '" + days + " days'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            return stmt.executeUpdate(sql);
            
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar logs antiguos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Extrae un AdminAuditLog desde un ResultSet
     */
    private AdminAuditLog extractLogFromResultSet(ResultSet rs) throws SQLException {
        AdminAuditLog log = new AdminAuditLog();
        log.setIdAudit(rs.getInt("id_audit"));
        log.setIdAdmin(rs.getInt("id_admin"));
        log.setAction(rs.getString("action"));
        log.setTargetType(rs.getString("target_type"));
        
        int targetId = rs.getInt("target_id");
        if (!rs.wasNull()) {
            log.setTargetId(targetId);
        }
        
        log.setOldValue(rs.getString("old_value"));
        log.setNewValue(rs.getString("new_value"));
        log.setIpAddress(rs.getString("ip_address"));
        log.setUserAgent(rs.getString("user_agent"));
        log.setTimestamp(rs.getTimestamp("timestamp"));
        log.setDetails(rs.getString("details"));
        
        // Información del admin
        log.setAdminName(rs.getString("name_user"));
        log.setAdminEmail(rs.getString("email"));
        
        return log;
    }
}