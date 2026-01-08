package com.laptrinhweb.zerostarcafe.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * <h2>Description:</h2>
 * <p>
 * DTO representing an option selected for a cart item.
 * Contains snapshot of option information for price consistency and UI display.
 * Supports both guest and logged-in user cart item options.
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
public class CartItemOptionDTO {
    private Long id;                              // null for guest cart item options
    private Long cartItemId;                      // null for guest cart item options
    private Long optionValueId;
    private String optionGroupNameSnapshot;       // JSON: {"vi": "...", "en": "..."}
    private String optionValueNameSnapshot;       // JSON: {"vi": "...", "en": "..."}
    private BigDecimal priceDeltaSnapshot;        // price difference from base product
}