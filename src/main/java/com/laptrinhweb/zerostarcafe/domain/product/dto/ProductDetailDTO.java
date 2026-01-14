package com.laptrinhweb.zerostarcafe.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Complete DTO for product detail pages containing all information
 * needed for full product display including options and pricing.
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
public class ProductDetailDTO {
    private long id;
    private long categoryId;
    private String name;
    private String slug;
    private String imageUrl;
    private String description;
    private int basePrice;
    private int currentPrice;
    private String unit;
    private boolean hasPromotion;
    private LocalDateTime promotionValidFrom;
    private LocalDateTime promotionValidTo;
    private boolean isAvailable;
    private String availabilityStatus;
    private LocalDateTime soldOutUntil;
    private String soldOutNote;
    private List<ProductOptionDTO> options;
}