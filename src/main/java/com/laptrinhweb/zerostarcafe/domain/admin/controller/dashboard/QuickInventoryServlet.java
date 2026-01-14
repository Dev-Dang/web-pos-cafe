package com.laptrinhweb.zerostarcafe.domain.admin.controller.dashboard;

import com.google.gson.Gson;
import com.laptrinhweb.zerostarcafe.domain.admin.dao.AdminDAO;
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

@WebServlet(name = "QuickInventoryServlet", value = "/admin/api/quick-inventory")
public class QuickInventoryServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();

        try {
            // 1. Lấy dữ liệu từ Form
            String productInput = request.getParameter("product");
            String quantityStr = request.getParameter("quantity");
            String type = request.getParameter("type");

            if (productInput == null || productInput.trim().isEmpty()) {
                throw new Exception("Vui lòng nhập tên hoặc mã sản phẩm.");
            }

            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                throw new Exception("Số lượng phải lớn hơn 0.");
            }

            HttpSession session = request.getSession();
            Object sessionObj = session.getAttribute("AUTH_USER");
            long userId = 1;

            if (sessionObj instanceof AuthUser) {
                userId = ((AuthUser) sessionObj).userId();
            }

            AdminDAO dao = new AdminDAO();
            boolean isImport = "import".equals(type);

            boolean success = dao.quickUpdateInventory(productInput, quantity, isImport, userId);

            if (success) {
                result.put("success", true);
                String actionText = isImport ? "Nhập kho" : "Xuất kho";
                result.put("message", actionText + " thành công cho: " + productInput);
            } else {
                result.put("success", false);
                result.put("message", "Không tìm thấy sản phẩm nào có tên hoặc ID là: " + productInput);
            }

        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "Số lượng không hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "Lỗi: " + e.getMessage());
        }

        Gson gson = new Gson();
        response.getWriter().write(gson.toJson(result));
    }
}