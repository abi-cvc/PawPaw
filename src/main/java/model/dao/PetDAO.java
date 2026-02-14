package model.dao;

import config.DatabaseConnection;
import model.entity.Pet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad Pet
 * Maneja todas las operaciones de base de datos relacionadas con mascotas
 */
public class PetDAO {
    
    /**
     * Crea una nueva mascota en la base de datos
     * 
     * @param pet Mascota a crear
     * @return true si se creó exitosamente
     */
    public boolean create(Pet pet) {
        String sql = "INSERT INTO pets (id_user, name_pet, age_pet, breed, sex_pet, " +
                     "medical_conditions, contact_phone, photo, status_pet, extra_comments) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, pet.getIdUser());
            pstmt.setString(2, pet.getNamePet());
            pstmt.setObject(3, pet.getAgePet(), Types.INTEGER);
            pstmt.setString(4, pet.getBreed());
            pstmt.setString(5, pet.getSexPet());
            pstmt.setString(6, pet.getMedicalConditions());
            pstmt.setString(7, pet.getContactPhone());
            pstmt.setString(8, pet.getPhoto());
            pstmt.setString(9, pet.getStatusPet() != null ? pet.getStatusPet() : "active");
            pstmt.setString(10, pet.getExtraComments());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pet.setIdPet(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear mascota: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Busca una mascota por su ID
     * 
     * @param idPet ID de la mascota
     * @return Mascota encontrada o null
     */
    public Pet findById(Integer idPet) {
        String sql = "SELECT * FROM pets WHERE id_pet = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPet);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractPetFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar mascota por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Obtiene todas las mascotas de un usuario específico
     * 
     * @param idUser ID del usuario
     * @return Lista de mascotas del usuario
     */
    public List<Pet> findByUserId(Integer idUser) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets WHERE id_user = ? ORDER BY creation_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUser);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pets.add(extractPetFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener mascotas del usuario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return pets;
    }
    
    /**
     * Obtiene todas las mascotas activas de un usuario
     * 
     * @param idUser ID del usuario
     * @return Lista de mascotas activas
     */
    public List<Pet> findActiveByUserId(Integer idUser) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets WHERE id_user = ? AND status_pet = 'active' ORDER BY creation_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUser);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pets.add(extractPetFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener mascotas activas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return pets;
    }
    
    /**
     * Obtiene todas las mascotas
     * 
     * @return Lista de todas las mascotas
     */
    public List<Pet> findAll() {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets ORDER BY creation_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pets.add(extractPetFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las mascotas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return pets;
    }
    
    /**
     * Actualiza una mascota existente
     * 
     * @param pet Mascota con los datos actualizados
     * @return true si se actualizó exitosamente
     */
    public boolean update(Pet pet) {
        String sql = "UPDATE pets SET name_pet = ?, age_pet = ?, breed = ?, sex_pet = ?, " +
                     "medical_conditions = ?, contact_phone = ?, photo = ?, status_pet = ?, " +
                     "extra_comments = ? WHERE id_pet = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, pet.getNamePet());
            pstmt.setObject(2, pet.getAgePet(), Types.INTEGER);
            pstmt.setString(3, pet.getBreed());
            pstmt.setString(4, pet.getSexPet());
            pstmt.setString(5, pet.getMedicalConditions());
            pstmt.setString(6, pet.getContactPhone());
            pstmt.setString(7, pet.getPhoto());
            pstmt.setString(8, pet.getStatusPet());
            pstmt.setString(9, pet.getExtraComments());
            pstmt.setInt(10, pet.getIdPet());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar mascota: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cambia el estado de una mascota
     * 
     * @param idPet ID de la mascota
     * @param status Nuevo estado (active, lost, found, inactive)
     * @return true si se actualizó exitosamente
     */
    public boolean updateStatus(Integer idPet, String status) {
        String sql = "UPDATE pets SET status_pet = ? WHERE id_pet = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, idPet);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de mascota: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Elimina una mascota (eliminación física)
     * 
     * @param idPet ID de la mascota a eliminar
     * @return true si se eliminó exitosamente
     */
    public boolean delete(Integer idPet) {
        String sql = "DELETE FROM pets WHERE id_pet = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPet);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar mascota: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cuenta el total de mascotas de un usuario
     * 
     * @param idUser ID del usuario
     * @return Número de mascotas
     */
    public int countByUserId(Integer idUser) {
        String sql = "SELECT COUNT(*) FROM pets WHERE id_user = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUser);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar mascotas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Cuenta las mascotas activas de un usuario
     * 
     * @param idUser ID del usuario
     * @return Número de mascotas activas
     */
    public int countActiveByUserId(Integer idUser) {
        String sql = "SELECT COUNT(*) FROM pets WHERE id_user = ? AND status_pet = 'active'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUser);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al contar mascotas activas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Verifica si una mascota pertenece a un usuario específico
     * 
     * @param idPet ID de la mascota
     * @param idUser ID del usuario
     * @return true si la mascota pertenece al usuario
     */
    public boolean belongsToUser(Integer idPet, Integer idUser) {
        String sql = "SELECT COUNT(*) FROM pets WHERE id_pet = ? AND id_user = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPet);
            pstmt.setInt(2, idUser);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar propiedad de mascota: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Extrae un objeto Pet desde un ResultSet
     * 
     * @param rs ResultSet con los datos de la mascota
     * @return Mascota creada desde el ResultSet
     * @throws SQLException si hay error al leer los datos
     */
    private Pet extractPetFromResultSet(ResultSet rs) throws SQLException {
        Pet pet = new Pet();
        pet.setIdPet(rs.getInt("id_pet"));
        pet.setIdUser(rs.getInt("id_user"));
        pet.setNamePet(rs.getString("name_pet"));
        
        // age_pet puede ser null
        int age = rs.getInt("age_pet");
        if (!rs.wasNull()) {
            pet.setAgePet(age);
        }
        
        pet.setBreed(rs.getString("breed"));
        pet.setSexPet(rs.getString("sex_pet"));
        pet.setMedicalConditions(rs.getString("medical_conditions"));
        pet.setContactPhone(rs.getString("contact_phone"));
        pet.setPhoto(rs.getString("photo"));
        pet.setStatusPet(rs.getString("status_pet"));
        pet.setCreationDate(rs.getTimestamp("creation_date"));
        pet.setExtraComments(rs.getString("extra_comments"));
        
        return pet;
    }
}