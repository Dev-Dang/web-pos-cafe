package com.laptrinhweb.zerostarcafe.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * Request DTO for processing cash payment.
 * Simple DTO containing only the necessary data for instant payment.
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
public class CashPaymentDTO {
    private Long userId;
    private Long storeId;
    private boolean applyLoyaltyPoints;
}
