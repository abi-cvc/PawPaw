package model.dao;

import config.DatabaseConnection;
import model.entity.Suggestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar sugerencias
 */
public class SuggestionDAO {
    private static final Logger logger = LoggerFactory.getLogger(SuggestionDAO.class);
    
    /**
     * Crea una nueva sugerencia
     */
    public boolean create(Suggestion suggestion) {
        String sql = "INSERT INTO suggestions (id_user, message, status_suggestion) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, suggestion.getIdUser());
            pstmt.setString(2, suggestion.getMessage());
            pstmt.setString(3, suggestion.getStatusSuggestion() != null ? suggestion.getStatusSuggestion() : "pending");
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        suggestion.setIdSuggestion(generatedKeys.getInt(1));
                    }
                }
                logger.info("Sugerencia creada con ID: {}", suggestion.getIdSuggestion());
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error al crear sugerencia: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Obtiene todas las sugerencias con información del usuario
     */
    public List<Suggestion> findAll() {
        List<Suggestion> suggestions = new ArrayList<>();
        String sql = "SELECT s.*, u.name_user, u.email " +
                     "FROM suggestions s " +
                     "INNER JOIN users u ON s.id_user = u.id_user " +
                     "ORDER BY s.submission_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                suggestions.add(extractSuggestionFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error al obtener sugerencias: {}", e.getMessage(), e);
        }
        
        return suggestions;
    }
    
    /**
     * Obtiene sugerencias por estado
     */
    public List<Suggestion> findByStatus(String status) {
        List<Suggestion> suggestions = new ArrayList<>();
        String sql = "SELECT s.*, u.name_user, u.email " +
                     "FROM suggestions s " +
                     "INNER JOIN users u ON s.id_user = u.id_user " +
                     "WHERE s.status_suggestion = ? " +
                     "ORDER BY s.submission_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    suggestions.add(extractSuggestionFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al obtener sugerencias por estado: {}", e.getMessage(), e);
        }
        
        return suggestions;
    }
    
    /**
     * Obtiene sugerencias de un usuario específico
     */
    public List<Suggestion> findByUserId(Integer userId) {
        List<Suggestion> suggestions = new ArrayList<>();
        String sql = "SELECT s.*, u.name_user, u.email " +
                     "FROM suggestions s " +
                     "INNER JOIN users u ON s.id_user = u.id_user " +
                     "WHERE s.id_user = ? " +
                     "ORDER BY s.submission_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    suggestions.add(extractSuggestionFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al obtener sugerencias del usuario: {}", e.getMessage(), e);
        }
        
        return suggestions;
    }
    
    /**
     * Busca una sugerencia por su ID
     */
    public Suggestion findById(Integer idSuggestion) {
        String sql = "SELECT s.*, u.name_user, u.email " +
                     "FROM suggestions s " +
                     "INNER JOIN users u ON s.id_user = u.id_user " +
                     "WHERE s.id_suggestion = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idSuggestion);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractSuggestionFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al buscar sugerencia por ID: {}", e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Actualiza el estado de una sugerencia
     */
    public boolean updateStatus(Integer idSuggestion, String newStatus) {
        String sql = "UPDATE suggestions SET status_suggestion = ? WHERE id_suggestion = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, idSuggestion);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Estado actualizado a '{}' para sugerencia ID: {}", newStatus, idSuggestion);
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error al actualizar estado: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Actualiza el estado y agrega respuesta del administrador
     */
    public boolean updateStatusWithResponse(Integer idSuggestion, String newStatus, String adminResponse) {
        String sql = "UPDATE suggestions SET status_suggestion = ?, admin_response = ?, response_date = CURRENT_TIMESTAMP WHERE id_suggestion = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newStatus);
            pstmt.setString(2, adminResponse);
            pstmt.setInt(3, idSuggestion);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Sugerencia ID {} actualizada con respuesta", idSuggestion);
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error al actualizar sugerencia con respuesta: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Cuenta sugerencias por estado
     */
    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM suggestions WHERE status_suggestion = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al contar sugerencias: {}", e.getMessage(), e);
        }

        return 0;
    }

    /**
     * Obtiene el conteo total de sugerencias
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM suggestions";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error al contar sugerencias: {}", e.getMessage(), e);
        }
        
        return 0;
    }
    
    /**
     * Elimina una sugerencia
     */
    public boolean delete(Integer idSuggestion) {
        String sql = "DELETE FROM suggestions WHERE id_suggestion = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idSuggestion);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.error("Error al eliminar sugerencia: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Extrae una sugerencia desde un ResultSet
     */
    private Suggestion extractSuggestionFromResultSet(ResultSet rs) throws SQLException {
        Suggestion suggestion = new Suggestion();
        suggestion.setIdSuggestion(rs.getInt("id_suggestion"));
        suggestion.setIdUser(rs.getInt("id_user"));
        suggestion.setMessage(rs.getString("message"));
        suggestion.setSubmissionDate(rs.getTimestamp("submission_date"));
        suggestion.setStatusSuggestion(rs.getString("status_suggestion"));
        suggestion.setAdminResponse(rs.getString("admin_response"));
        suggestion.setResponseDate(rs.getTimestamp("response_date"));
        suggestion.setUserName(rs.getString("name_user"));
        suggestion.setUserEmail(rs.getString("email"));
        return suggestion;
    }
}