package com.laptrinhweb.zerostarcafe.domain.loyalty.dao;

import com.laptrinhweb.zerostarcafe.core.context.DBContext;
import com.laptrinhweb.zerostarcafe.domain.loyalty.model.LoyaltyAccount;
import com.laptrinhweb.zerostarcafe.domain.loyalty.model.LoyaltyTransaction;
import com.laptrinhweb.zerostarcafe.domain.loyalty.model.TransactionType;

import java.sql.*;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC implementation of {@link LoyaltyDAO} that interacts with
 * the {@code loyalty_accounts} and {@code loyalty_transactions} tables.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
public class LoyaltyDAOImpl implements LoyaltyDAO {

    // ==========================================================
    // LOYALTY ACCOUNT OPERATIONS
    // ==========================================================

    @Override
    public Optional<LoyaltyAccount> findByUserId(long userId) throws SQLException {
        String sql = """
                SELECT id, user_id, points_balance
                FROM loyalty_accounts
                WHERE user_id = ?
                """;

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToLoyaltyAccount(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public LoyaltyAccount createAccount(long userId) throws SQLException {
        String sql = """
                INSERT INTO loyalty_accounts (user_id, points_balance)
                VALUES (?, 0)
                """;

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, userId);

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating loyalty account failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    LoyaltyAccount account = new LoyaltyAccount();
                    account.setId(rs.getLong(1));
                    account.setUserId(userId);
                    account.setPointsBalance(0);
                    return account;
                }
                throw new SQLException("Creating loyalty account failed, no ID obtained.");
            }
        }
    }

    @Override
    public boolean updateBalance(long userId, int newBalance) throws SQLException {
        String sql = """
                UPDATE loyalty_accounts
                SET points_balance = ?
                WHERE user_id = ?
                """;

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newBalance);
            ps.setLong(2, userId);

            return ps.executeUpdate() > 0;
        }
    }

    // ==========================================================
    // TRANSACTION OPERATIONS
    // ==========================================================

    @Override
    public long insertTransaction(LoyaltyTransaction transaction) throws SQLException {
        String sql = """
                INSERT INTO loyalty_transactions
                (user_id, store_id, type, points, payment_id, expiry_date, reason, occurred_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, transaction.getUserId());

            if (transaction.getStoreId() != null) {
                ps.setLong(2, transaction.getStoreId());
            } else {
                ps.setNull(2, Types.BIGINT);
            }

            ps.setString(3, transaction.getType().name().toLowerCase());
            ps.setInt(4, transaction.getPoints());

            if (transaction.getPaymentId() != null) {
                ps.setLong(5, transaction.getPaymentId());
            } else {
                ps.setNull(5, Types.BIGINT);
            }

            if (transaction.getExpiryDate() != null) {
                ps.setDate(6, Date.valueOf(transaction.getExpiryDate()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setString(7, transaction.getReason());

            if (transaction.getOccurredAt() != null) {
                ps.setTimestamp(8, Timestamp.valueOf(transaction.getOccurredAt()));
            } else {
                ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            }

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Inserting loyalty transaction failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                throw new SQLException("Inserting loyalty transaction failed, no ID obtained.");
            }
        }
    }

    // ==========================================================
    // MAPPING UTIL
    // ==========================================================

    private LoyaltyAccount mapToLoyaltyAccount(ResultSet rs) throws SQLException {
        LoyaltyAccount account = new LoyaltyAccount();
        account.setId(rs.getLong("id"));
        account.setUserId(rs.getLong("user_id"));
        account.setPointsBalance(rs.getInt("points_balance"));
        return account;
    }
}
