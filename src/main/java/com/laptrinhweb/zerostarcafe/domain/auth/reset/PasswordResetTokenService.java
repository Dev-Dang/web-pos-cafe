package com.laptrinhweb.zerostarcafe.domain.auth.reset;

import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.security.TokenUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Service for storing and validating password reset tokens.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PasswordResetTokenService {

    private static final PasswordResetTokenService INSTANCE = new PasswordResetTokenService();
    private final PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAOImpl();

    public static PasswordResetTokenService getInstance() {
        return INSTANCE;
    }

    public PasswordResetToken save(
            @NonNull Long userId,
            @NonNull PasswordResetToken token
    ) {
        try {
            tokenDAO.markUsedByUserId(userId);
            return tokenDAO.save(token);
        } catch (SQLException e) {
            throw new AppException("Fail to insert reset token for userId=" + userId, e);
        }
    }

    public Optional<PasswordResetToken> findValidByRawToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank())
            return Optional.empty();

        String hash = TokenUtils.hashToken(rawToken);

        try {
            return tokenDAO.findValidByHash(hash);
        } catch (SQLException e) {
            throw new AppException("Fail to find reset token by raw token", e);
        }
    }

    public void markUsedByRawToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank())
            return;

        String hash = TokenUtils.hashToken(rawToken);

        try {
            tokenDAO.markUsedByHash(hash);
        } catch (SQLException e) {
            throw new AppException("Fail to mark reset token used", e);
        }
    }
}
