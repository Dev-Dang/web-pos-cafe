package com.laptrinhweb.zerostarcafe.domain.cart.service;

import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.exception.BusinessException;
import com.laptrinhweb.zerostarcafe.domain.cart.dao.CartDAO;
import com.laptrinhweb.zerostarcafe.domain.cart.dao.CartDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.cart.dto.AddToCartDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartConstants;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;
import com.laptrinhweb.zerostarcafe.domain.cart.utils.CartUtils;
import com.laptrinhweb.zerostarcafe.domain.product.model.Product;
import com.laptrinhweb.zerostarcafe.domain.product.service.ProductService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Service layer for cart business logic.
 * Always returns CartDTO for view layer.
 * </p>
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
     * Add item to cart with validation and merging logic.
     * Returns CartDTO for UI rendering.
     *
     * @param userId  user ID from session
     * @param storeId store ID from session
     * @param dto     add to cart request
     * @return CartDTO with localized fields
     */
    public CartDTO addToCart(@NonNull Long userId, @NonNull Long storeId, @NonNull AddToCartDTO dto) {

        Product product = productService.getActiveProductById(dto.getMenuItemId(), storeId);

        if (product == null) {
            throw new BusinessException("Product is not available");
        }

        Cart cart = getOrCreateCart(userId, storeId);
        CartItem newItem = CartMapper.toCartItem(product, dto);

        // Check existing item by hash
        CartItem existingItem = CartUtils.findExistingItemByHash(cart, newItem.getItemHash());
        if (existingItem != null) {
            // Merge: calculate new quantity
            int requestedQty = existingItem.getQty() + newItem.getQty();

            // Cap at maximum allowed quantity
            int finalQty = Math.min(requestedQty, CartConstants.Validation.MAX_QUANTITY);
            updateCartItemQuantity(existingItem.getId(), finalQty);

            // Reload cart and convert to DTO
            cart = getOrCreateCart(userId, storeId);
            return CartMapper.toCartDTO(cart);
        }

        // Validate cart item limit
        if (cart.getItems().size() >= CartConstants.Validation.MAX_ITEMS_PER_CART) {
            throw new BusinessException("Cart is full. Maximum "
                    + CartConstants.Validation.MAX_ITEMS_PER_CART + " items allowed");
        }

        // Save new cart item with options
        newItem.setCartId(cart.getId());
        saveCartItemWithOptions(newItem, product, dto);

        // Reload cart AFTER transaction commits and convert to DTO
        cart = getOrCreateCart(userId, storeId);
        return CartMapper.toCartDTO(cart);
    }

    /**
     * Update cart item quantity.
     * Returns updated CartDTO.
     *
     * @param userId     user ID from session
     * @param storeId    store ID from session
     * @param cartItemId cart item ID to update
     * @param newQty     new quantity
     * @return CartDTO with updated data
     */
    public CartDTO updateQuantity(@NonNull Long userId, @NonNull Long storeId,
                                  @NonNull Long cartItemId, int newQty) {
        // Validate quantity range
        if (newQty < CartConstants.Validation.MIN_QUANTITY) {
            throw new BusinessException("Quantity must be at least "
                    + CartConstants.Validation.MIN_QUANTITY);
        }
        if (newQty > CartConstants.Validation.MAX_QUANTITY) {
            throw new BusinessException("Quantity cannot exceed "
                    + CartConstants.Validation.MAX_QUANTITY);
        }

        // Update quantity
        updateCartItemQuantity(cartItemId, newQty);

        // Reload and return DTO
        Cart cart = getOrCreateCart(userId, storeId);
        return CartMapper.toCartDTO(cart);
    }

    /**
     * Remove item from cart.
     * Returns updated CartDTO.
     *
     * @param userId     user ID from session
     * @param storeId    store ID from session
     * @param cartItemId cart item ID to remove
     * @return CartDTO with updated data
     */
    public CartDTO removeItem(@NonNull Long userId, @NonNull Long storeId,
                              @NonNull Long cartItemId) {
        try {
            cartDAO.deleteCartItem(cartItemId);

            // Reload and return DTO
            Cart cart = getOrCreateCart(userId, storeId);
            return CartMapper.toCartDTO(cart);

        } catch (SQLException e) {
            throw new AppException("Failed to remove cart item", e);
        }
    }

    /**
     * Get current cart for user.
     * Returns CartDTO or null if no cart exists.
     *
     * @param userId  user ID from session
     * @param storeId store ID from session
     * @return CartDTO or null
     */
    public CartDTO getCurrentCart(long userId, long storeId) {
        try {
            Optional<Cart> cartOpt = cartDAO.findByUserIdAndStoreId(userId, storeId);
            if (cartOpt.isEmpty()) {
                return null;
            }

            return CartMapper.toCartDTO(cartOpt.get());

        } catch (SQLException e) {
            throw new AppException("Failed to get cart", e);
        }
    }

    /**
     * Get or create cart with full details (items + options).
     * Internal method - returns Cart model.
     */
    private Cart getOrCreateCart(long userId, long storeId) {
        try {
            Optional<Cart> cartOpt = cartDAO.findByUserIdAndStoreId(userId, storeId);

            if (cartOpt.isPresent()) {
                return cartOpt.get();
            }

            // Create new cart
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setStoreId(storeId);

            return cartDAO.save(newCart);

        } catch (SQLException e) {
            throw new AppException("Failed to get or create cart", e);
        }
    }

    /**
     * Save cart item with all options using the same connection.
     * Ensures data integrity within the request's transaction scope.
     */
    private void saveCartItemWithOptions(CartItem newItem, Product product, AddToCartDTO dto) {
        try {
            // Save cart item
            long itemId = cartDAO.saveCartItem(newItem);

            // Save cart item options
            List<CartItemOption> options = CartMapper.toCartItemOptions(product, dto.getOptionValueIds());
            for (CartItemOption option : options) {
                option.setCartItemId(itemId);
                cartDAO.saveCartItemOption(option);
            }

        } catch (SQLException e) {
            throw new AppException("Failed to save cart item", e);
        }
    }

    /**
     * Update cart item quantity.
     */
    private void updateCartItemQuantity(long cartItemId, int newQty) {
        try {
            cartDAO.updateCartItemQuantity(cartItemId, newQty);
        } catch (SQLException e) {
            throw new AppException("Failed to update cart item quantity", e);
        }
    }
}