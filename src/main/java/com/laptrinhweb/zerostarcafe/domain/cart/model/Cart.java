package com.laptrinhweb.zerostarcafe.domain.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain model representing a shopping cart. Maps to the {@code carts} table.
 * Contains cart items and tracks user's shopping session for a specific store.
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
    private Long id;
    private Long storeId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Related entities
    private List<CartItem> items;
}