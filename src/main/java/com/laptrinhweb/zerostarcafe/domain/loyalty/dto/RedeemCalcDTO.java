package com.laptrinhweb.zerostarcafe.domain.loyalty.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * DTO containing calculation results for points redemption in cart.
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
public class RedeemCalcDTO {

    private int userTotalPoints;
    private int maxRedeemablePoints;
    private int pointsToRedeem;
    private int discountAmount;
    private boolean canRedeem;
    private boolean isApplied;
}

