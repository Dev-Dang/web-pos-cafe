<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
    <!-- Account -->
    <div id="account" class="page-content">
        <header>
            <h1>Quản Lý Tài Khoản</h1>
            <button class="btn btn-primary" id="create-account-btn"><i class="fas fa-user-plus"></i> Thêm Tài Khoản
                Mới
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
                <tr
                        data-id="TK001"
                        data-username="Nguyễn Văn An"
                        data-email="nguyen.van.an@admin.com"
                        data-role="admin">
                    <td>#TK001</td>
                    <td>Nguyễn Văn An</td>
                    <td>nguyen.van.an@admin.com</td>
                    <td>
                        <span class="status-badge status-completed">Quản Trị Viên</span>
                    </td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-account-modal">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn-action btn-delete" data-target="#delete-account-modal">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
                <tr
                        data-id="TK002"
                        data-username="Trần Thị Bích"
                        data-email="tran.thi.bich@editor.com"
                        data-role="editor">

                    <td>#TK002</td>
                    <td>Trần Thị Bích</td>
                    <td>tran.thi.bich@editor.com</td>
                    <td>
                        <span class="status-badge status-pending">Biên Tập Viên</span>
                    </td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-account-modal">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn-action btn-delete" data-target="#delete-account-modal">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
                <tr
                        data-id="TK003"
                        data-username="Lê Văn Dũng"
                        data-email="le.van.dung@viewer.com"
                        data-role="viewer">

                    <td>#TK003</td>
                    <td>Lê Văn Dũng</td>
                    <td>le.van.dung@viewer.com</td>
                    <td>
                        <span class="status-badge status-cancelled">Người Xem</span>
                    </td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-account-modal">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn-action btn-delete" data-target="#delete-account-modal">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <!-- Form modal create account -->
    <div id="create-account-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Tạo Tài Khoản Mới</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <form id="create-account-form" class="account-form">
                    <div class="form-group">
                        <label for="newFullName">Họ và Tên</label>
                        <input type="text" id="newFullName" placeholder="Nhập họ và tên đầy đủ">
                    </div>
                    <div class="form-group">
                        <label for="newEmail">Email</label>
                        <input type="email" id="newEmail" placeholder="Nhập địa chỉ email">
                    </div>
                    <div class="form-group">
                        <label for="newPasswordModal">Mật Khẩu</label>
                        <input type="password" id="newPasswordModal" placeholder="Tạo mật khẩu">
                    </div>
                    <div class="form-group">
                        <label for="newRole">Vai trò</label>
                        <select id="newRole">
                            <option value="" disabled selected>-- Chọn vai trò --</option>
                            <option value="viewer">Người Xem</option>
                            <option value="editor">Biên Tập Viên</option>
                            <option value="admin">Quản Trị Viên</option>
                        </select>
                    </div>
                </form>
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" form="create-account-form" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Tạo Tài Khoản
                </button>
            </footer>

        </div>
    </div>
    <!-- Form modal edit account -->
    <div id="edit-account-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Chỉnh Sửa Tài Khoản</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <form id="edit-account-form">
                    <div class="form-group">
                        <label>Tên Người Dùng</label>
                        <input type="text" data-fill="username" placeholder="Nhập tên người dùng">
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" data-fill="email" placeholder="Nhập email">
                    </div>
                    <div class="form-group">
                        <label>Vai Trò</label>
                        <select data-fill="role">
                            <option value="admin">Quản Trị Viên</option>
                            <option value="editor">Biên Tập Viên</option>
                            <option value="viewer">Người Xem</option>
                        </select>
                    </div>
                    <input type="hidden" data-fill="id" name="account_id">
                </form>
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-primary">Lưu Thay Đổi</button>
            </footer>
        </div>
    </div>
    <!-- Form modal delete account -->
    <div id="delete-account-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Xác nhận Xóa</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <p>Bạn có chắc muốn xóa tài khoản <strong data-fill-text="username">[Tên tài khoản]</strong> không?
                </p>
                <input type="hidden" data-fill="id" id="account-id-to-delete">
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-danger">Xóa</button>
            </footer>
        </div>
    </div>
