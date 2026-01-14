/**
 * ------------------------------------------------------------
 * Module: i18n - Minimal Client-Side Validation Messages
 * ------------------------------------------------------------
 * @description
 * Provides minimal validation messages for basic client-side checks.
 * Messages are synced from server locale cookie.
 *
 * @version 2.0.0
 * @since 1.0.0
 * @lastModified 03/01/2026
 * @module i18n
 * @author Dang Van Trung
 */
import * as Cookies from "./cookie.js";

const messages = {
    en: {
        emailRequired: "Email is required",
        emailInvalid: "Please enter a valid email address",
        passwordRequired: "Password is required",
        confirmPasswordRequired: "Please confirm your password",
        passwordMismatch: "Passwords do not match",
        termsRequired: "You must agree to the terms and conditions",
    },
    vi: {
        emailRequired: "Email là bắt buộc",
        emailInvalid: "Vui lòng nhập địa chỉ email hợp lệ",
        passwordRequired: "Mật khẩu là bắt buộc",
        confirmPasswordRequired: "Vui lòng xác nhận mật khẩu",
        passwordMismatch: "Mật khẩu không khớp",
        termsRequired: "Bạn phải đồng ý với điều khoản sử dụng",
    },
};

/**
 * Get current locale from cookie
 */
function getCurrentLocale() {
    const langCookie = Cookies.get("lang");
    if (!langCookie) return "vi";

    // Extract locale: "vi-VN" -> "vi", "en-US" -> "en"
    return langCookie.split("-")[0];
}

/**
 * Get validation message
 */
export function t(key) {
    const locale = getCurrentLocale();
    return messages[locale]?.[key] || messages.vi[key];
}
