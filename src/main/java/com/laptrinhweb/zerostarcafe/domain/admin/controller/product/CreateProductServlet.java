package com.laptrinhweb.zerostarcafe.domain.admin.controller.product;

import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.Product;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet(name = "CreateProductServlet", value = "/admin/api/create-product")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class CreateProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            HttpSession session = request.getSession();
            Object sessionObj = session.getAttribute("AUTH_USER");
            long userId = 1;

            if (sessionObj instanceof AuthUser) {
                AuthUser currentUser = (AuthUser) sessionObj;
                userId = currentUser.userId();
            }

            String name = request.getParameter("name");
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            double price = Double.parseDouble(request.getParameter("price"));
            double inventory = Double.parseDouble(request.getParameter("inventory"));
            String unit = request.getParameter("unit");

            String finalImageName = null;
            Part filePart = request.getPart("new_image");

            if (filePart != null && filePart.getSize() > 0 && filePart.getSubmittedFileName() != null && !filePart.getSubmittedFileName().isEmpty()) {

                System.out.println("DEBUG CREATE: Có file ảnh được upload: " + filePart.getSubmittedFileName());

                String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                finalImageName = "product_" + System.currentTimeMillis() + "_" + originalFileName;

                String buildPath = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "client" + File.separator + "img" + File.separator + "product";
                File buildDir = new File(buildPath);
                if (!buildDir.exists()) buildDir.mkdirs();

                filePart.write(buildPath + File.separator + finalImageName);

                String sourcePath = "D:\\For University\\University\\Year4Semester7\\Web-Programming\\Project\\web-pos-cafe\\src\\main\\webapp\\assets\\client\\img\\product";
                File sourceDir = new File(sourcePath);
                if (!sourceDir.exists()) sourceDir.mkdirs();

                File fileInBuild = new File(buildPath + File.separator + finalImageName);
                File fileInSource = new File(sourcePath + File.separator + finalImageName);

                Files.copy(fileInBuild.toPath(), fileInSource.toPath(), StandardCopyOption.REPLACE_EXISTING);

                System.out.println("DEBUG CREATE: Đã backup ảnh sang: " + fileInSource.getAbsolutePath());
            } else {
                finalImageName = null;
            }

            Product product = new Product();
            product.setName(name);
            product.setCategoryId(categoryId);
            product.setPrice((int) price);
            product.setInventory(inventory);
            product.setUnit(unit);
            product.setPicUrl(finalImageName);
            product.setActive(true);

            AdminDAO dao = new AdminDAO();
            boolean success = dao.createProduct(product, userId);

            if (success) {
                request.getSession().setAttribute("message", "Thêm mới sản phẩm <strong>" + name + "</strong> thành công!");
                request.getSession().setAttribute("messageType", "success");
            } else {
                request.getSession().setAttribute("message", "Thêm mới thất bại! Vui lòng thử lại.");
                request.getSession().setAttribute("messageType", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi hệ thống: " + e.getMessage());
            request.getSession().setAttribute("messageType", "error");
        }

        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
}