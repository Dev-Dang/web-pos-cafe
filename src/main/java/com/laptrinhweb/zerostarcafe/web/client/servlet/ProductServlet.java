package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.product.dto.ProductDetailDTO;
import com.laptrinhweb.zerostarcafe.domain.product.service.ProductService;
import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
import com.laptrinhweb.zerostarcafe.web.common.response.RespContext;
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
 * Handles product detail requests for Unpoly frontend integration.
 * Returns JSP fragments for product modal display.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 07/01/2026
 * @since 1.0.0
 */
@WebServlet(name = "ProductServlet", urlPatterns = {RouteMap.PRODUCTS + "/*"})
public class ProductServlet extends HttpServlet {

    private static final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Extract product slug from request path
        String productSlug = RequestUtils.
                extractStringParam(req, WebConstants.Param.PRODUCT_SLUG);

        if (productSlug == null) {
            AppRoute.sendError(HttpServletResponse.SC_BAD_REQUEST, resp);
            return;
        }

        // Retrieve product details
        Long storeId = RequestUtils.getStoreIdFromSession(req);
        ProductDetailDTO productDetail = productService.getProductBySlug(productSlug, storeId);

        if (productDetail == null) {
            AppRoute.sendError(HttpServletResponse.SC_NOT_FOUND, resp);
            return;
        }

        req.setAttribute(WebConstants.Attribute.PRODUCT_DETAIL, productDetail);

        // If partial request, render product modal fragment and return
        if (RequestUtils.isPartialRequest(req) && productDetail.isAvailable()) {
            View.render(ViewMap.Client.PRODUCT_MODAL, req, resp);
            return;
        }

        // Set auto-open modal flag in response context
        if (productDetail.isAvailable()) {
            RespContext.from(req).setData(
                    WebConstants.Flag.RE_OPEN_MODAL,
                    WebConstants.Attribute.PRODUCT_MODAL
            );
        }

        // Forward to home page to render full page with modal
        AppRoute.forward(RouteMap.HOME, req, resp);
    }
}