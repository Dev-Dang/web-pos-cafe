<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
    <!-- Log -->
    <div id="log" class="page-content">
        <header>
            <h1>Nhật Ký Hệ Thống</h1>
            <input type="text" class="search-bar" placeholder="Tìm kiếm hành động, người dùng...">
        </header>
        <div class="data-table-container">
            <div class="log-entry">
                <div class="log-icon log-info"><i class="fas fa-user-edit"></i></div>
                <div class="log-message">
                    <strong>Nguyễn Văn An</strong> đã cập nhật sản phẩm <strong>Áo Hoodie Nỉ Bông (#SP001)</strong>.
                </div>
                <div class="log-timestamp">5 phút trước</div>
            </div>
            <div class="log-entry">
                <div class="log-icon log-success"><i class="fas fa-plus-circle"></i></div>
                <div class="log-message">
                    <strong>Hệ thống</strong> đã thêm đơn hàng mới <strong>#DH-1710</strong>.
                </div>
                <div class="log-timestamp">1 giờ trước</div>
            </div>
            <div class="log-entry">
                <div class="log-icon log-warning"><i class="fas fa-exclamation-triangle"></i></div>
                <div class="log-message">
                    Đăng nhập không thành công từ IP <strong>203.162.1.47</strong>.
                </div>
                <div class="log-timestamp">3 giờ trước</div>
            </div>
            <div class="log-entry">
                <div class="log-icon log-info"><i class="fas fa-sign-in-alt"></i></div>
                <div class="log-message">
                    <strong>Lê Minh Anh</strong> đã đăng nhập vào hệ thống.
                </div>
                <div class="log-timestamp">8 giờ trước</div>
            </div>
        </div>
    </div>