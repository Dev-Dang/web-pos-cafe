package com.laptrinhweb.zerostarcafe.domain.auth.service;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RequestInfoDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthResult;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthStatus;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Handles the authentication flow for each request.
 * It validates tokens, refreshes sessions, and restores user context.
 * This service decides the final auth status returned to the filter.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * AuthResult result = authReqService.handleRequest(reqInfo, currentContext);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 07/12/2025
 * @since 1.0.0
 */

@RequiredArgsConstructor
public final class AuthReqService {

    private final AuthService authService;

    public AuthReqService() {
        this(new AuthService());
    }

    /**
     * Handle the full authentication flow for a request.
     *
     * @param reqInfo        request info from client
     * @param currentContext current auth session context, may be null
     * @return auth result containing status and optional new context
     */
    public AuthResult<AuthStatus, AuthContext> handleRequest(
            RequestInfoDTO reqInfo,
            AuthContext currentContext
    ) {
        if (reqInfo == null)
            return AuthResult.fail(AuthStatus.SESSION_NOT_FOUND);

        Map<String, String> reqTokens =
                reqInfo.extractTokensByPrefix(SecurityKeys.AUTH_COOKIE_PREFIX);

        if (reqTokens.isEmpty())
            return AuthResult.fail(AuthStatus.SESSION_NOT_FOUND);

        if (currentContext != null && currentContext.isValid())
            return reAuthenticateFlow(currentContext, reqInfo, reqTokens);

        return restoreFlow(reqInfo, reqTokens);
    }

    /**
     * Process re-authentication using existing session and tokens.
     *
     * @param context   the current authentication context
     * @param reqInfo   the request information
     * @param reqTokens the token map extracted from cookies
     * @return auth result containing status and optional updated context
     */
    private AuthResult<AuthStatus, AuthContext> reAuthenticateFlow(
            AuthContext context,
            RequestInfoDTO reqInfo,
            Map<String, String> reqTokens
    ) {
        String authToken = context.getTokenValue(SecurityKeys.TOKEN_AUTH);
        boolean isValid = authService.reAuthenticate(context, reqInfo, reqTokens);
        if (!isValid) {
            authService.clearAuthState(authToken);
            return AuthResult.fail(AuthStatus.SESSION_INVALID);
        }

        String reAuthToken = context.getTokenValue(SecurityKeys.TOKEN_AUTH);
        boolean rotated = authToken != null
                && reAuthToken != null
                && !authToken.equals(reAuthToken);

        if (rotated)
            return AuthResult.ok(AuthStatus.SESSION_ROTATED, context);

        return AuthResult.ok(AuthStatus.SESSION_REUSED);
    }

    /**
     * Try to restore auth context when the request has valid cookies but no session.
     *
     * @param reqInfo   request information
     * @param reqTokens token map from cookies
     * @return auth result with status and restored context if valid
     */
    private AuthResult<AuthStatus, AuthContext> restoreFlow(
            RequestInfoDTO reqInfo,
            Map<String, String> reqTokens
    ) {
        AuthContext restored = authService.restore(reqInfo, reqTokens);

        if (restored != null && restored.isValid())
            return AuthResult.ok(AuthStatus.SESSION_RESTORED, restored);

        String rawToken = reqTokens.get(SecurityKeys.TOKEN_AUTH);
        authService.clearAuthState(rawToken);

        return AuthResult.fail(AuthStatus.SESSION_INVALID);
    }
}
