package com.laptrinhweb.zerostarcafe.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * Lightweight DTO for product preview cards in the UI.
 * Contains only essential information needed for displaying product lists.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCardDTO {
    private long id;
    private String name;
    private String slug;
    private String imageUrl;
    private int basePrice;
    private int currentPrice;
    private int displayPrice;
    private String unit;
    private boolean hasPromotion;
    private boolean isAvailable;
    private String availabilityStatus;

    public int getDiscountAmount() {
        int amount = basePrice - currentPrice;
        return Math.max(0, amount);
    }

    public int getDiscountAmountK() {
        return getDiscountAmount() / 1000;
    }

    public int getDiscountPercent() {
        if (basePrice <= 0) {
            return 0;
        }
        return (getDiscountAmount() * 100) / basePrice;
    }
}
