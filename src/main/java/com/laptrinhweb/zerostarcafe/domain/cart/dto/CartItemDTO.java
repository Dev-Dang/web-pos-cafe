package com.laptrinhweb.zerostarcafe.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * DTO representing a cart item with all necessary information for UI display.
 * Contains product snapshot data and selected options with computed pricing.
 * Supports both guest and logged-in user cart items.
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
public class CartItemDTO {
    private Long id;                           // null for guest cart items
    private Long cartId;                       // null for guest cart items
    private Long menuItemId;
    private Integer qty;
    private BigDecimal unitPriceSnapshot;      // original product price at time of adding
    private BigDecimal optionsPriceSnapshot;   // total options price at time of adding
    private String note;
    private String itemHash;                   // for identifying identical items
    private String itemNameSnapshot;           // JSON: {"vi": "...", "en": "..."}
    private List<CartItemOptionDTO> options;
    private BigDecimal lineTotal;              // computed: (unitPrice + optionsPrice) * qty
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}