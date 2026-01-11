package com.laptrinhweb.zerostarcafe.domain.auth.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Holds all authentication information for a logged-in user.
 * It includes user data, session info, and all active auth tokens.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 14/12/2025
 * @since 1.0.0
 */

@Getter
public class AuthContext {
    private final AuthUser authUser;
    private final AuthSession sessionInfo;
    private List<AuthToken> tokens;

    public AuthContext(
            AuthUser authUser,
            AuthSession sessionInfo,
            List<AuthToken> tokens
    ) {
        this.authUser = authUser;
        this.sessionInfo = sessionInfo;
        this.tokens = List.copyOf(tokens);
    }

    /**
     * Validates this authentication context.
     *
     * @return true if user, session, and tokens are valid
     */
    public boolean isValid() {
        return authUser != null
                && sessionInfo != null
                && !sessionInfo.isExpired()
                && tokens != null
                && !tokens.isEmpty();
    }

    /**
     * Gets the value of a token by its name.
     *
     * @param name the token name
     * @return the token value, or null if not found
     */
    public String getTokenValue(String name) {
        if (tokens == null || tokens.isEmpty() || name == null)
            return null;

        for (AuthToken token : tokens) {
            if (token.getName().equals(name)) {
                return token.getValue();
            }
        }

        return null;
    }

    /**
     * Creates a new context with an updated token.
     *
     * @param newToken the new token to update
     */
    public void updateToken(AuthToken newToken) {
        if (tokens == null || newToken == null)
            return;

        List<AuthToken> newTokens = new ArrayList<>();
        for (AuthToken t : tokens) {
            if (!t.getName().equals(newToken.getName())) {
                newTokens.add(t);
            }
        }

        newTokens.add(newToken);
        this.tokens = List.copyOf(newTokens);
    }
}