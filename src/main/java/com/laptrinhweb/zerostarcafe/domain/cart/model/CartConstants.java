package com.laptrinhweb.zerostarcafe.domain.cart.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2>Description:</h2>
 * <p>
 * Constants for the cart domain including validation limits and default values.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CartConstants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Validation {
        public static final int MIN_QUANTITY = 1;
        public static final int MAX_QUANTITY = 99;
        public static final int MAX_ITEMS_PER_CART = 50;
    }
}
