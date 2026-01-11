package com.laptrinhweb.zerostarcafe.domain.auth.model;

import com.laptrinhweb.zerostarcafe.domain.auth.record.AuthRecord;

/**
 * <h2>Description:</h2>
 * <p>
 * AuthStatus values for a persistent authentication session or token.
 * Used together with {@link AuthRecord}.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
public enum TokenStatus {
    ACTIVE,
    REVOKED,
    EXPIRED
}
