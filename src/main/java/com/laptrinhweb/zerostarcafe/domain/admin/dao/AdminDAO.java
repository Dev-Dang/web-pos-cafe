package com.laptrinhweb.zerostarcafe.domain.admin.dao;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.domain.admin.dto.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    //Connection
    // Thay đổi method này trong AdminDAO
    public static Connection getConnection() throws SQLException {
        // Gọi sang class DBConnection để lấy kết nối từ Pool của Tomcat
        return DBConnection.getConnection();
    }

    //Get all products by store
    public List<Product> getAllProductsByStore(int storeId) throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT mi.*, "
                     + "JSON_UNQUOTE(JSON_EXTRACT(mi.name, ?)) as pName ,smi.inventory, JSON_UNQUOTE(JSON_EXTRACT(c.name, ?)) as cat_name "
                     + "FROM menu_items mi "
                     + "JOIN store_menu_items smi ON mi.id = smi.menu_item_id "
                     + "LEFT JOIN categories c ON mi.category_id = c.id "
                     + "WHERE smi.store_id = ? "
                     + "ORDER BY mi.id ASC";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String jsonPath = "$." + "vi";
            ps.setString(1, jsonPath);
            ps.setString(2, jsonPath);
            ps.setInt(3, storeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();

                    p.setId(rs.getInt("id"));
                    p.setPicUrl(rs.getString("image_url"));
                    p.setName(rs.getString("pName"));
                    p.setPrice(rs.getInt("base_price"));
                    p.setUnit(rs.getString("unit"));
                    p.setInventory(rs.getDouble("inventory"));
                    p.setActive(rs.getBoolean("is_active"));
                    p.setCategoryId(rs.getInt("category_id"));
                    p.setCategoryName(rs.getString("cat_name"));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //Update product
    public boolean updateProduct(Product p, long storeId, long userID) throws SQLException {
        boolean rowUpdated = false;

        String sqlMenuItem = "UPDATE menu_items SET category_id = ?, name = ?, description = ?, base_price = ?, unit = ?, image_url = ?, is_active = ? WHERE id = ?";

        String sqlStoreItem = "INSERT INTO store_menu_items (store_id, menu_item_id, inventory) VALUES (?, ?, ?) "
                              + "ON DUPLICATE KEY UPDATE inventory = ?";

        try (Connection conn = getConnection()) {
            if (conn == null) return false;

            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlMenuItem);
                 PreparedStatement ps2 = conn.prepareStatement(sqlStoreItem)) {

                ps1.setInt(1, p.getCategoryId());
                ps1.setString(2, p.getName());
                ps1.setString(3, p.getDescription());
                ps1.setDouble(4, p.getPrice());
                ps1.setString(5, p.getUnit());
                ps1.setString(6, p.getPicUrl());
                ps1.setBoolean(7, p.isActive());
                ps1.setInt(8, p.getId());

                int result1 = ps1.executeUpdate();

                ps2.setLong(1, storeId);
                ps2.setInt(2, p.getId());
                ps2.setDouble(3, p.getInventory());
                ps2.setDouble(4, p.getInventory());

                int result2 = ps2.executeUpdate();

                conn.commit();

                rowUpdated = result1 > 0 || result2 > 0;

                if (rowUpdated) {
                    LogDAO.log(userID, "UPDATE", "PRODUCT", (long) p.getId(),
                            "Cập nhật món ID: " + p.getId());
                }

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
        return rowUpdated;
    }

    //Update product hide status
    public boolean updateProductHideStatus(int id, boolean isActive, long userID) {
        String sql = "UPDATE store_menu_items SET in_menu = ? WHERE menu_item_id = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, isActive);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                String statusText = isActive ? "Hiện" : "Ẩn";
                LogDAO.log(userID, "UPDATE_STATUS", "PRODUCT", (long) id, statusText + " sản phẩm ID: " + id);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //Delete product
    public boolean deleteProduct(int id, int storeID, long userID) throws SQLException {
        String sql = "DELETE FROM store_menu_items WHERE menu_item_id = ? AND store_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, storeID);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                LogDAO.log(userID, "DELETE", "PRODUCT", (long) id, "Xóa sản phẩm ID " + id + " khỏi cửa hàng " + storeID);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("DEBUG DELETE ERROR: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean createProduct(Product p, long userID) throws SQLException {
        Connection conn = null;
        PreparedStatement psMenu = null;
        PreparedStatement psStore = null;
        ResultSet rs = null;

        String sqlInsertMenu = "INSERT INTO menu_items (category_id, name, image_url, base_price, unit, is_active) VALUES (?, ?, ?, ?, ?, ?)";

        String sqlInsertStore = "INSERT INTO store_menu_items (store_id, menu_item_id, inventory, in_menu, availability_status) VALUES (?, ?, ?, TRUE, 'available')";

        try {
            conn = getConnection();

            conn.setAutoCommit(false);
            psMenu = conn.prepareStatement(sqlInsertMenu, java.sql.Statement.RETURN_GENERATED_KEYS);

            psMenu.setInt(1, p.getCategoryId());
            psMenu.setString(2, p.getName());
            psMenu.setString(3, p.getPicUrl());
            psMenu.setDouble(4, p.getPrice());
            psMenu.setString(5, p.getUnit());
            psMenu.setBoolean(6, p.isActive());

            int affectedRows = psMenu.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Thêm sản phẩm thất bại, không có dòng nào được thêm vào menu_items.");
            }

            long newProductId = 0;
            rs = psMenu.getGeneratedKeys();
            if (rs.next()) {
                newProductId = rs.getLong(1);
            } else {
                throw new SQLException("Thêm sản phẩm thất bại, không lấy được ID.");
            }

            psStore = conn.prepareStatement(sqlInsertStore);
            psStore.setInt(1, 1);
            psStore.setLong(2, newProductId);
            psStore.setDouble(3, p.getInventory());

            psStore.executeUpdate();

            conn.commit();

            LogDAO.log(userID, "CREATE", "PRODUCT", newProductId, "Tạo món mới: " + p.getName());
            return true;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    System.err.println("Gặp lỗi, đang rollback: " + e.getMessage());
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (rs != null) rs.close();
            if (psMenu != null) psMenu.close();
            if (psStore != null) psStore.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<Category> getAllCategories() throws SQLException, ClassNotFoundException {
        List<Category> list = new ArrayList<>();
        String sql = """
                SELECT id, 
                       JSON_UNQUOTE(JSON_EXTRACT(name, "$.vi")) as catName 
                FROM categories ORDER BY order_index ASC """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("catName")));
            }
        }
        return list;
    }

    public Overview getDashboardOverview(String date) {
        long revenue = 0;
        long orders = 0;
        long products = 0;
        int customers = 0;

        String sqlRevenue = "SELECT " +
                            "   COALESCE(SUM(oi.qty * oi.unit_price_snapshot), 0), " +
                            "   COUNT(DISTINCT o.id) " +
                            "FROM orders o " +
                            "JOIN order_items oi ON o.id = oi.order_id " +
                            "WHERE DATE(o.opened_at) = ? AND o.STATUS = 'paid'";

        String sqlProduct = "SELECT COALESCE(SUM(oi.qty), 0) " +
                            "FROM order_items oi " +
                            "JOIN orders o ON oi.order_id = o.id " +
                            "WHERE DATE(o.opened_at) = ? AND o.STATUS = 'paid'";

        String sqlCustomer = "SELECT COUNT(id) FROM users WHERE DATE(created_at) = ?";

        try (Connection conn = getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(sqlRevenue)) {
                ps.setString(1, date);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        revenue = rs.getLong(1);
                        orders = rs.getLong(2);
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlProduct)) {
                ps.setString(1, date);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        products = rs.getLong(1);
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlCustomer)) {
                ps.setString(1, date);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        customers = rs.getInt(1);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Overview(revenue, orders, products, customers);
    }

    public List<Long> getRevenueLast12Months(int year) {
        List<Long> monthlyRevenue = new ArrayList<>();
        for (int i = 0; i < 12; i++) monthlyRevenue.add(0L);

        String sql = "SELECT " +
                     "   MONTH(o.opened_at) as month, " +
                     "   COALESCE(SUM(oi.qty * oi.unit_price_snapshot), 0) as total " +
                     "FROM orders o " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "WHERE o.STATUS = 'paid' AND YEAR(o.opened_at) = ? " +
                     "GROUP BY MONTH(o.opened_at)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, year);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("month");
                    long total = rs.getLong("total");

                    if (month >= 1 && month <= 12) {
                        monthlyRevenue.set(month - 1, total);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return monthlyRevenue;
    }

    public List<DashboardHandleOrder> getLiveOrders() {
        List<DashboardHandleOrder> list = new ArrayList<>();
        String sql = "SELECT o.id, t.table_uid, o.opened_at, o.STATUS, " +
                     "       SUM(oi.qty * oi.unit_price_snapshot) as total_price, " +
                     "       GROUP_CONCAT(CONCAT(oi.qty, 'x ', JSON_UNQUOTE(JSON_EXTRACT(mi.name, '$.vi'))) SEPARATOR ', ') as summary " +
                     "FROM orders o " +
                     "LEFT JOIN tables_ t ON o.table_id = t.id " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "JOIN menu_items mi ON oi.menu_item_id = mi.id " +
                     "WHERE o.STATUS IN ('pending', 'accept') " +
                     "GROUP BY o.id, t.table_uid, o.opened_at, o.STATUS " +
                     "ORDER BY FIELD(o.STATUS, 'pending', 'accept'), o.opened_at DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DashboardHandleOrder order = new DashboardHandleOrder();
                order.setId(rs.getLong("id"));
                order.setTableName(rs.getString("table_uid") != null ? "Bàn " + rs.getString("table_uid") : "Mang đi");
                order.setOpenedAt(rs.getTimestamp("opened_at"));
                order.setStatus(rs.getString("STATUS"));
                order.setTotalPrice(rs.getLong("total_price"));

                String summary = rs.getString("summary");
                if (summary != null && summary.length() > 50) {
                    summary = summary.substring(0, 47) + "...";
                }
                order.setItemSummary(summary);

                list.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateOrderStatus(long orderId, String newStatus, long userId) {
        Connection conn = null;
        PreparedStatement psUpdateStatus = null;
        PreparedStatement psDeductInventory = null;

        String sqlUpdateStatus = "UPDATE orders SET STATUS = ? WHERE id = ?";
        if ("paid".equals(newStatus) || "cancel".equals(newStatus)) {
            sqlUpdateStatus = "UPDATE orders SET STATUS = ?, closed_at = NOW() WHERE id = ?";
        }

        String sqlDeductInventory = "UPDATE store_menu_items smi " +
                                    "JOIN order_items oi ON smi.menu_item_id = oi.menu_item_id " +
                                    "JOIN orders o ON oi.order_id = o.id " +
                                    "SET smi.inventory = smi.inventory - oi.qty " +
                                    "WHERE o.id = ? AND smi.store_id = o.store_id";

        try {
            conn = getConnection();
            if (conn == null) return false;
            conn.setAutoCommit(false); // Bắt đầu Transaction

            psUpdateStatus = conn.prepareStatement(sqlUpdateStatus);
            psUpdateStatus.setString(1, newStatus);
            psUpdateStatus.setLong(2, orderId);
            int rowsUpdated = psUpdateStatus.executeUpdate();

            if (rowsUpdated > 0) {
                if ("paid".equals(newStatus)) {
                    psDeductInventory = conn.prepareStatement(sqlDeductInventory);
                    psDeductInventory.setLong(1, orderId);
                    int itemsDeducted = psDeductInventory.executeUpdate();

                    LogDAO.log(userId, "ORDER_PAID", "ORDER", orderId,
                            "Thanh toán đơn #" + orderId + ". Đã trừ kho " + itemsDeducted + " sản phẩm.");
                } else if ("cancel".equals(newStatus)) {
                    LogDAO.log(userId, "ORDER_CANCEL", "ORDER", orderId, "Hủy đơn hàng #" + orderId);
                } else if ("accept".equals(newStatus)) {
                    LogDAO.log(userId, "ORDER_ACCEPT", "ORDER", orderId, "Xác nhận đơn hàng #" + orderId);
                }

                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (psUpdateStatus != null) psUpdateStatus.close();
                if (psDeductInventory != null) psDeductInventory.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<InventoryAlert> getLowStockProducts(int limit) {
        List<InventoryAlert> list = new ArrayList<>();
        String sql = "SELECT mi.id, mi.image_url, smi.inventory, "
                     + "JSON_UNQUOTE(JSON_EXTRACT(mi.name, ?)) as pName "
                     + "FROM menu_items mi "
                     + "JOIN store_menu_items smi ON mi.id = smi.menu_item_id "
                     + "WHERE smi.inventory < 10 AND smi.store_id = 1 "
                     + "ORDER BY smi.inventory ASC LIMIT ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String jsonPath = "$." + "vi";

            ps.setString(1, jsonPath);
            ps.setInt(2, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String rawUrl = rs.getString("image_url");
                    String finalUrl = "";

                    if (rawUrl != null) {
                        if (!rawUrl.startsWith("assets/")) {
                            finalUrl = "assets/client/img/product/" + rawUrl;
                        } else {
                            finalUrl = rawUrl;
                        }
                    }

                    list.add(new InventoryAlert(
                            rs.getString("pName"),
                            "SP-" + rs.getInt("id"),
                            finalUrl,
                            rs.getDouble("inventory")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BestSeller> getBestSellers(int limit) {
        List<BestSeller> list = new ArrayList<>();
        String sql = "SELECT " +
                     "   JSON_UNQUOTE(JSON_EXTRACT(mi.name, '$.vi')) as pName, " +
                     "   JSON_UNQUOTE(JSON_EXTRACT(c.name, '$.vi')) as catName, " +
                     "   mi.image_url, " +
                     "   SUM(oi.qty) as total_sold " +
                     "FROM order_items oi " +
                     "JOIN menu_items mi ON oi.menu_item_id = mi.id " +
                     "JOIN categories c ON mi.category_id = c.id " +
                     "JOIN orders o ON oi.order_id = o.id " +
                     "WHERE o.STATUS = 'paid' " +
                     "GROUP BY mi.id, mi.name, c.name, mi.image_url " +
                     "ORDER BY total_sold DESC LIMIT ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String rawUrl = rs.getString("image_url");
                    String finalUrl = "";

                    if (rawUrl != null) {
                        if (!rawUrl.startsWith("assets/")) {
                            finalUrl = "assets/client/img/product/" + rawUrl;
                        } else {
                            finalUrl = rawUrl;
                        }
                    }

                    list.add(new BestSeller(
                            rs.getString("pName"),
                            rs.getString("catName"),
                            finalUrl, // Dùng url đã xử lý
                            rs.getLong("total_sold")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ActivityLog> getRecentActivities(int limit) {
        List<ActivityLog> list = new ArrayList<>();
        String sql = "SELECT description, created_at, action, target_type FROM activity_logs ORDER BY created_at DESC LIMIT ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String action = rs.getString("action");
                    String type = "inventory";
                    String icon = "fas fa-info-circle";

                    if (action != null) {
                        if (action.contains("ORDER")) {
                            type = "order";
                            icon = "fas fa-receipt";
                        } else if (action.contains("USER") || action.contains("LOGIN")) {
                            type = "user";
                            icon = "fas fa-user";
                        } else {
                            type = "inventory";
                            icon = "fas fa-boxes";
                        }
                    }

                    list.add(new ActivityLog(
                            rs.getString("description"),
                            rs.getString("created_at"),
                            type,
                            icon
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean quickUpdateInventory(String productNameOrId, int quantity, boolean isImport, long userId) {
        String sqlFind = "SELECT id, name FROM menu_items WHERE id = ? OR name LIKE ? LIMIT 1";
        String sqlUpdate = "UPDATE store_menu_items SET inventory = inventory + ? WHERE menu_item_id = ? AND store_id = 1";

        try (Connection conn = getConnection()) {
            long productId = -1;
            String prodName = "";

            try (PreparedStatement ps = conn.prepareStatement(sqlFind)) {
                long searchId = -1;
                try {
                    searchId = Long.parseLong(productNameOrId);
                } catch (NumberFormatException e) {
                    searchId = -1;
                }

                ps.setLong(1, searchId);
                ps.setString(2, "%" + productNameOrId + "%");

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        productId = rs.getLong("id");
                        prodName = rs.getString("name");
                    }
                }
            }

            if (productId == -1) return false;

            int changeQty = isImport ? quantity : -quantity;

            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                ps.setInt(1, changeQty);
                ps.setLong(2, productId);
                int rows = ps.executeUpdate();

                if (rows > 0) {
                    String action = isImport ? "QUICK_IMPORT" : "QUICK_EXPORT";
                    String desc = (isImport ? "Nhập kho nhanh: " : "Xuất kho nhanh: ") + quantity + " " + prodName;
                    LogDAO.log(userId, action, "INVENTORY", productId, desc);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Order> getAllOrders(int storeId) {

        List<Order> list = new ArrayList<>();


        String sql = "SELECT o.id, " +
                     "       COALESCE(u.username, 'Khách vãng lai') as cust_name, " +
                     "       t.table_uid, " +
                     "       o.opened_at, o.STATUS, " +
                     "       COALESCE(SUM(oi.qty * oi.unit_price_snapshot), 0) as total_price " +
                     "FROM orders o " +
                     "LEFT JOIN users u ON o.user_id = u.id " +
                     "LEFT JOIN tables_ t ON o.table_id = t.id " +
                     "LEFT JOIN order_items oi ON o.id = oi.order_id " +
                     "WHERE o.store_id = ? " +
                     "GROUP BY o.id, u.username, t.table_uid, o.opened_at, o.STATUS " +
                     "ORDER BY o.opened_at DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, storeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tableName = rs.getString("table_uid");
                    list.add(new Order(
                            rs.getLong("id"),
                            rs.getString("cust_name"),
                            tableName != null ? "Bàn " + tableName : "Mang đi",
                            rs.getTimestamp("opened_at"),
                            rs.getLong("total_price"),
                            rs.getString("STATUS")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<OrderDetail> getOrderItemsDetail(long orderId) {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT " +
                     "  JSON_UNQUOTE(JSON_EXTRACT(item_name_snapshot, '$.vi')) as pName, " +
                     "  qty, unit_price_snapshot, note " +
                     "FROM order_items WHERE order_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new OrderDetail(
                            rs.getString("pName"),
                            rs.getInt("qty"),
                            rs.getLong("unit_price_snapshot"),
                            rs.getString("note")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Log> getSystemLogs() {
        List<Log> list = new ArrayList<>();

        String sql = "SELECT l.id, " +
                     "       COALESCE(u.username, 'Hệ thống') as user_name, " +
                     "       l.action, l.description, l.ip_address, l.level, l.created_at " +
                     "FROM activity_logs l " +
                     "LEFT JOIN users u ON l.user_id = u.id " +
                     "ORDER BY l.created_at DESC " +
                     "LIMIT 50";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Log(
                        rs.getLong("id"),
                        rs.getString("user_name"),
                        rs.getString("action"),
                        rs.getString("description"),
                        rs.getString("ip_address"),
                        rs.getString("level"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) throws SQLException {
        AdminDAO dao = new AdminDAO();
        List<Product> list = dao.getAllProductsByStore(1);
        System.out.println(list.getFirst().getName());
    }
}
