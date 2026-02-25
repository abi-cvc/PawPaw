package model.dao;

import config.DatabaseConnection;
import model.entity.Promotion;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestión de promociones
 */
public class PromotionDAO {
    
    /**
     * Crea una nueva promoción
     */
    public boolean create(Promotion promotion) {
        String sql = "INSERT INTO promotions (promo_name, promo_description, slots_quantity, " +
                    "promo_price, regular_price, promo_code, start_date, end_date, is_active, " +
                    "max_uses, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, promotion.getPromoName());
            stmt.setString(2, promotion.getPromoDescription());
            stmt.setInt(3, promotion.getSlotsQuantity());
            stmt.setBigDecimal(4, promotion.getPromoPrice());
            stmt.setBigDecimal(5, promotion.getRegularPrice());
            stmt.setString(6, promotion.getPromoCode());
            stmt.setTimestamp(7, promotion.getStartDate());
            stmt.setTimestamp(8, promotion.getEndDate());
            stmt.setBoolean(9, promotion.getIsActive() != null ? promotion.getIsActive() : true);
            
            if (promotion.getMaxUses() != null) {
                stmt.setInt(10, promotion.getMaxUses());
            } else {
                stmt.setNull(10, Types.INTEGER);
            }
            
            if (promotion.getCreatedBy() != null) {
                stmt.setInt(11, promotion.getCreatedBy());
            } else {
                stmt.setNull(11, Types.INTEGER);
            }
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    promotion.setIdPromotion(rs.getInt(1));
                }
                System.out.println("✅ Promoción creada: " + promotion.getPromoName());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al crear promoción: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Obtiene una promoción por ID
     */
    public Promotion findById(Integer idPromotion) {
        String sql = "SELECT * FROM promotions WHERE id_promotion = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPromotion);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractPromotionFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar promoción: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todas las promociones
     */
    public List<Promotion> findAll() {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM promotions ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                promotions.add(extractPromotionFromResultSet(rs));
            }
            
            System.out.println("✅ Promociones encontradas: " + promotions.size());
            
        } catch (SQLException e) {
            System.err.println("❌ Error al listar promociones: " + e.getMessage());
            e.printStackTrace();
        }
        
        return promotions;
    }
    
    /**
     * Obtiene promociones activas
     */
    public List<Promotion> findActive() {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM promotions WHERE is_active = true " +
                    "AND (start_date IS NULL OR start_date <= CURRENT_TIMESTAMP) " +
                    "AND (end_date IS NULL OR end_date >= CURRENT_TIMESTAMP) " +
                    "ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                promotions.add(extractPromotionFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar promociones activas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return promotions;
    }
    
    /**
     * Obtiene la promoción activa actual (la más reciente)
     */
    public Promotion getCurrentActivePromotion() {
        String sql = "SELECT * FROM get_active_promotion()";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                Promotion promo = new Promotion();
                promo.setIdPromotion(rs.getInt("id_promotion"));
                promo.setPromoName(rs.getString("promo_name"));
                promo.setSlotsQuantity(rs.getInt("slots_quantity"));
                promo.setPromoPrice(rs.getBigDecimal("promo_price"));
                promo.setSavings(rs.getBigDecimal("savings"));
                return promo;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener promoción activa: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Actualiza una promoción
     */
    public boolean update(Promotion promotion) {
        String sql = "UPDATE promotions SET promo_name = ?, promo_description = ?, " +
                    "slots_quantity = ?, promo_price = ?, regular_price = ?, " +
                    "promo_code = ?, start_date = ?, end_date = ?, is_active = ?, " +
                    "max_uses = ?, updated_at = CURRENT_TIMESTAMP, updated_by = ? " +
                    "WHERE id_promotion = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, promotion.getPromoName());
            stmt.setString(2, promotion.getPromoDescription());
            stmt.setInt(3, promotion.getSlotsQuantity());
            stmt.setBigDecimal(4, promotion.getPromoPrice());
            stmt.setBigDecimal(5, promotion.getRegularPrice());
            stmt.setString(6, promotion.getPromoCode());
            stmt.setTimestamp(7, promotion.getStartDate());
            stmt.setTimestamp(8, promotion.getEndDate());
            stmt.setBoolean(9, promotion.getIsActive());
            
            if (promotion.getMaxUses() != null) {
                stmt.setInt(10, promotion.getMaxUses());
            } else {
                stmt.setNull(10, Types.INTEGER);
            }
            
            if (promotion.getUpdatedBy() != null) {
                stmt.setInt(11, promotion.getUpdatedBy());
            } else {
                stmt.setNull(11, Types.INTEGER);
            }
            
            stmt.setInt(12, promotion.getIdPromotion());
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✅ Promoción actualizada: " + promotion.getIdPromotion());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar promoción: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Activa o desactiva una promoción
     */
    public boolean toggleActive(Integer idPromotion, Integer updatedBy) {
        String sql = "UPDATE promotions SET is_active = NOT is_active, " +
                    "updated_at = CURRENT_TIMESTAMP, updated_by = ? " +
                    "WHERE id_promotion = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (updatedBy != null) {
                stmt.setInt(1, updatedBy);
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setInt(2, idPromotion);
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✅ Estado de promoción cambiado: " + idPromotion);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al cambiar estado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina una promoción
     */
    public boolean delete(Integer idPromotion) {
        String sql = "DELETE FROM promotions WHERE id_promotion = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPromotion);
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✅ Promoción eliminada: " + idPromotion);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar promoción: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cuenta total de promociones
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM promotions";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al contar promociones: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Extrae una promoción desde ResultSet
     */
    private Promotion extractPromotionFromResultSet(ResultSet rs) throws SQLException {
        Promotion promo = new Promotion();
        promo.setIdPromotion(rs.getInt("id_promotion"));
        promo.setPromoName(rs.getString("promo_name"));
        promo.setPromoDescription(rs.getString("promo_description"));
        promo.setSlotsQuantity(rs.getInt("slots_quantity"));
        promo.setPromoPrice(rs.getBigDecimal("promo_price"));
        promo.setRegularPrice(rs.getBigDecimal("regular_price"));
        promo.setPromoCode(rs.getString("promo_code"));
        promo.setStartDate(rs.getTimestamp("start_date"));
        promo.setEndDate(rs.getTimestamp("end_date"));
        promo.setIsActive(rs.getBoolean("is_active"));
        promo.setMaxUses(rs.getObject("max_uses") != null ? rs.getInt("max_uses") : null);
        promo.setCurrentUses(rs.getInt("current_uses"));
        promo.setCreatedAt(rs.getTimestamp("created_at"));
        promo.setCreatedBy(rs.getObject("created_by") != null ? rs.getInt("created_by") : null);
        promo.setUpdatedAt(rs.getTimestamp("updated_at"));
        promo.setUpdatedBy(rs.getObject("updated_by") != null ? rs.getInt("updated_by") : null);
        
        // Calcular ahorro
        if (promo.getRegularPrice() != null && promo.getPromoPrice() != null) {
            promo.setSavings(promo.getRegularPrice().subtract(promo.getPromoPrice()));
        }
        
        return promo;
    }
}