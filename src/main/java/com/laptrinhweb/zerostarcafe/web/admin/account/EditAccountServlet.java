package com.laptrinhweb.zerostarcafe.web.admin.account;

import com.google.gson.Gson;
import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "EditAccountServlet", value = "/admin/api/edit-account")
public class EditAccountServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        Map<String, Object> result = new HashMap<>();
        try {
            User user = new User();
            user.setId(Long.parseLong(request.getParameter("id")));
            user.setUsername(request.getParameter("username"));
            user.setEmail(request.getParameter("email"));

            String role = request.getParameter("role");
            user.setSuperAdmin("admin".equals(role));

            AdminDAO dao = new AdminDAO();
            if (dao.updateAccount(user)) {
                result.put("success", true);
                result.put("message", "Cập nhật thành công!");
            } else {
                result.put("success", false);
                result.put("message", "Cập nhật thất bại.");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Lỗi: " + e.getMessage());
        }
        new Gson().toJson(result, response.getWriter());
    }
}