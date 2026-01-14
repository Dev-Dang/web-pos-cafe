package com.laptrinhweb.zerostarcafe.web.admin.dashboard;

import com.google.gson.Gson;
import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Year;
import java.util.List;

@WebServlet(name = "ChartDashboardServlet", value = "/admin/api/chart-data")
public class ChartDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            AdminDAO dao = new AdminDAO();

            int currentYear = Year.now().getValue();

            List<Long> data = dao.getRevenueLast12Months(currentYear);

            Gson gson = new Gson();
            out.print(gson.toJson(data));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}