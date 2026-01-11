package com.laptrinhweb.zerostarcafe.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h2>Description:</h2>
 * <p>
 * Defines the application-wide roles (e.g., ADMIN, MANAGER, STAFF).
 * This enum is the single source of truth for all system roles.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 16/12/2025
 * @since 1.0.0
 */

@AllArgsConstructor
@Getter
public enum UserRole {
    SUPER_ADMIN("SUPER_ADMIN", "System Administrator"),
    STORE_MANAGER("STORE_MANAGER", "Store Manager"),
    STAFF("STAFF", "Store staff");

    private final String code;
    private final String description;
}