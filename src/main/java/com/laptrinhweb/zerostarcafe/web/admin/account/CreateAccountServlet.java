package com.laptrinhweb.zerostarcafe.web.admin.account;

import com.google.gson.Gson;
import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.User;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "CreateAccountServlet", value = "/admin/api/create-account")
public class CreateAccountServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        Map<String, Object> result = new HashMap<>();
        try {
            User user = new User();
            user.setUsername(request.getParameter("fullname"));
            user.setEmail(request.getParameter("email"));
            user.setPasswordHash(request.getParameter("password"));

            String role = request.getParameter("role");
            user.setSuperAdmin("admin".equals(role));

            AdminDAO dao = new AdminDAO();
            HttpSession session = request.getSession();
            Object sessionObj = session.getAttribute("AUTH_USER");
            long adminId = 1;
            if (sessionObj instanceof AuthUser) {
                adminId = ((AuthUser) sessionObj).getId();
            }
            if (dao.createAccount(user, adminId)) {
                result.put("success", true);
                result.put("message", "Tạo tài khoản thành công!");
            } else {
                result.put("success", false);
                result.put("message", "Thất bại (Email có thể đã tồn tại).");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Lỗi: " + e.getMessage());
        }
        new Gson().toJson(result, response.getWriter());
    }
}