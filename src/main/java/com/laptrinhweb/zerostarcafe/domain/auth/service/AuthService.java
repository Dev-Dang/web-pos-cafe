package com.laptrinhweb.zerostarcafe.domain.auth.service;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.security.PasswordUtil;
import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.security.TokenUtil;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.LoginDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RegisterDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RequestInfoDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.model.*;
import com.laptrinhweb.zerostarcafe.domain.auth.record.AuthRecord;
import com.laptrinhweb.zerostarcafe.domain.auth.record.AuthRecordService;
import com.laptrinhweb.zerostarcafe.domain.user.model.User;
import com.laptrinhweb.zerostarcafe.domain.user.model.UserMapper;
import com.laptrinhweb.zerostarcafe.domain.user.model.UserRole;
import com.laptrinhweb.zerostarcafe.domain.user.service.UserService;
import com.laptrinhweb.zerostarcafe.domain.user_role.UserStoreRole;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides core authentication business logic including registration,
 * login, credential verification, session/context creation, token validation,
 * rotation handling, and restoration from persisted auth records.
 * <br/><br/>
 * All methods return {@link AuthResult} or {@link AuthContext} to ensure
 * a unified and predictable contract for the web layer.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * AuthService auth = new AuthService();
 * LoginDTO dto = new LoginDTO(username, password);
 *
 * AuthResult<AuthStatus, AuthContext> result =
 *         auth.authenticate(dto, reqInfo);
 *
 * if (result.success()) {
 *     // Use AuthContext ...
 * }
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.2.2
 * @lastModified 14/12/2025
 * @since 1.0.0
 */
public final class AuthService {

    /**
     * Registers a new user.
     *
     * @param dto registration input
     * @return AuthResult indicating success or failure
     */
    public AuthResult<AuthStatus, Void> register(@NonNull RegisterDTO dto) {
        try (Connection conn = DBConnection.getConnection()) {
            UserService userService = new UserService(conn);

            String email = normalize(dto.getEmail());
            String username = normalize(dto.getUsername());

            // Check duplicate
            if (userService.existsByEmail(email))
                return AuthResult.fail(AuthStatus.EMAIL_EXISTS);

            if (userService.existsByUsername(username))
                return AuthResult.fail(AuthStatus.USERNAME_EXISTS);

            // Create a new user
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(username);

            // Hash password securely (Argon2)
            String hashedPassword = PasswordUtil.hash(dto.getPassword());
            newUser.setPasswordHash(hashedPassword);

            // Persist user to the database
            userService.save(newUser);

            LoggerUtil.info(AuthService.class,
                    "New User Registered: " + newUser.getUsername());
            return AuthResult.ok(AuthStatus.REGISTER_SUCCESS);

        } catch (AppException | SQLException e) {
            LoggerUtil.error(AuthService.class, e.getMessage(), e);
            return AuthResult.fail(AuthStatus.REGISTER_FAILED);
        }
    }

    /**
     * Authenticates a user and creates a new AuthContext.
     *
     * @param form    login input
     * @param reqInfo request metadata (ip, agent, device-id)
     * @return AuthResult with AuthContext or failure status
     */
    public AuthResult<AuthStatus, AuthContext> authenticate(
            @NonNull LoginDTO form,
            @NonNull RequestInfoDTO reqInfo
    ) {
        try (Connection conn = DBConnection.getConnection()) {
            AuthRecordService recordService = new AuthRecordService(conn);

            // Verify credential (username and password)
            AuthUser authUser = verifyCredential(conn, form);
            if (authUser == null)
                return AuthResult.fail(AuthStatus.INVALID_CREDENTIALS);

            // Create auth session info
            LocalDateTime expiredAt = resolveExpiredAt(authUser);
            AuthSession sessionInfo = new AuthSession(expiredAt);

            // Create auth tokens (auth token + device id)
            List<AuthToken> tokens = new ArrayList<>();

            AuthToken authToken = new AuthToken(
                    SecurityKeys.TOKEN_AUTH,
                    TokenUtil.generateToken(),
                    sessionInfo.getExpiredAt()
            );
            tokens.add(authToken);

            String deviceId = reqInfo.getCookieValue(SecurityKeys.TOKEN_DEVICE_ID);
            AuthToken deviceToken = new AuthToken(
                    SecurityKeys.TOKEN_DEVICE_ID,
                    deviceId != null ? deviceId : TokenUtil.generateToken(),
                    sessionInfo.getExpiredAt()
            );
            tokens.add(deviceToken);

            // Create auth context
            AuthContext context = new AuthContext(authUser, sessionInfo, tokens);

            // Save new auth record
            String authHash = TokenUtil.hashToken(authToken.getValue());
            String deviceIdHash = TokenUtil.hashToken(deviceToken.getValue());

            AuthRecord record = new AuthRecord();
            record.setUserId(authUser.getId());
            record.setAuthHash(authHash);
            record.setDeviceId(deviceIdHash);
            record.setStatus(TokenStatus.ACTIVE);
            record.setCreatedAt(LocalDateTime.now());
            record.setExpiredAt(expiredAt);
            record.setLastRotatedAt(LocalDateTime.now());
            record.setIpLast(reqInfo.getIpAddress());
            record.setUserAgent(reqInfo.getUserAgent());

            record = recordService.save(authUser.getId(), record);

            LoggerUtil.info(AuthService.class,
                    "New Login Record: \n" + record.toString());
            return AuthResult.ok(AuthStatus.LOGIN_SUCCESS, context);

        } catch (AppException | SQLException e) {
            LoggerUtil.error(AuthService.class, e.getMessage(), e);
            return AuthResult.fail(AuthStatus.LOGIN_FAILED);
        }
    }

    /**
     * Re-validates an existing context and rotates tokens if required.
     *
     * @param context   current authentication context
     * @param reqInfo   request metadata
     * @param reqTokens tokens extracted from client cookies
     * @return true if the context is still valid after re-validation, false otherwise
     */
    public boolean reAuthenticate(
            @NonNull AuthContext context,
            @NonNull RequestInfoDTO reqInfo,
            @NonNull Map<String, String> reqTokens
    ) {
        List<AuthToken> tokens = context.getTokens();

        // Validate tokens against server tokens
        if (!isValidTokens(reqTokens, tokens))
            return false;

        // Check session rotation
        AuthSession session = context.getSessionInfo();
        if (!session.shouldRotate())
            return true;

        // Rotation if needed
        String oldToken = context.getTokenValue(SecurityKeys.TOKEN_AUTH);
        if (oldToken == null || oldToken.isBlank())
            return false;

        // Generate new token
        String newToken = TokenUtil.generateToken();
        AuthToken newAuthToken = new AuthToken(
                SecurityKeys.TOKEN_AUTH, newToken, session.getExpiredAt());

        // Update auth context
        session.updateLastRotatedTime();
        context.updateToken(newAuthToken);

        // Update auth record
        try (Connection conn = DBConnection.getConnection()) {
            AuthRecordService recordService = new AuthRecordService(conn);
            recordService.updateByToken(reqInfo, newToken, oldToken);
            return true;
        } catch (Exception e) {
            LoggerUtil.error(AuthService.class, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Restores authentication context from persisted AuthRecord.
     *
     * @param reqInfo   request metadata
     * @param reqTokens tokens from client cookies
     * @return restored AuthContext or null if not restorable
     */
    public AuthContext restore(
            @NonNull RequestInfoDTO reqInfo,
            @NonNull Map<String, String> reqTokens
    ) {
        String rawAuthToken = reqTokens.get(SecurityKeys.TOKEN_AUTH);
        String rawDeviceId = reqTokens.get(SecurityKeys.TOKEN_DEVICE_ID);

        if (rawAuthToken == null || rawDeviceId == null)
            return null;

        try (Connection conn = DBConnection.getConnection()) {
            AuthRecordService recordService = new AuthRecordService(conn);
            UserService userService = new UserService(conn);

            // Get the current auth record by token
            Optional<AuthRecord> recordOpt = recordService.findValidByRawToken(rawAuthToken);
            if (recordOpt.isEmpty())
                return null;

            AuthRecord record = recordOpt.get();

            // Check if the auth session is expired
            LocalDateTime expiredAt = record.getExpiredAt();
            if (expiredAt == null || LocalDateTime.now().isAfter(expiredAt))
                return null;

            // Check device ID match
            String deviceIdHash = TokenUtil.hashToken(rawDeviceId);
            if (!deviceIdHash.equals(record.getDeviceId()))
                return null;

            // Build auth context from record
            Long userId = record.getUserId();
            User user = userService.getActiveById(userId);
            if (user == null)
                return null;

            List<UserStoreRole> roles = userService.getRolesOf(user);
            AuthUser authUser = UserMapper.toAuthUser(user, roles);

            // Create auth session info
            AuthSession sessionInfo = new AuthSession(expiredAt);

            // Create auth tokens (auth token + device id)
            List<AuthToken> tokens = new ArrayList<>();

            AuthToken authToken = new AuthToken(
                    SecurityKeys.TOKEN_AUTH,
                    rawAuthToken,
                    expiredAt
            );
            tokens.add(authToken);

            AuthToken deviceToken = new AuthToken(
                    SecurityKeys.TOKEN_DEVICE_ID,
                    rawDeviceId,
                    expiredAt
            );
            tokens.add(deviceToken);

            // Create auth context
            AuthContext context = new AuthContext(authUser, sessionInfo, tokens);

            // Update record with new metadata and last rotated time
            record.setIpLast(reqInfo.getIpAddress());
            record.setUserAgent(reqInfo.getUserAgent());
            record.setLastRotatedAt(LocalDateTime.now());
            recordService.save(userId, record);

            return context;
        } catch (Exception e) {
            LoggerUtil.error(AuthService.class, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Revokes authentication state associated with the given token.
     *
     * @param token raw auth token
     */
    public void clearAuthState(String token) {
        try (Connection conn = DBConnection.getConnection()) {
            AuthRecordService recordService = new AuthRecordService(conn);

            if (token == null || token.isBlank())
                return;

            recordService.revokeByRawToken(token);
        } catch (Exception e) {
            LoggerUtil.error(AuthService.class, e.getMessage(), e);
        }
    }

    /**
     * Verifies user credentials and loads roles.
     *
     * @param dto login input
     * @return authenticated AuthUser or null if invalid
     */
    public AuthUser verifyCredential(Connection conn, LoginDTO dto) {
        UserService userService = new UserService(conn);

        String username = normalize(dto.getUsername());
        User user = userService.getActiveByUsername(username);
        if (user == null)
            return null;

        if (!PasswordUtil.verify(dto.getPassword(), user.getPasswordHash()))
            return null;

        List<UserStoreRole> roles = userService.getRolesOf(user);
        return UserMapper.toAuthUser(user, roles);
    }

    /**
     * Validates request tokens against server tokens.
     *
     * @param reqTokens  tokens from the client
     * @param authTokens tokens stored on server
     * @return true if all tokens match and are valid
     */
    private boolean isValidTokens(
            Map<String, String> reqTokens,
            List<AuthToken> authTokens
    ) {
        for (AuthToken serverToken : authTokens) {
            String name = serverToken.getName();
            String value = reqTokens.get(name);

            if (value == null)
                return false;

            if (!value.equals(serverToken.getValue()))
                return false;

            if (serverToken.isExpired())
                return false;
        }

        return true;
    }

    /**
     * Resolve the expiry time for the given user based on user role.
     *
     * @param user the authenticated user
     * @return the calculated expiry time
     */
    public LocalDateTime resolveExpiredAt(AuthUser user) {
        LocalDateTime now = LocalDateTime.now();

        if (user == null)
            return now.plusMinutes(SecurityKeys.DEFAULT_SESSION_TTL);

        if (user.hasRole(UserRole.SUPER_ADMIN))
            return now.plusMinutes(SecurityKeys.SUPER_ADMIN_SESSION_TTL);

        if (user.hasRole(UserRole.STORE_MANAGER))
            return now.plusMinutes(SecurityKeys.MANAGER_SESSION_TTL);

        if (user.hasRole(UserRole.STAFF))
            return LocalDate.now().atTime(23, 59);

        return now.plusMinutes(SecurityKeys.DEFAULT_SESSION_TTL);
    }

    private static String normalize(String s) {
        return s == null ? null : s.trim().toLowerCase();
    }
}