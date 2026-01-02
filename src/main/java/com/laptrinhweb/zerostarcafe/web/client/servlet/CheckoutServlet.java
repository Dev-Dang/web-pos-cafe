package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.service.CartCacheService;
import com.laptrinhweb.zerostarcafe.domain.invoice.model.Invoice;
import com.laptrinhweb.zerostarcafe.domain.invoice.service.InvoiceService;
import com.laptrinhweb.zerostarcafe.domain.order.model.Order;
import com.laptrinhweb.zerostarcafe.domain.order.service.OrderService;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
import com.laptrinhweb.zerostarcafe.web.auth.session.AuthSessionManager;
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

/**
 * <h2>Description:</h2>
 * <p>
 * Handles checkout process - converts cart to order and displays invoice.
 * Simple implementation: immediate payment, no external payment gateway.
 * </p>
 *
 * <h2>Endpoints:</h2>
 * <ul>
 *     <li>GET  /checkout - Show checkout/cart review page</li>
 *     <li>POST /checkout - Process payment and create order</li>
 *     <li>GET  /invoice/{orderId} - Show invoice for completed order</li>
 * </ul>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/12/2025
 * @since 1.0.0
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = {
        "/checkout",
        "/invoice/*"
})
public class CheckoutServlet extends HttpServlet {

    private final CartCacheService cartCacheService = CartCacheService.getInstance();
    private final OrderService orderService = new OrderService();
    private final InvoiceService invoiceService = new InvoiceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        if (path.equals("/checkout")) {
            handleShowCheckout(req, resp);
        } else if (path.equals("/invoice")) {
            handleShowInvoice(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        if (path.equals("/checkout")) {
            handleProcessCheckout(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Shows checkout page with cart review.
     */
    private void handleShowCheckout(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        AuthUser user = getAuthUser(req);
        StoreContext storeCtx = getStoreContext(req);

        if (user == null || storeCtx == null) {
            AppRoute.redirect(RouteMap.LOGIN, req, resp);
            return;
        }

        Cart cart = cartCacheService.getCart(user.getId(), storeCtx.getStoreId());

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            // Empty cart - redirect to home
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        req.setAttribute("cart", cart);
        View.render(ViewMap.Client.CHECKOUT, req, resp);
    }

    /**
     * Processes checkout - creates order from cart, clears cart.
     */
    private void handleProcessCheckout(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        AuthUser user = getAuthUser(req);
        StoreContext storeCtx = getStoreContext(req);

        if (user == null || storeCtx == null) {
            AppRoute.redirect(RouteMap.LOGIN, req, resp);
            return;
        }

        Cart cart = cartCacheService.getCart(user.getId(), storeCtx.getStoreId());

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        // Create order from cart
        Order order = orderService.createOrderFromCart(cart, user.getId());

        if (order == null) {
            if (isAjaxRequest(req)) {
                resp.setContentType("application/json;charset=UTF-8");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"success\": false, \"error\": \"Không thể tạo đơn hàng\"}");
            } else {
                req.setAttribute("error", "Không thể tạo đơn hàng. Vui lòng thử lại.");
                req.setAttribute("cart", cart);
                View.render(ViewMap.Client.CHECKOUT, req, resp);
            }
            return;
        }

        // Create invoice
        Invoice invoice = invoiceService.createInvoice(
                order.getId(),
                storeCtx.getStoreId(),
                user.getId(),
                order.getTotalPrice(),
                user.getUsername(),
                user.getEmail()
        );

        // Clear cart after successful order
        cartCacheService.clearCart(user.getId(), storeCtx.getStoreId());

        if (isAjaxRequest(req)) {
            // Return success response for AJAX
            resp.setContentType("application/json;charset=UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(String.format(
                    "{\"success\": true, \"orderId\": %d, \"invoiceId\": %d, \"total\": %d}",
                    order.getId(), invoice.getId(), order.getTotalPrice()
            ));
        } else {
            // Redirect to invoice page
            resp.sendRedirect(req.getContextPath() + "/invoice?orderId=" + order.getId());
        }
    }

    /**
     * Shows invoice for a completed order.
     */
    private void handleShowInvoice(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String orderIdParam = req.getParameter("orderId");
        if (orderIdParam == null || orderIdParam.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing orderId");
            return;
        }

        long orderId;
        try {
            orderId = Long.parseLong(orderIdParam);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid orderId");
            return;
        }

        Order order = orderService.findById(orderId);

        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            return;
        }

        // Security check: order belongs to current user
        AuthUser user = getAuthUser(req);
        if (user == null || (order.getUserId() != null && order.getUserId() != user.getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        req.setAttribute("order", order);

        // Check if invoice exists for this order
        Invoice invoice = invoiceService.findByOrderId(order.getId());
        if (invoice != null) {
            req.setAttribute("invoice", invoice);
        }

        View.render(ViewMap.Client.INVOICE, req, resp);
    }

    private boolean isAjaxRequest(HttpServletRequest req) {
        return "XMLHttpRequest".equals(req.getHeader("X-Requested-With"));
    }

    // Helper methods
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
                StoreConstants.Session.CURRENT_STORE_CTX
        );
    }
}
