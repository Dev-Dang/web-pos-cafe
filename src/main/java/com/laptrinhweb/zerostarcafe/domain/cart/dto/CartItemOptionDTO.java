package com.laptrinhweb.zerostarcafe.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h2>Description:</h2>
 * <p>
 * DTO for cart item option view with localized names.
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
    private long id;
    private String optionGroupName;
    private String optionValueName;
    private int priceDelta;
}