package com.laptrinhweb.zerostarcafe.domain.auth.record;

import com.laptrinhweb.zerostarcafe.domain.auth.model.TokenStatus;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents a long-lived authentication token
 * that can be used to restore a login session.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 14/12/2025
 * @since 1.0.0
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthRecord {
    private Long id;
    private Long userId;
    private String authHash;
    private String deviceId;
    private TokenStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime lastRotatedAt;

    private String ipLast;
    private String userAgent;

    private LocalDateTime revokedAt;
    private String revokedReason;
}
