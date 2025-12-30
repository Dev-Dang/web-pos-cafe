package com.laptrinhweb.zerostarcafe.web.client.mapper;

import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItemOption;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Maps HTTP request parameters to cart-related domain objects.
 * Handles parsing of product IDs, quantities, and option selections
 * from form data submitted when adding items to cart.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * CartItem item = CartWebMapper.toCartItemFromRequest(request);
 * List<Long> optionIds = CartWebMapper.parseOptionValueIds(request);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
public final class CartWebMapper {

    private CartWebMapper() {
    }

    /**
     * Creates a basic CartItem from request parameters.
     * Only sets menuItemId, qty, and note from client.
     * Prices and options must be validated and set by the service layer.
     *
     * @param req the HTTP request
     * @return a CartItem with basic fields set, or null if menuItemId is missing
     */
    public static CartItem toCartItemFromRequest(HttpServletRequest req) {
        Long menuItemId = parseLong(req.getParameter("menuItemId"));
        if (menuItemId == null) {
            return null;
        }

        Integer qty = parseInt(req.getParameter("qty"));
        if (qty == null || qty <= 0) {
            qty = 1;
        }

        String note = req.getParameter("note");
        if (note != null) {
            note = note.trim();
            if (note.isEmpty()) {
                note = null;
            }
        }

        CartItem item = new CartItem();
        item.setMenuItemId(menuItemId);
        item.setQty(qty);
        item.setNote(note);
        item.setOptions(new ArrayList<>());

        return item;
    }

    /**
     * Parses option value IDs from request.
     * Expects parameter format: optionValueIds=1,2,3 or multiple optionValueIds params.
     *
     * @param req the HTTP request
     * @return list of option value IDs (never null)
     */
    public static List<Long> parseOptionValueIds(HttpServletRequest req) {
        List<Long> result = new ArrayList<>();

        String[] values = req.getParameterValues("optionValueIds");
        if (values == null || values.length == 0) {
            // Try comma-separated format
            String single = req.getParameter("optionValueIds");
            if (single != null && !single.isBlank()) {
                values = single.split(",");
            }
        }

        if (values == null) {
            return result;
        }

        for (String val : values) {
            Long id = parseLong(val.trim());
            if (id != null && !result.contains(id)) {
                result.add(id);
            }
        }

        return result;
    }

    /**
     * Parses a cart item ID from request (for update/remove operations).
     *
     * @param req the HTTP request
     * @return the item ID, or null if missing/invalid
     */
    public static Long parseItemId(HttpServletRequest req) {
        return parseLong(req.getParameter("itemId"));
    }

    /**
     * Parses a quantity value from request (for update operations).
     *
     * @param req the HTTP request
     * @return the quantity, or null if missing/invalid
     */
    public static Integer parseQty(HttpServletRequest req) {
        return parseInt(req.getParameter("qty"));
    }

    // ==========================================================
    // PRIVATE HELPERS
    // ==========================================================

    private static Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            long parsed = Long.parseLong(value.trim());
            return parsed > 0 ? parsed : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Integer parseInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parses guest cart items from JSON and validates each item.
     * Only trusted fields (menuItemId, qty, optionValueIds, note) are used.
     * Prices are fetched from server - NEVER trust client prices.
     *
     * @param req                   the HTTP request
     * @param cartValidationService the validation service
     * @param storeId               the store ID
     * @return list of validated cart items (invalid items are filtered out)
     */
    public static List<CartItem> parseGuestCartItems(
            HttpServletRequest req,
            com.laptrinhweb.zerostarcafe.domain.cart.service.CartValidationService cartValidationService,
            long storeId
    ) {
        List<CartItem> validatedItems = new ArrayList<>();

        String guestCartJson = req.getParameter("guestCart");
        if (guestCartJson == null || guestCartJson.isBlank()) {
            return validatedItems;
        }

        try {
            // Simple JSON array parsing without external library
            // Expected format: [{"menuItemId":1,"qty":2,"optionValueIds":[1,2],"note":"extra"}]
            List<GuestCartItemData> guestItems = parseGuestCartJson(guestCartJson);

            for (GuestCartItemData guestItem : guestItems) {
                if (guestItem.menuItemId == null) {
                    continue;
                }

                // Validate and build with SERVER prices (never trust client prices!)
                CartItem validated = cartValidationService.validateAndBuildItem(
                        guestItem.menuItemId,
                        guestItem.optionValueIds,
                        guestItem.qty,
                        guestItem.note,
                        storeId
                );

                if (validated != null) {
                    validatedItems.add(validated);
                }
            }
        } catch (Exception e) {
            com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil.warn(
                    CartWebMapper.class,
                    "Failed to parse guest cart JSON: " + e.getMessage()
            );
        }

        return validatedItems;
    }

    /**
     * Simple JSON parser for guest cart array.
     * Handles basic JSON format without external dependencies.
     */
    private static List<GuestCartItemData> parseGuestCartJson(String json) {
        List<GuestCartItemData> items = new ArrayList<>();

        // Remove whitespace and outer brackets
        json = json.trim();
        if (!json.startsWith("[") || !json.endsWith("]")) {
            return items;
        }
        json = json.substring(1, json.length() - 1).trim();

        if (json.isEmpty()) {
            return items;
        }

        // Split into objects (simple approach - doesn't handle nested arrays in strings)
        int depth = 0;
        int start = 0;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                if (depth == 0) {
                    start = i;
                }
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    String objStr = json.substring(start, i + 1);
                    GuestCartItemData item = parseGuestCartItemObject(objStr);
                    if (item != null) {
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    /**
     * Parse a single JSON object into GuestCartItemData.
     */
    private static GuestCartItemData parseGuestCartItemObject(String json) {
        GuestCartItemData item = new GuestCartItemData();

        // Extract menuItemId
        item.menuItemId = extractLongValue(json, "menuItemId");
        if (item.menuItemId == null) {
            return null;
        }

        // Extract qty (default 1)
        Integer qty = extractIntValue(json, "qty");
        item.qty = (qty != null && qty > 0) ? qty : 1;

        // Extract note
        item.note = extractStringValue(json, "note");

        // Extract optionValueIds array
        item.optionValueIds = extractLongArrayValue(json, "optionValueIds");

        return item;
    }

    private static Long extractLongValue(String json, String key) {
        String pattern = "\"" + key + "\"\\s*:\\s*(-?\\d+)";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(json);
        if (matcher.find()) {
            try {
                return Long.parseLong(matcher.group(1));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private static Integer extractIntValue(String json, String key) {
        Long val = extractLongValue(json, key);
        return val != null ? val.intValue() : null;
    }

    private static String extractStringValue(String json, String key) {
        // Match "key":"value" or "key":null
        String pattern = "\"" + key + "\"\\s*:\\s*(?:\"([^\"]*)\"|null)";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(json);
        if (matcher.find()) {
            return matcher.group(1); // Returns null if matched "null"
        }
        return null;
    }

    private static List<Long> extractLongArrayValue(String json, String key) {
        List<Long> result = new ArrayList<>();
        String pattern = "\"" + key + "\"\\s*:\\s*\\[([^\\]]*)\\]";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(pattern).matcher(json);
        if (matcher.find()) {
            String arrayContent = matcher.group(1).trim();
            if (!arrayContent.isEmpty()) {
                String[] parts = arrayContent.split(",");
                for (String part : parts) {
                    try {
                        result.add(Long.parseLong(part.trim()));
                    } catch (NumberFormatException e) {
                        // Skip invalid values
                    }
                }
            }
        }
        return result;
    }

    /**
     * Simple data holder for guest cart item parsing.
     */
    private static class GuestCartItemData {
        Long menuItemId;
        int qty = 1;
        String note;
        List<Long> optionValueIds = new ArrayList<>();
    }
}
