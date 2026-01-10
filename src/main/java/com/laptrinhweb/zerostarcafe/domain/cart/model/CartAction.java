package com.laptrinhweb.zerostarcafe.domain.cart.model;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents the possible actions that can be performed on a cart.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
public enum CartAction {
    ADD,
    REMOVE,
    UPDATE,
    INCREASE,
    DECREASE,
    EDIT,
    UPDATE_ITEM;

    public static CartAction fromPath(String path) {
        if (path == null || path.length() <= 1)
            return null;
        try {
            String action = path.substring(1).replace("-", "_").toUpperCase();
            return valueOf(action);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
