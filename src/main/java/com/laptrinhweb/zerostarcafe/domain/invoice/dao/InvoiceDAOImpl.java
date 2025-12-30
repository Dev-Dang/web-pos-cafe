package com.laptrinhweb.zerostarcafe.domain.invoice.dao;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.invoice.model.Invoice;
import com.laptrinhweb.zerostarcafe.domain.invoice.model.InvoiceStatus;

import java.sql.*;

/**
 * JDBC implementation of InvoiceDAO.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @since 1.0.0
 */
public class InvoiceDAOImpl implements InvoiceDAO {

    private static final String INSERT_INVOICE = """
            INSERT INTO invoices (order_id, invoice_number, store_id, user_id, issued_at, 
                                 subtotal_amount, discount_amount, tax_amount, total_amount, 
                                 status, buyer_name, buyer_tax_id, buyer_address, buyer_email)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_BY_ID = """
            SELECT id, order_id, invoice_number, store_id, user_id, issued_at,
                   subtotal_amount, discount_amount, tax_amount, total_amount,
                   status, buyer_name, buyer_tax_id, buyer_address, buyer_email, created_at
            FROM invoices WHERE id = ?
            """;

    private static final String SELECT_BY_ORDER_ID = """
            SELECT id, order_id, invoice_number, store_id, user_id, issued_at,
                   subtotal_amount, discount_amount, tax_amount, total_amount,
                   status, buyer_name, buyer_tax_id, buyer_address, buyer_email, created_at
            FROM invoices WHERE order_id = ?
            """;

    private static final String SELECT_BY_INVOICE_NUMBER = """
            SELECT id, order_id, invoice_number, store_id, user_id, issued_at,
                   subtotal_amount, discount_amount, tax_amount, total_amount,
                   status, buyer_name, buyer_tax_id, buyer_address, buyer_email, created_at
            FROM invoices WHERE invoice_number = ?
            """;

    private static final String UPDATE_INVOICE = """
            UPDATE invoices SET 
                subtotal_amount = ?, discount_amount = ?, tax_amount = ?, total_amount = ?,
                status = ?, buyer_name = ?, buyer_tax_id = ?, buyer_address = ?, buyer_email = ?
            WHERE id = ?
            """;

    private static final String EXISTS_BY_INVOICE_NUMBER = """
            SELECT 1 FROM invoices WHERE invoice_number = ?
            """;

    @Override
    public Invoice create(Invoice invoice) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_INVOICE, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, invoice.getOrderId());
            stmt.setString(2, invoice.getInvoiceNumber());
            stmt.setLong(3, invoice.getStoreId());
            if (invoice.getUserId() != null) {
                stmt.setLong(4, invoice.getUserId());
            } else {
                stmt.setNull(4, Types.BIGINT);
            }
            stmt.setTimestamp(5, Timestamp.valueOf(invoice.getIssuedAt()));
            stmt.setInt(6, invoice.getSubtotalAmount());
            stmt.setInt(7, invoice.getDiscountAmount());
            stmt.setInt(8, invoice.getTaxAmount());
            stmt.setInt(9, invoice.getTotalAmount());
            stmt.setString(10, invoice.getStatus().getDbValue());
            stmt.setString(11, invoice.getBuyerName());
            stmt.setString(12, invoice.getBuyerTaxId());
            stmt.setString(13, invoice.getBuyerAddress());
            stmt.setString(14, invoice.getBuyerEmail());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating invoice failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invoice.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating invoice failed, no ID obtained.");
                }
            }

            return invoice;

        } catch (SQLException e) {
            LoggerUtil.error(InvoiceDAOImpl.class, "Error creating invoice", e);
            throw new RuntimeException("Failed to create invoice", e);
        }
    }

    @Override
    public Invoice findById(Long id) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToInvoice(rs);
                }
            }

        } catch (SQLException e) {
            LoggerUtil.error(InvoiceDAOImpl.class, "Error finding invoice by ID: " + id, e);
        }
        return null;
    }

    @Override
    public Invoice findByOrderId(Long orderId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ORDER_ID)) {

            stmt.setLong(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToInvoice(rs);
                }
            }

        } catch (SQLException e) {
            LoggerUtil.error(InvoiceDAOImpl.class, "Error finding invoice by order ID: " + orderId, e);
        }
        return null;
    }

    @Override
    public Invoice findByInvoiceNumber(String invoiceNumber) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_INVOICE_NUMBER)) {

            stmt.setString(1, invoiceNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToInvoice(rs);
                }
            }

        } catch (SQLException e) {
            LoggerUtil.error(InvoiceDAOImpl.class, "Error finding invoice by number: " + invoiceNumber, e);
        }
        return null;
    }

    @Override
    public Invoice update(Invoice invoice) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_INVOICE)) {

            stmt.setInt(1, invoice.getSubtotalAmount());
            stmt.setInt(2, invoice.getDiscountAmount());
            stmt.setInt(3, invoice.getTaxAmount());
            stmt.setInt(4, invoice.getTotalAmount());
            stmt.setString(5, invoice.getStatus().getDbValue());
            stmt.setString(6, invoice.getBuyerName());
            stmt.setString(7, invoice.getBuyerTaxId());
            stmt.setString(8, invoice.getBuyerAddress());
            stmt.setString(9, invoice.getBuyerEmail());
            stmt.setLong(10, invoice.getId());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Updating invoice failed, no rows affected.");
            }

            return invoice;

        } catch (SQLException e) {
            LoggerUtil.error(InvoiceDAOImpl.class, "Error updating invoice", e);
            throw new RuntimeException("Failed to update invoice", e);
        }
    }

    @Override
    public boolean existsByInvoiceNumber(String invoiceNumber) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_BY_INVOICE_NUMBER)) {

            stmt.setString(1, invoiceNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            LoggerUtil.error(InvoiceDAOImpl.class, "Error checking invoice number existence: " + invoiceNumber, e);
            return false;
        }
    }

    private Invoice mapRowToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getLong("id"));
        invoice.setOrderId(rs.getLong("order_id"));
        invoice.setInvoiceNumber(rs.getString("invoice_number"));
        invoice.setStoreId(rs.getLong("store_id"));

        Long userId = rs.getLong("user_id");
        if (rs.wasNull()) {
            invoice.setUserId(null);
        } else {
            invoice.setUserId(userId);
        }

        invoice.setIssuedAt(rs.getTimestamp("issued_at").toLocalDateTime());
        invoice.setSubtotalAmount(rs.getInt("subtotal_amount"));
        invoice.setDiscountAmount(rs.getInt("discount_amount"));
        invoice.setTaxAmount(rs.getInt("tax_amount"));
        invoice.setTotalAmount(rs.getInt("total_amount"));
        invoice.setStatus(InvoiceStatus.fromDbValue(rs.getString("status")));
        invoice.setBuyerName(rs.getString("buyer_name"));
        invoice.setBuyerTaxId(rs.getString("buyer_tax_id"));
        invoice.setBuyerAddress(rs.getString("buyer_address"));
        invoice.setBuyerEmail(rs.getString("buyer_email"));
        invoice.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        return invoice;
    }
}