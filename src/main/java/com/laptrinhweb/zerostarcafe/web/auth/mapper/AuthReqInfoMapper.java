package com.laptrinhweb.zerostarcafe.web.auth.mapper;

import com.laptrinhweb.zerostarcafe.core.security.CookieUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.request.AuthReqInfo;
import jakarta.servlet.http.HttpServletRequest;

/**
 * <h2>Description:</h2>
 * <p>
 * Maps HttpServletRequest into an AuthReqInfo object. It extracts
 * client IP, user-agent, and all cookies from the request.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 25/11/2025
 * @since 1.0.0
 */
public class AuthReqInfoMapper {

    public static AuthReqInfo from(HttpServletRequest request) {
        AuthReqInfo reqInfo = new AuthReqInfo();

        reqInfo.setIpAddress(request.getRemoteAddr());
        reqInfo.setUserAgent(request.getHeader("User-Agent"));
        reqInfo.setCookies(CookieUtil.getAll(request));

        return reqInfo;
    }
}
