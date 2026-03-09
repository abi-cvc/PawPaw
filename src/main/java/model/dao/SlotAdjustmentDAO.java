package model.dao;

import config.DatabaseConnection;
import model.entity.SlotAdjustment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestión de ajustes de slots
 */
public class SlotAdjustmentDAO {
    
    /**
     * Crear un registro de ajuste
     */
    public boolean create(SlotAdjustment adjustment) {
        String sql = "INSERT INTO slot_adjustments (id_user, id_admin, previous_limit, new_limit, adjustment_reason) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, adjustment.getIdUser());
            stmt.setInt(2, adjustment.getIdAdmin());
            stmt.setInt(3, adjustment.getPreviousLimit());
            stmt.setInt(4, adjustment.getNewLimit());
            stmt.setString(5, adjustment.getAdjustmentReason());
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    adjustment.setIdAdjustment(rs.getInt(1));
                }
                System.out.println("✅ Ajuste de slots registrado: " + adjustment);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al crear ajuste: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Obtener historial de ajustes de un usuario
     */
    public List<SlotAdjustment> findByUserId(Integer userId) {
        List<SlotAdjustment> adjustments = new ArrayList<>();
        String sql = "SELECT * FROM v_slot_adjustments WHERE id_user = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                adjustments.add(extractAdjustmentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener ajustes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return adjustments;
    }
    
    /**
     * Obtener todos los ajustes (para admin)
     */
    public List<SlotAdjustment> findAll() {
        List<SlotAdjustment> adjustments = new ArrayList<>();
        String sql = "SELECT * FROM v_slot_adjustments ORDER BY created_at DESC LIMIT 100";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                adjustments.add(extractAdjustmentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener todos los ajustes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return adjustments;
    }
    
    /**
     * Obtener ajustes realizados por un admin
     */
    public List<SlotAdjustment> findByAdminId(Integer adminId) {
        List<SlotAdjustment> adjustments = new ArrayList<>();
        String sql = "SELECT * FROM v_slot_adjustments WHERE id_admin = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, adminId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                adjustments.add(extractAdjustmentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener ajustes del admin: " + e.getMessage());
            e.printStackTrace();
        }
        
        return adjustments;
    }
    
    /**
     * Contar ajustes totales
     */
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM slot_adjustments";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al contar ajustes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Obtener último ajuste de un usuario
     */
    public SlotAdjustment findLastByUserId(Integer userId) {
        String sql = "SELECT * FROM v_slot_adjustments WHERE id_user = ? ORDER BY created_at DESC LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractAdjustmentFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener último ajuste: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Extraer ajuste desde ResultSet
     */
    private SlotAdjustment extractAdjustmentFromResultSet(ResultSet rs) throws SQLException {
        SlotAdjustment adjustment = new SlotAdjustment();
        adjustment.setIdAdjustment(rs.getInt("id_adjustment"));
        adjustment.setIdUser(rs.getInt("id_user"));
        adjustment.setIdAdmin(rs.getInt("id_admin"));
        adjustment.setPreviousLimit(rs.getInt("previous_limit"));
        adjustment.setNewLimit(rs.getInt("new_limit"));
        adjustment.setAdjustmentReason(rs.getString("adjustment_reason"));
        adjustment.setCreatedAt(rs.getTimestamp("created_at"));
        
        // Datos de la vista
        adjustment.setUserName(rs.getString("user_name"));
        adjustment.setUserEmail(rs.getString("user_email"));
        adjustment.setAdminName(rs.getString("admin_name"));
        
        return adjustment;
    }
}
