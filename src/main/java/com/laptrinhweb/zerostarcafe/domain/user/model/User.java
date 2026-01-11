package com.laptrinhweb.zerostarcafe.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents an application user, including credentials, status,
 * and optional OAuth identity.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.2
 * @lastModified 16/12/2025
 * @since 1.0.0
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private Long id;
    private String email;
    private String username;
    private String passwordHash;
    private String oauthProvider;
    private String oauthId;
    private UserStatus status;
    private boolean isSuperAdmin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String email, String username, String passwordHash) {
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.status = UserStatus.ACTIVE;
        this.isSuperAdmin = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
