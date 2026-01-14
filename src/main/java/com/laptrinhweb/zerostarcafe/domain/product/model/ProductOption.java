package com.laptrinhweb.zerostarcafe.domain.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing an option group (e.g., "Size", "Toppings") with its available values.
 * Maps to the {@code option_groups} table and includes related option values.
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
public class ProductOption {
    private long id;
    private String nameJson;
    private String type;
    private boolean required;
    private int minSelect;
    private int maxSelect;
    private List<ProductOptionValue> values;
}