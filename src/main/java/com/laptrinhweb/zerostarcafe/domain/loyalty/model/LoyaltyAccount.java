package com.laptrinhweb.zerostarcafe.domain.loyalty.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing a user's loyalty points account.
 * Maps directly to the {@code loyalty_accounts} table.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoyaltyAccount {
    private long id;
    private long userId;
    private int pointsBalance;
}
