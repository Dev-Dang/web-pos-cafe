package com.laptrinhweb.zerostarcafe.core.transaction;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.exception.TransactionException;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC transaction wrapper with auto-commit disabled.
 * Exposes a single {@link Connection} and
 * must end with {@link #commit()} or {@link #rollback()}.
 * Closing an active transaction triggers a rollback and clears
 * {@link TransactionManager}.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
@Getter
public class Transaction implements AutoCloseable {
    private Connection connection;
    private boolean active = true;
    private boolean committed = false;
    private boolean rolledBack = false;

    Transaction() {
        try {
            this.connection = DBConnection.getConnection();
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new TransactionException("Failed to begin transaction", e);
        }
    }

    /**
     * Gets the connection associated with this transaction.
     *
     * @return Connection instance
     * @throws IllegalStateException if transaction is not active
     */
    public Connection getConnection() {
        if (!active) {
            throw new IllegalStateException("Transaction is not active");
        }
        return connection;
    }

    /**
     * Commits the transaction.
     *
     * @throws IllegalStateException if transaction is not active or already committed/rolled back
     * @throws RuntimeException      if commit fails
     */
    public void commit() {
        if (!active) {
            throw new IllegalStateException("Transaction is not active");
        }
        if (committed) {
            throw new IllegalStateException("Transaction already committed");
        }
        if (rolledBack) {
            throw new IllegalStateException("Transaction already rolled back");
        }

        try {
            connection.commit();
            committed = true;
            active = false;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to commit transaction", e);
        }
    }

    /**
     * Rolls back the transaction.
     *
     * @throws IllegalStateException if transaction is not active or already committed/rolled back
     * @throws RuntimeException      if rollback fails
     */
    public void rollback() {
        if (!active) {
            throw new IllegalStateException("Transaction is not active");
        }
        if (committed) {
            throw new IllegalStateException("Cannot rollback committed transaction");
        }
        if (rolledBack) {
            throw new IllegalStateException("Transaction already rolled back");
        }

        try {
            connection.rollback();
            rolledBack = true;
            active = false;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to rollback transaction", e);
        }
    }

    /**
     * Automatically called by try-with-resources.
     * Ends the transaction, cleaning up resources.
     * If transaction is still active, it will be rolled back.
     */
    @Override
    public void close() {
        try {
            if (active) {
                // Auto-rollback if not committed
                try {
                    connection.rollback();
                    rolledBack = true;
                } catch (SQLException e) {
                    System.err.println("Failed to auto-rollback transaction: " + e.getMessage());
                }
                active = false;
            }
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Failed to close transaction connection: " + e.getMessage());
            }

            // Clear from thread local
            TransactionManager.clearCurrent();
        }
    }

    @Override
    public String toString() {
        return String.format("Transaction{active=%s, committed=%s, rolledBack=%s}",
                active, committed, rolledBack);
    }
}
