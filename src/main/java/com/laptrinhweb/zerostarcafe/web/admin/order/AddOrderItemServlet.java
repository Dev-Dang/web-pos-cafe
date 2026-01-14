package com.laptrinhweb.zerostarcafe.web.admin.order;

import com.google.gson.Gson;
import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AddOrderItemServlet", value = "/admin/api/add-order-item")
public class AddOrderItemServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        Map<String, Object> result = new HashMap<>();

        try {
            long orderId = Long.parseLong(request.getParameter("orderId"));
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (new AdminDAO().addOrderItem(orderId, productId, quantity)) {
                result.put("success", true);
                result.put("message", "Đã thêm món thành công!");
            } else {
                result.put("success", false);
                result.put("message", "Lỗi khi thêm món.");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Lỗi: " + e.getMessage());
        }
        new Gson().toJson(result, response.getWriter());
    }
}