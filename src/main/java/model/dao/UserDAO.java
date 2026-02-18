package model.dao;

import config.DatabaseConnection;
import model.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad User
 * Maneja todas las operaciones de base de datos relacionadas con usuarios
 */
public class UserDAO {
    
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
            System.err.println("Error al crear usuario: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("Error al buscar usuario: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("Error al buscar usuario por email: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("Error al verificar email: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("Error al obtener usuarios: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
            e.printStackTrace();
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
    public boolean ressetPassword(Integer userId, String newHashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE id_user = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newHashedPassword);
            pstmt.setInt(2, userId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.println("✅ Contraseña actualizada para usuario ID: " + userId);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar contraseña: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("Error al desactivar usuario: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("Error al contar usuarios: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Extrae un objeto User desde un ResultSet
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
        return user;
    }
}