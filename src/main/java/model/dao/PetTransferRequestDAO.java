package model.dao;

import config.DatabaseConnection;
import model.entity.PetTransferRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetTransferRequestDAO {

    // ─── Crear solicitud de transferencia ────────────────────
    public boolean createTransferRequest(PetTransferRequest req) throws SQLException {
        String sql = """
            INSERT INTO pet_transfer_requests
                (id_pet, id_foundation, adopter_email, adopter_name, adopter_phone, transfer_token, message)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, req.getIdPet());
            ps.setInt(2, req.getIdFoundation());
            ps.setString(3, req.getAdopterEmail());
            ps.setString(4, req.getAdopterName());
            ps.setString(5, req.getAdopterPhone() != null ? req.getAdopterPhone() : "");
            ps.setString(6, req.getTransferToken());
            ps.setString(7, req.getMessage() != null ? req.getMessage() : "");
            return ps.executeUpdate() > 0;
        }
    }

    // ─── Buscar por token ─────────────────────────────────────
    public PetTransferRequest findByToken(String token) throws SQLException {
        String sql = "SELECT * FROM pet_transfer_requests WHERE transfer_token = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractFromResultSet(rs);
            }
        }
        return null;
    }

    // ─── Buscar por mascota (transferencias pendientes) ───────
    public List<PetTransferRequest> findPendingByPetId(int idPet) throws SQLException {
        String sql = "SELECT * FROM pet_transfer_requests WHERE id_pet = ? AND status = 'pending' ORDER BY created_at DESC";
        List<PetTransferRequest> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPet);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(extractFromResultSet(rs));
            }
        }
        return list;
    }

    // ─── Actualizar estado ────────────────────────────────────
    public boolean updateStatus(int idTransfer, String status, Timestamp acceptedAt) throws SQLException {
        String sql = "UPDATE pet_transfer_requests SET status = ?, accepted_at = ? WHERE id_transfer = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setTimestamp(2, acceptedAt);
            ps.setInt(3, idTransfer);
            return ps.executeUpdate() > 0;
        }
    }

    // ─── Expirar solicitudes vencidas ─────────────────────────
    public int expireOldRequests() throws SQLException {
        String sql = "UPDATE pet_transfer_requests SET status = 'expired' WHERE status = 'pending' AND expires_at < NOW()";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return ps.executeUpdate();
        }
    }

    // ─── Extract helper ───────────────────────────────────────
    private PetTransferRequest extractFromResultSet(ResultSet rs) throws SQLException {
        PetTransferRequest req = new PetTransferRequest();
        req.setIdTransfer(rs.getInt("id_transfer"));
        req.setIdPet(rs.getInt("id_pet"));
        req.setIdFoundation(rs.getInt("id_foundation"));
        req.setAdopterEmail(rs.getString("adopter_email"));
        req.setAdopterName(rs.getString("adopter_name"));
        req.setAdopterPhone(rs.getString("adopter_phone"));
        req.setTransferToken(rs.getString("transfer_token"));
        req.setStatus(rs.getString("status"));
        req.setMessage(rs.getString("message"));
        req.setCreatedAt(rs.getTimestamp("created_at"));
        req.setAcceptedAt(rs.getTimestamp("accepted_at"));
        req.setExpiresAt(rs.getTimestamp("expires_at"));
        return req;
    }
}