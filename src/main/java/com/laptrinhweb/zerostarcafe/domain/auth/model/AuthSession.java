package com.laptrinhweb.zerostarcafe.domain.auth.model;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Stores session state for an authenticated user.
 * It tracks the expiry time and the last rotation time.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 14/12/2025
 * @since 1.0.0
 */

@Getter
public final class AuthSession implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDateTime expiredAt;
    private LocalDateTime lastRotatedAt;

    public AuthSession(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
        this.lastRotatedAt = LocalDateTime.now();
    }

    /**
     * Checks if this session is expired.
     *
     * @return true if the session has passed its expiry time
     */
    public boolean isExpired() {
        return expiredAt != null
                && LocalDateTime.now().isAfter(expiredAt);
    }

    /**
     * Checks if the session should rotate its token.
     * Rotation happens only when the session is not expired
     * and enough time has passed since the last rotation.
     *
     * @return true if rotation is required
     */
    public boolean shouldRotate() {
        if (isExpired() || lastRotatedAt == null)
            return false;

        LocalDateTime now = LocalDateTime.now();
        long minutes = Duration.between(lastRotatedAt, now).toMinutes();
        return minutes >= SecurityKeys.REFRESH_MINUTES;
    }

    /**
     * Marks this session as rotated at the current time.
     */
    public void updateLastRotatedTime() {
        this.lastRotatedAt = LocalDateTime.now();
    }
}
