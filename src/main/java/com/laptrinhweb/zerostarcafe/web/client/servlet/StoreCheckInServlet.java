package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.store.model.Store;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
import com.laptrinhweb.zerostarcafe.domain.store.service.StoreService;
import com.laptrinhweb.zerostarcafe.web.client.mapper.StoreCtxMapper;
import com.laptrinhweb.zerostarcafe.web.client.utils.StoreContextUtil;
import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import com.laptrinhweb.zerostarcafe.web.common.routing.RouteMap;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Validate store check-in and persist store context. Redirects to home.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 02/12/2025
 * @since 1.0.0
 */
@WebServlet(name = "StoreCheckInServlet", urlPatterns = {"/store/check-in"})
public class StoreCheckInServlet extends HttpServlet {

    private final StoreService storeService = StoreService.getInstance();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get current store context
        StoreContext storeCtx = StoreCtxMapper.toStoreContext(req);
        if (storeCtx == null) {
            AppRoute.sendError(HttpServletResponse.SC_NOT_FOUND, resp);
            return;
        }

        // Validate store check-in
        Store store = storeService.getActiveStoreById(storeCtx.getStoreId());
        if (store == null) {
            AppRoute.sendError(HttpServletResponse.SC_NOT_FOUND, resp);
            return;
        }

        // Persist store context
        StoreContextUtil.persist(req, resp, storeCtx);

        AppRoute.redirect(RouteMap.HOME, req, resp);
    }
}
