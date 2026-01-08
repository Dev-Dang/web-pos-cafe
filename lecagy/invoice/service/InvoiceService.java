package com.laptrinhweb.zerostarcafe.domain.invoice.service;

import com.laptrinhweb.zerostarcafe.domain.invoice.dao.InvoiceDAO;
import com.laptrinhweb.zerostarcafe.domain.invoice.dao.InvoiceDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.invoice.model.Invoice;
import com.laptrinhweb.zerostarcafe.domain.invoice.model.InvoiceStatus;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Invoice service for creating and managing invoices.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @since 1.0.0
 */
public class InvoiceService {

    private final InvoiceDAO invoiceDAO;
    private static final DateTimeFormatter INVOICE_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public InvoiceService() {
        this.invoiceDAO = new InvoiceDAOImpl();
    }

    public InvoiceService(InvoiceDAO invoiceDAO) {
        this.invoiceDAO = invoiceDAO;
    }

    /**
     * Creates an invoice for an order.
     *
     * @param orderId      the order ID
     * @param storeId      the store ID
     * @param userId       the user ID (can be null for guest orders)
     * @param totalAmount  the order total amount
     * @param buyerName    optional buyer name
     * @param buyerEmail   optional buyer email
     * @return the created invoice
     */
    public Invoice createInvoice(Long orderId, Long storeId, Long userId, Integer totalAmount,
                                String buyerName, String buyerEmail) {
        try {
            // Generate unique invoice number
            String invoiceNumber = generateInvoiceNumber(storeId);
            
            // Ensure uniqueness
            int counter = 1;
            while (invoiceDAO.existsByInvoiceNumber(invoiceNumber)) {
                invoiceNumber = generateInvoiceNumber(storeId) + "-" + counter;
                counter++;
                if (counter > 100) {
                    throw new RuntimeException("Unable to generate unique invoice number");
                }
            }

            Invoice invoice = new Invoice();
            invoice.setOrderId(orderId);
            invoice.setInvoiceNumber(invoiceNumber);
            invoice.setStoreId(storeId);
            invoice.setUserId(userId);
            invoice.setIssuedAt(LocalDateTime.now());
            invoice.setSubtotalAmount(totalAmount);
            invoice.setTotalAmount(totalAmount);
            invoice.setStatus(InvoiceStatus.ISSUED);
            invoice.setBuyerName(buyerName);
            invoice.setBuyerEmail(buyerEmail);

            return invoiceDAO.create(invoice);

        } catch (Exception e) {
            LoggerUtil.error(InvoiceService.class, "Error creating invoice for order: " + orderId, e);
            throw new RuntimeException("Failed to create invoice", e);
        }
    }

    /**
     * Finds an invoice by order ID.
     *
     * @param orderId the order ID
     * @return the invoice, or null if not found
     */
    public Invoice findByOrderId(Long orderId) {
        return invoiceDAO.findByOrderId(orderId);
    }

    /**
     * Finds an invoice by ID.
     *
     * @param id the invoice ID
     * @return the invoice, or null if not found
     */
    public Invoice findById(Long id) {
        return invoiceDAO.findById(id);
    }

    /**
     * Finds an invoice by invoice number.
     *
     * @param invoiceNumber the invoice number
     * @return the invoice, or null if not found
     */
    public Invoice findByInvoiceNumber(String invoiceNumber) {
        return invoiceDAO.findByInvoiceNumber(invoiceNumber);
    }

    /**
     * Generates an invoice number based on store ID and timestamp.
     *
     * @param storeId the store ID
     * @return generated invoice number
     */
    private String generateInvoiceNumber(Long storeId) {
        String timestamp = LocalDateTime.now().format(INVOICE_NUMBER_FORMAT);
        return "INV-" + storeId + "-" + timestamp;
    }
}