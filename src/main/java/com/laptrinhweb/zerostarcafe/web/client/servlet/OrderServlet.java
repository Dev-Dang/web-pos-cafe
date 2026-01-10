package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.cart.dto.CartDTO;
import com.laptrinhweb.zerostarcafe.domain.cart.service.CartService;
import com.laptrinhweb.zerostarcafe.domain.invoice.dto.InvoiceDTO;
import com.laptrinhweb.zerostarcafe.domain.invoice.dto.InvoiceDetailDTO;
import com.laptrinhweb.zerostarcafe.domain.invoice.service.InvoiceService;
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
import java.util.List;

/**
 * Order history modal servlet.
 * Returns order history modal fragment and supports reordering from invoice.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 09/01/2026
 * @since 1.0.0
 */
@WebServlet(name = "OrderServlet", urlPatterns = {
        RouteMap.USER_ORDERS,
        RouteMap.USER_ORDERS + "/*"
})
public class OrderServlet extends HttpServlet {

    private static final InvoiceService invoiceService = InvoiceService.getInstance();
    private static final CartService cartService = CartService.getInstance();
    private static final LoyaltyService loyaltyService = LoyaltyService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long userId = RequestUtils.getUserIdFromSession(req);
        if (userId == null) {
            Message.warn(req, "general.client.requiredLogin");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        Long invoiceId = RequestUtils.getLongParam(req, "invoiceId");
        List<InvoiceDTO> invoices = invoiceService.getInvoicesByUser(userId);

        InvoiceDTO selectedInvoice = null;
        if (invoices != null && !invoices.isEmpty()) {
            if (invoiceId != null) {
                for (InvoiceDTO invoice : invoices) {
                    if (invoice.getId() == invoiceId) {
                        selectedInvoice = invoice;
                        break;
                    }
                }
            }

            if (selectedInvoice == null) {
                selectedInvoice = invoices.get(0);
            }
        }

        InvoiceDetailDTO invoiceDetail = null;
        if (selectedInvoice != null) {
            invoiceDetail = invoiceService.getInvoiceDetail(selectedInvoice.getId());
        }

        req.setAttribute("invoices", invoices);
        req.setAttribute("selectedInvoice", selectedInvoice);
        req.setAttribute("invoiceDetail", invoiceDetail);

        View.render(ViewMap.Client.ORDER_HISTORY_MODAL, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        if ("/clone".equals(pathInfo)) {
            handleCloneCart(req, resp);
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    private void handleCloneCart(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long userId = RequestUtils.getUserIdFromSession(req);
        Long storeId = RequestUtils.getStoreIdFromSession(req);

        if (userId == null || storeId == null) {
            Message.warn(req, "general.client.requiredLogin");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        Long invoiceId = RequestUtils.getLongParam(req, "invoiceId");
        if (invoiceId == null) {
            Message.warn(req, "general.error.badRequest");
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        CartDTO cartDTO = cartService.cloneCartFromInvoice(userId, storeId, invoiceId);
        applyLoyaltyRedemption(userId, cartDTO, req);

        req.setAttribute(WebConstants.Cart.CART, cartDTO);
        View.render(ViewMap.Client.CART_PANEL, req, resp);
    }

    private void applyLoyaltyRedemption(Long userId, CartDTO cartDTO, HttpServletRequest req) {
        HttpSession session = req.getSession();

        LoyaltyPointsDTO loyaltyPoints = RequestUtils.getLoyaltyPointsFromSession(req);
        if (loyaltyPoints == null) {
            loyaltyPoints = loyaltyService.getUserPoints(userId);
            session.setAttribute(WebConstants.Loyalty.POINTS, loyaltyPoints);
        }

        if (cartDTO == null || cartDTO.getItems().isEmpty()) {
            RedeemCalcDTO emptyRedemption = new RedeemCalcDTO();
            emptyRedemption.setUserTotalPoints(loyaltyPoints.getPointsBalance());
            emptyRedemption.setMaxRedeemablePoints(0);
            emptyRedemption.setPointsToRedeem(0);
            emptyRedemption.setDiscountAmount(0);
            emptyRedemption.setCanRedeem(false);
            emptyRedemption.setApplied(false);
            req.setAttribute(WebConstants.Loyalty.REDEMPTION, emptyRedemption);
            return;
        }

        boolean applyPoints = RequestUtils.getApplyLoyaltyFromSession(req);
        RedeemCalcDTO redemption = loyaltyService.calculateRedemption(
                userId, cartDTO.getTotal(), applyPoints
        );
        req.setAttribute(WebConstants.Loyalty.REDEMPTION, redemption);
    }
}
