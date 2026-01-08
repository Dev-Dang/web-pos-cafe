package com.laptrinhweb.zerostarcafe.domain.invoice.model;

import java.time.LocalDateTime;

/**
 * Invoice entity representing an invoice record for legal/tax purposes.
 * Contains only metadata - item details are read from associated order.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @since 1.0.0
 */
public class Invoice {

    private Long id;
    private Long orderId;
    private String invoiceNumber;
    private Long storeId;
    private Long userId;
    private LocalDateTime issuedAt;
    private Integer subtotalAmount;
    private Integer discountAmount;
    private Integer taxAmount;
    private Integer totalAmount;
    private InvoiceStatus status;
    private String buyerName;
    private String buyerTaxId;
    private String buyerAddress;
    private String buyerEmail;
    private LocalDateTime createdAt;

    // Constructor
    public Invoice() {
        this.subtotalAmount = 0;
        this.discountAmount = 0;
        this.taxAmount = 0;
        this.totalAmount = 0;
        this.status = InvoiceStatus.ISSUED;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Integer getSubtotalAmount() {
        return subtotalAmount;
    }

    public void setSubtotalAmount(Integer subtotalAmount) {
        this.subtotalAmount = subtotalAmount;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Integer taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerTaxId() {
        return buyerTaxId;
    }

    public void setBuyerTaxId(String buyerTaxId) {
        this.buyerTaxId = buyerTaxId;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", storeId=" + storeId +
                ", userId=" + userId +
                ", issuedAt=" + issuedAt +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                '}';
    }
}