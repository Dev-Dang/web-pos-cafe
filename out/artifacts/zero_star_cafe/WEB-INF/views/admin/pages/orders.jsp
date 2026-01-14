<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
    <!-- Order (Quản lý các đơn hàng được đặt, xử lý các thao tác gồm Xác nhận, Hủy)-->
    <div id="order" class="page-content">
        <header>
            <h1>Quản Lý Đơn Hàng</h1>
            <input type="text" class="search-bar" id="order-search-input"
                   placeholder="Tìm kiếm theo mã đơn, tên KH...">
        </header>
        <div class="data-table-container">
            <table class="data-table" id="table-heading">
                <thead>
                <tr>
                    <th>Mã Đơn</th>
                    <th>Khách Hàng</th>
                    <th>Ngày Đặt</th>
                    <th>Tổng Tiền</th>
                    <th>Trạng Thái</th>
                    <th>Thời gian</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>#DH-1710</td>
                    <td>Trần Minh Tuấn</td>
                    <td>16/10/2025</td>
                    <td>1,240,000 ₫</td>
                    <td>
                        <span class="status-badge status-completed">Đã hoàn thành</span>
                    </td>
                    <td>13:40 , 16/10/2025</td>
                </tr>
                <tr>
                    <td>#DH-1709</td>
                    <td>Lê Thị Hoa</td>
                    <td>16/10/2025</td>
                    <td>790,000 ₫</td>
                    <td>
                        <span class="status-badge status-completed">Đã hoàn thành</span>
                    </td>
                    <td>13:40 , 16/10/2025</td>
                </tr>
                <tr>
                    <td>#DH-1708</td>
                    <td>Phạm Văn Dũng</td>
                    <td>15/10/2025</td>
                    <td>450,000 ₫</td>
                    <td>
                        <span class="status-badge status-cancelled">Đã hủy</span>
                    </td>
                    <td>13:40 , 16/10/2025</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
