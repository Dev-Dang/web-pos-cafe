package com.laptrinhweb.zerostarcafe.web.client.servlet;

import com.laptrinhweb.zerostarcafe.web.common.WebConstants;
import com.laptrinhweb.zerostarcafe.web.common.routing.RouteMap;
import com.laptrinhweb.zerostarcafe.web.common.utils.RequestUtils;
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
 * @lastModified 08/01/2026
 * @since 1.0.0
 */
@WebServlet(name = "CartServlet", urlPatterns = {
        RouteMap.CART, RouteMap.CART_ADD, RouteMap.CART_PREVIEW, RouteMap.CART_MERGE})
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // TODO: Implement cart page rendering logic
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = RequestUtils.extractStringParam(req, WebConstants.Cart.CART_ACTION);

    }
}
