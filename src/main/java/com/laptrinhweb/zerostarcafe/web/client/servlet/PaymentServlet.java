package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.service.CartService;
import com.laptrinhweb.zerostarcafe.domain.loyalty.dto.RedeemCalcDTO;
import com.laptrinhweb.zerostarcafe.domain.loyalty.service.LoyaltyService;
import com.laptrinhweb.zerostarcafe.domain.payment.dto.PaymentResultDTO;
import com.laptrinhweb.zerostarcafe.domain.payment.model.PaymentAction;
import com.laptrinhweb.zerostarcafe.domain.payment.model.PaymentResult;
import com.laptrinhweb.zerostarcafe.domain.payment.model.PaymentStatus;
import com.laptrinhweb.zerostarcafe.domain.payment.service.PaymentService;
import com.laptrinhweb.zerostarcafe.domain.store.model.Store;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.service.StoreService;
import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
import com.laptrinhweb.zerostarcafe.web.common.response.Message;
import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import com.laptrinhweb.zerostarcafe.web.common.routing.RouteMap;
import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
import com.laptrinhweb.zerostarcafe.web.common.view.View;
import com.laptrinhweb.zerostarcafe.web.common.view.ViewMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Payment processing servlet.
 * Handles payment method selection modal and payment processing.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@WebServlet(name = "PaymentServlet", urlPatterns = {
        RouteMap.PAYMENT_SELECT,
        RouteMap.PAYMENT_PROCESS_CASH
})
public class PaymentServlet extends HttpServlet {

    private static final StoreService storeService = StoreService.getInstance();
    private static final CartService cartService = CartService.getInstance();
    private static final LoyaltyService loyaltyService = LoyaltyService.getInstance();
    private static final PaymentService paymentService = PaymentService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Authentication is enforced by RoleFilter
        Long userId = RequestUtils.getUserIdFromSession(req);
        Long storeId = RequestUtils.getStoreIdFromSession(req);

        // Get current store info
        Store currentStore = storeService.getActiveStoreById(storeId);
        req.setAttribute(StoreConstants.Attribute.CURRENT_STORE, currentStore);

        // Get current cart
        CartDTO cartDTO = cartService.getCurrentCart(userId, storeId);

        // Validate cart not empty
        if (cartDTO == null || cartDTO.getItems().isEmpty()) {
            Message.warn(req, "general.cart.empty");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        req.setAttribute(WebConstants.Cart.CART, cartDTO);

        // Calculate loyalty redemption
        boolean applyPoints = RequestUtils.getApplyLoyaltyFromSession(req);
        RedeemCalcDTO redemption = loyaltyService.calculateRedemption(
                userId, cartDTO.getTotal(), applyPoints
        );
        req.setAttribute(WebConstants.Loyalty.REDEMPTION, redemption);

        // Render payment modal fragment
        View.render(ViewMap.Client.PAYMENT_MODAL, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        PaymentAction action = PaymentAction.fromPath(pathInfo);

        if (action == null) {
            String servletPath = req.getServletPath();
            if (RouteMap.PAYMENT_PROCESS_CASH.equals(servletPath)) {
                action = PaymentAction.PROCESS_CASH;
            }
        }

        if (action == PaymentAction.PROCESS_CASH) {
            handleCashPayment(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Handles cash payment processing.
     */
    private void handleCashPayment(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Authentication is enforced by RoleFilter
        Long userId = RequestUtils.getUserIdFromSession(req);
        Long storeId = RequestUtils.getStoreIdFromSession(req);

        Boolean applyLoyalty = (Boolean) req.getSession()
                .getAttribute(WebConstants.Loyalty.APPLY_POINTS);

        // Process payment
        PaymentResult<PaymentStatus, PaymentResultDTO> result =
                paymentService.processCashPayment(userId, storeId,
                        applyLoyalty != null && applyLoyalty);

        if (result.isSuccess()) {
            // SUCCESS - Store result and show success modal
            req.getSession().setAttribute(WebConstants.Payment.PAYMENT_RESULT, result.getData());
            Message.success(req, result.getStatus().getMessageKey());

            // Clear cached loyalty points so next page load fetches fresh data
            req.getSession().removeAttribute(WebConstants.Loyalty.POINTS);

            // Render success modal in same Unpoly layer
            View.render(ViewMap.Client.PAYMENT_SUCCESS, req, resp);

        } else {
            // FAILURE - Show error message and redirect home
            Message.error(req, result.getStatus().getMessageKey());
            AppRoute.redirect(RouteMap.HOME, req, resp);
        }
    }
}
