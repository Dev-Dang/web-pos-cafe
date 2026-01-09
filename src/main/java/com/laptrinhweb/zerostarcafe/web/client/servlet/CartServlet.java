package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.cart.dto.AddToCartDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartAction;
import com.laptrinhweb.zerostarcafe.domain.cart.model.CartConstants;
import com.laptrinhweb.zerostarcafe.domain.cart.service.CartService;
import com.laptrinhweb.zerostarcafe.web.client.mapper.CartWebMapper;
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

@WebServlet(name = "CartServlet", urlPatterns = {RouteMap.CART + "/*"})
public class CartServlet extends HttpServlet {

    private final CartService cartService = CartService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Fail fast: Check authentication
        Long userId = RequestUtils.getUserIdFromSession(req);
        Long storeId = RequestUtils.getStoreIdFromSession(req);

        if (userId == null || storeId == null) {
            Message.warn(req, "general.client.requiredLogin");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        // Determine action from path
        String requestPath = req.getPathInfo();
        CartAction action = CartAction.fromPath(requestPath);

        switch (action) {
            case ADD -> handleAddToCart(storeId, userId, req, resp);
            case UPDATE -> handleUpdateQuantity(storeId, userId, req, resp);
            case REMOVE -> handleRemoveItem(storeId, userId, req, resp);
            default -> {
                Message.warn(req, "general.error.badRequest");
                AppRoute.redirect(RouteMap.HOME, req, resp);
            }
        }
    }

    /**
     * Handle POST /cart/add - returns cart fragment.
     */
    private void handleAddToCart(Long storeId, Long userId,
                                 HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        // Parse request to DTO
        AddToCartDTO dto = CartWebMapper.toAddToCartDTO(req);
        if (dto == null || !dto.isValid()) {
            Message.warn(req, "general.error.badRequest");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        // Call service (returns CartDTO)
        CartDTO cartDTO = cartService.addToCart(userId, storeId, dto);

        // Return cart fragment for client
        req.setAttribute(WebConstants.Cart.CART, cartDTO);
        View.render(ViewMap.Client.CART_PANEL, req, resp);
    }

    /**
     * Handle POST /cart/update - returns cart fragment.
     * Supports operation-based updates: action=increase/decrease with current qty.
     */
    private void handleUpdateQuantity(Long storeId, Long userId,
                                      HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        // Parse parameters
        Long cartItemId = RequestUtils.getLongParam(req, WebConstants.Cart.CART_ITEM_ID);
        Integer currentQty = RequestUtils.getIntParam(req, WebConstants.Cart.QUANTITY);
        String action = req.getParameter(WebConstants.Cart.ACTION);

        if (cartItemId == null || currentQty == null || action == null) {
            Message.warn(req, "general.error.badRequest");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        // Calculate new quantity based on action
        int newQty;
        if ("increase".equals(action)) {
            newQty = currentQty + 1;
        } else if ("decrease".equals(action)) {
            newQty = currentQty - 1;
        } else {
            Message.warn(req, "general.error.badRequest");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        // Validate new quantity
        if (newQty < CartConstants.Validation.MIN_QUANTITY
                || newQty >= CartConstants.Validation.MAX_QUANTITY) {
            Message.warn(req, "general.error.badRequest");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        // Call service
        CartDTO cartDTO = cartService
                .updateQuantity(userId, storeId, cartItemId, newQty);

        // Return cart fragment for client
        req.setAttribute(WebConstants.Cart.CART, cartDTO);
        View.render(ViewMap.Client.CART_PANEL, req, resp);
    }

    /**
     * Handle POST /cart/remove - returns cart fragment.
     */
    private void handleRemoveItem(Long storeId, Long userId,
                                  HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        // Parse parameters
        Long cartItemId = RequestUtils
                .getLongParam(req, WebConstants.Cart.CART_ITEM_ID);

        if (cartItemId == null) {
            Message.warn(req, "general.error.badRequest");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        // Call service (returns CartDTO)
        CartDTO cartDTO = cartService.removeItem(userId, storeId, cartItemId);

        // Return cart fragment for client
        req.setAttribute(WebConstants.Cart.CART, cartDTO);
        View.render(ViewMap.Client.CART_PANEL, req, resp);
    }
}
