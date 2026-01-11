package com.laptrinhweb.zerostarcafe.domain.user.model;

import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import com.laptrinhweb.zerostarcafe.domain.user_role.UserStoreRole;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Maps between user-related domain objects. This mapper creates
 * {@link User} instances from registration input and builds
 * {@link AuthUser} views for authentication and session storage.
 * </p>
 *
 * <h2>Usage:</h2>
 * <pre>{@code
 * // Convert registration DTO to User entity
 * User user = UserMapper.fromRegister(dto);
 *
 * // Convert User + store roles to AuthUser for session
 * AuthUser auth = UserMapper.toAuthenticatedUser(user, roles);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.2
 * @lastModified 16/12/2025
 * @since 1.0.0
 */
public class UserMapper {

    private UserMapper() {
    }

    public static AuthUser toAuthUser(User user, List<UserStoreRole> storeRoles) {
        List<String> roleCodes = new ArrayList<>();

        // Iterate through the list of user roles at the store
        for (UserStoreRole usr : storeRoles) {
            String code = usr.getRoleCode();

            // Avoid duplicating user role codes
            if (!roleCodes.contains(code)) {
                roleCodes.add(code);
            }
        }

        return new AuthUser(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isSuperAdmin(),
                roleCodes
        );
    }
}
