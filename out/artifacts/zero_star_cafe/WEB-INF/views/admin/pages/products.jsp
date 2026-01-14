<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!-- Product (Các sản phẩm mặt hàng,chi tiết của sản phẩm, nhập/xuất hàng hóa)-->
<div id="product" class="page-content">
    <header>
        <h1>Quản Lý Sản Phẩm</h1>
        <button class="btn btn-primary" id="create-product-btn"><i class="fas fa-plus"></i> Thêm Sản Phẩm Mới
        </button>
        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-${sessionScope.messageType == 'success' ? 'success' : 'danger'} alert-dismissible fade show"
                 role="alert">

                <strong>${sessionScope.messageType == 'success' ? 'Thành công!' : 'Thông báo:'}</strong> ${sessionScope.message}
            </div>

            <c:remove var="message" scope="session"/>
            <c:remove var="messageType" scope="session"/>
        </c:if>
    </header>

    <div class="page-controls" id="found-bar">
        <input type="text" class="search-bar" id="product-search-input"
               placeholder="Tìm kiếm sản phẩm theo tên, ID...">
    </div>
    <div class="data-table-container">
        <table class="data-table">
            <thead>
            <tr>
                <th>ID</th>
                <th>Hình Ảnh</th>
                <th>Tên Sản Phẩm</th>
                <th>Danh Mục</th>
                <th>Giá</th>
                <th>Tồn Kho</th>
                <th>Đơn vị</th>
                <th>Hành Động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="p" items="${productsList}">

                <c:choose>
                    <c:when test="${empty p.picUrl}">
                        <c:set var="imageUrl" value="https://picsum.photos/50"/>
                    </c:when>
                    <c:when test="${p.picUrl.startsWith('http')}">
                        <c:set var="imageUrl" value="${p.picUrl}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="imageUrl"
                               value="${pageContext.request.contextPath}/assets/client/img/product/${p.picUrl}"/>
                    </c:otherwise>
                </c:choose>

                <tr class="${!p.active ? 'hidden-row' : ''}"
                    data-id="${p.id}"
                    data-name="${p.name}"
                    data-price="${p.price}"
                    data-inventory="${p.inventory}"
                    data-unit="${p.unit}"
                    data-category-id="${p.categoryId}"
                    data-image-src="${imageUrl}"
                    data-hide="${!p.active}"
                >
                    <td>#${p.id}</td>

                    <td>
                        <img src="${imageUrl}"
                             alt="${p.name}">
                    </td>

                    <td>${p.name}</td>
                    <td>${p.categoryName}</td>
                    <td>
                        <fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/> ₫
                    </td>

                    <td>${p.inventory}</td>

                    <td>${p.unit}</td>

                    <td>
                        <button class="btn-action btn-edit" data-target="#edit-product-modal">
                            <i class="fas fa-edit"></i>
                        </button>

                        <button class="btn-action btn-delete" data-target="#delete-product-modal">
                            <i class="fas fa-trash"></i>
                        </button>

                        <button class="btn-action btn-hide">
                            <i class="fa-regular ${!p.active ? 'fa-eye-slash' : 'fa-eye'}"></i>
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<!-- Form modal create product -->
<div id="create-product-modal" class="modal">
    <div class="modal-content">
        <header class="modal-header">
            <h2 class="modal-title">Tạo Sản Phẩm Mới</h2>
            <button class="close-btn">&times;</button>
        </header>
        <div class="modal-body">
            <form id="create-product-form" class="product-form"
                  action="${pageContext.request.contextPath}/admin/api/create-product"
                  method="post"
                  enctype="multipart/form-data">
                <div class="form-group">
                    <label for="newFullProductName">Tên</label>
                    <input type="text" name="name" id="newFullProductName" placeholder="Nhập tên sản phẩm" required>
                </div>
                <div class="form-group">
                    <label>Danh mục</label>
                    <select name="categoryId" class="form-control" required>
                        <c:forEach var="c" items="${categoriesList}">
                            <option value="${c.id}">${c.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="newPrice">Giá(₫)</label>
                    <input type="number" name="price" id="newPrice" placeholder="Nhập giá sản phẩm" min="0" required>
                </div>
                <div class="form-group">
                    <label for="newInventory">Số lượng Tồn kho hiện tại</label>
                    <input type="number" name="inventory" id="newInventory" placeholder="Nhập số tồn kho" min="0"
                           required>
                </div>
                <div class="form-group">
                    <label for="newInventoryUnit">Đơn vị</label>
                    <input type="text" name="unit" id="newInventoryUnit" placeholder="Nhập đơn vị" required>
                </div>
                <div class="form-group">
                    <label for="newPic" class="upload-label">Thêm hình ảnh</label>
                    <input type="file" id="newPic" name="new_image" class="file-input" accept="image/*">
                    <div class="preview-container">
                        <img id="imagePreview" src="" alt="Xem trước ảnh" class="image-preview">
                        <button type="button" id="removeImageBtn" class="remove-btn">Xóa</button>
                    </div>
                </div>
            </form>
        </div>
        <footer class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
            <button type="submit" form="create-product-form" class="btn btn-primary">
                <i class="fas fa-plus"></i> Tạo Sản Phẩm
            </button>
        </footer>
    </div>
</div>
<!-- Form modal edit product -->
<div id="edit-product-modal" class="modal">
    <div class="modal-content">
        <header class="modal-header">
            <h2 class="modal-title">Chỉnh Sửa Sản Phẩm</h2>
            <button class="close-btn">&times;</button>
        </header>

        <div class="modal-body">
            <form id="edit-product-form" class="product-form"
                  action="${pageContext.request.contextPath}/admin/edit-product" method="post"
                  enctype="multipart/form-data">
                <div class="form-group">
                    <label>Tên</label>
                    <input type="text" data-fill="name" name="name" placeholder="Nhập tên sản phẩm">
                </div>
                <div class="form-group">
                    <label>Danh mục</label>
                    <select name="categoryId" data-fill="categoryId" class="form-control" required>
                        <c:forEach var="c" items="${categoriesList}">
                            <option value="${c.id}">${c.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Giá(₫)</label>
                    <input type="number" data-fill="price" name="price" placeholder="Nhập giá sản phẩm">
                </div>
                <div class="form-group">
                    <label>Tồn kho</label>
                    <input type="number" data-fill="inventory" name="inventory" placeholder="Nhập số tồn kho">
                </div>
                <div class="form-group">
                    <label>Đơn vị</label>
                    <input type="text" data-fill="unit" name="unit" placeholder="Nhập đơn vị">
                </div>
                <div class="form-group">
                    <label>Trạng thái</label>
                    <div style="display: flex; gap: 10px; align-items: center;">
                        <input type="checkbox" id="edit-active" name="active" value="on">
                        <label for="edit-active" style="margin:0">Đang kinh doanh</label>
                    </div>
                </div>
                <div class="form-group">
                    <label>Hình ảnh sản phẩm</label>
                    <div class="image-upload-container">

                        <img id="edit-image-preview"
                             data-fill-src="imageSrc"
                             alt="Xem trước ảnh"
                             src="https://via.placeholder.com/200x200?text=No+Image">
                        <input type="file" id="edit-product-image" name="new_image" accept="image/*"
                               style="display: none;">
                        <div>
                            <button type="button" class="btn btn-outline-primary btn-sm"
                                    onclick="$('#edit-product-image').click();">
                                <i class="fas fa-camera"></i> Tải ảnh khác
                            </button>
                        </div>

                        <input type="hidden" data-fill="imageSrc" name="old_image">
                    </div>
                </div>

                <input type="hidden" data-fill="id" name="product-id">
            </form>
        </div>

        <footer class="modal-footer">
            <button type="button" class="btn btn-secondary"
                    data-dismiss="modal">Hủy
            </button>
            <button type="submit" form="edit-product-form" class="btn btn-primary">Lưu Thay Đổi</button>
        </footer>
    </div>
</div>
<!-- Form modal delete product -->
<div id="delete-product-modal" class="modal">
    <div class="modal-content">
        <header class="modal-header">
            <h2 class="modal-title">Xác nhận Xóa</h2>
            <button class="close-btn">&times;</button>
        </header>

        <div class="modal-body">
            <form id="delete-product-form" action="${pageContext.request.contextPath}/admin/api/delete-product"
                  method="post">
                <p>Bạn có chắc muốn xóa <strong data-fill-text="name"></strong> không?</p>
                <input type="hidden" data-fill="id" name="product-id">
                <div>
                    <i class="fas fa-info-circle"></i> Lưu ý: Nếu sản phẩm đã từng được bán, bạn sẽ không thể xóa. Hãy
                    chọn tính năng <strong>Ẩn/Hiện</strong> thay thế.
                </div>
            </form>
        </div>

        <footer class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
            <button type="submit" form="delete-product-form" class="btn btn-danger">Xóa</button>
        </footer>
    </div>
</div>
<%-- Preview picture --%>
<div id="image-viewer-modal">
    <div class="modal-content-img">
        <span class="close-btn-img">&times;</span>
        <img id="full-size-image" src="" alt="Full Size">
    </div>
</div>