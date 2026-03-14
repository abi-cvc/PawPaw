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
                "medical_conditions, contact_phone, photo, status_pet, extra_comments, " +
                "available_for_adoption, adoption_description, adoption_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            pstmt.setBoolean(11, pet.isAvailableForAdoption());
            pstmt.setString(12, pet.getAdoptionDescription());
            pstmt.setString(13, pet.getAdoptionStatus() != null ? pet.getAdoptionStatus() : "owned");
            
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
        String sql = "UPDATE pets SET name_pet = ?, age_pet = ?, breed = ?, " +
                "sex_pet = ?, medical_conditions = ?, contact_phone = ?, photo = ?, " +
                "status_pet = ?, extra_comments = ?, available_for_adoption = ?, " +
                "adoption_description = ?, adoption_status = ? " +
                "WHERE id_pet = ?";

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
            pstmt.setBoolean(10, pet.isAvailableForAdoption());
            pstmt.setString(11, pet.getAdoptionDescription());
            pstmt.setString(12, pet.getAdoptionStatus() != null ? pet.getAdoptionStatus() : "owned");
            pstmt.setInt(13, pet.getIdPet());
            
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
        pet.setAvailableForAdoption(rs.getBoolean("available_for_adoption"));
        pet.setAdoptionDescription(rs.getString("adoption_description"));
        pet.setAdoptionStatus(rs.getString("adoption_status"));

        return pet;
    }
    
    /**
     * Obtiene mascotas por estado de adopción
     */
    public List<Pet> findByAdoptionStatus(String adoptionStatus) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets WHERE adoption_status = ? ORDER BY name_pet";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, adoptionStatus);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
            	pets.add(extractPetFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar mascotas por estado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return pets;
    }

    /**
     * Obtiene mascotas de una fundación por estado de adopción
     */
    public List<Pet> findByUserAndStatus(Integer idUser, String adoptionStatus) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets WHERE id_user = ? AND adoption_status = ? ORDER BY name_pet";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUser);
            stmt.setString(2, adoptionStatus);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
            	pets.add(extractPetFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar mascotas por usuario y estado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return pets;
    }

    /**
     * Actualiza el estado de adopción de una mascota
     */
    public boolean updateAdoptionStatus(Integer idPet, String newStatus) {
        String sql = "UPDATE pets SET adoption_status = ? WHERE id_pet = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newStatus);
            stmt.setInt(2, idPet);
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                System.out.println("✅ Estado de adopción actualizado: " + idPet + " → " + newStatus);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar estado de adopción: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Cuenta mascotas por estado de adopción para un usuario
     */
    public int countByUserAndStatus(Integer idUser, String adoptionStatus) {
        String sql = "SELECT COUNT(*) FROM pets WHERE id_user = ? AND adoption_status = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idUser);
            stmt.setString(2, adoptionStatus);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al contar mascotas por estado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }

    /**
     * Obtiene todas las mascotas disponibles para adopción (público)
     */
    public List<Pet> findAvailableForAdoption() {
        return findByAdoptionStatus("available");
    }

    /**
     * Obtiene mascotas de fundaciones con info adicional
     */
    /**
     * Cuenta mascotas por usuario en bulk (evita N+1 queries)
     * DB-003: Reemplaza llamadas individuales a countByUserId en loop
     */
    public java.util.Map<Integer, Integer> countByUserIdBulk() {
        java.util.Map<Integer, Integer> counts = new java.util.HashMap<>();
        String sql = "SELECT id_user, COUNT(*) AS pet_count FROM pets GROUP BY id_user";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                counts.put(rs.getInt("id_user"), rs.getInt("pet_count"));
            }

        } catch (SQLException e) {
            System.err.println("Error al contar mascotas por usuario (bulk): " + e.getMessage());
            e.printStackTrace();
        }

        return counts;
    }

    /**
     * Cuenta el total de mascotas en el sistema
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM pets";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error al contar mascotas: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    public List<Pet> findFoundationPets(Integer idFoundation) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT p.* FROM pets p " +
                    "INNER JOIN users u ON p.id_user = u.id_user " +
                    "WHERE u.id_user = ? AND u.is_partner = true " +
                    "ORDER BY " +
                    "CASE p.adoption_status " +
                    "  WHEN 'available' THEN 1 " +
                    "  WHEN 'adopted_pending' THEN 2 " +
                    "  WHEN 'adopted_transferred' THEN 3 " +
                    "  ELSE 4 " +
                    "END, p.name_pet";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idFoundation);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
            	pets.add(extractPetFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar mascotas de fundación: " + e.getMessage());
            e.printStackTrace();
        }
        
        return pets;
    }
}