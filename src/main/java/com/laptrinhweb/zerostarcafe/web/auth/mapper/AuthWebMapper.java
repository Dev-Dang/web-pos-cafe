package com.laptrinhweb.zerostarcafe.web.auth.mapper;

import com.laptrinhweb.zerostarcafe.core.security.CookieUtils;
import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.LoginDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RegisterDTO;
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
 * Maps web request data to domain DTOs and vice versa.
 * - Login: Extracts username prefix from email (backward compatibility)
 * - Register: Generates unique username with random suffix (prevents collisions)
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.3
 * @lastModified 28/12/2025
 * @since 1.0.0
 */
public final class AuthWebMapper {

    public static LoginDTO toLoginDTO(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        return new LoginDTO(email, password);
    }

    public static RegisterDTO toRegisterDTO(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String agreeTerms = request.getParameter("agreeTerms");

        return new RegisterDTO(email, password, agreeTerms);
    }

    public static RequestInfoDTO toReqInfoDTO(HttpServletRequest request) {
        if (request == null)
            return null;

        return new RequestInfoDTO(
                request.getRemoteAddr(),
                request.getHeader("User-Agent"),
                CookieUtils.getAll(request)
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
