package com.laptrinhweb.zerostarcafe.web.admin.order;

import com.google.gson.Gson;
import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "DeleteOrderServlet", value = "/admin/api/delete-order")
public class DeleteOrderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        Map<String, Object> result = new HashMap<>();
        try {
            long orderId = Long.parseLong(request.getParameter("id"));
            User admin = (User) request.getSession().getAttribute("AUTH_USER");
            long adminId = (admin != null) ? admin.getId() : 0;

            if (new AdminDAO().deleteOrder(orderId, adminId)) {
                result.put("success", true);
                result.put("message", "Đã xóa đơn hàng!");
            } else {
                result.put("success", false);
                result.put("message", "Không thể xóa.");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Lỗi: " + e.getMessage());
        }
        new Gson().toJson(result, response.getWriter());
    }
}