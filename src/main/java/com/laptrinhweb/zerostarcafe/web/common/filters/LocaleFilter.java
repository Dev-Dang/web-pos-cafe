package com.laptrinhweb.zerostarcafe.web.common.filters;

import com.laptrinhweb.zerostarcafe.core.context.LocaleContext;
import com.laptrinhweb.zerostarcafe.core.security.AppCookie;
import com.laptrinhweb.zerostarcafe.core.security.CookieUtils;
import com.laptrinhweb.zerostarcafe.core.utils.I18n;
import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

/**
 * Resolves user locale (param > session > cookie > default) and prepares I18n for JSP.
 * Sets locale in ThreadLocal context for global access throughout the request lifecycle.
 *
 * @author Dang Van Trung
 * @version 1.1.0
 * @lastModified 05/01/2026
 * @since 1.0.0
 */
@WebFilter(filterName = "LocaleFilter", urlPatterns = {"/*"})
public class LocaleFilter implements Filter {

    private static final Set<String> SUPPORTED = Set.of(
            WebConstants.Locale.SUPPORTED_VI,
            WebConstants.Locale.SUPPORTED_EN
    );
    private static final Locale DEFAULT = Locale
            .forLanguageTag(WebConstants.Locale.DEFAULT_LOCALE);

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // Skip static resources
        if (RequestUtils.isStaticRequest(request)) {
            chain.doFilter(req, resp);
            return;
        }

        // Try to get lang from param and cookie
        String paramLang = request.getParameter(WebConstants.Locale.COOKIE_NAME);
        String cookieLang = CookieUtils.get(request, WebConstants.Locale.COOKIE_NAME);

        // Get current locale from session
        HttpSession session = request.getSession();
        Locale sessionLocale = (Locale)
                session.getAttribute(WebConstants.Locale.SESSION_ATTRIBUTE);

        // Resolve locale from request
        Locale locale = resolveLocale(paramLang, cookieLang, sessionLocale);
        session.setAttribute(WebConstants.Locale.SESSION_ATTRIBUTE, locale);

        // Update cookie if needed
        String localeTag = locale.toLanguageTag();
        if (cookieLang == null || !cookieLang.equalsIgnoreCase(localeTag)) {
            response.addCookie(
                    AppCookie.accessible(
                            WebConstants.Locale.COOKIE_NAME,
                            localeTag,
                            WebConstants.Locale.COOKIE_MAX_AGE)
            );
        }

        // Update I18n in session if needed
        I18n i18n = (I18n) session.getAttribute(WebConstants.Locale.I18N_ATTRIBUTE);
        if (i18n == null || !i18n.getLocale().equals(locale)) {
            i18n = new I18n(locale, request.getServletContext());
            session.setAttribute(WebConstants.Locale.I18N_ATTRIBUTE, i18n);
        }

        // Update LocaleContext for global access
        LocaleContext.setLocale(locale);

        chain.doFilter(req, resp);
    }

    private Locale resolveLocale(String paramLang,
                                 String cookieLang,
                                 Locale sessionLocale) {
        
        // 1. Check if parameter language is valid
        if (paramLang != null && SUPPORTED.contains(paramLang)) {
            return Locale.forLanguageTag(paramLang);
        }

        // 2. Check if cookie language is valid
        if (cookieLang != null && SUPPORTED.contains(cookieLang)) {
            return Locale.forLanguageTag(cookieLang);
        }

        // 3. Check session for existing locale
        if (sessionLocale != null) {
            return sessionLocale;
        }

        // 4. Fall back to default locale
        return DEFAULT;
    }
}