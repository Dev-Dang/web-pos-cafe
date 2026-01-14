package com.laptrinhweb.zerostarcafe.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * Request DTO for creating a new payment.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatePaymentRequest {
    private String sourceType;
    private Long orderId;
    private Long bookingId;
    private String method;
    private String type;
    private int amount;
    private String returnUrl;
}
