package com.laptrinhweb.zerostarcafe.domain.auth.record;

import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.security.TokenUtils;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RequestInfoDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Manages authentication record storage in the database.
 * Supports creating, updating, validating, and revoking auth records.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * AuthRecordService service = new AuthRecordService(conn);
 * service.create(context, reqInfo);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.2
 * @lastModified 13/12/2025
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthRecordService {

    private static final AuthRecordService INSTANCE = new AuthRecordService();
    private final AuthRecordDAO recordDAO = new AuthRecordDAOImpl();

    /**
     * Get the singleton instance of AuthRecordService.
     *
     * @return the singleton instance
     */
    public static AuthRecordService getInstance() {
        return INSTANCE;
    }

    /**
     * Revoke all active auth records for the given userID.
     * Saved the new record in the database.
     *
     * @param userId    the user ID owning the record
     * @param newRecord the auth record to persist
     * @return the persisted record
     * @throws AppException if SQL error occurs
     */
    public AuthRecord save(
            @NonNull Long userId,
            @NonNull AuthRecord newRecord
    ) {
        try {
            revokeAllByUserId(userId);
            return recordDAO.save(newRecord);
        } catch (SQLException e) {
            throw new AppException("Fail to insert new Auth Record", e);
        }
    }

    /**
     * Update an auth record using the previous token value.
     *
     * @param reqInfo  the request information
     * @param newToken the new raw token value
     * @param oldToken the previous raw token value to find the record
     * @throws AppException if SQL error occurs
     */
    public void updateByToken(
            @NonNull RequestInfoDTO reqInfo,
            @NonNull String newToken,
            @NonNull String oldToken
    ) {
        String oldHash = TokenUtils.hashToken(oldToken);

        try {
            // Find the record to update
            Optional<AuthRecord> recordOpt = recordDAO.findValidByAuthHash(oldHash);
            if (recordOpt.isEmpty())
                return;

            AuthRecord record = recordOpt.get();

            // Generate token hash from the new token in context
            String newHash = TokenUtils.hashToken(newToken);

            // Update record with new metadata
            record.setAuthHash(newHash);
            record.setIpLast(reqInfo.getIpAddress());
            record.setUserAgent(reqInfo.getUserAgent());
            record.setLastRotatedAt(LocalDateTime.now());

            // Save the updated record
            recordDAO.save(record);
        } catch (SQLException e) {
            throw new AppException("Fail to update Auth Record by token=" + oldToken, e);
        }
    }

    /**
     * Revoke all active auth records belonging to the given user.
     *
     * @param userId the user ID
     * @throws AppException if SQL error occurs
     */
    public void revokeAllByUserId(Long userId) {
        try {
            recordDAO.revokeAllByUserId(userId);
        } catch (SQLException e) {
            throw new AppException("Fail to revoke Auth Record by UserId=" + userId, e);
        }
    }

    /**
     * Revoke a record using a raw token from the client.
     *
     * @param rawToken the token value from cookies
     * @throws AppException if SQL error occurs
     */
    public void revokeByRawToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank())
            return;

        String hash = TokenUtils.hashToken(rawToken);

        try {
            recordDAO.revokeByAuthHash(hash);
        } catch (SQLException e) {
            throw new AppException("Fail to revoke Auth Record by token=" + rawToken, e);
        }
    }

    /**
     * Find the valid (not expired and not revoked) record by raw token.
     *
     * @param rawToken the token value from cookies
     * @return an optional containing the valid record, or empty if none found
     * @throws AppException if SQL error occurs
     */
    public Optional<AuthRecord> findValidByRawToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank())
            return Optional.empty();

        String hash = TokenUtils.hashToken(rawToken);

        try {
            return recordDAO.findValidByAuthHash(hash);
        } catch (SQLException e) {
            throw new AppException("Fail to find valid Auth Record by token=" + rawToken, e);
        }
    }
}