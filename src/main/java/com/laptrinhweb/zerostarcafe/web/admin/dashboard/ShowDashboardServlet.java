package com.laptrinhweb.zerostarcafe.web.admin.dashboard;

import com.google.gson.Gson;
import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.Overview;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@WebServlet(name = "ShowDashboardOverviewServlet", value = "/admin/api/dashboard-overview")
public class ShowDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            String dateParam = request.getParameter("date");

            if (dateParam == null || dateParam.isEmpty()) {
                dateParam = LocalDate.now().toString();
            }

            AdminDAO dao = new AdminDAO();
            Overview overviewData = dao.getDashboardOverview(dateParam);

            Gson gson = new Gson();
            String jsonResult = gson.toJson(overviewData);

            out.print(jsonResult);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
}