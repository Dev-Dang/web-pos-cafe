package com.laptrinhweb.zerostarcafe.web.auth.mapper;

import com.laptrinhweb.zerostarcafe.core.security.CookieUtil;
import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RequestInfoDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthSession;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthToken;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 *
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ... code here
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 15/12/2025
 * @since 1.0.0
 */
public final class AuthWebMapper {

    public static RequestInfoDTO toReqInfoDTO(HttpServletRequest request) {
        if (request == null)
            return null;

        return new RequestInfoDTO(
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                CookieUtil.getAll(request)
        );
    }

    public static AuthContext toAuthContext(HttpSession session) {
        if (session == null)
            return null;

        AuthUser authUser = (AuthUser)
                session.getAttribute(SecurityKeys.SESSION_AUTH_USER);

        AuthSession sessionInfo = (AuthSession)
                session.getAttribute(SecurityKeys.SESSION_AUTH_SESSION);

        @SuppressWarnings("unchecked")
        List<AuthToken> tokens = (List<AuthToken>)
                session.getAttribute(SecurityKeys.SESSION_AUTH_TOKENS);
        if (tokens == null) {
            tokens = new ArrayList<>();
        }

        return new AuthContext(authUser, sessionInfo, tokens);
    }
}
