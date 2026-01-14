package com.laptrinhweb.zerostarcafe.web.admin.order;

import com.google.gson.Gson;
import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.OrderDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderDetailAPI", value = "/admin/api/order-details")
public class OrderDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            long orderId = Long.parseLong(request.getParameter("id"));
            AdminDAO dao = new AdminDAO();
            List<OrderDetail> details = dao.getOrderItemsDetail(orderId);
            new Gson().toJson(details, response.getWriter());
        } catch (Exception e) {
            response.setStatus(500);
        }
    }
}