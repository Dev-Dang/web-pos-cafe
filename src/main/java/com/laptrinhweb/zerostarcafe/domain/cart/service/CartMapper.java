package com.laptrinhweb.zerostarcafe.domain.cart.service;

import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartItemDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartItemOptionDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Mapper utility for converting between cart domain models and DTOs.
 * Handles bidirectional conversion and price calculations.
 * Uses basic Java syntax for better readability.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CartMapper {

    // ===== Model to DTO Conversion =====

    /**
     * Convert Cart model to CartDTO with computed totals.
     * @param cart The cart model
     * @return CartDTO with computed fields
     */
    public static CartDTO toCartDTO(Cart cart) {
        if (cart == null) {
            return null;
        }

        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setStoreId(cart.getStoreId());
        cartDTO.setUserId(cart.getUserId());
        cartDTO.setCreatedAt(cart.getCreatedAt());
        cartDTO.setUpdatedAt(cart.getUpdatedAt());

        // Convert items
        List<CartItemDTO> itemDTOs = new ArrayList<>();
        if (cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                CartItemDTO itemDTO = toCartItemDTO(item);
                if (itemDTO != null) {
                    itemDTOs.add(itemDTO);
                }
            }
        }
        cartDTO.setItems(itemDTOs);
        
        // Compute totals
        computeCartTotals(cartDTO);
        
        return cartDTO;
    }

    /**
     * Convert CartItem model to CartItemDTO with computed line total.
     * @param cartItem The cart item model
     * @return CartItemDTO with computed fields
     */
    public static CartItemDTO toCartItemDTO(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        CartItemDTO itemDTO = new CartItemDTO();
        itemDTO.setId(cartItem.getId());
        itemDTO.setCartId(cartItem.getCartId());
        itemDTO.setMenuItemId(cartItem.getMenuItemId());
        itemDTO.setQty(cartItem.getQty());
        itemDTO.setUnitPriceSnapshot(BigDecimal.valueOf(cartItem.getUnitPriceSnapshot()));
        itemDTO.setOptionsPriceSnapshot(BigDecimal.valueOf(cartItem.getOptionsPriceSnapshot()));
        itemDTO.setNote(cartItem.getNote());
        itemDTO.setItemHash(cartItem.getItemHash());
        itemDTO.setItemNameSnapshot(cartItem.getItemNameSnapshot());
        itemDTO.setCreatedAt(cartItem.getCreatedAt());
        itemDTO.setUpdatedAt(cartItem.getUpdatedAt());

        // Convert options
        List<CartItemOptionDTO> optionDTOs = new ArrayList<>();
        if (cartItem.getOptions() != null) {
            for (CartItemOption option : cartItem.getOptions()) {
                CartItemOptionDTO optionDTO = toCartItemOptionDTO(option);
                if (optionDTO != null) {
                    optionDTOs.add(optionDTO);
                }
            }
        }
        itemDTO.setOptions(optionDTOs);
        
        // Compute line total
        BigDecimal itemPrice = itemDTO.getUnitPriceSnapshot().add(itemDTO.getOptionsPriceSnapshot());
        BigDecimal lineTotal = itemPrice.multiply(BigDecimal.valueOf(itemDTO.getQty()));
        itemDTO.setLineTotal(lineTotal);
        
        return itemDTO;
    }

    /**
     * Convert CartItemOption model to CartItemOptionDTO.
     * @param cartItemOption The cart item option model
     * @return CartItemOptionDTO
     */
    public static CartItemOptionDTO toCartItemOptionDTO(CartItemOption cartItemOption) {
        if (cartItemOption == null) {
            return null;
        }

        CartItemOptionDTO optionDTO = new CartItemOptionDTO();
        optionDTO.setId(cartItemOption.getId());
        optionDTO.setCartItemId(cartItemOption.getCartItemId());
        optionDTO.setOptionValueId(cartItemOption.getOptionValueId());
        optionDTO.setOptionGroupNameSnapshot(cartItemOption.getOptionGroupNameSnapshot());
        optionDTO.setOptionValueNameSnapshot(cartItemOption.getOptionValueNameSnapshot());
        optionDTO.setPriceDeltaSnapshot(BigDecimal.valueOf(cartItemOption.getPriceDeltaSnapshot()));
        
        return optionDTO;
    }

    // ===== DTO to Model Conversion =====

    /**
     * Convert CartDTO to Cart model.
     * @param cartDTO The cart DTO
     * @return Cart model
     */
    public static Cart toCartModel(CartDTO cartDTO) {
        if (cartDTO == null) {
            return null;
        }

        Cart cart = new Cart();
        cart.setId(cartDTO.getId());
        cart.setStoreId(cartDTO.getStoreId());
        cart.setUserId(cartDTO.getUserId());
        cart.setCreatedAt(cartDTO.getCreatedAt());
        cart.setUpdatedAt(cartDTO.getUpdatedAt());

        // Convert items
        List<CartItem> items = new ArrayList<>();
        if (cartDTO.getItems() != null) {
            for (CartItemDTO itemDTO : cartDTO.getItems()) {
                CartItem item = toCartItemModel(itemDTO);
                if (item != null) {
                    items.add(item);
                }
            }
        }
        cart.setItems(items);
        
        return cart;
    }

    /**
     * Convert CartItemDTO to CartItem model.
     * @param itemDTO The cart item DTO
     * @return CartItem model
     */
    public static CartItem toCartItemModel(CartItemDTO itemDTO) {
        if (itemDTO == null) {
            return null;
        }

        CartItem cartItem = new CartItem();
        cartItem.setId(itemDTO.getId());
        cartItem.setCartId(itemDTO.getCartId());
        cartItem.setMenuItemId(itemDTO.getMenuItemId());
        cartItem.setQty(itemDTO.getQty());
        cartItem.setUnitPriceSnapshot(itemDTO.getUnitPriceSnapshot().intValue());
        cartItem.setOptionsPriceSnapshot(itemDTO.getOptionsPriceSnapshot().intValue());
        cartItem.setNote(itemDTO.getNote());
        cartItem.setItemHash(itemDTO.getItemHash());
        cartItem.setItemNameSnapshot(itemDTO.getItemNameSnapshot());
        cartItem.setCreatedAt(itemDTO.getCreatedAt());
        cartItem.setUpdatedAt(itemDTO.getUpdatedAt());

        // Convert options
        List<CartItemOption> options = new ArrayList<>();
        if (itemDTO.getOptions() != null) {
            for (CartItemOptionDTO optionDTO : itemDTO.getOptions()) {
                CartItemOption option = toCartItemOptionModel(optionDTO);
                if (option != null) {
                    options.add(option);
                }
            }
        }
        cartItem.setOptions(options);
        
        return cartItem;
    }

    /**
     * Convert CartItemOptionDTO to CartItemOption model.
     * @param optionDTO The cart item option DTO
     * @return CartItemOption model
     */
    public static CartItemOption toCartItemOptionModel(CartItemOptionDTO optionDTO) {
        if (optionDTO == null) {
            return null;
        }

        CartItemOption option = new CartItemOption();
        option.setId(optionDTO.getId());
        option.setCartItemId(optionDTO.getCartItemId());
        option.setOptionValueId(optionDTO.getOptionValueId());
        option.setOptionGroupNameSnapshot(optionDTO.getOptionGroupNameSnapshot());
        option.setOptionValueNameSnapshot(optionDTO.getOptionValueNameSnapshot());
        option.setPriceDeltaSnapshot(optionDTO.getPriceDeltaSnapshot().intValue());
        
        return option;
    }

    // ===== Helper Methods =====

    /**
     * Compute cart totals (quantity, subtotal, total).
     * @param cartDTO The cart DTO to compute totals for
     */
    private static void computeCartTotals(CartDTO cartDTO) {
        if (cartDTO.getItems() == null || cartDTO.getItems().isEmpty()) {
            cartDTO.setTotalQty(0);
            cartDTO.setSubtotal(BigDecimal.ZERO);
            cartDTO.setTotal(BigDecimal.ZERO);
            return;
        }

        // Calculate total quantity using basic loop
        int totalQty = 0;
        for (CartItemDTO item : cartDTO.getItems()) {
            totalQty += item.getQty();
        }
        
        // Calculate subtotal using basic loop
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItemDTO item : cartDTO.getItems()) {
            if (item.getLineTotal() != null) {
                subtotal = subtotal.add(item.getLineTotal());
            }
        }

        cartDTO.setTotalQty(totalQty);
        cartDTO.setSubtotal(subtotal);
        cartDTO.setTotal(subtotal); // For now, total = subtotal (no payment processing)
    }
}