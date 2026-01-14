<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div id="order" class="page-content">
    <header>
        <h1>Quản Lý Đơn Hàng</h1>
        <input type="text" class="search-bar" id="order-search-input"
               placeholder="Tìm kiếm theo mã đơn, tên KH...">
    </header>

    <div class="data-table-container">
        <table class="data-table">
            <thead>
            <tr>
                <th>Mã Đơn</th>
                <th>Khách Hàng</th>
                <th>Vị Trí</th>
                <th>Ngày Đặt</th>
                <th>Tổng Tiền</th>
                <th>Trạng Thái</th>
                <th>Hành Động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="o" items="${ordersList}">
                <tr data-id="${o.id}"
                    data-customer="${o.customerName}"
                    data-status="${o.status}">

                    <td><strong>#DH-${o.id}</strong></td>
                    <td>${o.customerName}</td>
                    <td>${o.tableName}</td>
                    <td><fmt:formatDate value="${o.openedAt}" pattern="HH:mm dd/MM/yyyy"/></td>
                    <td class="text-bold">
                        <fmt:formatNumber value="${o.totalPrice}" type="currency" currencySymbol="₫"/>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${o.status == 'paid'}"><span class="status-badge status-completed">Đã thanh toán</span></c:when>
                            <c:when test="${o.status == 'cancel'}"><span
                                    class="status-badge status-cancelled">Đã hủy</span></c:when>
                            <c:when test="${o.status == 'pending'}"><span class="status-badge status-pending">Chờ xác nhận</span></c:when>
                            <c:when test="${o.status == 'accept'}"><span class="status-badge status-processing">Đang phục vụ</span></c:when>
                        </c:choose>
                    </td>
                    <td>
                        <button class="btn-action btn-viewdetail" data-target="#order-detail-modal">
                            <i class="fas fa-edit"></i>
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div id="order-detail-modal" class="modal">
        <div class="modal-content" >
            <header class="modal-header">
                <h2 class="modal-title">Chi Tiết Đơn Hàng #<span data-fill-text="id"></span></h2>
                <button class="close-btn">&times;</button>
            </header>

            <div class="modal-body">
                <p><strong>Khách hàng:</strong> <span data-fill-text="customer"></span></p>
                <p><strong>Trạng thái hiện tại:</strong> <span data-fill-text="status"></span></p>
                <hr>

                <div>
                    <table class="data-table">
                        <thead>
                        <tr>
                            <th>Tên món</th>
                            <th>SL</th>
                            <th>Đơn giá</th>
                            <th>Thành tiền</th>
                            <th>Ghi chú</th>
                        </tr>
                        </thead>
                        <tbody id="modal-order-items-body">
                        </tbody>
                    </table>
                </div>
            </div>

            <footer class="modal-footer">
                <button class="btn btn-secondary close-btn">Đóng</button>
            </footer>
        </div>
    </div>
</div>