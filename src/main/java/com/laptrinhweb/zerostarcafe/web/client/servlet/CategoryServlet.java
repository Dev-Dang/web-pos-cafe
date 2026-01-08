package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.product.dto.ProductCardDTO;
import com.laptrinhweb.zerostarcafe.domain.product.service.ProductService;
import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
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
import java.util.List;

/**
 * Handles category-based product filtering using slug-based operations.
 * Simplified logic that works directly with slugs without ID conversion.
 * Returns either full page or partial fragments based on request type.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 07/01/2026
 * @since 1.0.0
 */
@WebServlet(name = "CategoryServlet", urlPatterns = {RouteMap.CATEGORY_PRODUCTS + "/*"})
public class CategoryServlet extends HttpServlet {

    private static final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get category slug from path
        String categorySlug = RequestUtils
                .extractStringParam(req, WebConstants.Param.CATEGORY_SLUG);
        if (categorySlug == null) {
            AppRoute.sendError(HttpServletResponse.SC_BAD_REQUEST, resp);
            return;
        }

        // Perform search based on category slug
        Long storeId = RequestUtils.getStoreIdFromSession(req);
        List<ProductCardDTO> productCards = productService
                .getProductsByCategorySlug(categorySlug, storeId);
        req.setAttribute(WebConstants.Attribute.PRODUCT_CARDS, productCards);
        req.setAttribute(WebConstants.Attribute.SELECTED_CATEGORY, categorySlug);

        // Handle partial request
        if (RequestUtils.isPartialRequest(req)) {
            View.render(ViewMap.Client.PRODUCT_SECTION, req, resp);
            return;
        }

        // Handle full page load
        AppRoute.forward(RouteMap.HOME, req, resp);
    }
}