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
 * Domain model representing a shopping cart with full details including all items and options.
 * Maps to the {@code carts} table and includes related cart items via join.
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
public class Cart {
    private long id;
    private long userId;
    private long storeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItem> items = new ArrayList<>();
}