package com.laptrinhweb.zerostarcafe.domain.invoice.dao;

import com.laptrinhweb.zerostarcafe.core.context.DBContext;
import com.laptrinhweb.zerostarcafe.domain.invoice.model.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC implementation of {@link InvoiceDAO}.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
public class InvoiceDAOImpl implements InvoiceDAO {

    @Override
    public Optional<Invoice> findById(long invoiceId) throws SQLException {
        String sql = "SELECT id, order_id, invoice_number, store_id, user_id, issued_at, " +
                     "subtotal_amount, discount_amount, tax_amount, total_amount, status, " +
                     "buyer_name, buyer_tax_id, buyer_address, buyer_email, created_at " +
                     "FROM invoices WHERE id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, invoiceId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToInvoice(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Invoice> findByOrderId(long orderId) throws SQLException {
        String sql = "SELECT id, order_id, invoice_number, store_id, user_id, issued_at, " +
                     "subtotal_amount, discount_amount, tax_amount, total_amount, status, " +
                     "buyer_name, buyer_tax_id, buyer_address, buyer_email, created_at " +
                     "FROM invoices WHERE order_id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToInvoice(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Invoice> findByInvoiceNumber(String invoiceNumber) throws SQLException {
        String sql = "SELECT id, order_id, invoice_number, store_id, user_id, issued_at, " +
                     "subtotal_amount, discount_amount, tax_amount, total_amount, status, " +
                     "buyer_name, buyer_tax_id, buyer_address, buyer_email, created_at " +
                     "FROM invoices WHERE invoice_number = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, invoiceNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapToInvoice(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Invoice> findByUserId(long userId) throws SQLException {
        String sql = "SELECT id, order_id, invoice_number, store_id, user_id, issued_at, " +
                     "subtotal_amount, discount_amount, tax_amount, total_amount, status, " +
                     "buyer_name, buyer_tax_id, buyer_address, buyer_email, created_at " +
                     "FROM invoices WHERE user_id = ? ORDER BY created_at DESC";

        Connection conn = DBContext.getOrCreate();
        List<Invoice> invoices = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapToInvoice(rs));
                }
            }
        }

        return invoices;
    }

    @Override
    public List<Invoice> findByStoreId(long storeId) throws SQLException {
        String sql = "SELECT id, order_id, invoice_number, store_id, user_id, issued_at, " +
                     "subtotal_amount, discount_amount, tax_amount, total_amount, status, " +
                     "buyer_name, buyer_tax_id, buyer_address, buyer_email, created_at " +
                     "FROM invoices WHERE store_id = ? ORDER BY created_at DESC";

        Connection conn = DBContext.getOrCreate();
        List<Invoice> invoices = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapToInvoice(rs));
                }
            }
        }

        return invoices;
    }

    @Override
    public long save(Invoice invoice) throws SQLException {
        String sql = "INSERT INTO invoices (order_id, invoice_number, store_id, user_id, issued_at, " +
                     "subtotal_amount, discount_amount, tax_amount, total_amount, status, " +
                     "buyer_name, buyer_tax_id, buyer_address, buyer_email, created_at) " +
                     "VALUES (?, ?, ?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, invoice.getOrderId());
            ps.setString(2, invoice.getInvoiceNumber());
            ps.setLong(3, invoice.getStoreId());
            ps.setObject(4, invoice.getUserId());
            ps.setInt(5, invoice.getSubtotalAmount());
            ps.setInt(6, invoice.getDiscountAmount());
            ps.setInt(7, invoice.getTaxAmount());
            ps.setInt(8, invoice.getTotalAmount());
            ps.setString(9, invoice.getStatus());
            ps.setString(10, invoice.getBuyerName());
            ps.setString(11, invoice.getBuyerTaxId());
            ps.setString(12, invoice.getBuyerAddress());
            ps.setString(13, invoice.getBuyerEmail());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }

        throw new SQLException("Failed to create invoice, no ID obtained");
    }

    @Override
    public void updateStatus(long invoiceId, String status) throws SQLException {
        String sql = "UPDATE invoices SET status = ? WHERE id = ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, invoiceId);

            ps.executeUpdate();
        }
    }

    @Override
    public int getNextSequenceNumber(String datePrefix) throws SQLException {
        // Use HD prefix (matching InvoiceConstants.Format.INVOICE_NUMBER_PREFIX)
        String pattern = "HD-" + datePrefix + "-%";
        
        // Use MAX instead of COUNT to avoid race conditions
        String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING(invoice_number, -5) AS UNSIGNED)), 0) as max_seq " +
                     "FROM invoices " +
                     "WHERE invoice_number LIKE ?";

        Connection conn = DBContext.getOrCreate();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int maxSeq = rs.getInt("max_seq");
                    return maxSeq + 1;
                }
            }
        }

        return 1;
    }

    private Invoice mapToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getLong("id"));
        invoice.setOrderId(rs.getLong("order_id"));
        invoice.setInvoiceNumber(rs.getString("invoice_number"));
        invoice.setStoreId(rs.getLong("store_id"));
        invoice.setUserId(rs.getObject("user_id", Long.class));
        invoice.setIssuedAt(rs.getTimestamp("issued_at") != null ?
                rs.getTimestamp("issued_at").toLocalDateTime() : null);
        invoice.setSubtotalAmount(rs.getInt("subtotal_amount"));
        invoice.setDiscountAmount(rs.getInt("discount_amount"));
        invoice.setTaxAmount(rs.getInt("tax_amount"));
        invoice.setTotalAmount(rs.getInt("total_amount"));
        invoice.setStatus(rs.getString("status"));
        invoice.setBuyerName(rs.getString("buyer_name"));
        invoice.setBuyerTaxId(rs.getString("buyer_tax_id"));
        invoice.setBuyerAddress(rs.getString("buyer_address"));
        invoice.setBuyerEmail(rs.getString("buyer_email"));
        invoice.setCreatedAt(rs.getTimestamp("created_at") != null ?
                rs.getTimestamp("created_at").toLocalDateTime() : null);
        return invoice;
    }
}
