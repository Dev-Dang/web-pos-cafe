package com.laptrinhweb.zerostarcafe.web.common.filters;

import com.laptrinhweb.zerostarcafe.core.security.AppCookie;
import com.laptrinhweb.zerostarcafe.core.security.CookieUtils;
import com.laptrinhweb.zerostarcafe.core.utils.I18n;
import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
import com.laptrinhweb.zerostarcafe.web.common.utils.WebConstants;
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
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 03/01/2026
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

        HttpSession session = request.getSession();

        String paramLang = request.getParameter(WebConstants.Locale.COOKIE_NAME);
        String cookieLang = CookieUtils.get(request, WebConstants.Locale.COOKIE_NAME);

        // Resolve locale from param > session > cookie > default
        Locale locale = resolveLocale(paramLang, session, cookieLang);
        session.setAttribute(WebConstants.Locale.SESSION_ATTRIBUTE, locale);

        String localeTag = locale.toLanguageTag();
        if (cookieLang == null || !cookieLang.equalsIgnoreCase(localeTag)) {
            response.addCookie(
                    AppCookie.accessible(
                            WebConstants.Locale.COOKIE_NAME,
                            localeTag,
                            WebConstants.Locale.COOKIE_MAX_AGE)
            );
        }

        // Ensure session I18n is up to date
        I18n i18n = (I18n) session.getAttribute(WebConstants.Locale.I18N_ATTRIBUTE);
        if (i18n == null || !i18n.getLocale().equals(locale)) {
            i18n = new I18n(locale, request.getServletContext());
            session.setAttribute(WebConstants.Locale.I18N_ATTRIBUTE, i18n);
        }

        chain.doFilter(req, resp);
    }

    private Locale resolveLocale(String paramLang, HttpSession session, String cookieLang) {
        // 1. Check if parameter language is valid
        if (paramLang != null && SUPPORTED.contains(paramLang)) {
            return Locale.forLanguageTag(paramLang);
        }

        // 2. Check session for existing locale
        Locale sessionLocale = (Locale) session.getAttribute(WebConstants.Locale.SESSION_ATTRIBUTE);
        if (sessionLocale != null) {
            return sessionLocale;
        }

        // 3. Check if cookie language is valid
        if (cookieLang != null && SUPPORTED.contains(cookieLang)) {
            return Locale.forLanguageTag(cookieLang);
        }

        // 4. Fall back to default locale
        return DEFAULT;
    }
}