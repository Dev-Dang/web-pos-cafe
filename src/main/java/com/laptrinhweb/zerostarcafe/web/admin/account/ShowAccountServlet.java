package com.laptrinhweb.zerostarcafe.web.admin.account;

import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ShowAccountServlet", value = "/admin/accounts")
public class ShowAccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminDAO dao = new AdminDAO();
        request.setAttribute("accountsList", dao.getAllAccounts());
        request.setAttribute("pageId", "account");
        request.setAttribute("pageContent", "/WEB-INF/views/admin/pages/accounts.jsp");
        request.getRequestDispatcher("/WEB-INF/views/admin/layouts/admin-layout.jsp").forward(request, response);
    }
}