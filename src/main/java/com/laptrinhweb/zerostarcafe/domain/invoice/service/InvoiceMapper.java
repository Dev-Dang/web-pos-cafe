package com.laptrinhweb.zerostarcafe.domain.invoice.service;

import com.laptrinhweb.zerostarcafe.domain.invoice.model.Invoice;
import com.laptrinhweb.zerostarcafe.domain.invoice.model.InvoiceConstants;
import com.laptrinhweb.zerostarcafe.domain.order.model.Order;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Mapper for invoice domain conversions.
 * Handles Order → Invoice transformations.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InvoiceMapper {

    /**
     * Converts Order to Invoice with calculated amounts.
     *
     * @param order          the order (source of truth)
     * @param discountAmount the loyalty discount applied
     * @return Invoice ready to save
     */
    public static Invoice toInvoice(Order order, int discountAmount) {
        Invoice invoice = new Invoice();

        invoice.setOrderId(order.getId());
        invoice.setStoreId(order.getStoreId());
        invoice.setUserId(order.getUserId());
        invoice.setIssuedAt(LocalDateTime.now());

        // Calculate amounts from ORDER ITEMS (source of truth)
        int subtotal = calculateSubtotal(order.getItems());

        invoice.setSubtotalAmount(subtotal);
        invoice.setDiscountAmount(discountAmount);
        invoice.setTaxAmount(0);  // No tax for now
        invoice.setTotalAmount(subtotal - discountAmount);

        // Set status
        invoice.setStatus(InvoiceConstants.Status.ISSUED);

        // Optional buyer info (NULL for retail)
        invoice.setBuyerName(null);
        invoice.setBuyerTaxId(null);
        invoice.setBuyerAddress(null);
        invoice.setBuyerEmail(null);

        return invoice;
    }

    /**
     * Generates invoice number with HD prefix.
     * Format: HD-YYYYMMDD-XXXXX
     * Example: HD-20260109-00042
     *
     * @param sequence the daily sequence number
     * @return formatted invoice number
     */
    public static String generateInvoiceNumber(int sequence) {
        String datePrefix = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(InvoiceConstants.Format.DATE_FORMAT));

        return String.format("%s-%s-%0" + InvoiceConstants.Format.SEQUENCE_LENGTH + "d",
                InvoiceConstants.Format.INVOICE_NUMBER_PREFIX, datePrefix, sequence);
    }

    /**
     * Calculates subtotal from order items.
     * Sum of (unit price + options price) × quantity for all items.
     *
     * @param items the order items
     * @return subtotal amount in VND
     */
    private static int calculateSubtotal(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            return 0;
        }

        int subtotal = 0;
        for (OrderItem item : items) {
            int itemPrice = item.getUnitPriceSnapshot() + item.getOptionsPriceSnapshot();
            subtotal += itemPrice * item.getQty();
        }
        
        return subtotal;
    }
}
