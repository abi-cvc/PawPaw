package model.dao;

import config.DatabaseConnection;
import model.entity.PaymentRequest;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestión de pagos de slots
 */
public class PaymentRequestDAO {
    
    /**
     * Crea una solicitud de pago
     */
    public boolean create(PaymentRequest payment) {
        String sql = "INSERT INTO payment_requests (id_user, id_promotion, amount, " +
                    "slots_purchased, payment_method, payment_status, paypal_transaction_id, " +
                    "paypal_payer_id, paypal_order_id, payment_proof) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, payment.getIdUser());
            
            if (payment.getIdPromotion() != null) {
                stmt.setInt(2, payment.getIdPromotion());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            
            stmt.setBigDecimal(3, payment.getAmount());
            stmt.setInt(4, payment.getSlotsPurchased());
            stmt.setString(5, payment.getPaymentMethod());
            stmt.setString(6, payment.getPaymentStatus());
            stmt.setString(7, payment.getPaypalTransactionId());
            stmt.setString(8, payment.getPaypalPayerId());
            stmt.setString(9, payment.getPaypalOrderId());
            stmt.setString(10, payment.getPaymentProof());
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    payment.setIdPayment(rs.getInt(1));
                }
                System.out.println("✅ Pago registrado: " + payment.getIdPayment());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al crear pago: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Obtiene un pago por ID
     */
    public PaymentRequest findById(Integer idPayment) {
        String sql = "SELECT p.*, u.name_user, u.email, pr.promo_name " +
                    "FROM payment_requests p " +
                    "INNER JOIN users u ON p.id_user = u.id_user " +
                    "LEFT JOIN promotions pr ON p.id_promotion = pr.id_promotion " +
                    "WHERE p.id_payment = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPayment);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractPaymentFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar pago: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene pagos por usuario
     */
    public List<PaymentRequest> findByUserId(Integer idUser) {
        List<PaymentRequest> payments = new ArrayList<>();
        String sql = "SELECT p.*, u.name_user, u.email, pr.promo_name " +
                    "FROM payment_requests p " +
                    "INNER JOIN users u ON p.id_user = u.id_user " +
                    "LEFT JOIN promotions pr ON p.id_promotion = pr.id_promotion " +
                    "WHERE p.id_user = ? ORDER BY p.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar pagos por usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return payments;
    }
    
    /**
     * Obtiene pagos por estado
     */
    public List<PaymentRequest> findByStatus(String status) {
        List<PaymentRequest> payments = new ArrayList<>();
        String sql = "SELECT p.*, u.name_user, u.email, pr.promo_name " +
                    "FROM payment_requests p " +
                    "INNER JOIN users u ON p.id_user = u.id_user " +
                    "LEFT JOIN promotions pr ON p.id_promotion = pr.id_promotion " +
                    "WHERE p.payment_status = ? ORDER BY p.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar pagos por estado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return payments;
    }
    
    /**
     * Obtiene todos los pagos
     */
    public List<PaymentRequest> findAll() {
        List<PaymentRequest> payments = new ArrayList<>();
        String sql = "SELECT p.*, u.name_user, u.email, pr.promo_name " +
                    "FROM payment_requests p " +
                    "INNER JOIN users u ON p.id_user = u.id_user " +
                    "LEFT JOIN promotions pr ON p.id_promotion = pr.id_promotion " +
                    "ORDER BY p.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
            
            System.out.println("✅ Pagos encontrados: " + payments.size());
            
        } catch (SQLException e) {
            System.err.println("❌ Error al listar pagos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return payments;
    }
    
    /**
     * Actualiza el estado de un pago
     */
    public boolean updateStatus(Integer idPayment, String newStatus, Integer verifiedBy) {
        String sql = "UPDATE payment_requests SET payment_status = ?, " +
                    "verified_at = CURRENT_TIMESTAMP, verified_by = ? " +
                    "WHERE id_payment = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newStatus);
            
            if (verifiedBy != null) {
                stmt.setInt(2, verifiedBy);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            
            stmt.setInt(3, idPayment);
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✅ Estado de pago actualizado: " + idPayment + " -> " + newStatus);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar estado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Actualiza notas del admin
     */
    public boolean updateAdminNotes(Integer idPayment, String notes) {
        String sql = "UPDATE payment_requests SET admin_notes = ? WHERE id_payment = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, notes);
            stmt.setInt(2, idPayment);
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✅ Notas actualizadas para pago: " + idPayment);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar notas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cuenta total de pagos
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM payment_requests";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al contar pagos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Cuenta pagos por estado
     */
    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM payment_requests WHERE payment_status = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al contar pagos por estado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Extrae un pago desde ResultSet
     */
    private PaymentRequest extractPaymentFromResultSet(ResultSet rs) throws SQLException {
        PaymentRequest payment = new PaymentRequest();
        payment.setIdPayment(rs.getInt("id_payment"));
        payment.setIdUser(rs.getInt("id_user"));
        payment.setIdPromotion(rs.getObject("id_promotion") != null ? rs.getInt("id_promotion") : null);
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setSlotsPurchased(rs.getInt("slots_purchased"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setPaymentStatus(rs.getString("payment_status"));
        payment.setPaypalTransactionId(rs.getString("paypal_transaction_id"));
        payment.setPaypalPayerId(rs.getString("paypal_payer_id"));
        payment.setPaypalOrderId(rs.getString("paypal_order_id"));
        payment.setPaymentProof(rs.getString("payment_proof"));
        payment.setCreatedAt(rs.getTimestamp("created_at"));
        payment.setVerifiedAt(rs.getTimestamp("verified_at"));
        payment.setVerifiedBy(rs.getObject("verified_by") != null ? rs.getInt("verified_by") : null);
        payment.setAdminNotes(rs.getString("admin_notes"));
        
        // Info adicional
        payment.setUserName(rs.getString("name_user"));
        payment.setUserEmail(rs.getString("email"));
        payment.setPromoName(rs.getString("promo_name"));
        
        return payment;
    }
}