package com.laptrinhweb.zerostarcafe.web.admin.dashboard;

import com.google.gson.Gson;
import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.DashboardHandleOrder;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "LiveOrderServlet", value = "/admin/api/live-orders")
public class LiveOrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        AdminDAO dao = new AdminDAO();
        List<DashboardHandleOrder> liveOrders = dao.getLiveOrders();

        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(liveOrders));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String idStr = request.getParameter("id");

        HttpSession session = request.getSession();

        Object sessionObj = session.getAttribute("AUTH_USER");

        long userId = 1;

        if (sessionObj instanceof AuthUser) {
            AuthUser currentUser = (AuthUser) sessionObj;
            userId = currentUser.getId();
        }

        Map<String, Object> result = new HashMap<>();
        AdminDAO dao = new AdminDAO();

        try {
            long orderId = Long.parseLong(idStr);
            String newStatus = "";

            if ("confirm".equals(action)) {
                newStatus = "accept";
            } else if ("cancel".equals(action)) {
                newStatus = "cancel";
            } else if ("pay".equals(action)) {
                newStatus = "paid";
            } else {
                throw new Exception("Invalid action");
            }

            boolean success = dao.updateOrderStatus(orderId, newStatus, userId);
            result.put("success", success);
            if (success) {
                result.put("message", "Cập nhật trạng thái thành công!");
            } else {
                result.put("message", "Cập nhật thất bại.");
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Lỗi: " + e.getMessage());
        }

        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(result));
    }
}