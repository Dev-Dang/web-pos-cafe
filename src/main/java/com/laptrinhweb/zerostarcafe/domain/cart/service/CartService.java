package com.laptrinhweb.zerostarcafe.domain.cart.service;

import com.laptrinhweb.zerostarcafe.core.security.TokenUtils;
import com.laptrinhweb.zerostarcafe.domain.cart.dao.CartDAO;
import com.laptrinhweb.zerostarcafe.domain.cart.dao.CartDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartConstants;
import com.laptrinhweb.zerostarcafe.domain.product.service.ProductService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Business service for cart management operations.
 * Handles cart operations, item management, and price calculations.
 * Supports both guest (localStorage) and logged-in user carts.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * CartService cartService = CartService.getInstance();
 *
 * // Get or create cart for logged-in user
 * CartDTO cart = cartService.getOrCreateCart(userId, storeId);
 *
 * // Add item to cart
 * CartDTO updatedCart = cartService.addItem(userId, storeId, addItemRequest);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CartService {

    private static final CartService INSTANCE = new CartService();
    private final CartDAO cartDAO = new CartDAOImpl();
    private final ProductService productService = ProductService.getInstance();

    public static CartService getInstance() {
        return INSTANCE;
    }

    /**
     * Generate unique hash for cart item based on product and options.
     */
    private String generateItemHash(Long menuItemId, List<Long> optionValueIds, String note) {
        // Create canonical string for hashing
        StringBuilder hashInput = new StringBuilder();
        hashInput.append(CartConstants.Keys.ITEM).append(menuItemId);

        if (optionValueIds != null && !optionValueIds.isEmpty()) {
            List<Long> sortedIds = new ArrayList<>(optionValueIds);
            sortedIds.sort(Long::compareTo);
            hashInput.append(CartConstants.Keys.OPTIONS);
            for (int i = 0; i < sortedIds.size(); i++) {
                if (i > 0) hashInput.append(",");
                hashInput.append(sortedIds.get(i));
            }
        }

        if (note != null && !note.trim().isEmpty()) {
            hashInput.append(CartConstants.Keys.NOTE).append(note.trim());
        }

        return TokenUtils.hashToken(hashInput.toString());
    }
}