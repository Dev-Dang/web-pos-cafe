package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.domain.store.model.Store;
import com.laptrinhweb.zerostarcafe.domain.store.model.StoreContext;
import com.laptrinhweb.zerostarcafe.domain.store.service.StoreService;
import com.laptrinhweb.zerostarcafe.web.client.mapper.ClientWebMapper;
import com.laptrinhweb.zerostarcafe.web.client.utils.StoreContextUtil;
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
 * @lastModified 02/12/2025
 * @since 1.0.0
 */
@WebServlet(name = "StoreCheckInServlet", urlPatterns = {"/store/check-in"})
public class StoreCheckInServlet extends HttpServlet {

    private final StoreService storeService = new StoreService();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        StoreContext storeCtx = ClientWebMapper.toStoreContext(req);
        if (storeCtx == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Store store = storeService.getActiveStoreById(storeCtx.getStoreId());
        if (store == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        StoreContextUtil.persist(req, resp, storeCtx);
        AppRoute.HOME.redirect(req, resp);
    }
}
