package com.laptrinhweb.zerostarcafe.domain.loyalty.dao;

import com.laptrinhweb.zerostarcafe.domain.loyalty.model.LoyaltyAccount;
import com.laptrinhweb.zerostarcafe.domain.loyalty.model.LoyaltyTransaction;

import java.sql.SQLException;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides database access operations for loyalty points system.
 * Handles both {@link LoyaltyAccount} and {@link LoyaltyTransaction} entities.
 * </p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *     <li>Find or create loyalty account for user</li>
 *     <li>Update points balance</li>
 *     <li>Record transaction history</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * LoyaltyDAO loyaltyDAO = new LoyaltyDAOImpl();
 *
 * // Get user's points balance
 * Optional<LoyaltyAccount> accountOpt = loyaltyDAO.findByUserId(userId);
 *
 * // Record redemption
 * loyaltyDAO.insertTransaction(transaction);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
public interface LoyaltyDAO {

    /**
     * Finds a loyalty account by user ID.
     *
     * @param userId the user ID
     * @return an {@link Optional} containing the account if found
     * @throws SQLException if a database access error occurs
     */
    Optional<LoyaltyAccount> findByUserId(long userId) throws SQLException;

    /**
     * Creates a new loyalty account for a user with zero balance.
     *
     * @param userId the user ID
     * @return the newly created account with generated ID
     * @throws SQLException if a database access error occurs
     */
    LoyaltyAccount createAccount(long userId) throws SQLException;

    /**
     * Updates the points balance for a loyalty account.
     *
     * @param userId     the user ID
     * @param newBalance the new points balance
     * @return true if update successful
     * @throws SQLException if a database access error occurs
     */
    boolean updateBalance(long userId, int newBalance) throws SQLException;

    /**
     * Inserts a new loyalty transaction record.
     *
     * @param transaction the transaction to insert
     * @return the generated transaction ID
     * @throws SQLException if a database access error occurs
     */
    long insertTransaction(LoyaltyTransaction transaction) throws SQLException;
}
