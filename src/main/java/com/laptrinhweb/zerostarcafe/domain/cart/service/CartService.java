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
import com.laptrinhweb.zerostarcafe.domain.invoice.dao.InvoiceDAO;
import com.laptrinhweb.zerostarcafe.domain.invoice.dao.InvoiceDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.invoice.model.Invoice;
import com.laptrinhweb.zerostarcafe.domain.order.dao.OrderDAO;
import com.laptrinhweb.zerostarcafe.domain.order.dao.OrderDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.order.model.Order;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderItem;
import com.laptrinhweb.zerostarcafe.domain.order.model.OrderItemOption;
import com.laptrinhweb.zerostarcafe.domain.product.model.Product;
import com.laptrinhweb.zerostarcafe.domain.product.service.ProductService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.SQLException;
import java.util.ArrayList;
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
    private final InvoiceDAO invoiceDAO = new InvoiceDAOImpl();
    private final OrderDAO orderDAO = new OrderDAOImpl();

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
     * Get cart item for editing.
     * Returns CartItem with options if it belongs to the user's cart.
     */
    public CartItem getCartItemForEdit(@NonNull Long userId, @NonNull Long storeId,
                                       @NonNull Long cartItemId) {
        Cart cart = getExistingCart(userId, storeId);
        if (cart == null || cart.getItems() == null) {
            return null;
        }

        for (CartItem item : cart.getItems()) {
            if (item.getId() == cartItemId) {
                return item;
            }
        }

        return null;
    }

    /**
     * Update cart item details (qty, note, options).
     * Returns updated CartDTO.
     */
    public CartDTO updateCartItem(@NonNull Long userId, @NonNull Long storeId,
                                  @NonNull Long cartItemId, @NonNull AddToCartDTO dto) {
        Cart cart = getExistingCart(userId, storeId);
        if (cart == null) {
            throw new BusinessException("Cart not found");
        }

        CartItem currentItem = null;
        for (CartItem item : cart.getItems()) {
            if (item.getId() == cartItemId) {
                currentItem = item;
                break;
            }
        }

        if (currentItem == null) {
            throw new BusinessException("Cart item not found");
        }

        if (currentItem.getMenuItemId() != dto.getMenuItemId()) {
            throw new BusinessException("Invalid cart item update");
        }

        Product product = productService.getActiveProductById(dto.getMenuItemId(), storeId);
        if (product == null) {
            throw new BusinessException("Product is not available");
        }

        CartItem updatedItem = CartMapper.toCartItem(product, dto);
        updatedItem.setId(currentItem.getId());
        updatedItem.setCartId(currentItem.getCartId());

        CartItem existingItem = CartUtils.findExistingItemByHash(cart, updatedItem.getItemHash());
        if (existingItem != null && existingItem.getId() != currentItem.getId()) {
            int requestedQty = existingItem.getQty() + updatedItem.getQty();
            int finalQty = Math.min(requestedQty, CartConstants.Validation.MAX_QUANTITY);
            updateCartItemQuantity(existingItem.getId(), finalQty);

            try {
                cartDAO.deleteCartItem(currentItem.getId());
            } catch (SQLException e) {
                throw new AppException("Failed to remove merged cart item", e);
            }
        } else {
            try {
                cartDAO.updateCartItemDetails(updatedItem);
                cartDAO.deleteCartItemOptions(currentItem.getId());

                List<CartItemOption> options = CartMapper.toCartItemOptions(product, dto.getOptionValueIds());
                for (CartItemOption option : options) {
                    option.setCartItemId(currentItem.getId());
                    cartDAO.saveCartItemOption(option);
                }
            } catch (SQLException e) {
                throw new AppException("Failed to update cart item", e);
            }
        }

        Cart updatedCart = getExistingCart(userId, storeId);
        return CartMapper.toCartDTO(updatedCart);
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
     * Clear all items from user's cart.
     * Used after successful payment.
     *
     * @param userId  user ID from session
     * @param storeId store ID from session
     */
    public void clearCart(@NonNull Long userId, @NonNull Long storeId) {
        try {
            Optional<Cart> cartOpt = cartDAO.findByUserIdAndStoreId(userId, storeId);

            if (cartOpt.isPresent()) {
                Cart cart = cartOpt.get();
                cartDAO.clearCart(cart.getId());
            }

        } catch (SQLException e) {
            throw new AppException("Failed to clear cart", e);
        }
    }

    /**
     * Clone cart items from an invoice's order.
     * Clears current cart then adds items from the invoice order.
     */
    public CartDTO cloneCartFromInvoice(@NonNull Long userId, @NonNull Long storeId,
                                        @NonNull Long invoiceId) {
        try {
            Optional<Invoice> invoiceOpt = invoiceDAO.findById(invoiceId);
            if (invoiceOpt.isEmpty()) {
                throw new BusinessException("Invoice not found");
            }

            Invoice invoice = invoiceOpt.get();
            Optional<Order> orderOpt = orderDAO.findById(invoice.getOrderId());
            if (orderOpt.isEmpty()) {
                throw new BusinessException("Order not found");
            }

            Order order = orderOpt.get();
            Cart cart = getOrCreateCart(userId, storeId);
            cartDAO.clearCart(cart.getId());

            List<OrderItem> items = order.getItems();
            if (items != null) {
                for (OrderItem item : items) {
                    AddToCartDTO dto = new AddToCartDTO();
                    dto.setMenuItemId(item.getMenuItemId());
                    dto.setQty(item.getQty());
                    dto.setNote(item.getNote());

                    List<Long> optionValueIds = new ArrayList<>();
                    List<OrderItemOption> options = item.getOptions();
                    if (options != null) {
                        for (OrderItemOption option : options) {
                            optionValueIds.add(option.getOptionValueId());
                        }
                    }

                    dto.setOptionValueIds(optionValueIds);
                    addToCart(userId, storeId, dto);
                }
            }

            Cart refreshedCart = getOrCreateCart(userId, storeId);
            return CartMapper.toCartDTO(refreshedCart);

        } catch (SQLException e) {
            throw new AppException("Failed to clone cart from invoice: " + invoiceId, e);
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
     * Get current cart for user.
     * Returns Cart model or null if no cart exists.
     */
    private Cart getExistingCart(long userId, long storeId) {
        try {
            Optional<Cart> cartOpt = cartDAO.findByUserIdAndStoreId(userId, storeId);
            if (cartOpt.isPresent()) {
                return cartOpt.get();
            }
            return null;
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
