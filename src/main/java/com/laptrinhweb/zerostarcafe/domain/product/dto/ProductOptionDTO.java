package com.laptrinhweb.zerostarcafe.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * DTO representing an option group for UI display (e.g., "Size", "Toppings").
 * Contains localized name and available option values.
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
public class ProductOptionDTO {
    private long id;
    private String name;
    private String type;
    private boolean required;
    private int minSelect;
    private int maxSelect;
    private List<ProductOptionValueDTO> values;
}