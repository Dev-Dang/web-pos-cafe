package com.laptrinhweb.zerostarcafe.domain.cart.utils;

import com.laptrinhweb.zerostarcafe.core.security.TokenUtils;
import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.product.model.Product;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductOption;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductOptionValue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CartUtils {

    public static List<ProductOptionValue> extractOptionValues(Product product, List<Long> optionValueIds) {
        if (product.getOptions() == null || optionValueIds == null || optionValueIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<ProductOptionValue> result = new ArrayList<>();

        for (ProductOption group : product.getOptions()) {
            if (group.getValues() == null) {
                continue;
            }

            for (ProductOptionValue value : group.getValues()) {
                if (optionValueIds.contains(value.getId())) {
                    result.add(value);
                }
            }
        }

        return result;
    }

    public static int calculateOptionsPriceDelta(List<ProductOptionValue> options) {
        if (options == null || options.isEmpty()) {
            return 0;
        }

        int total = 0;
        for (ProductOptionValue value : options) {
            total += value.getPriceDelta();
        }
        return total;
    }

    public static int calculateOptionsPriceDelta(Product product, List<Long> optionValueIds) {
        List<ProductOptionValue> options = extractOptionValues(product, optionValueIds);
        return calculateOptionsPriceDelta(options);
    }

    public static String findOptionGroupName(Product product, long groupId) {
        if (product.getOptions() == null) {
            return "";
        }

        for (ProductOption group : product.getOptions()) {
            if (group.getId() == groupId) {
                return group.getNameJson();
            }
        }
        return "";
    }

    public static String generateItemHash(long menuItemId, List<Long> optionValueIds, String note) {
        StringBuilder input = new StringBuilder();
        input.append(menuItemId);

        if (optionValueIds != null && !optionValueIds.isEmpty()) {
            List<Long> sorted = new ArrayList<>(optionValueIds);
            Collections.sort(sorted);
            input.append("|");

            for (int i = 0; i < sorted.size(); i++) {
                if (i > 0) input.append(",");
                input.append(sorted.get(i));
            }
        }

        if (note != null && !note.trim().isEmpty()) {
            input.append("|").append(note.trim());
        }

        return TokenUtils.hashToken(input.toString());
    }

    public static CartItem findExistingItemByHash(Cart cart, String hash) {
        if (cart.getItems() == null || hash == null) {
            return null;
        }

        for (CartItem item : cart.getItems()) {
            if (hash.equals(item.getItemHash())) {
                return item;
            }
        }
        return null;
    }
}
