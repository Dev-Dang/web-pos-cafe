package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.product.model.ProductConstants;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductDetail;
import com.laptrinhweb.zerostarcafe.domain.product.service.ProductService;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
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
import java.util.Objects;

/**
 * <h2>Description:</h2>
 * <p>
 * Handles AJAX requests to fetch complete product details including options
 * for the product modal. Implements ETag caching for efficient revalidation.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * GET /api/products/1
 * Accept: text/html
 * If-None-Match: "a3c5f"
 *
 * Response: 304 Not Modified (if ETag matches)
 * OR
 * Response: 200 OK
 * Cache-Control: public, max-age=3600
 * ETag: "a3c5f"
 * Content-Type: text/html; charset=UTF-8
 *
 * <div class="modal fade" id="productModal">...</div>
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/12/2025
 * @since 1.0.0
 */
@WebServlet(name = "ProductModalServlet", urlPatterns = {"/api/products/*"})
public class ProductModalServlet extends HttpServlet {

    private static final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Extract product SLUG from path (SEO-friendly)
            String productSlug = RequestUtils.extractStringParam(req, "Product slug");

            // Get current store from session
            HttpSession session = req.getSession();
            StoreContext storeCtx = (StoreContext) session.getAttribute(StoreConstants.Session.CURRENT_STORE_CTX);
            if (storeCtx == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No store context found");
                return;
            }

            // Fetch full product details by slug
            ProductDetail productDetail = productService.getProductDetailBySlugAndStoreId(productSlug, storeCtx.getStoreId());
            if (productDetail == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found: " + productSlug);
                return;
            }

            // Generate ETag from product data INCLUDING store ID
            String serverETag = generateETag(productDetail, storeCtx.getStoreId());
            String clientETag = req.getHeader("If-None-Match");

            // Check if client has cached version
            if (clientETag != null && clientETag.equals("\"" + serverETag + "\"")) {
                resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                resp.setHeader("ETag", "\"" + serverETag + "\"");
                return;
            }

            // Set cache headers with Vary to prevent wrong store data
            resp.setHeader("Cache-Control", "private, max-age=3600"); // private = per-user
            resp.setHeader("ETag", "\"" + serverETag + "\"");
            resp.setHeader("Vary", "Cookie"); // Cache per cookie state
            resp.setContentType("text/html; charset=UTF-8");

            // Set attribute and render fragment
            req.setAttribute(ProductConstants.Request.PRODUCT_DETAIL, productDetail);
            View.render(ViewMap.Client.PRODUCT_DETAIL, req, resp);

        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Generates ETag based on product ID, price, options, AND store ID.
     * This ensures different stores get different cache entries.
     *
     * @param productDetail the product detail to generate ETag for
     * @param storeId the store ID
     * @return ETag string (hexadecimal hash)
     */
    private String generateETag(ProductDetail productDetail, Long storeId) {
        int hash = Objects.hash(
                productDetail.getItem().getId(),
                productDetail.getItem().getResolvedPrice(),
                productDetail.getItem().getBasePrice(),
                productDetail.getOptionGroups().size(),
                storeId // Include store ID in cache key!
        );
        return Integer.toHexString(hash);
    }
}

