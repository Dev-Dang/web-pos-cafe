package com.laptrinhweb.zerostarcafe.domain.invoice.dao;

import com.laptrinhweb.zerostarcafe.domain.invoice.model.Invoice;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Data access interface for invoice operations.
 * Provides CRUD operations for invoices.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
public interface InvoiceDAO {

    /**
     * Finds an invoice by ID.
     *
     * @param invoiceId the invoice ID
     * @return optional invoice if found
     * @throws SQLException if database error occurs
     */
    Optional<Invoice> findById(long invoiceId) throws SQLException;

    /**
     * Finds an invoice by order ID.
     *
     * @param orderId the order ID
     * @return optional invoice if found
     * @throws SQLException if database error occurs
     */
    Optional<Invoice> findByOrderId(long orderId) throws SQLException;

    /**
     * Finds an invoice by invoice number.
     *
     * @param invoiceNumber the invoice number
     * @return optional invoice if found
     * @throws SQLException if database error occurs
     */
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber) throws SQLException;

    /**
     * Finds all invoices for a user.
     *
     * @param userId the user ID
     * @return list of invoices
     * @throws SQLException if database error occurs
     */
    List<Invoice> findByUserId(long userId) throws SQLException;

    /**
     * Finds all invoices for a store.
     *
     * @param storeId the store ID
     * @return list of invoices
     * @throws SQLException if database error occurs
     */
    List<Invoice> findByStoreId(long storeId) throws SQLException;

    /**
     * Saves a new invoice.
     *
     * @param invoice the invoice to save
     * @return generated invoice ID
     * @throws SQLException if database error occurs
     */
    long save(Invoice invoice) throws SQLException;

    /**
     * Updates invoice status.
     *
     * @param invoiceId the invoice ID
     * @param status the new status
     * @throws SQLException if database error occurs
     */
    void updateStatus(long invoiceId, String status) throws SQLException;

    /**
     * Gets the next sequence number for invoice generation.
     *
     * @param datePrefix the date prefix (e.g., "20260108")
     * @return next sequence number
     * @throws SQLException if database error occurs
     */
    int getNextSequenceNumber(String datePrefix) throws SQLException;
}
