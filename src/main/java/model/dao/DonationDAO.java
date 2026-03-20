package model.dao;

import config.DatabaseConnection;
import model.entity.Donation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonationDAO {
    private static final Logger logger = LoggerFactory.getLogger(DonationDAO.class);

    public boolean create(Donation donation) {
        String sql = "INSERT INTO donations (id_user, donor_name, donor_email, amount, message, " +
                    "is_anonymous, payment_status, paypal_order_id, paypal_capture_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (donation.getIdUser() != null) {
                stmt.setInt(1, donation.getIdUser());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }

            stmt.setString(2, donation.getDonorName());
            stmt.setString(3, donation.getDonorEmail());
            stmt.setBigDecimal(4, donation.getAmount());
            stmt.setString(5, donation.getMessage());
            stmt.setBoolean(6, donation.isAnonymous());
            stmt.setString(7, donation.getPaymentStatus());
            stmt.setString(8, donation.getPaypalOrderId());
            stmt.setString(9, donation.getPaypalCaptureId());

            int affected = stmt.executeUpdate();

            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    donation.setIdDonation(rs.getInt(1));
                }
                logger.info("Donacion registrada: {}", donation.getIdDonation());
                return true;
            }

        } catch (SQLException e) {
            logger.error("Error al crear donacion: {}", e.getMessage(), e);
        }

        return false;
    }

    public List<Donation> findAll() {
        List<Donation> donations = new ArrayList<>();
        String sql = "SELECT * FROM donations ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                donations.add(extractDonationFromResultSet(rs));
            }

        } catch (SQLException e) {
            logger.error("Error al listar donaciones: {}", e.getMessage(), e);
        }

        return donations;
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM donations WHERE payment_status = 'completed'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("Error al contar donaciones: {}", e.getMessage(), e);
        }

        return 0;
    }

    public BigDecimal sumTotal() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM donations WHERE payment_status = 'completed'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getBigDecimal(1);
            }

        } catch (SQLException e) {
            logger.error("Error al sumar donaciones: {}", e.getMessage(), e);
        }

        return BigDecimal.ZERO;
    }

    public int countByMonth(int year, int month) {
        String sql = "SELECT COUNT(*) FROM donations WHERE payment_status = 'completed' " +
                    "AND EXTRACT(YEAR FROM created_at) = ? AND EXTRACT(MONTH FROM created_at) = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, year);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("Error al contar donaciones por mes: {}", e.getMessage(), e);
        }

        return 0;
    }

    public BigDecimal sumByMonth(int year, int month) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM donations WHERE payment_status = 'completed' " +
                    "AND EXTRACT(YEAR FROM created_at) = ? AND EXTRACT(MONTH FROM created_at) = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, year);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal(1);
            }

        } catch (SQLException e) {
            logger.error("Error al sumar donaciones por mes: {}", e.getMessage(), e);
        }

        return BigDecimal.ZERO;
    }

    /**
     * Retorna stats mensuales para un año: array de 12 sumas (una por mes)
     */
    public BigDecimal[] getMonthlyStats(int year) {
        BigDecimal[] stats = new BigDecimal[12];
        for (int i = 0; i < 12; i++) {
            stats[i] = BigDecimal.ZERO;
        }

        String sql = "SELECT EXTRACT(MONTH FROM created_at) AS mes, COALESCE(SUM(amount), 0) AS total " +
                    "FROM donations WHERE payment_status = 'completed' " +
                    "AND EXTRACT(YEAR FROM created_at) = ? " +
                    "GROUP BY EXTRACT(MONTH FROM created_at)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int month = rs.getInt("mes");
                BigDecimal total = rs.getBigDecimal("total");
                if (month >= 1 && month <= 12) {
                    stats[month - 1] = total;
                }
            }

        } catch (SQLException e) {
            logger.error("Error al obtener stats mensuales: {}", e.getMessage(), e);
        }

        return stats;
    }

    private Donation extractDonationFromResultSet(ResultSet rs) throws SQLException {
        Donation donation = new Donation();
        donation.setIdDonation(rs.getInt("id_donation"));
        donation.setIdUser(rs.getObject("id_user") != null ? rs.getInt("id_user") : null);
        donation.setDonorName(rs.getString("donor_name"));
        donation.setDonorEmail(rs.getString("donor_email"));
        donation.setAmount(rs.getBigDecimal("amount"));
        donation.setMessage(rs.getString("message"));
        donation.setAnonymous(rs.getBoolean("is_anonymous"));
        donation.setPaymentStatus(rs.getString("payment_status"));
        donation.setPaypalOrderId(rs.getString("paypal_order_id"));
        donation.setPaypalCaptureId(rs.getString("paypal_capture_id"));
        donation.setCreatedAt(rs.getTimestamp("created_at"));
        return donation;
    }
}
