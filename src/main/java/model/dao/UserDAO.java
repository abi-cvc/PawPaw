package model.dao;

import config.DatabaseConnection;
import model.entity.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad User
 * Maneja todas las operaciones de base de datos relacionadas con usuarios
 */
public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    
    /**
     * Crea un nuevo usuario en la base de datos
     * La contraseña se hashea automáticamente con BCrypt
     * 
     * @param user Usuario a crear
     * @return true si se creó exitosamente, false en caso contrario
     */
    public boolean create(User user) {
        String sql = "INSERT INTO users (name_user, email, password, rol, active) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Hashear la contraseña antes de guardarla
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
            
            pstmt.setString(1, user.getNameUser());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, user.getRol() != null ? user.getRol() : "user");
            pstmt.setBoolean(5, user.getActive() != null ? user.getActive() : true);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Obtener el ID generado
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setIdUser(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error al crear usuario: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Crea un usuario de fundación con campos de partner
     */
    public boolean createFoundation(User user) {
        String sql = "INSERT INTO users (name_user, email, password, rol, active, pet_limit, is_partner, partner_badge) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));

            pstmt.setString(1, user.getNameUser());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, user.getRol() != null ? user.getRol() : "user");
            pstmt.setBoolean(5, user.getActive() != null ? user.getActive() : true);
            pstmt.setInt(6, user.getPetLimit() != null ? user.getPetLimit() : 9999);
            pstmt.setBoolean(7, true);
            pstmt.setString(8, user.getPartnerBadge());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setIdUser(generatedKeys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            logger.error("Error al crear usuario fundación: {}", e.getMessage(), e);
        }

        return false;
    }

    /**
     * Actualiza la visibilidad de una fundación en la página pública
     */
    public boolean updatePartnerVisibility(Integer userId, boolean visible) {
        String sql = "UPDATE users SET partner_visible = ? WHERE id_user = ? AND is_partner = true";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, visible);
            pstmt.setInt(2, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Error al actualizar visibilidad: {}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * Busca un usuario por su email y verifica la contraseña
     * 
     * @param email Email del usuario
     * @param password Contraseña sin hashear
     * @return Usuario si las credenciales son correctas, null en caso contrario
     */
    public User findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND active = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    
                    // Verificar la contraseña con BCrypt
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        return extractUserFromResultSet(rs);
                    }
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al buscar usuario: {}", e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Busca un usuario por su email
     * 
     * @param email Email del usuario
     * @return Usuario encontrado o null
     */
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al buscar usuario por email: {}", e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Verifica si un email ya existe en la base de datos
     * 
     * @param email Email a verificar
     * @return true si existe, false si no existe
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al verificar email: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Busca un usuario por su ID
     * 
     * @param idUser ID del usuario
     * @return Usuario encontrado o null
     */
    public User findById(Integer idUser) {
        String sql = "SELECT * FROM users WHERE id_user = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUser);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al buscar usuario por ID: {}", e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Obtiene todos los usuarios
     * 
     * @return Lista de usuarios
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY registration_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error al obtener usuarios: {}", e.getMessage(), e);
        }
        
        return users;
    }
    
    /**
     * Actualiza un usuario existente
     * 
     * @param user Usuario con los datos actualizados
     * @return true si se actualizó exitosamente
     */
    public boolean update(User user) {
        String sql = "UPDATE users SET name_user = ?, email = ?, rol = ?, active = ? WHERE id_user = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getNameUser());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getRol());
            pstmt.setBoolean(4, user.getActive());
            pstmt.setInt(5, user.getIdUser());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.error("Error al actualizar usuario: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    // ✅ NUEVO: Actualizar límite de mascotas
    public boolean updatePetLimit(Integer userId, Integer newLimit) {
        String sql = "UPDATE users SET pet_limit = ? WHERE id_user = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newLimit);
            pstmt.setInt(2, userId);
            
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                logger.info("Límite actualizado para usuario {}: {}", userId, newLimit);
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error al actualizar límite: {}", e.getMessage(), e);
        }

        return false;
    }

    // ✅ NUEVO: Incrementar límite de mascotas (al comprar slots)
    public boolean incrementPetLimit(Integer userId, Integer slotsToAdd) {
        String sql = "UPDATE users SET pet_limit = pet_limit + ? WHERE id_user = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, slotsToAdd);
            pstmt.setInt(2, userId);
            
            int affected = pstmt.executeUpdate();
            
            if (affected > 0) {
                logger.info("Se agregaron {} slots al usuario {}", slotsToAdd, userId);
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error al incrementar límite: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Actualiza la contraseña de un usuario
     * 
     * @param idUser ID del usuario
     * @param newPassword Nueva contraseña (sin hashear)
     * @return true si se actualizó exitosamente
     */
    public boolean updatePassword(Integer idUser, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id_user = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));
            
            pstmt.setString(1, hashedPassword);
            pstmt.setInt(2, idUser);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.error("Error al actualizar contraseña: {}", e.getMessage(), e);
        }

        return false;
    }

    /**
     * Actualiza solo la contraseña de un usuario
     * 
     * @param userId ID del usuario
     * @param newHashedPassword Nueva contraseña hasheada
     * @return true si se actualizó correctamente
     */
    public boolean resetPassword(Integer userId, String newHashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE id_user = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newHashedPassword);
            pstmt.setInt(2, userId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("Contraseña actualizada para usuario ID: {}", userId);
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error al actualizar contraseña: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Elimina un usuario (eliminación física)
     * 
     * @param idUser ID del usuario a eliminar
     * @return true si se eliminó exitosamente
     */
    public boolean delete(Integer idUser) {
        String sql = "DELETE FROM users WHERE id_user = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUser);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.error("Error al eliminar usuario: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Desactiva un usuario (eliminación lógica)
     * 
     * @param idUser ID del usuario a desactivar
     * @return true si se desactivó exitosamente
     */
    public boolean deactivate(Integer idUser) {
        String sql = "UPDATE users SET active = false WHERE id_user = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUser);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            logger.error("Error al desactivar usuario: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Obtiene el conteo total de usuarios
     * 
     * @return Número total de usuarios
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM users";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error al contar usuarios: {}", e.getMessage(), e);
        }
        
        return 0;
    }
    
    /**
     * Extrae un objeto User desde un ResultSet
     * ✅ ACTUALIZADO: Incluye nuevos campos pet_limit, is_partner, partner_badge
     * 
     * @param rs ResultSet con los datos del usuario
     * @return Usuario creado desde el ResultSet
     * @throws SQLException si hay error al leer los datos
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setIdUser(rs.getInt("id_user"));
        user.setNameUser(rs.getString("name_user"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRegistrationDate(rs.getTimestamp("registration_date"));
        user.setRol(rs.getString("rol"));
        user.setActive(rs.getBoolean("active"));
        
        // ✅ AGREGAR: Campos nuevos con manejo de NULL
        try {
            user.setPetLimit(rs.getObject("pet_limit") != null ? rs.getInt("pet_limit") : 2);
        } catch (SQLException e) {
            user.setPetLimit(2); // Default si la columna no existe aún
        }
        
        try {
            user.setIsPartner(rs.getObject("is_partner") != null ? rs.getBoolean("is_partner") : false);
        } catch (SQLException e) {
            user.setIsPartner(false); // Default
        }
        
        try {
            user.setPartnerBadge(rs.getString("partner_badge"));
        } catch (SQLException e) {
            user.setPartnerBadge(null);
        }

        try {
            user.setPartnerVisible(rs.getObject("partner_visible") != null ? rs.getBoolean("partner_visible") : true);
        } catch (SQLException e) {
            user.setPartnerVisible(true);
        }

        return user;
    }
}