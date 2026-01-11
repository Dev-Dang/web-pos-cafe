package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.category.Category;
import com.laptrinhweb.zerostarcafe.domain.category.CategoryService;
import com.laptrinhweb.zerostarcafe.domain.store.model.Store;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreConstants;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
import com.laptrinhweb.zerostarcafe.domain.store.service.StoreService;
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Store> stores = storeService.getAllActiveStores();
        req.setAttribute(StoreConstants.Request.STORE_LIST, stores);

        HttpSession session = req.getSession();
        StoreContext storeCtx = (StoreContext) session.getAttribute(StoreConstants.Session.CURRENT_STORE_CTX);
        Store currentStore = storeService.getActiveStoreById(storeCtx.getStoreId());
        req.setAttribute(StoreConstants.Request.CURRENT_STORE, currentStore);

        List<Category> categories = categoryService.loadActiveCategories();
        req.setAttribute(StoreConstants.Request.CATEGORIES, categories);

        View.render(ViewMap.Client.HOME, req, resp);
    }
}
