<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
    <!-- Staff -->
    <div id="staff" class="page-content">
        <header>
            <h1>Quản Lý Nhân Viên</h1>
            <button id="create-staff-btn" class="btn btn-primary"><i class="fas fa-user-plus"></i> Thêm Nhân Viên
            </button>
        </header>
        <div class="data-table-container">
            <table class="data-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Họ Tên</th>
                    <th>Chức Vụ</th>
                    <th>Email</th>
                    <th>Ngày Vào Làm</th>
                    <th>Hành Động</th>
                </tr>
                </thead>
                <tbody>
                <tr data-id="NV001"
                    data-name="Lê Minh Anh"
                    data-position="Quản lý Kho"
                    data-email="minhanh.le@company.com"
                    data-join-date="12/05/2024">

                    <td>#NV001</td>
                    <td>Lê Minh Anh</td>
                    <td>Quản lý Kho</td>
                    <td title="minhanh.le@company.com">minhanh.le@company.com</td>
                    <td>12/05/2024</td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-staff-modal"><i
                                class="fas fa-edit"></i>
                        </button>
                        <button class="btn-action btn-delete" data-target="#delete-staff-modal"><i
                                class="fas fa-trash"></i></button>
                    </td>
                </tr>
                <tr data-id="NV002"
                    data-name="Hoàng Thị Bích"
                    data-position="Nhân viên Bán hàng"
                    data-email="bich.hoang@company.com"
                    data-join-date="20/08/2025">

                    <td>#NV002</td>
                    <td>Hoàng Thị Bích</td>
                    <td>Nhân viên Bán hàng</td>
                    <td title="bich.hoang@company.com">bich.hoang@company.com</td>
                    <td>20/08/2025</td>
                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-staff-modal"><i
                                class="fas fa-edit"></i>
                        </button>
                        <button class="btn-action btn-delete" data-target="#delete-staff-modal"><i
                                class="fas fa-trash"></i></button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <!-- Form create staff modal -->
    <div id="create-staff-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Thêm Nhân Viên Mới</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <form id="create-staff-form">
                    <div class="form-group">
                        <label for="createStaffName">Họ và Tên</label>
                        <input type="text" id="createStaffName" placeholder="Nhập họ và tên đầy đủ">
                    </div>

                    <div class="form-group">
                        <label for="createStaffPosition">Chức Vụ</label>
                        <input type="text" id="createStaffPosition"
                               placeholder="Ví dụ: Nhân viên bán hàng, Quản lý kho...">
                    </div>

                    <div class="form-group">
                        <label for="createStaffEmail">Email</label>
                        <input type="email" id="createStaffEmail" placeholder="Nhập địa chỉ email">
                    </div>

                    <div class="form-group">
                        <label for="createStaffJoinDate">Ngày Vào Làm</label>
                        <div class="datepicker-wrapper">
                            <input type="text" id="createStaffJoinDate" placeholder="Chọn ngày vào làm...">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="createStaffPassword">Mật Khẩu</label>
                        <input type="password" id="createStaffPassword" placeholder="Tạo mật khẩu đăng nhập">
                    </div>
                </form>
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" form="create-staff-form" class="btn btn-primary">
                    <i class="fas fa-plus"></i> Thêm Nhân Viên
                </button>
            </footer>
        </div>
    </div>
    <!-- Form edit staff modal -->
    <div id="edit-staff-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Chỉnh Sửa Nhân Viên</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <form id="edit-staff-form">
                    <div class="form-group">
                        <label>Họ Tên</label>
                        <input type="text" data-fill="name" placeholder="Nhập họ tên">
                    </div>
                    <div class="form-group">
                        <label>Chức Vụ</label>
                        <input type="text" data-fill="position" placeholder="Nhập chức vụ">
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" data-fill="email" placeholder="Nhập email">
                    </div>
                    <div class="form-group">
                        <label>Ngày Vào Làm</label>
                        <input type="text" data-fill="joinDate" placeholder="DD/MM/YYYY">
                    </div>

                    <input type="hidden" data-fill="id" name="staff_id">
                </form>
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-primary">Lưu Thay Đổi</button>
            </footer>
        </div>
    </div>
    <!-- Form delete staff modal -->
    <div id="delete-staff-modal" class="modal">
        <div class="modal-content">
            <header class="modal-header">
                <h2 class="modal-title">Xác nhận Xóa</h2>
                <button class="close-btn">&times;</button>
            </header>
            <div class="modal-body">
                <p>Bạn có chắc muốn xóa nhân viên <strong data-fill-text="name">[Tên nhân viên]</strong> không?</p>

                <input type="hidden" data-fill="id" id="staff-id-to-delete">
            </div>
            <footer class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-danger">Xóa</button>
            </footer>
        </div>
    </div>