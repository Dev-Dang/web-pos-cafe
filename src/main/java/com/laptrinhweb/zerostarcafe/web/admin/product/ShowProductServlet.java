package com.laptrinhweb.zerostarcafe.web.admin.product;

import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.Category;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ShowProductServlet", value = "/admin/products")
public class ShowProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminDAO dao = new AdminDAO();
        try {
            List<Product> productsList = dao.getAllProductsByStore(1);
            request.setAttribute("productsList", productsList);
            List<Category> categoriesList = dao.getAllCategories();
            request.setAttribute("categoriesList", categoriesList);
            request.setAttribute("pageId", "product");

            request.setAttribute("pageContent", "/WEB-INF/views/admin/pages/products.jsp");
            request.getRequestDispatcher("/WEB-INF/views/admin/layouts/admin-layout.jsp")
                    .forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}