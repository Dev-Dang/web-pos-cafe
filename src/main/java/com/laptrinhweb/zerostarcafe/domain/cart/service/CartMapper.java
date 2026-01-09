package com.laptrinhweb.zerostarcafe.domain.cart.service;

import com.laptrinhweb.zerostarcafe.core.i18n.I18nUtils;
import com.laptrinhweb.zerostarcafe.domain.cart.dto.AddToCartDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartItemDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartItemOptionDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;
import com.laptrinhweb.zerostarcafe.domain.cart.utils.CartUtils;
import com.laptrinhweb.zerostarcafe.domain.product.model.Product;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductOptionValue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.laptrinhweb.zerostarcafe.domain.cart.utils.CartUtils.*;

/**
 * <h2>Description:</h2>
 * <p>
 * Mapper for cart domain conversions.
 * Handles Product → CartItem and Cart → CartDTO transformations.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CartMapper {

    /**
     * Convert Product and AddToCartDTO to CartItem with snapshots.
     */
    public static CartItem toCartItem(Product product, AddToCartDTO dto) {
        CartItem item = new CartItem();

        item.setMenuItemId(product.getId());
        item.setQty(dto.getQty());
        item.setNote(dto.getNote());
        item.setItemNameSnapshot(product.getNameJson());
        item.setImageUrlSnapshot(product.getImageUrl());
        item.setUnitPriceSnapshot(product.getCurrentPrice() != null ?
                product.getCurrentPrice() : product.getBasePrice());

        List<ProductOptionValue> selectedOptions = extractOptionValues(product, dto.getOptionValueIds());
        item.setOptionsPriceSnapshot(calculateOptionsPriceDelta(selectedOptions));
        item.setItemHash(generateItemHash(product.getId(), dto.getOptionValueIds(), dto.getNote()));

        return item;
    }

    /**
     * Convert selected option value IDs to CartItemOption list.
     */
    public static List<CartItemOption> toCartItemOptions(Product product, List<Long> optionValueIds) {
        if (optionValueIds == null || optionValueIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<CartItemOption> cartItemOptions = new ArrayList<>();
        List<ProductOptionValue> selectedOptions = extractOptionValues(product, optionValueIds);

        for (ProductOptionValue value : selectedOptions) {
            CartItemOption option = new CartItemOption();
            option.setOptionValueId(value.getId());
            option.setOptionGroupNameSnapshot(CartUtils.findOptionGroupName(product, value.getOptionGroupId()));
            option.setOptionValueNameSnapshot(value.getNameJson());
            option.setPriceDeltaSnapshot(value.getPriceDelta());

            cartItemOptions.add(option);
        }

        return cartItemOptions;
    }
    
    /**
     * Convert Cart to CartDTO with localized fields and calculated totals.
     */
    public static CartDTO toCartDTO(Cart cart) {
        if (cart == null) {
            return null;
        }

        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setStoreId(cart.getStoreId());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());

        // Convert items
        List<CartItemDTO> itemDTOs = new ArrayList<>();
        int totalQty = 0;
        int subtotal = 0;

        for (CartItem item : cart.getItems()) {
            CartItemDTO itemDTO = toCartItemDTO(item);
            itemDTOs.add(itemDTO);

            totalQty += item.getQty();
            subtotal += itemDTO.getItemTotal();
        }

        dto.setItems(itemDTOs);
        dto.setTotalQty(totalQty);
        dto.setSubtotal(subtotal);
        dto.setTotal(subtotal);

        return dto;
    }

    /**
     * Convert CartItem to CartItemDTO with localized name and options.
     */
    private static CartItemDTO toCartItemDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.getId());
        dto.setCartId(item.getCartId());
        dto.setMenuItemId(item.getMenuItemId());
        dto.setItemName(I18nUtils.extract(item.getItemNameSnapshot()));
        dto.setImageUrl(item.getImageUrlSnapshot());
        dto.setQty(item.getQty());
        dto.setUnitPrice(item.getUnitPriceSnapshot());
        dto.setOptionsPrice(item.getOptionsPriceSnapshot());
        dto.setItemTotal((item.getUnitPriceSnapshot() + item.getOptionsPriceSnapshot()) * item.getQty());
        dto.setNote(item.getNote());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());

        // Convert options
        List<CartItemOptionDTO> optionDTOs = new ArrayList<>();
        for (CartItemOption option : item.getOptions()) {
            optionDTOs.add(toCartItemOptionDTO(option));
        }
        dto.setOptions(optionDTOs);

        return dto;
    }

    /**
     * Convert CartItemOption to CartItemOptionDTO with localized names.
     */
    private static CartItemOptionDTO toCartItemOptionDTO(CartItemOption option) {
        CartItemOptionDTO dto = new CartItemOptionDTO();
        dto.setId(option.getId());
        dto.setOptionGroupName(I18nUtils.extract(option.getOptionGroupNameSnapshot()));
        dto.setOptionValueName(I18nUtils.extract(option.getOptionValueNameSnapshot()));
        dto.setPriceDelta(option.getPriceDeltaSnapshot());
        return dto;
    }
}