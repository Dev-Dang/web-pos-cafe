package com.laptrinhweb.zerostarcafe.domain.invoice.service;

import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.i18n.I18nUtils;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.invoice.dao.InvoiceDAO;
import com.laptrinhweb.zerostarcafe.domain.invoice.dao.InvoiceDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.invoice.dto.InvoiceDTO;
import com.laptrinhweb.zerostarcafe.domain.invoice.dto.InvoiceDetailDTO;
import com.laptrinhweb.zerostarcafe.domain.invoice.dto.InvoiceItemDTO;
import com.laptrinhweb.zerostarcafe.domain.invoice.model.Invoice;
import com.laptrinhweb.zerostarcafe.domain.order.dao.OrderDAO;
import com.laptrinhweb.zerostarcafe.domain.order.dao.OrderDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.order.model.Order;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderItem;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderItemOption;
import com.laptrinhweb.zerostarcafe.domain.payment.dao.PaymentDAO;
import com.laptrinhweb.zerostarcafe.domain.payment.dao.PaymentDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.payment.model.Payment;
import com.laptrinhweb.zerostarcafe.domain.store.model.Store;
import com.laptrinhweb.zerostarcafe.domain.store.service.StoreService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Service layer for invoice business logic.
 * Handles invoice generation from orders and invoice management.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InvoiceService {

    private static final InvoiceService INSTANCE = new InvoiceService();
    private final InvoiceDAO invoiceDAO = new InvoiceDAOImpl();
    private final OrderDAO orderDAO = new OrderDAOImpl();
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final StoreService storeService = StoreService.getInstance();

    public static InvoiceService getInstance() {
        return INSTANCE;
    }

    /**
     * Generates invoice from order (source of truth).
     * Loads order from database and calculates amounts from order items.
     *
     * @param orderId the order ID
     * @param discountAmount the loyalty discount applied
     * @return Invoice ready to save
     */
    public Invoice generateInvoice(@NonNull Long orderId, int discountAmount) {
        try {
            // Load order from database (source of truth)
            Order order = orderDAO.findById(orderId)
                    .orElseThrow(() -> new AppException("Order not found: " + orderId));

            // Generate invoice using mapper
            Invoice invoice = InvoiceMapper.toInvoice(order, discountAmount);

            // Generate unique invoice number
            String invoiceNumber = generateUniqueInvoiceNumber();
            invoice.setInvoiceNumber(invoiceNumber);

            LoggerUtil.info(InvoiceService.class,
                    String.format("Generated invoice %s for order ID=%d", invoiceNumber, orderId));

            return invoice;

        } catch (SQLException e) {
            throw new AppException("Failed to generate invoice for order: " + orderId, e);
        }
    }

    /**
     * Saves invoice and returns generated ID.
     *
     * @param invoice the invoice to save
     * @return generated invoice ID
     */
    public long save(@NonNull Invoice invoice) {
        try {
            long invoiceId = invoiceDAO.save(invoice);

            LoggerUtil.info(InvoiceService.class,
                    String.format("Saved invoice ID=%d, number=%s",
                            invoiceId, invoice.getInvoiceNumber()));

            return invoiceId;

        } catch (SQLException e) {
            throw new AppException("Failed to save invoice", e);
        }
    }

    /**
     * Gets invoices for a user, ordered by newest first.
     */
    public List<InvoiceDTO> getInvoicesByUser(@NonNull Long userId) {
        try {
            List<Invoice> invoices = invoiceDAO.findByUserId(userId);
            List<InvoiceDTO> results = new ArrayList<>();

            if (invoices == null || invoices.isEmpty()) {
                return results;
            }

            for (Invoice invoice : invoices) {
                results.add(mapToInvoiceDTO(invoice));
            }

            return results;
        } catch (SQLException e) {
            throw new AppException("Failed to load invoices for user: " + userId, e);
        }
    }

    /**
     * Gets detailed invoice data with localized names and payment method.
     */
    public InvoiceDetailDTO getInvoiceDetail(@NonNull Long invoiceId) {
        try {
            Optional<Invoice> invoiceOpt = invoiceDAO.findById(invoiceId);
            if (invoiceOpt.isEmpty()) {
                throw new AppException("Invoice not found: " + invoiceId);
            }

            Invoice invoice = invoiceOpt.get();

            Optional<Order> orderOpt = orderDAO.findById(invoice.getOrderId());
            if (orderOpt.isEmpty()) {
                throw new AppException("Order not found: " + invoice.getOrderId());
            }

            Order order = orderOpt.get();

            Store store = storeService.getActiveStoreById(invoice.getStoreId());

            InvoiceDetailDTO detail = new InvoiceDetailDTO();
            detail.setId(invoice.getId());
            detail.setInvoiceNumber(invoice.getInvoiceNumber());
            detail.setOrderId(invoice.getOrderId());
            detail.setStoreName(store != null ? store.getName() : "");
            detail.setStoreAddress(store != null ? store.getAddress() : "");
            detail.setCustomerName(invoice.getBuyerName());
            detail.setCustomerPhone(null);
            detail.setSubtotal(invoice.getSubtotalAmount());
            detail.setDiscountAmount(invoice.getDiscountAmount());
            detail.setTaxAmount(invoice.getTaxAmount());
            detail.setTotalAmount(invoice.getTotalAmount());
            detail.setPaymentMethod(resolvePaymentMethod(invoice.getOrderId()));
            detail.setStatus(invoice.getStatus());
            detail.setIssuedAt(invoice.getIssuedAt());
            detail.setIssuedTimeDisplay(formatIssuedTime(invoice.getIssuedAt()));
            detail.setIssuedDateDisplay(formatIssuedDate(invoice.getIssuedAt()));

            List<InvoiceItemDTO> itemDTOs = new ArrayList<>();
            List<OrderItem> orderItems = order.getItems();
            if (orderItems != null) {
                for (OrderItem item : orderItems) {
                    InvoiceItemDTO itemDTO = new InvoiceItemDTO();
                    itemDTO.setProductName(I18nUtils.extract(item.getItemNameSnapshot()));
                    itemDTO.setQuantity(item.getQty());

                    int unitPrice = item.getUnitPriceSnapshot() + item.getOptionsPriceSnapshot();
                    itemDTO.setUnitPrice(unitPrice);
                    itemDTO.setTotalPrice(unitPrice * item.getQty());
                    itemDTO.setNote(item.getNote());

                    List<String> optionLines = new ArrayList<>();
                    List<OrderItemOption> options = item.getOptions();
                    if (options != null) {
                        for (OrderItemOption option : options) {
                            String optionName = I18nUtils.extract(option.getOptionValueNameSnapshot());
                            if (optionName != null && !optionName.isBlank()) {
                                optionLines.add(optionName);
                            }
                        }
                    }
                    itemDTO.setOptionLines(optionLines);

                    itemDTOs.add(itemDTO);
                }
            }
            detail.setItems(itemDTOs);

            return detail;
        } catch (SQLException e) {
            throw new AppException("Failed to load invoice detail: " + invoiceId, e);
        }
    }

    /**
     * Generates unique invoice number using daily sequence.
     * Format: HD-20260109-00042
     *
     * @return unique invoice number
     */
    private String generateUniqueInvoiceNumber() {
        try {
            String datePrefix = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            // Get next sequence for today
            int sequence = invoiceDAO.getNextSequenceNumber(datePrefix);

            return InvoiceMapper.generateInvoiceNumber(sequence);

        } catch (SQLException e) {
            // Fallback: use timestamp if sequence fails
            LoggerUtil.warn(InvoiceService.class,
                    "Failed to get sequence, using timestamp fallback");

            long timestamp = System.currentTimeMillis() % 100000;
            return InvoiceMapper.generateInvoiceNumber((int) timestamp);
        }
    }

    private InvoiceDTO mapToInvoiceDTO(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(invoice.getId());
        dto.setOrderId(invoice.getOrderId());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setStoreId(invoice.getStoreId());
        dto.setUserId(invoice.getUserId());
        dto.setIssuedAt(invoice.getIssuedAt());
        dto.setIssuedAtDisplay(formatIssuedAt(invoice.getIssuedAt()));
        dto.setSubtotalAmount(invoice.getSubtotalAmount());
        dto.setDiscountAmount(invoice.getDiscountAmount());
        dto.setTaxAmount(invoice.getTaxAmount());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setStatus(invoice.getStatus());
        dto.setBuyerName(invoice.getBuyerName());
        dto.setBuyerEmail(invoice.getBuyerEmail());
        dto.setCreatedAt(invoice.getCreatedAt());

        Store store = storeService.getActiveStoreById(invoice.getStoreId());
        dto.setStoreName(store != null ? store.getName() : "");

        return dto;
    }

    private String formatIssuedAt(java.time.LocalDateTime issuedAt) {
        if (issuedAt == null) {
            return "";
        }
        return issuedAt.format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy"));
    }

    private String formatIssuedTime(java.time.LocalDateTime issuedAt) {
        if (issuedAt == null) {
            return "";
        }
        return issuedAt.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String formatIssuedDate(java.time.LocalDateTime issuedAt) {
        if (issuedAt == null) {
            return "";
        }
        return issuedAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String resolvePaymentMethod(long orderId) throws SQLException {
        List<Payment> payments = paymentDAO.findByOrderId(orderId);
        if (payments == null || payments.isEmpty()) {
            return null;
        }
        return payments.get(0).getMethod();
    }
}
