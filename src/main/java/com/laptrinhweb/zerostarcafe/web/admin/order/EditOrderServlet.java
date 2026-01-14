package com.laptrinhweb.zerostarcafe.web.admin.order;

import com.google.gson.Gson;
import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "EditOrderServlet", value = "/admin/api/edit-order")
public class EditOrderServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        Map<String, Object> result = new HashMap<>();

        try {
            HttpSession session = request.getSession();
            User admin = (User) session.getAttribute("AUTH_USER");
            long adminId = (admin != null) ? admin.getId() : 0;

            long orderId = Long.parseLong(request.getParameter("id"));
            String status = request.getParameter("status");
            long tableId = Long.parseLong(request.getParameter("tableId"));

            AdminDAO dao = new AdminDAO();
            if (dao.updateOrderInfo(orderId, status, tableId, adminId)) {
                result.put("success", true);
                result.put("message", "Cập nhật đơn hàng thành công!");
            } else {
                result.put("success", false);
                result.put("message", "Lỗi cập nhật.");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Lỗi: " + e.getMessage());
        }
        new Gson().toJson(result, response.getWriter());
    }
}