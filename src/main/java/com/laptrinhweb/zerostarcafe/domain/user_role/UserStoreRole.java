package com.laptrinhweb.zerostarcafe.domain.user_role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents a user's role assignment in a specific store.
 * Each record links a user to a store with a given {@code role_code}.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 16/12/2025
 * @since 1.0.0
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserStoreRole {
    private Long id;
    private Long userId;
    private Long storeId;
    private String roleCode;
    private LocalDateTime createdAt;
}
