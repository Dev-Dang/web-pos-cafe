package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.service.CartService;
import com.laptrinhweb.zerostarcafe.domain.loyalty.dto.LoyaltyPointsDTO;
import com.laptrinhweb.zerostarcafe.domain.loyalty.dto.RedeemCalcDTO;
import com.laptrinhweb.zerostarcafe.domain.loyalty.service.LoyaltyService;
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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * <h2>Description:</h2>
 * <p>
 * Servlet to toggle loyalty points redemption on/off.
 * Updates session state and recalculates cart with new discount.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@WebServlet(name = "ToggleLoyaltyServlet", urlPatterns = {RouteMap.CART + "/toggle-loyalty"})
public class ToggleLoyaltyServlet extends HttpServlet {

    private final CartService cartService = CartService.getInstance();
    private final LoyaltyService loyaltyService = LoyaltyService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Check authentication
        Long userId = RequestUtils.getUserIdFromSession(req);
        Long storeId = RequestUtils.getStoreIdFromSession(req);

        if (userId == null || storeId == null) {
            Message.warn(req, "general.client.requiredLogin");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        // Get apply state from checkbox
        String applyParam = req.getParameter("apply");
        boolean apply = "true".equals(applyParam);

        // Update session state
        HttpSession session = req.getSession();
        session.setAttribute(WebConstants.Loyalty.APPLY_POINTS, apply);

        // Get current cart
        CartDTO cartDTO = cartService.getCurrentCart(userId, storeId);
        req.setAttribute(WebConstants.Cart.CART, cartDTO);

        // Recalculate redemption with new toggle state
        if (cartDTO != null && !cartDTO.getItems().isEmpty()) {
            // Ensure loyalty points in session
            LoyaltyPointsDTO loyaltyPoints = RequestUtils.getLoyaltyPointsFromSession(req);
            if (loyaltyPoints == null) {
                loyaltyPoints = loyaltyService.getUserPoints(userId);
                session.setAttribute(WebConstants.Loyalty.POINTS, loyaltyPoints);
            }

            // Calculate with new apply state
            RedeemCalcDTO redemption = loyaltyService.calculateRedemption(
                    userId, cartDTO.getTotal(), apply
            );

            req.setAttribute(WebConstants.Loyalty.REDEMPTION, redemption);
        }

        // Return updated cart panel
        View.render(ViewMap.Client.CART_PANEL, req, resp);
    }
}
