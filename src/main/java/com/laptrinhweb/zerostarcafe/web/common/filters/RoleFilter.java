//package com.laptrinhweb.zerostarcafe.web.common.filters;
//
//import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
//import com.laptrinhweb.zerostarcafe.core.utils.Flash;
//import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
//import com.laptrinhweb.zerostarcafe.domain.user.model.UserRole;
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//
///**
// * <h2>Description:</h2>
// * <p>
// * This filter checks the user's role before allowing access to
// * protected areas such as "/admin", "/manager", or "/staff".
// * If the user is not logged in or does not have the correct role,
// * the filter blocks the request.
// * </p>
// *
// * <h2>Example Usage:</h2>
// * <pre>
// * /admin/*   -> requires SUPER_ADMIN
// * /manager/* -> requires STORE_MANAGER
// * /staff/*   -> requires STAFF
// * </pre>
// *
// * @author Dang Van Trung
// * @version 1.0.0
// * @lastModified 17/11/2025
// * @since 1.0.0
// */
//@WebFilter(filterName = "RoleFilter", urlPatterns = {
//        "/admin/*",
//        "/manager/*",
//        "/staff/*"
//})
//public class RoleFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse resp,
//                         FilterChain chain) throws IOException, ServletException {
//
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) resp;
//
//        AuthUser user = (AuthUser) request.getSession(false)
//                .getAttribute(SecurityKeys.SESSION_AUTH_USER);
//
//        Flash flash = new Flash(request);
//
//        // User not logged in -> redirect to home
//        if (user == null) {
//            flash.error("general.error.userNotLoggedIn").send();
//            response.sendRedirect(request.getContextPath() + "/home");
//            return;
//        }
//
//        String ctx = request.getContextPath();       // /zero_star_cafe
//        String uri = request.getRequestURI();        // /zero_star_cafe/admin/dashboard
//        String path = uri.substring(ctx.length());   // /admin/dashboard
//
//        // Check individual role requirements
//        if (path.startsWith("/admin/") && !user.hasRole(UserRole.SUPER_ADMIN)) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN);
//            return;
//        }
//
//        if (path.startsWith("/manager/") && !user.hasRole(UserRole.STORE_MANAGER)) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN);
//            return;
//        }
//
//        if (path.startsWith("/staff/") && !user.hasRole(UserRole.STAFF)) {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN);
//            return;
//        }
//
//        // User passed all checks -> continue request
//        chain.doFilter(req, resp);
//    }
//}