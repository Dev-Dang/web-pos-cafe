<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div id="account" class="page-content">
    <header>
        <h1>Quản Lý Tài Khoản</h1>
        <button class="btn btn-primary" onclick="$('#create-account-modal').show()">
            <i class="fas fa-user-plus"></i> Thêm Tài Khoản Mới
        </button>
    </header>

    <div class="data-table-container">
        <table class="data-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Tên Người Dùng</th>
                <th>Email</th>
                <th>Vai Trò</th>
                <th>Hành Động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="u" items="${accountsList}">
                <tr data-id="${u.id}"
                    data-username="${u.username}"
                    data-email="${u.email}"
                    data-role="${u.superAdmin ? 'admin' : 'user'}"> <td>#${u.id}</td>
                    <td>${u.username}</td>
                    <td>${u.email}</td>
                    <td>
                        <c:choose>
                            <c:when test="${u.superAdmin}">
                                <span class="status-badge status-completed">Quản Trị Viên</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-badge status-pending">Người Dùng</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-account-modal"><i class="fas fa-edit"></i></button>
                        <button class="btn-action btn-delete" data-target="#delete-account-modal"><i class="fas fa-trash"></i></button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div id="create-account-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Tạo Tài Khoản</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <form id="create-account-form">
                    <div class="form-group">
                        <label>Họ và Tên</label>
                        <input type="text" name="fullname" required placeholder="Nhập tên hiển thị">
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="email" required placeholder="Nhập email đăng nhập">
                    </div>
                    <div class="form-group">
                        <label>Mật Khẩu</label>
                        <input type="password" name="password" required placeholder="Nhập mật khẩu">
                    </div>
                    <div class="form-group">
                        <label>Vai trò</label>
                        <select name="role">
                            <option value="user">Người Dùng</option>
                            <option value="admin">Quản Trị Viên</option>
                        </select>
                    </div>
                </form>
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary close-btn">Hủy</button>
                <button type="button" id="btn-submit-create" class="btn btn-primary">Tạo Tài Khoản</button>
            </footer>
        </div>
    </div>

    <div id="edit-account-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Chỉnh Sửa Tài Khoản</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <form id="edit-account-form">
                    <input type="hidden" data-fill="id" name="id">
                    <div class="form-group">
                        <label>Tên Người Dùng</label>
                        <input type="text" data-fill="username" name="username" required>
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" data-fill="email" name="email" required>
                    </div>
                    <div class="form-group">
                        <label>Vai Trò</label>
                        <select data-fill="role" name="role">
                            <option value="user">Người Dùng</option>
                            <option value="admin">Quản Trị Viên</option>
                        </select>
                    </div>
                </form>
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary close-btn">Hủy</button>
                <button type="button" id="btn-submit-edit" class="btn btn-primary">Lưu Thay Đổi</button>
            </footer>
        </div>
    </div>

    <div id="delete-account-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Xác nhận Xóa</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <p>Bạn có chắc muốn xóa tài khoản <strong data-fill-text="username"></strong> không?</p>
                <input type="hidden" data-fill="id" id="delete-id">
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary close-btn">Hủy</button>
                <button type="button" id="btn-submit-delete" class="btn btn-danger">Xóa</button>
            </footer>
        </div>
    </div>
</div>