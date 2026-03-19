package model.dao;

import config.DatabaseConnection;
import model.entity.FoundationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DAO para gestión de solicitudes de fundaciones
 */
public class FoundationRequestDAO {
    private static final Logger logger = LoggerFactory.getLogger(FoundationRequestDAO.class);
    
    /**
     * Crea una solicitud de fundación
     */
    public boolean create(FoundationRequest request) {
        String sql = "INSERT INTO foundation_requests (foundation_name, contact_name, email, " +
                    "phone, whatsapp, animal_types, current_animals, description, website) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, request.getFoundationName());
            stmt.setString(2, request.getContactName());
            stmt.setString(3, request.getEmail());
            stmt.setString(4, request.getPhone());
            stmt.setString(5, request.getWhatsapp());
            stmt.setString(6, request.getAnimalTypes());
            stmt.setInt(7, request.getCurrentAnimals());
            stmt.setString(8, request.getDescription());
            stmt.setString(9, request.getWebsite());
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    request.setIdRequest(rs.getInt(1));
                }
                logger.info("Solicitud de fundación creada: {}", request.getFoundationName());
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error al crear solicitud: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Obtiene una solicitud por ID
     */
    public FoundationRequest findById(Integer idRequest) {
        String sql = "SELECT f.*, u.name_user as reviewer_name " +
                    "FROM foundation_requests f " +
                    "LEFT JOIN users u ON f.reviewed_by = u.id_user " +
                    "WHERE f.id_request = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idRequest);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractFoundationFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error al buscar solicitud: {}", e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Obtiene solicitud por email
     */
    public FoundationRequest findByEmail(String email) {
        String sql = "SELECT f.*, u.name_user as reviewer_name " +
                    "FROM foundation_requests f " +
                    "LEFT JOIN users u ON f.reviewed_by = u.id_user " +
                    "WHERE f.email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractFoundationFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error al buscar solicitud por email: {}", e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Obtiene solicitud por token de registro
     */
    public FoundationRequest findByToken(String token) {
        String sql = "SELECT f.*, u.name_user as reviewer_name " +
                    "FROM foundation_requests f " +
                    "LEFT JOIN users u ON f.reviewed_by = u.id_user " +
                    "WHERE f.registration_token = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractFoundationFromResultSet(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error al buscar solicitud por token: {}", e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Obtiene solicitudes por estado
     */
    public List<FoundationRequest> findByStatus(String status) {
        List<FoundationRequest> requests = new ArrayList<>();
        String sql = "SELECT f.*, u.name_user as reviewer_name " +
                    "FROM foundation_requests f " +
                    "LEFT JOIN users u ON f.reviewed_by = u.id_user " +
                    "WHERE f.status = ? ORDER BY f.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                requests.add(extractFoundationFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error al buscar solicitudes por estado: {}", e.getMessage(), e);
        }
        
        return requests;
    }
    
    /**
     * Obtiene todas las solicitudes
     */
    public List<FoundationRequest> findAll() {
        List<FoundationRequest> requests = new ArrayList<>();
        String sql = "SELECT f.*, u.name_user as reviewer_name " +
                    "FROM foundation_requests f " +
                    "LEFT JOIN users u ON f.reviewed_by = u.id_user " +
                    "ORDER BY f.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                requests.add(extractFoundationFromResultSet(rs));
            }
            
            logger.info("Solicitudes de fundación encontradas: {}", requests.size());
            
        } catch (SQLException e) {
            logger.error("Error al listar solicitudes: {}", e.getMessage(), e);
        }
        
        return requests;
    }
    
    /**
     * Aprueba una solicitud y genera token de registro
     */
    public boolean approve(Integer idRequest, Integer reviewedBy) {
        String token = UUID.randomUUID().toString();
        String sql = "UPDATE foundation_requests SET status = 'approved', " +
                    "registration_token = ?, reviewed_at = CURRENT_TIMESTAMP, " +
                    "reviewed_by = ? WHERE id_request = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, token);
            
            if (reviewedBy != null) {
                stmt.setInt(2, reviewedBy);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            
            stmt.setInt(3, idRequest);
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                logger.info("Solicitud aprobada: {} - Token: {}", idRequest, token);
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error al aprobar solicitud: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Rechaza una solicitud
     */
    public boolean reject(Integer idRequest, Integer reviewedBy, String reason) {
        String sql = "UPDATE foundation_requests SET status = 'rejected', " +
                    "rejection_reason = ?, reviewed_at = CURRENT_TIMESTAMP, " +
                    "reviewed_by = ? WHERE id_request = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, reason);
            
            if (reviewedBy != null) {
                stmt.setInt(2, reviewedBy);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            
            stmt.setInt(3, idRequest);
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                logger.info("Solicitud rechazada: {}", idRequest);
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error al rechazar solicitud: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Cuenta solicitudes por estado
     */
    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM foundation_requests WHERE status = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error al contar solicitudes: {}", e.getMessage(), e);
        }

        return 0;
    }

    /**
     * Cuenta total de solicitudes
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM foundation_requests";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error al contar solicitudes: {}", e.getMessage(), e);
        }
        
        return 0;
    }
    
    /**
     * Marca la solicitud como registrada (token ya usado)
     */
    public boolean markAsRegistered(Integer idRequest) {
        String sql = "UPDATE foundation_requests SET status = 'registered' WHERE id_request = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRequest);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Error al marcar como registrada: {}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * Extrae una solicitud desde ResultSet
     */
    private FoundationRequest extractFoundationFromResultSet(ResultSet rs) throws SQLException {
        FoundationRequest request = new FoundationRequest();
        request.setIdRequest(rs.getInt("id_request"));
        request.setFoundationName(rs.getString("foundation_name"));
        request.setContactName(rs.getString("contact_name"));
        request.setEmail(rs.getString("email"));
        request.setPhone(rs.getString("phone"));
        request.setWhatsapp(rs.getString("whatsapp"));
        request.setAnimalTypes(rs.getString("animal_types"));
        request.setCurrentAnimals(rs.getInt("current_animals"));
        request.setDescription(rs.getString("description"));
        request.setWebsite(rs.getString("website"));
        request.setStatus(rs.getString("status"));
        request.setRegistrationToken(rs.getString("registration_token"));
        request.setCreatedAt(rs.getTimestamp("created_at"));
        request.setReviewedAt(rs.getTimestamp("reviewed_at"));
        request.setReviewedBy(rs.getObject("reviewed_by") != null ? rs.getInt("reviewed_by") : null);
        request.setRejectionReason(rs.getString("rejection_reason"));
        request.setReviewerName(rs.getString("reviewer_name"));
        
        return request;
    }
}