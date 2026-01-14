package com.laptrinhweb.zerostarcafe.domain.payment.dao;

import com.laptrinhweb.zerostarcafe.core.context.DBContext;
import com.laptrinhweb.zerostarcafe.domain.payment.model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC implementation of {@link PaymentDAO}.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public Optional<Payment> findById(long paymentId) throws SQLException {
        String sql = "SELECT id, source_type, order_id, booking_id, user_id, method, type, " +
                "status, amount, redeem_points_used, redeem_value, paid_at, txn_ref " +
                "FROM payments WHERE id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, paymentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToPayment(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Payment> findByOrderId(long orderId) throws SQLException {
        String sql = "SELECT id, source_type, order_id, booking_id, user_id, method, type, " +
                "status, amount, redeem_points_used, redeem_value, paid_at, txn_ref " +
                "FROM payments WHERE order_id = ? ORDER BY paid_at DESC";

        Connection conn = DBContext.getOrCreate();
        List<Payment> payments = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapToPayment(rs));
                }
            }
        }

        return payments;
    }

    @Override
    public List<Payment> findByUserId(long userId) throws SQLException {
        String sql = "SELECT id, source_type, order_id, booking_id, user_id, method, type, " +
                "status, amount, redeem_points_used, redeem_value, paid_at, txn_ref " +
                "FROM payments WHERE user_id = ? ORDER BY paid_at DESC";

        Connection conn = DBContext.getOrCreate();
        List<Payment> payments = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    payments.add(mapToPayment(rs));
                }
            }
        }

        return payments;
    }

    @Override
    public Optional<Payment> findByTransactionId(String transactionId) throws SQLException {
        String sql = "SELECT id, source_type, order_id, booking_id, user_id, method, type, " +
                "status, amount, redeem_points_used, redeem_value, paid_at, txn_ref " +
                "FROM payments WHERE txn_ref = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transactionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToPayment(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public long save(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (source_type, order_id, booking_id, user_id, method, type, " +
                "status, amount, redeem_points_used, redeem_value, paid_at, txn_ref) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, payment.getSourceType());
            ps.setObject(2, payment.getOrderId());
            ps.setObject(3, payment.getBookingId());
            ps.setObject(4, payment.getUserId());
            ps.setString(5, payment.getMethod());
            ps.setString(6, payment.getType());
            ps.setString(7, payment.getStatus());
            ps.setInt(8, payment.getAmount());
            ps.setInt(9, payment.getRedeemPointsUsed());
            ps.setInt(10, payment.getRedeemValue());
            if (payment.getPaidAt() != null) {
                ps.setTimestamp(11, Timestamp.valueOf(payment.getPaidAt()));
            } else {
                ps.setNull(11, Types.TIMESTAMP);
            }
            ps.setString(12, payment.getTxnRef());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        throw new SQLException("Failed to create payment, no ID obtained");
    }

    @Override
    public void updateStatus(long paymentId, String status) throws SQLException {
        String sql = "UPDATE payments SET status = ? WHERE id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, paymentId);

            ps.executeUpdate();
        }
    }

    @Override
    public void updateTransactionInfo(long paymentId, String transactionId, String gatewayResponse) throws SQLException {
        String sql = "UPDATE payments SET txn_ref = ? WHERE id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transactionId);
            ps.setLong(2, paymentId);

            ps.executeUpdate();
        }
    }

    @Override
    public void markAsCompleted(long paymentId, String transactionId) throws SQLException {
        String sql = "UPDATE payments SET status = 'paid', txn_ref = ?, paid_at = NOW() WHERE id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transactionId);
            ps.setLong(2, paymentId);

            ps.executeUpdate();
        }
    }

    /**
     * Marks payment as paid with custom timestamp.
     * Used for instant payments like cash.
     *
     * @param paymentId the payment ID
     * @param paidAt the paid timestamp
     * @throws SQLException if database error occurs
     */
    public void markAsPaid(long paymentId, java.time.LocalDateTime paidAt) throws SQLException {
        String sql = "UPDATE payments SET status = 'paid', paid_at = ? WHERE id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(paidAt));
            ps.setLong(2, paymentId);

            ps.executeUpdate();
        }
    }

    private Payment mapToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setId(rs.getLong("id"));
        payment.setSourceType(rs.getString("source_type"));
        payment.setOrderId(rs.getObject("order_id", Long.class));
        payment.setBookingId(rs.getObject("booking_id", Long.class));
        payment.setUserId(rs.getObject("user_id", Long.class));
        payment.setMethod(rs.getString("method"));
        payment.setType(rs.getString("type"));
        payment.setStatus(rs.getString("status"));
        payment.setAmount(rs.getInt("amount"));
        payment.setRedeemPointsUsed(rs.getInt("redeem_points_used"));
        payment.setRedeemValue(rs.getInt("redeem_value"));
        payment.setPaidAt(rs.getTimestamp("paid_at") != null ?
                rs.getTimestamp("paid_at").toLocalDateTime() : null);
        payment.setTxnRef(rs.getString("txn_ref"));
        return payment;
    }
}
