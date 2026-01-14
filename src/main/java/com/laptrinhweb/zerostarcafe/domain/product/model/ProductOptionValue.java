package com.laptrinhweb.zerostarcafe.domain.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing an individual option value (e.g., "Medium", "Large", "Extra Sugar").
 * Maps to the {@code option_values} table with store-specific availability.
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
public class ProductOptionValue {
    private long id;
    private long optionGroupId;
    private String nameJson;
    private int priceDelta;
    private boolean isActive;
    
    // Store-specific fields
    private boolean isAvailableInStore;
    private String storeAvailabilityStatus;
    private String storeNote;
}