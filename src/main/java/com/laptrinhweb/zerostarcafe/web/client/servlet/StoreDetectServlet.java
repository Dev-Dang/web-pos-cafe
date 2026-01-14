package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.core.location.Location;
import com.laptrinhweb.zerostarcafe.domain.store.model.Store;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
import com.laptrinhweb.zerostarcafe.domain.store.service.StoreService;
import com.laptrinhweb.zerostarcafe.web.client.mapper.LocationMapper;
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
 * Detect nearest store from client location and persist store context.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 12/12/2025
 * @since 1.0.0
 */
@WebServlet(name = "StoreDetectServlet", urlPatterns = {"/store-detect"})
public class StoreDetectServlet extends HttpServlet {

    private final StoreService storeService = StoreService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get client location
        Location clientLoc = LocationMapper.from(req);
        if (clientLoc == null) {
            AppRoute.redirect(RouteMap.HOME, req, resp);
            return;
        }

        // Compute and persist nearest store
        Store store = storeService.resolveStoreByLocation(clientLoc);
        if (store != null) {
            StoreContext storeCtx = new StoreContext(store.getId(), null);
            StoreContextUtil.persist(req, resp, storeCtx);
        }

        AppRoute.redirect(RouteMap.HOME, req, resp);
    }
}