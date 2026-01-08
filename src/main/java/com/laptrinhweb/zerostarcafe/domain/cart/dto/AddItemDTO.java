package com.laptrinhweb.zerostarcafe.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Request DTO for adding items to cart.
 * Contains all necessary information to create a cart item with options.
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
public class AddItemDTO {
    private Long menuItemId;
    private Integer quantity;
    private String note;
    private List<Long> optionValueIds;
}