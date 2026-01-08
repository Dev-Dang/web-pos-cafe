package com.laptrinhweb.zerostarcafe.domain.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing a complete product with all details including options and pricing.
 * Maps to the {@code menu_items} table and includes related option groups and pricing schedules.
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
public class Product {
    private long id;
    private long categoryId;
    private String nameJson;
    private String slug;
    private String imageUrl;
    private String descriptionJson;
    private int basePrice;
    private String unit;
    private boolean isActive;
    private LocalDateTime createdAt;

    // Store-specific fields
    private boolean inMenu;
    private String availabilityStatus;
    private LocalDateTime soldOutUntil;
    private String soldOutNote;

    // Pricing fields
    private Integer currentPrice;
    private boolean hasPromotion;
    private LocalDateTime promotionValidFrom;
    private LocalDateTime promotionValidTo;

    // Option groups
    private List<ProductOption> options;
}