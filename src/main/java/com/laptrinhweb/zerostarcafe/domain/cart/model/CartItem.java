package com.laptrinhweb.zerostarcafe.domain.cart.model;

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
 * Domain model representing an item in a shopping cart with full option details.
 * Maps to the {@code cart_items} table and includes related options via join.
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
public class CartItem {
    private long id;
    private long cartId;
    private long menuItemId;
    private int qty;
    private int unitPriceSnapshot;
    private int optionsPriceSnapshot;
    private String note;
    private String itemHash;
    private String itemNameSnapshot;
    private String imageUrlSnapshot;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CartItemOption> options = new ArrayList<>();
}