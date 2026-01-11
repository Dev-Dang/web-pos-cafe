package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.core.location.Location;
import com.laptrinhweb.zerostarcafe.domain.store.model.Store;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
import com.laptrinhweb.zerostarcafe.domain.store.service.StoreService;
import com.laptrinhweb.zerostarcafe.web.client.utils.StoreContextUtil;
import com.laptrinhweb.zerostarcafe.web.common.mapper.LocationMapper;
import com.laptrinhweb.zerostarcafe.web.common.routing.AppRoute;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <h2>Description:</h2>
 * <p>
 *
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
 * @lastModified 12/12/2025
 * @since 1.0.0
 */
@WebServlet(name = "StoreDetectServlet", urlPatterns = {"/store-detect"})
public class StoreDetectServlet extends HttpServlet {

    private final StoreService storeService = new StoreService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Location clientLoc = LocationMapper.from(req);
        if (clientLoc == null) {
            AppRoute.HOME.redirect(req, resp);
            return;
        }

        Store store = storeService.findNearestStore(clientLoc);
        if (store != null) {
            StoreContext storeCtx = new StoreContext(store.getId(), null);
            StoreContextUtil.persist(req, resp, storeCtx);
        }

        AppRoute.HOME.redirect(req, resp);
    }
}
