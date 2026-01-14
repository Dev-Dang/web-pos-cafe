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

@WebServlet(name = "CreateOrderServlet", value = "/admin/api/create-order")
public class CreateOrderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        Map<String, Object> result = new HashMap<>();
        try {
            long tableId = Long.parseLong(request.getParameter("tableId"));
            User admin = (User) request.getSession().getAttribute("AUTH_USER");
            long adminId = (admin != null) ? admin.getId() : 0;

            if (new AdminDAO().createOrder(tableId, "web", adminId)) {
                result.put("success", true);
                result.put("message", "Tạo đơn hàng mới thành công!");
            } else {
                result.put("success", false);
                result.put("message", "Lỗi khi tạo đơn.");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Lỗi: " + e.getMessage());
        }
        new Gson().toJson(result, response.getWriter());
    }
}