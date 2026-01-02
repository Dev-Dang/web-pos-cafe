package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.cart.model.Cart;
import com.laptrinhweb.zerostarcafe.domain.cart.service.CartCacheService;
import com.laptrinhweb.zerostarcafe.domain.category.Category;
import com.laptrinhweb.zerostarcafe.domain.category.CategoryService;
import com.laptrinhweb.zerostarcafe.domain.product.model.CatalogItem;
import com.laptrinhweb.zerostarcafe.domain.product.model.ProductConstants;
import com.laptrinhweb.zerostarcafe.domain.product.service.ProductService;
import com.laptrinhweb.zerostarcafe.domain.store.model.Store;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
import com.laptrinhweb.zerostarcafe.domain.store.service.StoreService;
import com.laptrinhweb.zerostarcafe.web.auth.session.AuthSessionManager;
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
 * \
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ... code here
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 11/12/2025
 * @since 1.0.0
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    private static final StoreService storeService = new StoreService();
    private static final CategoryService categoryService = new CategoryService();
    private static final ProductService productService = new ProductService();
    private static final CartCacheService cartCacheService = CartCacheService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Load all active stores for store selector
        List<Store> stores = storeService.getAllActiveStores();
        req.setAttribute(StoreConstants.Request.STORE_LIST, stores);

        // Get current store from session context
        HttpSession session = req.getSession();
        StoreContext storeCtx = (StoreContext) session.getAttribute(StoreConstants.Session.CURRENT_STORE_CTX);
        Store currentStore = null;
        for (Store store : stores) {
            if (store.getId() == storeCtx.getStoreId())
                currentStore = store;
        }
        req.setAttribute(StoreConstants.Request.CURRENT_STORE, currentStore);

        // Load active categories for navigation
        List<Category> categories = categoryService.loadActiveCategories();
        req.setAttribute(StoreConstants.Request.CATEGORIES, categories);

        // Load ONLY featured category products (first category by order_index)
        if (!categories.isEmpty()) {
            Category featuredCategory = categories.get(0); // First category is featured
            List<CatalogItem> catalogItems = productService.getCatalogItemsByStoreIdAndCategoryId(
                    storeCtx.getStoreId(),
                    featuredCategory.getId()
            );
            req.setAttribute(ProductConstants.Request.CATALOG_ITEMS, catalogItems);
        }

        // Load cart for logged-in users (SSR)
        AuthSessionManager sessionManager = (AuthSessionManager) getServletContext()
                .getAttribute("authSessionManager");
        if (sessionManager != null) {
            AuthContext authCtx = sessionManager.getContext(req);
            if (authCtx != null && authCtx.isValid()) {
                Cart cart = cartCacheService.getCart(
                        authCtx.getAuthUser().getId(),
                        storeCtx.getStoreId()
                );
                req.setAttribute("cart", cart);
            }
        }

        View.render(ViewMap.Client.HOME, req, resp);
    }
}
