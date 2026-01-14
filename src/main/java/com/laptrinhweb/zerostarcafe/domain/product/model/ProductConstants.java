package com.laptrinhweb.zerostarcafe.domain.product.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2>Description:</h2>
 * <p>
 * Constants for the product domain including option types,
 * availability statuses, and default values.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 06/01/2026
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProductConstants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Attributes {
        public static final String PRODUCT_CARDS = "productCards";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class OptionType {
        public static final String SIZE = "size";
        public static final String TOPPING = "topping";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class AvailabilityStatus {
        public static final String AVAILABLE = "available";
        public static final String SOLD_OUT = "sold_out";
        public static final String DISCONTINUED = "discontinued";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Defaults {
        public static final String DEFAULT_IMAGE_URL = "assets/client/img/product/default.png";
        public static final String DEFAULT_UNIT = "ly";
    }
}