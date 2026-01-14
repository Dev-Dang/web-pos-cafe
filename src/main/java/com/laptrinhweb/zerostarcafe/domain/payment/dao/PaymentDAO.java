package com.laptrinhweb.zerostarcafe.domain.payment.dao;

import com.laptrinhweb.zerostarcafe.domain.payment.model.Payment;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Data access interface for payment operations.
 * Provides CRUD operations for payments.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
public interface PaymentDAO {

    /**
     * Finds a payment by ID.
     *
     * @param paymentId the payment ID
     * @return optional payment if found
     * @throws SQLException if database error occurs
     */
    Optional<Payment> findById(long paymentId) throws SQLException;

    /**
     * Finds all payments for an order.
     *
     * @param orderId the order ID
     * @return list of payments
     * @throws SQLException if database error occurs
     */
    List<Payment> findByOrderId(long orderId) throws SQLException;

    /**
     * Finds all payments for a user.
     *
     * @param userId the user ID
     * @return list of payments
     * @throws SQLException if database error occurs
     */
    List<Payment> findByUserId(long userId) throws SQLException;

    /**
     * Finds a payment by transaction ID.
     *
     * @param transactionId the transaction ID from payment gateway
     * @return optional payment if found
     * @throws SQLException if database error occurs
     */
    Optional<Payment> findByTransactionId(String transactionId) throws SQLException;

    /**
     * Saves a new payment.
     *
     * @param payment the payment to save
     * @return generated payment ID
     * @throws SQLException if database error occurs
     */
    long save(Payment payment) throws SQLException;

    /**
     * Updates payment status.
     *
     * @param paymentId the payment ID
     * @param status the new status
     * @throws SQLException if database error occurs
     */
    void updateStatus(long paymentId, String status) throws SQLException;

    /**
     * Updates payment transaction information after payment gateway callback.
     *
     * @param paymentId the payment ID
     * @param transactionId the transaction ID from gateway
     * @param gatewayResponse the raw gateway response
     * @throws SQLException if database error occurs
     */
    void updateTransactionInfo(long paymentId, String transactionId, String gatewayResponse) throws SQLException;

    /**
     * Marks payment as completed.
     *
     * @param paymentId the payment ID
     * @param transactionId the transaction ID from gateway
     * @throws SQLException if database error occurs
     */
    void markAsCompleted(long paymentId, String transactionId) throws SQLException;
}
