package model.dao;

import config.DatabaseConnection;
import model.entity.SystemMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SystemMessageDAO {

    public boolean create(SystemMessage msg) {
        String sql = "INSERT INTO system_messages (id_user, subject, message) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, msg.getIdUser());
            pstmt.setString(2, msg.getSubject());
            pstmt.setString(3, msg.getMessage());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        msg.setIdMessage(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al crear mensaje de sistema: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<SystemMessage> findByUserId(Integer userId) {
        List<SystemMessage> messages = new ArrayList<>();
        String sql = "SELECT * FROM system_messages WHERE id_user = ? ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    messages.add(extractFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar mensajes de sistema: " + e.getMessage());
            e.printStackTrace();
        }
        return messages;
    }

    public int countUnreadByUserId(Integer userId) {
        String sql = "SELECT COUNT(*) FROM system_messages WHERE id_user = ? AND is_read = false";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar mensajes no leídos: " + e.getMessage());
        }
        return 0;
    }

    public boolean markAsRead(Integer messageId) {
        String sql = "UPDATE system_messages SET is_read = true WHERE id_message = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, messageId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al marcar mensaje como leído: " + e.getMessage());
        }
        return false;
    }

    public boolean markAllAsRead(Integer userId) {
        String sql = "UPDATE system_messages SET is_read = true WHERE id_user = ? AND is_read = false";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al marcar todos como leídos: " + e.getMessage());
        }
        return false;
    }

    public boolean delete(Integer messageId) {
        String sql = "DELETE FROM system_messages WHERE id_message = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, messageId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar mensaje: " + e.getMessage());
        }
        return false;
    }

    private SystemMessage extractFromResultSet(ResultSet rs) throws SQLException {
        SystemMessage msg = new SystemMessage();
        msg.setIdMessage(rs.getInt("id_message"));
        msg.setIdUser(rs.getInt("id_user"));
        msg.setSubject(rs.getString("subject"));
        msg.setMessage(rs.getString("message"));
        msg.setIsRead(rs.getBoolean("is_read"));
        msg.setCreatedAt(rs.getTimestamp("created_at"));
        return msg;
    }
}
