package com.laptrinhweb.zerostarcafe.domain.auth.request;

import java.util.HashMap;
import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Holds basic request information needed for authentication checks.
 * Contains IP address, user-agent, and all request cookies.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 24/11/2025
 * @since 1.0.0
 */
public class AuthReqInfo {
    private String ipAddress;
    private String userAgent;
    private Map<String, String> cookies;

    public AuthReqInfo() {
    }

    public AuthReqInfo(
            String ipAddress,
            String userAgent,
            Map<String, String> cookies
    ) {
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.cookies = cookies;
    }

    /**
     * Extracts cookies by exact names.
     *
     * @param names cookie names to extract
     * @return a map of cookie name/value pairs
     */
    public Map<String, String> extractTokens(String... names) {
        if (cookies == null || cookies.isEmpty() || names == null || names.length == 0)
            return Map.of();

        Map<String, String> result = new HashMap<>();
        for (String name : names) {
            if (cookies.containsKey(name)) {
                result.put(name, cookies.get(name));
            }
        }
        return result;
    }

    /**
     * Extracts cookies that start with the given prefix.
     *
     * @param prefix the cookie name prefix
     * @return a map of cookie name/value pairs
     */
    public Map<String, String> extractTokensByPrefix(String prefix) {
        if (cookies == null || cookies.isEmpty() || prefix == null || prefix.isBlank()) {
            return Map.of();
        }

        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            String name = entry.getKey();
            if (name != null && name.startsWith(prefix)) {
                result.put(name, entry.getValue());
            }
        }
        return result;
    }

    /**
     * Gets a cookie value by name.
     *
     * @param key the cookie name
     * @return the cookie value, or null if missing
     */
    public String getCookieValue(String key) {
        if (key != null && cookies.containsKey(key))
            return cookies.get(key);

        return null;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}