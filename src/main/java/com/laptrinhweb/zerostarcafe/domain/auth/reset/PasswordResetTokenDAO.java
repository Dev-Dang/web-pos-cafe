package com.laptrinhweb.zerostarcafe.domain.auth.reset;

import java.sql.SQLException;
import java.util.Optional;

/**
 * DAO for password reset token persistence.
 */
public interface PasswordResetTokenDAO {

    PasswordResetToken save(PasswordResetToken token) throws SQLException;

    Optional<PasswordResetToken> findValidByHash(String tokenHash) throws SQLException;

    void markUsedByHash(String tokenHash) throws SQLException;

    void markUsedByUserId(Long userId) throws SQLException;
}
