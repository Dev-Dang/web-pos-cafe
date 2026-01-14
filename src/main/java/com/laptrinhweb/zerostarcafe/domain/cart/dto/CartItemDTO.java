package com.laptrinhweb.zerostarcafe.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * DTO for cart item view with localized name and options.
 * Supports both logged-in users (full options) and guests (optionValueIds only).
 * </p>
 *
 * @author Dang Van Trung
 * @version 2.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDTO {
    private long id;
    private long cartId;
    private long menuItemId;
    private String itemName;
    private String imageUrl;
    private int qty;
    private int unitPrice;
    private int optionsPrice;
    private int itemTotal;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private List<CartItemOptionDTO> options = new ArrayList<>();
}