package com.laptrinhweb.zerostarcafe.web.admin.revenue;

import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.Revenue;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ShowRevenue", value = "/admin/revenue")
public class ShowRevenue extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminDAO dao = new AdminDAO();
        List<Revenue> monthlyList = dao.getMonthlyRevenue(1); // Store ID = 1

        // Lấy dữ liệu tháng gần nhất (phần tử đầu tiên)
        Revenue current = null;
        if (!monthlyList.isEmpty()) {
            current = monthlyList.get(0);
        } else {
            current = new Revenue(0, 0, 0, 0, 0);
        }

        long avgOrder = current.getTotalOrders() > 0 ? (current.getTotalRevenue() / current.getTotalOrders()) : 0;

        // Đẩy dữ liệu sang JSP
        request.setAttribute("monthlyList", monthlyList);

        // 4 Chỉ số cho Cards
        request.setAttribute("cardRevenue", current.getTotalRevenue());
        request.setAttribute("cardOrders", current.getTotalOrders());
        request.setAttribute("cardProducts", current.getTotalProducts()); // Mới
        request.setAttribute("cardAvg", avgOrder);

        request.setAttribute("cardGrowth", current.getGrowth());

        request.setAttribute("pageId", "revenue");
        request.setAttribute("pageContent", "/WEB-INF/views/admin/pages/revenue.jsp");
        request.getRequestDispatcher("/WEB-INF/views/admin/layouts/admin-layout.jsp").forward(request, response);
    }
}