package model.dao;

import config.DatabaseConnection;
import model.entity.PetTransferRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar transferencias de mascotas
 */
public class PetTransferRequestDAO {
    
    /**
     * Crea una nueva solicitud de transferencia
     */
    public boolean create(PetTransferRequest transfer) {
        String sql = "INSERT INTO pet_transfer_requests (id_pet, id_foundation, adopter_email, " +
                    "adopter_name, adopter_phone, transfer_token, message) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, transfer.getIdPet());
            stmt.setInt(2, transfer.getIdFoundation());
            stmt.setString(3, transfer.getAdopterEmail());
            stmt.setString(4, transfer.getAdopterName());
            stmt.setString(5, transfer.getAdopterPhone());
            stmt.setString(6, transfer.getTransferToken());
            stmt.setString(7, transfer.getMessage());
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    transfer.setIdTransfer(rs.getInt(1));
                }
                System.out.println("✅ Solicitud de transferencia creada: " + transfer.getIdTransfer());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al crear transferencia: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Busca una transferencia por token
     */
    public PetTransferRequest findByToken(String token) {
        String sql = "SELECT t.*, p.name_pet, u.name_user as foundation_name " +
                    "FROM pet_transfer_requests t " +
                    "INNER JOIN pets p ON t.id_pet = p.id_pet " +
                    "INNER JOIN users u ON t.id_foundation = u.id_user " +
                    "WHERE t.transfer_token = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar transferencia por token: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Busca una transferencia por ID
     */
    public PetTransferRequest findById(Integer idTransfer) {
        String sql = "SELECT t.*, p.name_pet, u.name_user as foundation_name " +
                    "FROM pet_transfer_requests t " +
                    "INNER JOIN pets p ON t.id_pet = p.id_pet " +
                    "INNER JOIN users u ON t.id_foundation = u.id_user " +
                    "WHERE t.id_transfer = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idTransfer);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar transferencia: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todas las transferencias de una fundación
     */
    public List<PetTransferRequest> findByFoundation(Integer idFoundation) {
        List<PetTransferRequest> transfers = new ArrayList<>();
        String sql = "SELECT t.*, p.name_pet, u.name_user as foundation_name " +
                    "FROM pet_transfer_requests t " +
                    "INNER JOIN pets p ON t.id_pet = p.id_pet " +
                    "INNER JOIN users u ON t.id_foundation = u.id_user " +
                    "WHERE t.id_foundation = ? " +
                    "ORDER BY t.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idFoundation);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                transfers.add(extractFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar transferencias de fundación: " + e.getMessage());
            e.printStackTrace();
        }
        
        return transfers;
    }
    
    /**
     * Obtiene transferencias por estado
     */
    public List<PetTransferRequest> findByStatus(String status) {
        List<PetTransferRequest> transfers = new ArrayList<>();
        String sql = "SELECT t.*, p.name_pet, u.name_user as foundation_name " +
                    "FROM pet_transfer_requests t " +
                    "INNER JOIN pets p ON t.id_pet = p.id_pet " +
                    "INNER JOIN users u ON t.id_foundation = u.id_user " +
                    "WHERE t.status = ? " +
                    "ORDER BY t.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                transfers.add(extractFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar transferencias por estado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return transfers;
    }
    
    /**
     * Acepta una transferencia y actualiza el dueño de la mascota
     */
    public boolean accept(String token, Integer newOwnerId) {
        String updateTransferSQL = "UPDATE pet_transfer_requests " +
                                   "SET status = 'accepted', accepted_at = CURRENT_TIMESTAMP " +
                                   "WHERE transfer_token = ? AND status = 'pending'";
        
        String updatePetSQL = "UPDATE pets SET id_user = ?, adoption_status = 'adopted_transferred' " +
                             "WHERE id_pet = (SELECT id_pet FROM pet_transfer_requests WHERE transfer_token = ?)";
        
        Connection conn = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // Actualizar transferencia
            try (PreparedStatement stmt = conn.prepareStatement(updateTransferSQL)) {
                stmt.setString(1, token);
                int affected = stmt.executeUpdate();
                
                if (affected == 0) {
                    conn.rollback();
                    System.err.println("❌ No se encontró transferencia pendiente con ese token");
                    return false;
                }
            }
            
            // Actualizar dueño de mascota
            try (PreparedStatement stmt = conn.prepareStatement(updatePetSQL)) {
                stmt.setInt(1, newOwnerId);
                stmt.setString(2, token);
                stmt.executeUpdate();
            }
            
            conn.commit();
            System.out.println("✅ Transferencia aceptada correctamente");
            return true;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al aceptar transferencia: " + e.getMessage());
            e.printStackTrace();
            
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return false;
    }
    
    /**
     * Rechaza una transferencia
     */
    public boolean reject(String token) {
        String sql = "UPDATE pet_transfer_requests " +
                    "SET status = 'rejected' " +
                    "WHERE transfer_token = ? AND status = 'pending'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, token);
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✅ Transferencia rechazada");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al rechazar transferencia: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Marca transferencias expiradas
     */
    public int markExpired() {
        String sql = "UPDATE pet_transfer_requests " +
                    "SET status = 'expired' " +
                    "WHERE status = 'pending' AND expires_at < CURRENT_TIMESTAMP";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✅ " + affected + " transferencias marcadas como expiradas");
            }
            
            return affected;
            
        } catch (SQLException e) {
            System.err.println("❌ Error al marcar expiradas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Cuenta transferencias por estado para una fundación
     */
    public int countByFoundationAndStatus(Integer idFoundation, String status) {
        String sql = "SELECT COUNT(*) FROM pet_transfer_requests " +
                    "WHERE id_foundation = ? AND status = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idFoundation);
            stmt.setString(2, status);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al contar transferencias: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Verifica si existe una transferencia pendiente para una mascota
     */
    public boolean hasPendingTransfer(Integer idPet) {
        String sql = "SELECT COUNT(*) FROM pet_transfer_requests " +
                    "WHERE id_pet = ? AND status = 'pending'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idPet);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar transferencia pendiente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Extrae una transferencia desde ResultSet
     */
    private PetTransferRequest extractFromResultSet(ResultSet rs) throws SQLException {
        PetTransferRequest transfer = new PetTransferRequest();
        transfer.setIdTransfer(rs.getInt("id_transfer"));
        transfer.setIdPet(rs.getInt("id_pet"));
        transfer.setIdFoundation(rs.getInt("id_foundation"));
        transfer.setAdopterEmail(rs.getString("adopter_email"));
        transfer.setAdopterName(rs.getString("adopter_name"));
        transfer.setAdopterPhone(rs.getString("adopter_phone"));
        transfer.setTransferToken(rs.getString("transfer_token"));
        transfer.setStatus(rs.getString("status"));
        transfer.setMessage(rs.getString("message"));
        transfer.setCreatedAt(rs.getTimestamp("created_at"));
        transfer.setAcceptedAt(rs.getTimestamp("accepted_at"));
        transfer.setExpiresAt(rs.getTimestamp("expires_at"));
        transfer.setPetName(rs.getString("name_pet"));
        transfer.setFoundationName(rs.getString("foundation_name"));
        
        return transfer;
    }
}
