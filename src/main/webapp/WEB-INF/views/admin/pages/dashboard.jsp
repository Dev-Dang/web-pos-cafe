<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<script>
    const ctx = "${pageContext.request.contextPath}";
</script>
<script src="<c:url value='/assets/admin/js/dashboard.js'/>"></script>
<!-- Dashboard (Chứa Tổng quan doanh thu, đơn hàng, sản phẩm đã bán,)-->
<div id="dashboard" class="page-content">
    <header>
        <h1>Tổng Quan</h1>
        <button id="date-filter-button" class="date-filter">
            <i class="fas fa-calendar-alt"></i>
            <span id="date-filter-text"></span>
        </button>
    </header>

    <div class="card-container">
        <div class="card revenue">
            <div class="card-icon"><i class="fas fa-dollar-sign"></i></div>
            <div class="card-info">
                <p class="card-title">Tổng Doanh Thu</p>
                <p class="card-value" id="val-revenue">Loading...</p>
            </div>
        </div>
        <div class="card orders">
            <div class="card-icon"><i class="fas fa-shopping-bag"></i></div>
            <div class="card-info">
                <p class="card-title">Tổng Đơn Hàng</p>
                <p class="card-value" id="val-orders">Loading...</p>
            </div>
        </div>
        <div class="card products">
            <div class="card-icon"><i class="fas fa-box"></i></div>
            <div class="card-info">
                <p class="card-title">Sản Phẩm Đã Bán</p>
                <p class="card-value" id="val-products">Loading...</p>
            </div>
        </div>
        <div class="card users">
            <div class="card-icon"><i class="fas fa-users"></i></div>
            <div class="card-info">
                <p class="card-title">Khách Hàng Mới</p>
                <p class="card-value" id="val-customers">Loading...</p>
            </div>
        </div>
    </div>

    <div class="details-container">

        <div class="detail-card revenue-chart">
            <h2>Phân Tích Doanh Thu</h2>
            <canvas id="myRevenueChart"></canvas>
        </div>
        <div class="detail-card live-orders">
            <h2>Đơn Hàng Mới (Đang Chờ)</h2>
            <ul class="order-list">
                <li class="empty-state-text">
                    <i class="fas fa-spinner fa-spin"></i> Đang tải dữ liệu...
                </li>
            </ul>
        </div>
        <div id="payment-modal" class="modal">
            <div class="modal-content">
                <header class="modal-header">
                    <h2 class="modal-title">Xác Nhận Thanh Toán</h2>
                    <button class="close-btn">&times;</button>
                </header>

                <div class="modal-body">
                    <div class="payment-info-center">
                        <h3 id="payment-table-name">Bàn ...</h3>
                        <p class="payment-order-ref">Mã đơn: <strong id="payment-order-id">...</strong></p>
                    </div>

                    <div class="payment-summary">
                        <p class="payment-total-row">
                            <span>Tổng tiền:</span>
                            <span id="payment-total">0 ₫</span>
                        </p>
                    </div>

                    <div class="form-group cancel-reason-group">
                        <label for="cancel-note">Ghi chú hủy (nếu chọn hủy):</label>
                        <input type="text" id="cancel-note" placeholder="Lý do hủy..." class="form-control">
                    </div>
                </div>

                <footer class="modal-footer">
                    <button type="button" class="btn btn-danger" id="btn-confirm-cancel-order">
                        <i class="fas fa-times"></i> Hủy Đơn Hàng
                    </button>

                    <button type="button" class="btn btn-success" id="btn-confirm-payment">
                        <i class="fas fa-check-circle"></i> Xác Nhận Đã Thu Tiền
                    </button>
                </footer>
            </div>
        </div>
        <div class="detail-card inventory-status">
            <h2>Cảnh Báo Tồn Kho</h2>
            <ul class="inventory-list">
                <li class="text-center p-3 text-muted">
                    <i class="fas fa-spinner fa-spin"></i> Đang tải dữ liệu...
                </li>
            </ul>
        </div>

        <div class="detail-card best-sellers">
            <div class="card-header">
                <h2>Sản Phẩm Bán Chạy</h2>
                <div class="sort-controls">
                    <select id="bestseller-sort">
                        <option value="desc">Bán chạy nhất</option>
                        <option value="asc">Bán ít nhất</option>
                    </select>
                </div>
            </div>
            <ul id="bestseller-list">
                <li class="text-center p-3 text-muted">
                    <i class="fas fa-spinner fa-spin"></i> Đang tải dữ liệu...
                </li>
            </ul>
        </div>

        <div class="detail-card today-activity">
            <h2>Hoạt Động Hôm Nay</h2>
            <ul class="activity-feed">
                <li class="text-center p-3 text-muted">
                    <i class="fas fa-spinner fa-spin"></i> Đang tải hoạt động...
                </li>
            </ul>
        </div>

        <div class="detail-card inventory-actions">
            <h2>Xuất/Nhập Kho Nhanh</h2>
            <form id="inventory-form">
                <div class="form-group">
                    <label for="product-select">Chọn Sản Phẩm</label>
                    <input type="text" id="product-select" placeholder="Tìm SKU hoặc Tên SP...">
                </div>
                <div class="form-group">
                    <label for="product-quantity">Số Lượng</label>
                    <input type="number" id="product-quantity" value="1" min="1">
                </div>
                <div class="form-buttons">
                    <button type="button" class="btn btn-import">
                        <i class="fas fa-plus-circle"></i> Nhập Kho
                    </button>
                    <button type="button" class="btn btn-export">
                        <i class="fas fa-minus-circle"></i> Xuất Kho
                    </button>
                </div>
            </form>
        </div>

    </div>
</div>

<!-- Chart(Biểu đồ doanh thu) -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>