package model.dao;

import config.DatabaseConnection;
import model.entity.PasswordResetToken;

import java.sql.*;

/**
 * DAO para gestionar tokens de recuperación de contraseña
 */
public class PasswordResetTokenDAO {
    
    /**
     * Crea un nuevo token de recuperación
     */
    public boolean create(PasswordResetToken token) {
        String sql = "INSERT INTO password_reset_tokens (id_user, token, expiration_date) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, token.getIdUser());
            pstmt.setString(2, token.getToken());
            pstmt.setTimestamp(3, token.getExpirationDate());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        token.setIdToken(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear token de recuperación: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Busca un token por su valor
     */
    public PasswordResetToken findByToken(String token) {
        String sql = "SELECT * FROM password_reset_tokens WHERE token = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, token);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractTokenFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar token: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Marca un token como usado
     */
    public boolean markAsUsed(String token) {
        String sql = "UPDATE password_reset_tokens SET used = TRUE WHERE token = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, token);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al marcar token como usado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Invalida todos los tokens anteriores de un usuario
     */
    public boolean invalidateUserTokens(Integer userId) {
        String sql = "UPDATE password_reset_tokens SET used = TRUE WHERE id_user = ? AND used = FALSE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al invalidar tokens del usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina tokens expirados (limpieza de BD)
     */
    public int deleteExpiredTokens() {
        String sql = "DELETE FROM password_reset_tokens WHERE expiration_date < CURRENT_TIMESTAMP OR (used = TRUE AND created_at < CURRENT_TIMESTAMP - INTERVAL '7 days')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            return pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar tokens expirados: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Extrae un PasswordResetToken desde un ResultSet
     */
    private PasswordResetToken extractTokenFromResultSet(ResultSet rs) throws SQLException {
        PasswordResetToken token = new PasswordResetToken();
        token.setIdToken(rs.getInt("id_token"));
        token.setIdUser(rs.getInt("id_user"));
        token.setToken(rs.getString("token"));
        token.setExpirationDate(rs.getTimestamp("expiration_date"));
        token.setUsed(rs.getBoolean("used"));
        token.setCreatedAt(rs.getTimestamp("created_at"));
        return token;
    }
}