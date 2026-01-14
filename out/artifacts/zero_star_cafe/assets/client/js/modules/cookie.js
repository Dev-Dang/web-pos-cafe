/**
 * ------------------------------------------------------------
 * Module: Cookie Manager
 * ------------------------------------------------------------
 * @description
 * A lightweight utility module for handling browser cookies.
 * Provides a simple API to create, read, and delete cookies
 * with support for encoding/decoding values.
 *
 * Supports:
 * - Getting cookie value by name (safely handling special chars).
 * - Setting cookies with expiration days.
 * - Removing cookies.
 *
 * @example
 * import * as Cookie from './cookie.js';
 * Cookie.set('user_token', 'abc-xyz', 7); // Set for 7 days
 * const token = Cookie.get('user_token');
 * Cookie.remove('user_token');
 *
 * @version 1.0.0
 * @since 1.0.0
 * @lastModified 12/12/2025
 * @module cookie
 * @author Dang Van Trung
 */

/**
 * Retrieves a cookie value by its name.
 * @param {string} name - The name of the cookie to retrieve.
 * @returns {string|null} The decoded value of the cookie, or null if not found.
 */
export function get(name) {
    const cookies = document.cookie.split(';');

    for (const c of cookies) {
        const trimmed = c.trim();

        // Check if the cookie string starts with the name followed by "="
        // This prevents the split('=') bug when the value itself contains "="
        if (trimmed.startsWith(name + '=')) {
            const value = trimmed.substring(name.length + 1);
            return decodeURIComponent(value);
        }
    }

    return null;
}

/**
 * Sets a cookie with a name, value, and expiration in days.
 * @param {string} name - The name of the cookie.
 * @param {string} value - The value to store.
 * @param {number} [days] - Number of days until expiration (optional).
 */
export function set(name, value, days) {
    let expires = "";

    if (days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }

    // encodeURIComponent is crucial to handle special characters safely
    document.cookie = name + "=" + encodeURIComponent(value || "") + expires + "; path=/";
}

/**
 * Removes a cookie by setting its expiration date to the past.
 * @param {string} name - The name of the cookie to remove.
 */
export function remove(name) {
    // To delete, we set the cookie again with the same name but empty value and expired date
    document.cookie = name + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}