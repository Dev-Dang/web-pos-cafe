package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.category.Category;
import com.laptrinhweb.zerostarcafe.domain.category.CategoryService;
import com.laptrinhweb.zerostarcafe.domain.product.model.CatalogItem;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductConstants;
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
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Handles AJAX requests to fetch products by category for dynamic catalog switching.
 * Returns HTML fragment (not full page) for seamless client-side updates.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * GET /api/products/category/1
 * Accept: text/html
 *
 * Response: 200 OK
 * Cache-Control: public, max-age=1800
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
@WebServlet(name = "CategoryProductServlet", urlPatterns = {"/api/products/category/*"})
public class CategoryProductServlet extends HttpServlet {

    private static final ProductService productService = new ProductService();
    private static final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // Extract category SLUG from path (SEO-friendly)
            String categorySlug = RequestUtils.extractStringParam(req, "Category slug");

            // Get category by slug
            Category category = categoryService.getCategoryBySlug(categorySlug);
            if (category == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Category not found: " + categorySlug);
                return;
            }

            // Get current store from session
            HttpSession session = req.getSession();
            StoreContext storeCtx = (StoreContext) session.getAttribute(StoreConstants.Session.CURRENT_STORE_CTX);
            if (storeCtx == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No store context found");
                return;
            }

            // Fetch products for the category and store (filters out sold-out items)
            List<CatalogItem> catalogItems = productService.getCatalogItemsByStoreIdAndCategoryId(
                    storeCtx.getStoreId(),
                    category.getId()
            );

            // Set cache headers with Vary to prevent wrong store data
            resp.setHeader("Cache-Control", "private, max-age=1800"); // private = per-user  
            resp.setHeader("Vary", "Cookie"); // Cache per cookie state
            resp.setContentType("text/html; charset=UTF-8");

            // Set attribute and render fragment
            req.setAttribute(ProductConstants.Request.CATALOG_ITEMS, catalogItems);
            View.render(ViewMap.Client.PRODUCT_CATALOG, req, resp);

        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}

