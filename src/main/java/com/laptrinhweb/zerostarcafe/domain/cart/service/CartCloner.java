package com.laptrinhweb.zerostarcafe.domain.cart.service;

import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Utility class for deep copying cart objects.
 * Ensures cache isolation by creating independent copies.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
public final class CartCloner {

    private CartCloner() {
    }

    /**
     * Creates a deep copy of a cart.
     *
     * @param source the cart to copy
     * @return a new cart instance with copied data
     */
    public static Cart cloneCart(Cart source) {
        if (source == null) {
            return null;
        }

        Cart clone = new Cart();
        clone.setId(source.getId());
        clone.setUserId(source.getUserId());
        clone.setStoreId(source.getStoreId());
        clone.setCreatedAt(source.getCreatedAt());
        clone.setUpdatedAt(source.getUpdatedAt());

        List<CartItem> clonedItems = new ArrayList<>();
        for (CartItem item : source.getItems()) {
            clonedItems.add(cloneCartItem(item));
        }
        clone.setItems(clonedItems);

        return clone;
    }

    /**
     * Creates a deep copy of a cart item.
     *
     * @param source the item to copy
     * @return a new item instance with copied data
     */
    public static CartItem cloneCartItem(CartItem source) {
        if (source == null) {
            return null;
        }

        CartItem clone = new CartItem();
        clone.setId(source.getId());
        clone.setCartId(source.getCartId());
        clone.setMenuItemId(source.getMenuItemId());
        clone.setQty(source.getQty());
        clone.setUnitPriceSnapshot(source.getUnitPriceSnapshot());
        clone.setOptionsPriceSnapshot(source.getOptionsPriceSnapshot());
        clone.setNote(source.getNote());
        clone.setItemHash(source.getItemHash());
        clone.setItemNameSnapshot(source.getItemNameSnapshot());
        clone.setCreatedAt(source.getCreatedAt());
        clone.setUpdatedAt(source.getUpdatedAt());

        List<CartItemOption> clonedOptions = new ArrayList<>();
        for (CartItemOption opt : source.getOptions()) {
            clonedOptions.add(cloneCartItemOption(opt));
        }
        clone.setOptions(clonedOptions);

        return clone;
    }

    /**
     * Creates a deep copy of a cart item option.
     *
     * @param source the option to copy
     * @return a new option instance with copied data
     */
    public static CartItemOption cloneCartItemOption(CartItemOption source) {
        if (source == null) {
            return null;
        }

        CartItemOption clone = new CartItemOption();
        clone.setId(source.getId());
        clone.setCartItemId(source.getCartItemId());
        clone.setOptionValueId(source.getOptionValueId());
        clone.setOptionGroupNameSnapshot(source.getOptionGroupNameSnapshot());
        clone.setOptionValueNameSnapshot(source.getOptionValueNameSnapshot());
        clone.setPriceDeltaSnapshot(source.getPriceDeltaSnapshot());
        return clone;
    }

    /**
     * Normalizes a cart item for cache insertion.
     * Creates a copy and ensures all required fields are set.
     *
     * @param source the item to normalize
     * @param cartId the cart ID to assign
     * @param itemHash the item hash to assign
     * @return a normalized copy of the item
     */
    public static CartItem normalizeForCache(CartItem source, long cartId, String itemHash) {
        CartItem normalized = cloneCartItem(source);
        normalized.setCartId(cartId);
        normalized.setItemHash(itemHash);
        normalized.setOptionsPriceSnapshot(normalized.calculateOptionsPriceFromList());
        return normalized;
    }
}
