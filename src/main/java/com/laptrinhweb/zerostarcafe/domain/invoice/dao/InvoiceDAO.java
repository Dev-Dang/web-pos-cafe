package com.laptrinhweb.zerostarcafe.domain.invoice.dao;

import com.laptrinhweb.zerostarcafe.domain.invoice.model.Invoice;

/**
 * Data Access Object for Invoice operations.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @since 1.0.0
 */
public interface InvoiceDAO {

    /**
     * Creates a new invoice record.
     *
     * @param invoice the invoice to create
     * @return the saved invoice with generated ID
     */
    Invoice create(Invoice invoice);

    /**
     * Finds an invoice by ID.
     *
     * @param id the invoice ID
     * @return the invoice, or null if not found
     */
    Invoice findById(Long id);

    /**
     * Finds an invoice by order ID.
     *
     * @param orderId the order ID
     * @return the invoice, or null if not found
     */
    Invoice findByOrderId(Long orderId);

    /**
     * Finds an invoice by invoice number.
     *
     * @param invoiceNumber the invoice number
     * @return the invoice, or null if not found
     */
    Invoice findByInvoiceNumber(String invoiceNumber);

    /**
     * Updates an existing invoice.
     *
     * @param invoice the invoice to update
     * @return the updated invoice
     */
    Invoice update(Invoice invoice);

    /**
     * Checks if an invoice number already exists.
     *
     * @param invoiceNumber the invoice number to check
     * @return true if exists, false otherwise
     */
    boolean existsByInvoiceNumber(String invoiceNumber);
}