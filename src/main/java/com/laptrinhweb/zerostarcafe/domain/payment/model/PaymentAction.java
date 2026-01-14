package com.laptrinhweb.zerostarcafe.domain.payment.model;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents the possible actions that can be performed on payment endpoints.
 * Used for routing in PaymentServlet.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
public enum PaymentAction {
    PROCESS_CASH,
    PROCESS_QR,      // Future: VNPay QR payment
    VERIFY_PAYMENT;  // Future: Payment gateway callback

    /**
     * Parses payment action from request path.
     * Example: "/process-cash" → PROCESS_CASH
     *
     * @param path the request path (e.g., "/process-cash")
     * @return corresponding PaymentAction or null if invalid
     */
    public static PaymentAction fromPath(String path) {
        if (path == null || path.length() <= 1) {
            return null;
        }
        try {
            // Convert: "/process-cash" → "PROCESS_CASH"
            return valueOf(path.substring(1).replace("-", "_").toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
