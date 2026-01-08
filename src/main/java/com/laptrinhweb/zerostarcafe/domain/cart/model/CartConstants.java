package com.laptrinhweb.zerostarcafe.domain.cart.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <h2>Description:</h2>
 * <p>
 *
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ... code here
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
public class CartConstants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Keys {

        public static final String ITEM = "item:";
        public static final String OPTIONS = ":options:";
        public static final String NOTE = ":note:";
    }

}
