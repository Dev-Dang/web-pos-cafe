package com.laptrinhweb.zerostarcafe.core.transaction;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <h2>Description:</h2>
 * <p>
 * Transaction manager that provides fine-grained transaction control
 * while working with the existing TransactionFilter.
 * Uses single transaction per thread policy with automatic resource cleanup.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * try (Transaction tx = TransactionManager.begin()) {
 *     // do database operations
 *     tx.commit();
 * } // Automatic cleanup guaranteed
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
public class TransactionManager {

    private static final ThreadLocal<Transaction> currentTx = new ThreadLocal<>();

    /**
     * Begins a new transaction and makes it the current transaction
     * for this thread. Enforces single transaction per thread policy.
     *
     * @return new Transaction instance
     * @throws IllegalStateException if a transaction is already active
     */
    public static Transaction begin() {
        Transaction existing = currentTx.get();
        if (existing != null && existing.isActive()) {
            throw new IllegalStateException(
                    "Transaction already active! " +
                            "Must end current transaction before starting new one."
            );
        }

        Transaction tx = new Transaction();
        currentTx.set(tx);
        return tx;
    }

    /**
     * Gets the connection from the current active transaction.
     * Returns null if no transaction is active.
     *
     * @return Connection from current transaction or null
     */
    public static Connection getCurrentConnection() {
        Transaction tx = currentTx.get();
        if (tx != null && tx.isActive()) {
            return tx.getConnection();
        }
        return null;
    }

    /**
     * Checks if there is an active transaction in the current thread.
     *
     * @return true if transaction is active
     */
    public static boolean hasActiveTransaction() {
        Transaction tx = currentTx.get();
        return tx != null && tx.isActive();
    }

    /**
     * Clears the current transaction from thread local.
     * Called internally when transaction ends.
     */
    static void clearCurrent() {
        currentTx.remove();
    }
}