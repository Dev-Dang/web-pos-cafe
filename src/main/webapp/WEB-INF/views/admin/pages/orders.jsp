<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div id="order" class="page-content">
    <header>
        <h1>Quản Lý Đơn Hàng</h1>
        <div class="header-actions">
            <input type="text" class="search-bar" id="order-search-input" placeholder="Tìm kiếm mã đơn, khách hàng...">
            <button class="btn btn-primary" onclick="$('#create-order-modal').show()">
                <i class="fas fa-plus"></i> Tạo Đơn
            </button>
        </div>
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
                    data-status="${o.status}"
                    data-tablename="${o.tableName}"
                    data-time="<fmt:formatDate value="${o.openedAt}" pattern="HH:mm dd/MM/yyyy"/>"
                    data-total="<fmt:formatNumber value="${o.totalPrice}" type="currency" currencySymbol="₫"/>">

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
                            <c:otherwise><span class="status-badge">${o.status}</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <button class="btn-action btn-viewdetail" data-target="#order-detail-modal">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn-action btn-delete-order" data-id="${o.id}">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div id="order-detail-modal" class="modal">
        <div class="modal-content modal-lg">
            <header class="modal-header">
                <h2 class="modal-title"><i class="fas fa-file-invoice"></i> Chi Tiết Đơn Hàng #<span
                        data-fill-text="id"></span></h2>
                <button class="close-btn">&times;</button>
            </header>

            <div class="modal-body">
                <form id="edit-order-form">
                    <input type="hidden" name="id" data-fill="id">

                    <div class="order-info-grid">
                        <div class="form-group">
                            <label>Trạng thái:</label>
                            <select name="status" data-fill="status" class="form-control">
                                <option value="pending">🟡 Chờ xác nhận</option>
                                <option value="accept">🔵 Đang phục vụ</option>
                                <option value="paid">🟢 Đã thanh toán</option>
                                <option value="cancel">🔴 Đã hủy</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Bàn / Vị trí:</label>
                            <input type="number" name="tableId" class="form-control" placeholder="0 = Mang đi">
                        </div>
                    </div>

                    <hr class="modal-divider">

                    <div class="add-item-box">
                        <h5><i class="fas fa-plus-circle"></i> Thêm món</h5>
                        <div class="add-item-row">
                            <select id="add-product-select" class="form-control item-select">
                                <option value="" disabled selected>-- Chọn món ăn --</option>
                                <c:forEach var="p" items="${productsList}">
                                    <option value="${p.id}">${p.name} - <fmt:formatNumber value="${p.price}"
                                                                                          type="currency"/>/ ${p.unit}</option>
                                </c:forEach>
                            </select>
                            <input type="number" id="add-product-qty" class="form-control qty-input" value="1" min="1">
                            <button type="button" id="btn-add-item-to-order" class="btn btn-success">Thêm</button>
                        </div>
                    </div>

                    <div class="order-items-list">
                        <h4>Danh sách đã gọi</h4>
                        <div class="table-scroll">
                            <table class="data-table items-table">
                                <thead>
                                <tr>
                                    <th>Tên món</th>
                                    <th class="text-center">SL</th>
                                    <th class="text-right">Đơn giá</th>
                                    <th class="text-right">Thành tiền</th>
                                    <th>Ghi chú</th>
                                </tr>
                                </thead>
                                <tbody id="modal-order-items-body">
                                </tbody>
                                <tfoot>
                                <tr class="total-row">
                                    <td colspan="3" class="text-right"><strong>Tổng cộng:</strong></td>
                                    <td class="text-right highlight-price"><span data-fill-text="total">0 ₫</span></td>
                                    <td></td>
                                </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                </form>
            </div>

            <footer class="modal-footer">
                <button class="btn btn-secondary close-btn">Đóng</button>
                <button class="btn btn-primary" id="btn-save-order">Lưu Thay Đổi</button>
            </footer>
        </div>
    </div>

    <div id="create-order-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Tạo Đơn Hàng Mới</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <form id="create-order-form">
                    <div class="form-group">
                        <label>Chọn Bàn (Table ID)</label>
                        <input type="number" name="tableId" class="form-control"
                               placeholder="Nhập ID bàn (Ví dụ: 1, 2...). Nhập 0 là Mang đi" required>
                    </div>
                    <div class="info-box">
                        <i class="fas fa-info-circle"></i>
                        <p>Sau khi tạo đơn, vui lòng vào phần "Chi tiết" để thêm món ăn.</p>
                    </div>
                </form>
            </div>
            <footer class="modal-footer">
                <button class="btn btn-secondary close-btn">Hủy</button>
                <button class="btn btn-primary" id="btn-submit-create-order">Tạo Đơn</button>
            </footer>
        </div>
    </div>
</div>

