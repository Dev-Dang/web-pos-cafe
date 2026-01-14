package com.laptrinhweb.zerostarcafe.domain.auth.reset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Represents a password reset token stored in the database.
 * Only a hashed token is persisted; the raw token is sent by email.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PasswordResetToken {
    private Long id;
    private Long userId;
    private String tokenHash;
    private LocalDateTime expiresAt;
    private LocalDateTime usedAt;
    private LocalDateTime createdAt;
    private String requestIp;
    private String userAgent;
}
