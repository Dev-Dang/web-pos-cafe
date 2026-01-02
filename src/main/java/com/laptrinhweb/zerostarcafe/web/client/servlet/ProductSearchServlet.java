package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.product.model.CatalogItem;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductConstants;
import com.laptrinhweb.zerostarcafe.domain.product.service.ProductService;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
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
 * <h2>Description:</h2>
 * <p>
 * Handles AJAX requests to search products by name for dynamic catalog filtering.
 * Returns HTML fragment (not full page) for seamless client-side updates.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * GET /api/products/search?q=coffee
 * Accept: text/html
 *
 * Response: 200 OK
 * Cache-Control: private, max-age=600
 * Content-Type: text/html; charset=UTF-8
 *
 * <div class="product-grid">...</div>
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/12/2025
 * @since 1.0.0
 */
@WebServlet(name = "ProductSearchServlet", urlPatterns = {"/api/products/search"})
public class ProductSearchServlet extends HttpServlet {

    private static final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Get search query parameter
            String searchTerm = req.getParameter("q");
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Search term is required");
                return;
            }

            searchTerm = searchTerm.trim();

            // Get current store from session
            HttpSession session = req.getSession();
            StoreContext storeCtx = (StoreContext) session.getAttribute(StoreConstants.Session.CURRENT_STORE_CTX);
            if (storeCtx == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No store context found");
                return;
            }

            // Search products by name
            List<CatalogItem> catalogItems = productService.searchCatalogItemsByNameAndStoreId(
                    storeCtx.getStoreId(),
                    searchTerm
            );

            // Set cache headers
            resp.setHeader("Cache-Control", "private, max-age=600");
            resp.setHeader("Vary", "Cookie");
            resp.setContentType("text/html; charset=UTF-8");

            // Set attribute and render fragment
            req.setAttribute(ProductConstants.Request.CATALOG_ITEMS, catalogItems);
            View.render(ViewMap.Client.PRODUCT_CATALOG, req, resp);

        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Search failed: " + e.getMessage());
        }
    }
}
