package com.laptrinhweb.zerostarcafe.domain.loyalty.service;

import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.loyalty.dao.LoyaltyDAO;
import com.laptrinhweb.zerostarcafe.domain.loyalty.dao.LoyaltyDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.loyalty.dto.LoyaltyPointsDTO;
import com.laptrinhweb.zerostarcafe.domain.loyalty.dto.RedeemCalcDTO;
import com.laptrinhweb.zerostarcafe.domain.loyalty.model.LoyaltyAccount;
import com.laptrinhweb.zerostarcafe.domain.loyalty.model.LoyaltyConstants;
import com.laptrinhweb.zerostarcafe.domain.loyalty.model.LoyaltyTransaction;
import com.laptrinhweb.zerostarcafe.domain.loyalty.model.TransactionType;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Service layer for loyalty points business logic. Handles points balance retrieval,
 * redemption calculations, and transaction recording.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * LoyaltyService loyaltyService = LoyaltyService.getInstance();
 *
 * // Get user's points balance
 * LoyaltyPointsDTO points = loyaltyService.getUserPoints(userId);
 *
 * // Calculate redemption for cart
 * RedeemCalcDTO calc = loyaltyService.calculateRedemption(userId, cartTotal, true);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class LoyaltyService {

    private static final LoyaltyService INSTANCE = new LoyaltyService();
    private final LoyaltyDAO loyaltyDAO = new LoyaltyDAOImpl();

    public static LoyaltyService getInstance() {
        return INSTANCE;
    }

    /**
     * Gets user's current loyalty points balance.
     * Creates account automatically if it doesn't exist.
     *
     * @param userId the user ID
     * @return DTO containing points balance
     */
    public LoyaltyPointsDTO getUserPoints(@NonNull Long userId) {
        try {
            Optional<LoyaltyAccount> accountOpt = loyaltyDAO.findByUserId(userId);

            LoyaltyAccount account;
            if (accountOpt.isEmpty()) {
                // Auto-create account for new user
                account = loyaltyDAO.createAccount(userId);
                LoggerUtil.info(LoyaltyService.class,
                        "Created new loyalty account for userId=" + userId);
            } else {
                account = accountOpt.get();
            }

            return new LoyaltyPointsDTO(account.getUserId(), account.getPointsBalance());

        } catch (SQLException e) {
            throw new AppException("Failed to get user points for userId=" + userId, e);
        }
    }

    /**
     * Calculates redemption details for cart checkout.
     *
     * @param userId      the user ID
     * @param cartTotal   the cart total amount in VND
     * @param applyPoints whether to apply points redemption
     * @return calculation result with redeemable points and discount
     */
    public RedeemCalcDTO calculateRedemption(
            @NonNull Long userId,
            int cartTotal,
            boolean applyPoints) {

        RedeemCalcDTO result = new RedeemCalcDTO();
        result.setApplied(applyPoints);

        try {
            // Get user's points balance
            Optional<LoyaltyAccount> accountOpt = loyaltyDAO.findByUserId(userId);
            if (accountOpt.isEmpty()) {
                // No account yet, no points to redeem
                result.setUserTotalPoints(0);
                result.setMaxRedeemablePoints(0);
                result.setPointsToRedeem(0);
                result.setDiscountAmount(0);
                result.setCanRedeem(false);
                return result;
            }

            LoyaltyAccount account = accountOpt.get();
            int userPoints = account.getPointsBalance();

            result.setUserTotalPoints(userPoints);

            // Calculate max redeemable points
            int maxAllowedByCart = (int) (cartTotal * LoyaltyConstants.MAX_REDEMPTION_PERCENT);
            int maxRedeemable = Math.min(userPoints, maxAllowedByCart);

            result.setMaxRedeemablePoints(maxRedeemable);

            // Check if user can redeem
            boolean canRedeem = userPoints >= LoyaltyConstants.MIN_POINTS_TO_REDEEM
                    && maxRedeemable >= LoyaltyConstants.MIN_POINTS_TO_REDEEM;

            result.setCanRedeem(canRedeem);

            // Calculate actual redemption if applied
            if (applyPoints && canRedeem) {
                result.setPointsToRedeem(maxRedeemable);
                result.setDiscountAmount(maxRedeemable * LoyaltyConstants.POINTS_TO_VND_RATIO);
            } else {
                result.setPointsToRedeem(0);
                result.setDiscountAmount(0);
            }

            return result;

        } catch (SQLException e) {
            throw new AppException("Failed to calculate redemption for userId=" + userId, e);
        }
    }

    /**
     * Records a points redemption transaction and updates user's balance.
     *
     * @param userId        the user ID
     * @param storeId       the store ID
     * @param paymentId     the payment ID
     * @param pointsUsed    the points to redeem
     * @param discountValue the VND value of discount applied
     * @return true if redemption successful
     */
    public boolean redeemPoints(
            @NonNull Long userId,
            @NonNull Long storeId,
            Long paymentId,
            int pointsUsed,
            int discountValue) {

        if (pointsUsed <= 0) {
            throw new IllegalArgumentException("Points to redeem must be positive");
        }

        try {
            // Get current balance
            Optional<LoyaltyAccount> accountOpt = loyaltyDAO.findByUserId(userId);
            if (accountOpt.isEmpty()) {
                throw new AppException("Loyalty account not found for userId=" + userId);
            }

            LoyaltyAccount account = accountOpt.get();
            int currentBalance = account.getPointsBalance();

            // Validate balance
            if (currentBalance < pointsUsed) {
                throw new AppException("Insufficient points. Has: " + currentBalance +
                        ", needs: " + pointsUsed);
            }

            // Update balance
            int newBalance = currentBalance - pointsUsed;
            boolean updated = loyaltyDAO.updateBalance(userId, newBalance);

            if (!updated) {
                throw new AppException("Failed to update points balance for userId=" + userId);
            }

            // Record transaction with discount value in reason
            LoyaltyTransaction transaction = new LoyaltyTransaction();
            transaction.setUserId(userId);
            transaction.setStoreId(storeId);
            transaction.setType(TransactionType.REDEEM);
            transaction.setPoints(-pointsUsed);
            transaction.setPaymentId(paymentId);
            transaction.setReason(String.format("Redeemed %d points for %,d VND discount",
                    pointsUsed, discountValue));
            transaction.setOccurredAt(LocalDateTime.now());

            loyaltyDAO.insertTransaction(transaction);

            LoggerUtil.info(LoyaltyService.class,
                    String.format("Redeemed %d points (-%,d VND) for userId=%d, new balance=%d",
                            pointsUsed, discountValue, userId, newBalance));

            return true;

        } catch (SQLException e) {
            throw new AppException("Failed to redeem points for userId=" + userId, e);
        }
    }

    /**
     * Awards points to user after a purchase.
     *
     * @param userId      the user ID
     * @param storeId     the store ID
     * @param orderAmount the order total amount
     * @param paymentId   the payment ID
     * @return points earned
     */
    public int awardPoints(
            @NonNull Long userId,
            @NonNull Long storeId,
            int orderAmount,
            Long paymentId) {

        if (orderAmount <= 0) {
            return 0;
        }

        try {
            // Calculate points earned (1 point per 1000 VND)
            int pointsEarned = orderAmount / LoyaltyConstants.EARN_RATE_PER_VND;

            if (pointsEarned <= 0) {
                return 0;
            }

            // Get or create account
            Optional<LoyaltyAccount> accountOpt = loyaltyDAO.findByUserId(userId);
            LoyaltyAccount account;

            if (accountOpt.isEmpty()) {
                account = loyaltyDAO.createAccount(userId);
            } else {
                account = accountOpt.get();
            }

            // Update balance
            int newBalance = account.getPointsBalance() + pointsEarned;
            loyaltyDAO.updateBalance(userId, newBalance);

            // Record transaction
            LoyaltyTransaction transaction = new LoyaltyTransaction();
            transaction.setUserId(userId);
            transaction.setStoreId(storeId);
            transaction.setType(TransactionType.EARN);
            transaction.setPoints(pointsEarned); // Positive for earning
            transaction.setPaymentId(paymentId);
            transaction.setReason("Earned from purchase of " + orderAmount + " VND");
            transaction.setOccurredAt(LocalDateTime.now());

            loyaltyDAO.insertTransaction(transaction);

            LoggerUtil.info(LoyaltyService.class,
                    String.format("Awarded %d points to userId=%d for order amount=%d",
                            pointsEarned, userId, orderAmount));

            return pointsEarned;

        } catch (SQLException e) {
            throw new AppException("Failed to award points for userId=" + userId, e);
        }
    }
}

