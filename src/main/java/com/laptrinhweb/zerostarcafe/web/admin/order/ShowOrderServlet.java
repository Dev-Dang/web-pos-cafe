package com.laptrinhweb.zerostarcafe.web.admin.order;

import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ShowOrderServlet", value = "/admin/orders")
public class ShowOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminDAO dao = new AdminDAO();
        List<Order> orders = dao.getAllOrders(1);

        request.setAttribute("ordersList", orders);
        request.setAttribute("pageId", "order");
        request.setAttribute("pageContent", "/WEB-INF/views/admin/pages/orders.jsp");
        try {
            request.setAttribute("productsList", dao.getAllProductsByStore(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.getRequestDispatcher("/WEB-INF/views/admin/layouts/admin-layout.jsp").forward(request, response);
    }
}