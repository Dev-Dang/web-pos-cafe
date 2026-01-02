package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartItem;
import com.laptrinhweb.zerostarcafe.domain.cart.service.CartCacheService;
import com.laptrinhweb.zerostarcafe.domain.cart.service.CartValidationService;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
import com.laptrinhweb.zerostarcafe.web.auth.session.AuthSessionManager;
import com.laptrinhweb.zerostarcafe.web.client.mapper.CartWebMapper;
import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import com.laptrinhweb.zerostarcafe.web.common.routing.RouteMap;
import com.laptrinhweb.zerostarcafe.web.common.view.View;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Handles cart operations for logged-in users using SSR (Server-Side Rendering).
 * Returns HTML fragments for AJAX requests and full page redirects for non-AJAX.
 * </p>
 *
 * <h2>Endpoints:</h2>
 * <ul>
 *     <li>GET  /cart - Get cart view (full page or fragment)</li>
 *     <li>POST /cart/add - Add item to cart</li>
 *     <li>POST /cart/update - Update item quantity</li>
 *     <li>POST /cart/remove - Remove item from cart</li>
 *     <li>POST /cart/clear - Clear all items from cart</li>
 * </ul>
 *
 * <h2>Security:</h2>
 * <ul>
 *     <li>Requires user to be logged in</li>
 *     <li>Requires valid store context</li>
 *     <li>Prices are always validated server-side (never trust client)</li>
 * </ul>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
@WebServlet(name = "CartServlet", urlPatterns = {
        "/cart",
        "/cart/add",
        "/cart/update",
        "/cart/remove",
        "/cart/clear",
        "/cart/merge"
})
public class CartServlet extends HttpServlet {

    private final CartCacheService cartCacheService = CartCacheService.getInstance();
    private final CartValidationService cartValidationService = new CartValidationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        AuthUser user = getAuthUser(req);
        StoreContext storeCtx = getStoreContext(req);

        if (user == null || storeCtx == null) {
            sendUnauthorized(req, resp);
            return;
        }

        Cart cart = cartCacheService.getCart(user.getId(), storeCtx.getStoreId());
        setCartResponseHeaders(resp, cart);
        req.setAttribute("cart", cart);

        if (isAjaxRequest(req)) {
            resp.setContentType("text/html;charset=UTF-8");
            View.render(ViewMap.Client.CART_SIDEBAR, req, resp);
        } else {
            View.render(ViewMap.Client.CART, req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        switch (path) {
            case "/cart/add":
                handleAddItem(req, resp);
                break;
            case "/cart/update":
                handleUpdateItem(req, resp);
                break;
            case "/cart/remove":
                handleRemoveItem(req, resp);
                break;
            case "/cart/clear":
                handleClearCart(req, resp);
                break;
            case "/cart/merge":
                handleMergeGuestCart(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // ==========================================================
    // POST HANDLERS
    // ==========================================================

    private void handleAddItem(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        AuthUser user = getAuthUser(req);
        StoreContext storeCtx = getStoreContext(req);

        if (user == null || storeCtx == null) {
            sendUnauthorized(req, resp);
            return;
        }

        // Parse request
        CartItem rawItem = CartWebMapper.toCartItemFromRequest(req);
        if (rawItem == null) {
            sendBadRequest(resp, "Missing menuItemId");
            return;
        }

        List<Long> optionValueIds = CartWebMapper.parseOptionValueIds(req);

        // Validate and build item with server prices
        CartItem validatedItem = cartValidationService.validateAndBuildItem(
                rawItem.getMenuItemId(),
                optionValueIds,
                rawItem.getQty(),
                rawItem.getNote(),
                storeCtx.getStoreId()
        );

        if (validatedItem == null) {
            sendBadRequest(resp, "Product not available");
            return;
        }

        // Add to cart cache
        Cart cart = cartCacheService.addItem(user.getId(), storeCtx.getStoreId(), validatedItem);

        sendCartResponse(req, resp, cart);
    }

    private void handleUpdateItem(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        AuthUser user = getAuthUser(req);
        StoreContext storeCtx = getStoreContext(req);

        if (user == null || storeCtx == null) {
            sendUnauthorized(req, resp);
            return;
        }

        Long itemId = CartWebMapper.parseItemId(req);
        Integer qty = CartWebMapper.parseQty(req);

        if (itemId == null) {
            sendBadRequest(resp, "Missing itemId");
            return;
        }

        if (qty == null) {
            sendBadRequest(resp, "Missing qty");
            return;
        }

        Cart cart = cartCacheService.updateItemQty(user.getId(), storeCtx.getStoreId(), itemId, qty);
        if (cart == null) {
            sendBadRequest(resp, "Item not found");
            return;
        }

        sendCartResponse(req, resp, cart);
    }

    private void handleRemoveItem(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        AuthUser user = getAuthUser(req);
        StoreContext storeCtx = getStoreContext(req);

        if (user == null || storeCtx == null) {
            sendUnauthorized(req, resp);
            return;
        }

        Long itemId = CartWebMapper.parseItemId(req);
        if (itemId == null) {
            sendBadRequest(resp, "Missing itemId");
            return;
        }

        Cart cart = cartCacheService.removeItem(user.getId(), storeCtx.getStoreId(), itemId);
        if (cart == null) {
            sendBadRequest(resp, "Item not found");
            return;
        }

        sendCartResponse(req, resp, cart);
    }

    private void handleClearCart(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        AuthUser user = getAuthUser(req);
        StoreContext storeCtx = getStoreContext(req);

        if (user == null || storeCtx == null) {
            sendUnauthorized(req, resp);
            return;
        }

        Cart cart = cartCacheService.clearCart(user.getId(), storeCtx.getStoreId());
        sendCartResponse(req, resp, cart);
    }

    private void handleMergeGuestCart(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        AuthUser user = getAuthUser(req);
        StoreContext storeCtx = getStoreContext(req);

        if (user == null || storeCtx == null) {
            sendUnauthorized(req, resp);
            return;
        }

        // Clear the merge flag from session
        req.getSession().removeAttribute("needsCartMerge");

        // Parse guest cart items from JSON
        List<CartItem> validatedItems = CartWebMapper.parseGuestCartItems(
                req,
                cartValidationService,
                storeCtx.getStoreId()
        );

        if (validatedItems.isEmpty()) {
            // No valid items to merge, just return current cart
            Cart cart = cartCacheService.getCart(user.getId(), storeCtx.getStoreId());
            sendCartResponse(req, resp, cart);
            return;
        }

        // Merge validated items into server cart
        Cart cart = cartCacheService.mergeGuestCart(
                user.getId(),
                storeCtx.getStoreId(),
                validatedItems
        );

        sendCartResponse(req, resp, cart);
    }

    // ==========================================================
    // RESPONSE HELPERS
    // ==========================================================

    private void sendCartResponse(HttpServletRequest req, HttpServletResponse resp, Cart cart)
            throws ServletException, IOException {

        setCartResponseHeaders(resp, cart);
        req.setAttribute("cart", cart);

        if (isAjaxRequest(req)) {
            resp.setContentType("text/html;charset=UTF-8");
            View.render(ViewMap.Client.CART_SIDEBAR, req, resp);
        } else {
            AppRoute.redirect(RouteMap.HOME, req, resp);
        }
    }

    private void setCartResponseHeaders(HttpServletResponse resp, Cart cart) {
        resp.setHeader("Cache-Control", "no-store");
        if (cart != null) {
            resp.setHeader("X-Cart-Count", String.valueOf(cart.getTotalQty()));
            resp.setHeader("X-Cart-Total", String.valueOf(cart.getTotalPrice()));
        }
    }

    private void sendUnauthorized(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        if (isAjaxRequest(req)) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            // Redirect to login - will render home page with login modal
            AppRoute.redirect(RouteMap.LOGIN, req, resp);
        }
    }

    private void sendBadRequest(HttpServletResponse resp, String message) throws IOException {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
    }

    // ==========================================================
    // CONTEXT HELPERS
    // ==========================================================

    private boolean isAjaxRequest(HttpServletRequest req) {
        return "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
    }

    private AuthUser getAuthUser(HttpServletRequest req) {
        AuthSessionManager sessionManager = (AuthSessionManager) getServletContext()
                .getAttribute("authSessionManager");

        if (sessionManager == null) {
            return null;
        }

        AuthContext authCtx = sessionManager.getContext(req);
        if (authCtx == null || !authCtx.isValid()) {
            return null;
        }

        return authCtx.getAuthUser();
    }

    private StoreContext getStoreContext(HttpServletRequest req) {
        return (StoreContext) req.getSession().getAttribute(
                com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants.Session.CURRENT_STORE_CTX
        );
    }
}
