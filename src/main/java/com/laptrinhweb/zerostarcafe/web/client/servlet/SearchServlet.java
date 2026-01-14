package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.product.dto.ProductCardDTO;
import com.laptrinhweb.zerostarcafe.domain.product.service.ProductService;
import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
import com.laptrinhweb.zerostarcafe.web.common.response.RespContext;
import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import com.laptrinhweb.zerostarcafe.web.common.routing.RouteMap;
import com.laptrinhweb.zerostarcafe.web.common.utils.PaginationContext;
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
 * Handles product search requests with Unpoly autosubmit integration.
 * Returns JSP fragments for dynamic UI updates or full page for direct access.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 07/01/2026
 * @since 1.0.0
 */
@WebServlet(name = "SearchServlet", urlPatterns = {RouteMap.SEARCH})
public class SearchServlet extends HttpServlet {

    private static final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get search parameters
        String query = req.getParameter(WebConstants.Param.SEARCH_KEYWORD);
        if (query == null || query.isBlank()) {
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        // Get current store from session
        Long storeId = RequestUtils.getStoreIdFromSession(req);
        Integer page = RequestUtils.getIntParam(req, WebConstants.Param.PAGE);
        int PAGE_SIZE = WebConstants.Default.PAGE_SIZE;

        // Calculate offset for pagination
        if (page == null || page < 1)
            page = 1;
        int offset = (page - 1) * PAGE_SIZE;

        // Perform product search
        List<ProductCardDTO> productCards = productService
                .searchProducts(query, storeId, PAGE_SIZE, offset);

        // Determine if there are more results for pagination
        int resultCount = productService.countSearchResults(query, storeId);
        boolean isHasMore = (offset + productCards.size()) < resultCount;
        PaginationContext context = PaginationContext.search(query, page, isHasMore, resultCount);

        // Set response context data
        RespContext.from(req)
                .setData(WebConstants.Attribute.PRODUCT_CARDS, productCards)
                .setPaginationData(context);

        // Handle partial request for search results with pagination
        if (RequestUtils.isPartialRequest(req)) {
            View.render(ViewMap.Client.PRODUCT_SECTION, req, resp);
            return;
        }

        // For full page requests, forward to home with search results
        AppRoute.forward(RouteMap.HOME, req, resp);
    }
}