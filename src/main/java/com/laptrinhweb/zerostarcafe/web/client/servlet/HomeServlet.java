package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.category.Category;
import com.laptrinhweb.zerostarcafe.domain.category.CategoryService;
import com.laptrinhweb.zerostarcafe.domain.product.dto.ProductCardDTO;
import com.laptrinhweb.zerostarcafe.domain.product.service.ProductService;
import com.laptrinhweb.zerostarcafe.domain.store.model.Store;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.service.StoreService;
import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
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
 * Render the client home page with store, categories, and featured products.
 * Enhanced to support slug-based category selection with pagination for performance.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 07/01/2026
 * @since 1.0.0
 */
@WebServlet(name = "HomeServlet", urlPatterns = {RouteMap.HOME})
public class HomeServlet extends HttpServlet {

    // Services
    private static final StoreService storeService = StoreService.getInstance();
    private static final CategoryService categoryService = CategoryService.getInstance();
    private static final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Load all active stores for store selector (localized via LocaleContext)
        List<Store> stores = storeService.getAllActiveStores();
        req.setAttribute(StoreConstants.Attribute.STORE_LIST, stores);

        // Get current store from session context
        Long currentStoreId = RequestUtils.getStoreIdFromSession(req);
        Store currentStore = null;
        for (Store store : stores) {
            if (store.getId() == currentStoreId)
                currentStore = store;
        }
        req.setAttribute(StoreConstants.Attribute.CURRENT_STORE, currentStore);

        // Load active categories for navigation
        List<Category> categories = categoryService.loadActiveCategories();
        req.setAttribute(StoreConstants.Attribute.CATEGORIES, categories);

        // Load product cards
        @SuppressWarnings("unchecked")
        List<ProductCardDTO> productCards = (List<ProductCardDTO>)
                req.getAttribute(WebConstants.Attribute.PRODUCT_CARDS);

        if (productCards == null || productCards.isEmpty()) {
            loadDefaultProducts(req, currentStoreId, categories);
        }

        // Render the home view
        View.render(ViewMap.Client.HOME, req, resp);
    }

    private void loadDefaultProducts(HttpServletRequest req, Long storeId, List<Category> categories) {
        Category defaultCategory = categories.getFirst();
        List<ProductCardDTO> productCards = productService
                .getProductsByCategoryId(defaultCategory.getId(), storeId);
        req.setAttribute(WebConstants.Attribute.PRODUCT_CARDS, productCards);
        req.setAttribute(WebConstants.Attribute.SELECTED_CATEGORY, defaultCategory.getSlug());
    }
}