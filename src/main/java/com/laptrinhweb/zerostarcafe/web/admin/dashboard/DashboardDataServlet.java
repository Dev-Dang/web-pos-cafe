package com.laptrinhweb.zerostarcafe.web.admin.dashboard;

import com.google.gson.Gson;
import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "DashboardDataServlet", value = "/admin/api/dashboard-extra")
public class DashboardDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        AdminDAO dao = new AdminDAO();
        Map<String, Object> data = new HashMap<>();

        try {
            data.put("lowStock", dao.getLowStockProducts(5));

            data.put("bestSellers", dao.getBestSellers(5));

            data.put("activities", dao.getRecentActivities(10));

            data.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            data.put("success", false);
            data.put("message", e.getMessage());
        }

        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(data));
    }
}