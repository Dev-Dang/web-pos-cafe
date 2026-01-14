<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!-- Thanh sidebar -->
<div class="sidebar">
    <div class="user-profile">
        <div class="user-avatar"><i class="fas fa-user-shield"></i></div>
        <div class="user-details">
            <p class="user-name">Nguyễn Văn An</p>
            <p class="user-role">Quản Trị Viên</p>
        </div>
    </div>
    <!-- Phần điều hướng -->
    <nav class="navigation">
        <ul>
            <li class="${empty pageId || pageId == 'dashboard' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/admin/dashboard">
                    <i class="fas fa-tachometer-alt"></i>
                    <span>Dashboard</span>
                </a>
            </li>
            <li class="${pageId == 'product' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/admin/products">
                    <i class="fas fa-box-open"></i>
                    <span>Sản phẩm</span>
                </a>
            </li>
            <li class="${pageId == 'order' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/admin/orders">
                    <i class="fas fa-shopping-cart"></i>
                    <span>Đơn hàng</span>
                </a>
            </li>
            <li class="${pageId == 'revenue' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/admin/revenue">
                    <i class="fas fa-chart-line"></i>
                    <span>Doanh thu</span>
                </a>
            </li>
            <li class="${pageId == 'account' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/admin/accounts">
                    <i class="fas fa-user-circle"></i>
                    <span>Tài khoản</span>
                </a>
            </li>
            <li class="${pageId == 'staff' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/admin/staffs">
                    <i class="fas fa-users-cog"></i>
                    <span>Nhân viên</span>
                </a>
            </li>
            <li class="${pageId == 'log' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/admin/logs">
                    <i class="fas fa-clipboard-list"></i>
                    <span>Nhật ký</span>
                </a>
            </li>
        </ul>
    </nav>
    <div class="sidebar-footer">
        <a href="auth/logout">
            <i class="fas fa-sign-out-alt"></i>
            <span>Đăng xuất</span>
        </a>
    </div>
</div>
