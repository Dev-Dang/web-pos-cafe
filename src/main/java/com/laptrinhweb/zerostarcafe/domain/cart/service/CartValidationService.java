package com.laptrinhweb.zerostarcafe.domain.cart.service;

import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;
import com.laptrinhweb.zerostarcafe.domain.product.model.AvailabilityStatus;
import com.laptrinhweb.zerostarcafe.domain.product.model.CatalogItem;
import com.laptrinhweb.zerostarcafe.domain.product.model.OptionGroup;
import com.laptrinhweb.zerostarcafe.domain.product.model.OptionValue;
import com.laptrinhweb.zerostarcafe.domain.product.service.ProductService;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Validates cart items against the product catalog and builds
 * CartItem objects with correct server-side prices.
 * Never trusts prices from client - always fetches from database.
 * </p>
 *
 * <h2>Security:</h2>
 * <ul>
 *     <li>All prices are fetched from DB, never from client</li>
 *     <li>Option values are validated to belong to the product</li>
 *     <li>Product availability is checked</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * CartValidationService validator = new CartValidationService();
 * CartItem validated = validator.validateAndBuildItem(menuItemId, optionIds, qty, note, storeId);
 * if (validated == null) {
 *     // Product not available or options invalid
 * }
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
public final class CartValidationService {

    private final ProductService productService;

    public CartValidationService() {
        this.productService = new ProductService();
    }

    /**
     * Validates a cart item request and builds a CartItem with server prices.
     *
     * @param menuItemId     the product ID
     * @param optionValueIds the selected option value IDs
     * @param qty            the quantity
     * @param note           customer note (can be null)
     * @param storeId        the store ID
     * @return a validated CartItem with correct prices, or null if validation fails
     */
    public CartItem validateAndBuildItem(
            long menuItemId,
            List<Long> optionValueIds,
            int qty,
            String note,
            long storeId
    ) {
        // 1. Validate product exists and is available
        CatalogItem product = productService.getCatalogItemByIdAndStoreId(menuItemId, storeId);
        if (product == null) {
            return null;
        }

        // Check if product is available (not sold out)
        if (product.getAvailability() != null) {
            AvailabilityStatus status = product.getAvailability().getStatus();
            if (status == AvailabilityStatus.SOLD_OUT) {
                return null;
            }
        }

        // 2. Validate and build options
        List<CartItemOption> validatedOptions = new ArrayList<>();
        int optionsPriceTotal = 0;

        if (optionValueIds != null && !optionValueIds.isEmpty()) {
            for (Long optionValueId : optionValueIds) {
                CartItemOption validatedOption = validateOption(optionValueId);
                if (validatedOption != null) {
                    validatedOptions.add(validatedOption);
                    optionsPriceTotal += validatedOption.getPriceDeltaSnapshot();
                }
                // Skip invalid options silently (they might have been removed)
            }
        }

        // 3. Build CartItem with server prices
        CartItem item = new CartItem();
        item.setMenuItemId(menuItemId);
        item.setQty(Math.max(1, qty));
        item.setNote(note);
        item.setUnitPriceSnapshot(product.getResolvedPrice());
        item.setOptionsPriceSnapshot(optionsPriceTotal);
        item.setItemNameSnapshot(product.getName());
        item.setOptions(validatedOptions);

        return item;
    }

    /**
     * Validates a single option value and returns a CartItemOption with snapshot data.
     *
     * @param optionValueId the option value ID
     * @return a CartItemOption with snapshot data, or null if invalid
     */
    private CartItemOption validateOption(long optionValueId) {
        OptionValue optionValue = productService.getOptionValueById(optionValueId);
        if (optionValue == null || !optionValue.isActive()) {
            return null;
        }

        // Check store-specific availability
        if (optionValue.getStoreAvailability() != null) {
            AvailabilityStatus status = optionValue.getStoreAvailability().getStatus();
            if (status == AvailabilityStatus.SOLD_OUT) {
                return null;
            }
        }

        // Get option group name for snapshot
        OptionGroup group = productService.getOptionGroupByOptionValueId(optionValueId);
        String groupName = (group != null) ? group.getName() : "";

        CartItemOption option = new CartItemOption();
        option.setOptionValueId(optionValueId);
        option.setOptionGroupNameSnapshot(groupName);
        option.setOptionValueNameSnapshot(optionValue.getName());
        option.setPriceDeltaSnapshot(optionValue.getPriceDelta());

        return option;
    }

    /**
     * Re-validates an existing cart for checkout.
     * Checks that all products and options are still available and updates prices.
     *
     * @param items   the cart items to validate
     * @param storeId the store ID
     * @return list of validated items with current prices (invalid items are removed)
     */
    public List<CartItem> revalidateForCheckout(List<CartItem> items, long storeId) {
        List<CartItem> validItems = new ArrayList<>();

        for (CartItem item : items) {
            // Rebuild option value IDs from options
            List<Long> optionValueIds = new ArrayList<>();
            for (CartItemOption opt : item.getOptions()) {
                optionValueIds.add(opt.getOptionValueId());
            }

            // Re-validate with current prices
            CartItem revalidated = validateAndBuildItem(
                    item.getMenuItemId(),
                    optionValueIds,
                    item.getQty(),
                    item.getNote(),
                    storeId
            );

            if (revalidated != null) {
                // Preserve cart-specific fields
                revalidated.setId(item.getId());
                revalidated.setCartId(item.getCartId());
                revalidated.setItemHash(item.getItemHash());
                validItems.add(revalidated);
            }
        }

        return validItems;
    }
}
