package com.laptrinhweb.zerostarcafe.domain.loyalty.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2>Description:</h2>
 * <p>
 * Constants for loyalty points system business rules and configuration.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LoyaltyConstants {

    public static final int POINTS_TO_VND_RATIO = 1;
    public static final int MIN_POINTS_TO_REDEEM = 100;
    public static final double MAX_REDEMPTION_PERCENT = 0.12;
    public static final int EARN_RATE_PER_VND = 1000;
    public static final int DEFAULT_EXPIRY_DAYS = 365;
}
