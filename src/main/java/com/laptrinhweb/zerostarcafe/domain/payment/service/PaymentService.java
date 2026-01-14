package com.laptrinhweb.zerostarcafe.domain.payment.service;

import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.service.CartService;
import com.laptrinhweb.zerostarcafe.domain.invoice.model.Invoice;
import com.laptrinhweb.zerostarcafe.domain.invoice.service.InvoiceService;
import com.laptrinhweb.zerostarcafe.domain.loyalty.dto.RedeemCalcDTO;
import com.laptrinhweb.zerostarcafe.domain.loyalty.service.LoyaltyService;
import com.laptrinhweb.zerostarcafe.domain.order.service.OrderService;
import com.laptrinhweb.zerostarcafe.domain.payment.dao.PaymentDAO;
import com.laptrinhweb.zerostarcafe.domain.payment.dao.PaymentDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.payment.dto.PaymentResultDTO;
import com.laptrinhweb.zerostarcafe.domain.payment.model.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Service layer for payment processing orchestration.
 * Handles full payment workflow: validation → order → payment → loyalty → invoice.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PaymentService {

    private static final PaymentService INSTANCE = new PaymentService();
    
    private final CartService cartService = CartService.getInstance();
    private final OrderService orderService = OrderService.getInstance();
    private final LoyaltyService loyaltyService = LoyaltyService.getInstance();
    private final InvoiceService invoiceService = InvoiceService.getInstance();
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();

    public static PaymentService getInstance() {
        return INSTANCE;
    }

    /**
     * Processes cash payment with full validation and Result pattern.
     * 
     * @param userId the user ID
     * @param storeId the store ID
     * @param applyLoyalty whether to apply loyalty points
     * @return PaymentResult with status and optional data
     */
    public PaymentResult<PaymentStatus, PaymentResultDTO> processCashPayment(
            @NonNull Long userId,
            @NonNull Long storeId,
            boolean applyLoyalty) {

        try {
            // STEP 1: VALIDATE CART
            CartDTO cart = cartService.getCurrentCart(userId, storeId);
            if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
                return PaymentResult.fail(PaymentStatus.CART_EMPTY);
            }

            // STEP 2: CALCULATE LOYALTY
            RedeemCalcDTO redemption = loyaltyService.calculateRedemption(
                    userId, cart.getTotal(), applyLoyalty
            );

            if (applyLoyalty && !redemption.isCanRedeem()) {
                return PaymentResult.fail(PaymentStatus.INSUFFICIENT_POINTS);
            }

            int finalAmount = cart.getTotal() - redemption.getDiscountAmount();

            // STEP 3: CREATE ORDER
            long orderId;
            try {
                orderId = orderService.createOrderFromCart(cart, userId, storeId);
            } catch (Exception e) {
                LoggerUtil.error(PaymentService.class, "Order creation failed", e);
                return PaymentResult.fail(PaymentStatus.ORDER_CREATION_FAILED);
            }

            // STEP 4: CREATE PAYMENT
            Payment payment = new Payment();
            payment.setSourceType(PaymentConstants.SourceType.ORDER);
            payment.setOrderId(orderId);
            payment.setUserId(userId);
            payment.setMethod(PaymentConstants.Method.CASH);
            payment.setType(PaymentConstants.Type.PREPAID);
            payment.setStatus(PaymentConstants.Status.PENDING);
            payment.setAmount(finalAmount);
            payment.setRedeemPointsUsed(redemption.getPointsToRedeem());
            payment.setRedeemValue(redemption.getDiscountAmount());

            long paymentId;
            try {
                paymentId = paymentDAO.save(payment);
            } catch (SQLException e) {
                LoggerUtil.error(PaymentService.class, "Payment record failed", e);
                return PaymentResult.fail(PaymentStatus.PAYMENT_RECORD_FAILED);
            }

            // STEP 5: PROCESS LOYALTY
            int earnedPoints = 0;
            try {
                if (redemption.getPointsToRedeem() > 0) {
                    loyaltyService.redeemPoints(userId, storeId, paymentId,
                        redemption.getPointsToRedeem(), redemption.getDiscountAmount());
                }
                
                earnedPoints = loyaltyService.awardPoints(userId, storeId, finalAmount, paymentId);
            } catch (Exception e) {
                LoggerUtil.error(PaymentService.class, "Loyalty processing failed", e);
            }

            // STEP 6: GENERATE INVOICE
            Invoice invoice;
            long invoiceId;
            try {
                invoice = invoiceService.generateInvoice(orderId, redemption.getDiscountAmount());
                invoiceId = invoiceService.save(invoice);
            } catch (Exception e) {
                LoggerUtil.error(PaymentService.class, "Invoice generation failed", e);
                try {
                    paymentDAO.updateStatus(paymentId, PaymentConstants.Status.FAILED);
                } catch (SQLException updateError) {
                    LoggerUtil.error(PaymentService.class, "Failed to update payment status", updateError);
                }
                try {
                    orderService.markOrderAsVoid(orderId);
                } catch (Exception orderError) {
                    LoggerUtil.error(PaymentService.class, "Failed to void order after invoice failure", orderError);
                }
                return PaymentResult.fail(PaymentStatus.INVOICE_GENERATION_FAILED);
            }

            // STEP 7: FINALIZE
            String txnRef = generateTxnRef(orderId);
            try {
                orderService.markOrderAsPaid(orderId);
                paymentDAO.markAsCompleted(paymentId, txnRef);
                cartService.clearCart(userId, storeId);
            } catch (Exception e) {
                LoggerUtil.error(PaymentService.class, "Finalization failed", e);
                return PaymentResult.fail(PaymentStatus.SYSTEM_ERROR);
            }

            // STEP 8: BUILD RESULT
            PaymentResultDTO resultDTO = new PaymentResultDTO();
            resultDTO.setSuccess(true);
            resultDTO.setPaymentId(paymentId);
            resultDTO.setTxnRef(txnRef);
            resultDTO.setStatus(PaymentConstants.Status.PAID);
            resultDTO.setOrderId(orderId);
            resultDTO.setInvoiceId(invoiceId);
            resultDTO.setInvoiceNumber(invoice.getInvoiceNumber());
            resultDTO.setFinalAmount(finalAmount);
            resultDTO.setPointsEarned(earnedPoints);

            return PaymentResult.ok(PaymentStatus.SUCCESS, resultDTO);

        } catch (Exception e) {
            LoggerUtil.error(PaymentService.class, "Unexpected payment error", e);
            return PaymentResult.fail(PaymentStatus.SYSTEM_ERROR);
        }
    }

    private String generateTxnRef(long orderId) {
        String timestamp = LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return String.format("CASH-%s-%d", timestamp, orderId);
    }
}
