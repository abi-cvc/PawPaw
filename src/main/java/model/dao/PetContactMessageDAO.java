package model.dao;

import config.DatabaseConnection;
import model.entity.PetContactMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestión de mensajes de contacto de mascotas
 */
public class PetContactMessageDAO {
    
    /**
     * Crear un nuevo mensaje
     */
    public boolean create(PetContactMessage message) {
        String sql = "INSERT INTO pet_contact_messages (id_pet, id_user, sender_name, sender_phone, message) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, message.getIdPet());
            stmt.setInt(2, message.getIdUser());
            stmt.setString(3, message.getSenderName());
            stmt.setString(4, message.getSenderPhone());
            stmt.setString(5, message.getMessage());
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    message.setIdMessage(rs.getInt(1));
                }
                System.out.println("✅ Mensaje guardado en BD: ID " + message.getIdMessage());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al crear mensaje: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Obtener mensajes de un usuario (desde la vista)
     */
    public List<PetContactMessage> findByUserId(Integer userId) {
        List<PetContactMessage> messages = new ArrayList<>();
        String sql = "SELECT * FROM v_pet_contact_messages WHERE id_user = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                messages.add(extractMessageFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener mensajes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }
    
    /**
     * Obtener mensajes no leídos de un usuario
     */
    public List<PetContactMessage> findUnreadByUserId(Integer userId) {
        List<PetContactMessage> messages = new ArrayList<>();
        String sql = "SELECT * FROM v_pet_contact_messages WHERE id_user = ? AND is_read = false " +
                    "ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                messages.add(extractMessageFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener mensajes no leídos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }
    
    /**
     * Contar mensajes no leídos de un usuario
     */
    public int countUnreadByUserId(Integer userId) {
        String sql = "SELECT count_unread_messages(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al contar mensajes no leídos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Marcar mensaje como leído
     */
    public boolean markAsRead(Integer messageId) {
        String sql = "UPDATE pet_contact_messages SET is_read = true WHERE id_message = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, messageId);
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✅ Mensaje " + messageId + " marcado como leído");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al marcar mensaje como leído: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Marcar todos los mensajes de un usuario como leídos
     */
    public boolean markAllAsRead(Integer userId) {
        String sql = "UPDATE pet_contact_messages SET is_read = true WHERE id_user = ? AND is_read = false";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✅ " + affected + " mensajes marcados como leídos");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al marcar todos como leídos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Obtener un mensaje por ID
     */
    public PetContactMessage findById(Integer messageId) {
        String sql = "SELECT * FROM v_pet_contact_messages WHERE id_message = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, messageId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractMessageFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar mensaje: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Eliminar un mensaje
     */
    public boolean delete(Integer messageId) {
        String sql = "DELETE FROM pet_contact_messages WHERE id_message = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, messageId);
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✅ Mensaje " + messageId + " eliminado");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar mensaje: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Extraer mensaje desde ResultSet
     */
    private PetContactMessage extractMessageFromResultSet(ResultSet rs) throws SQLException {
        PetContactMessage message = new PetContactMessage();
        message.setIdMessage(rs.getInt("id_message"));
        message.setIdPet(rs.getInt("id_pet"));
        message.setIdUser(rs.getInt("id_user"));
        message.setSenderName(rs.getString("sender_name"));
        message.setSenderPhone(rs.getString("sender_phone"));
        message.setMessage(rs.getString("message"));
        message.setCreatedAt(rs.getTimestamp("created_at"));
        message.setIsRead(rs.getBoolean("is_read"));
        
        // Datos de la vista
        message.setPetName(rs.getString("name_pet"));
        message.setPetPhoto(rs.getString("pet_photo"));
        message.setPetBreed(rs.getString("pet_breed"));
        message.setOwnerName(rs.getString("owner_name"));
        
        return message;
    }
}
