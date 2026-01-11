package com.laptrinhweb.zerostarcafe.domain.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents a single authentication token.
 * Each token has a name, value, and expiry time.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 14/12/2025
 * @since 1.0.0
 */

@AllArgsConstructor
@Getter
public final class AuthToken implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String value;
    private final LocalDateTime expiredAt;

    /**
     * Checks if this token is expired.
     *
     * @return true if the expiry time is in the past
     */
    public boolean isExpired() {
        return expiredAt != null && LocalDateTime.now().isAfter(expiredAt);
    }

}
