package com.laptrinhweb.zerostarcafe.web.admin.log;

import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.Log;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ShowLogServlet", value = "/admin/logs")
public class ShowLogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        AdminDAO dao = new AdminDAO();
        List<Log> logs = dao.getSystemLogs();

        request.setAttribute("logsList", logs);
        request.setAttribute("pageId", "log");
        request.setAttribute("pageContent", "/WEB-INF/views/admin/pages/logs.jsp");

        request.getRequestDispatcher("/WEB-INF/views/admin/layouts/admin-layout.jsp").forward(request, response);
    }
}