package com.laptrinhweb.zerostarcafe.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * DTO representing an individual option value for UI display (e.g., "Medium", "Large").
 * Contains localized name and pricing information.
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
public class ProductOptionValueDTO {
    private long id;
    private String name;
    private int priceDelta;
    private boolean isAvailable;
    private String note;
}