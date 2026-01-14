// Tính toán thời gian trôi qua
function calculateTimeAgo(timestampStr) {
    if (!timestampStr) return '';
    const now = new Date();
    const past = new Date(timestampStr);
    const diffMs = now - past;
    const diffMins = Math.floor(diffMs / 60000);

    if (diffMins < 1) return 'Vừa xong';
    if (diffMins < 60) return diffMins + ' phút trước';
    const diffHours = Math.floor(diffMins / 60);
    if (diffHours < 24) return diffHours + ' giờ trước';
    return 'Hôm qua';
}

// Hàm format tiền tệ VNĐ
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency', currency: 'VND'
    }).format(amount);
}

// Hàm hiển thị thông báo (Toast Alert)
function showNotification(message, type) {
    $('.alert').remove();

    const alertType = type === 'success' ? 'alert-success' : 'alert-danger';
    const title = type === 'success' ? 'Thành công!' : 'Thông báo:';

    const alertHtml = `
        <div class="alert ${alertType}" role="alert">
            <strong>${title}</strong> ${message}
        </div>
    `;

    $('body').append(alertHtml);
    if ($(".alert").length) {
        window.setTimeout(function () {
            $(".alert").fadeTo(500, 0).slideUp(500, function () {
                $(this).remove();
            });
        }, 3000);
    }
}

// Load danh sách đơn hàng Live
function loadLiveOrders() {
    const $container = $('.order-list');
    if (!$container.length) return;

    $.ajax({
        url: 'admin/api/live-orders', method: 'GET', dataType: 'json', success: function (orders) {
            $container.empty();

            if (!orders || orders.length === 0) {
                $container.html('<li style="text-align:center; padding: 20px; color:#999;">Không có đơn hàng nào đang chờ.</li>');
                return;
            }

            orders.forEach(order => {
                const timeAgo = calculateTimeAgo(order.openedAt);
                const priceFormatted = formatCurrency(order.totalPrice);

                let statusClass = 'status-new';
                let actionsHtml = '';

                if (order.status === 'pending') {
                    statusClass = 'status-new';
                    // Dùng data-action để ủy quyền sự kiện
                    actionsHtml = `
                        <button class="btn btn-confirm btn-live-action" data-id="${order.id}" data-action="confirm">
                            <i class="fas fa-check"></i> Xác nhận
                        </button>
                        <button class="btn btn-cancel btn-live-action" data-id="${order.id}" data-action="cancel">
                            <i class="fas fa-times"></i> Hủy
                        </button>
                    `;
                } else if (order.status === 'accept') {
                    statusClass = 'status-processing';
                    // Nút mở modal thanh toán
                    actionsHtml = `
                        <button class="btn btn-payment btn-open-payment" 
                                data-id="${order.id}" 
                                data-table="${order.tableName}" 
                                data-price="${priceFormatted}">
                            <i class="fa-solid fa-money-check"></i> Thanh toán
                        </button>
                    `;
                }

                const html = `
                    <li class="order-item ${statusClass}">
                        <div class="order-table">
                            <i class="fas fa-user-circle"></i>
                            <span>${order.tableName}</span>
                        </div>
                        <div class="order-details">
                            <p class="order-id">#DH-${order.id}
                                <span class="order-time">${timeAgo}</span>
                            </p>
                            <p class="order-summary">${order.itemSummary || ''}</p>
                        </div>
                        <p class="order-total">${priceFormatted}</p>
                        <div class="order-actions">
                            ${actionsHtml}
                        </div>
                    </li>
                `;
                $container.append(html);
            });
        }
    });
}

// Cập nhật trạng thái đơn hàng (Confirm/Cancel/Pay)
function updateOrderStatus(id, action) {
    if (action === 'cancel' && !confirm('Bạn chắc chắn muốn hủy đơn hàng này?')) {
        return;
    }

    $.ajax({
        url: 'admin/api/live-orders', method: 'POST', data: {id: id, action: action}, success: function (res) {
            if (res.success) {
                showNotification(res.message, 'success');
                loadLiveOrders();
            } else {
                showNotification(res.message, 'error');
            }
        }, error: function () {
            showNotification("Lỗi kết nối server!", 'error');
        }
    });
}

// Biến lưu ID đơn hàng đang thanh toán
let currentOrderIdToPay = null;

// Mở Modal Thanh Toán
function openPaymentModal(id, tableName, totalFormatted) {
    currentOrderIdToPay = id;

    $('#payment-table-name').text(tableName);
    $('#payment-order-id').text('#DH-' + id);
    $('#payment-total').text(totalFormatted);
    $('#cancel-note').val('');

    $('#payment-modal').show();
}

// Load số liệu tổng quan (4 card trên cùng)
function loadOverviewData(dateStr) {
    $('#val-revenue, #val-orders, #val-products, #val-customers').css('opacity', '0.5');

    $.ajax({
        url: 'admin/api/dashboard-overview',
        method: 'GET',
        data: {date: dateStr},
        dataType: 'json',
        success: function (data) {
            const numberFormatter = new Intl.NumberFormat('vi-VN');

            $('#val-revenue').text(formatCurrency(data.totalRevenue));
            $('#val-orders').text(numberFormatter.format(data.totalOrders));
            $('#val-products').text(numberFormatter.format(data.sellingProducts));
            $('#val-customers').text(numberFormatter.format(data.newCustomer));

            $('#val-revenue, #val-orders, #val-products, #val-customers').css('opacity', '1');
        },
        error: function () {
            showNotification("Không thể tải dữ liệu báo cáo!", "error");
            $('#val-revenue, #val-orders, #val-products, #val-customers').css('opacity', '1');
        }
    });
}

// Vẽ biểu đồ doanh thu
function setupRevenueChart() {
    const $ctx = $('#myRevenueChart');
    if ($ctx.length === 0) return;

    $.ajax({
        url: 'admin/api/chart-data', method: 'GET', dataType: 'json', success: function (serverData) {
            new Chart($ctx[0], {
                type: 'line', data: {
                    labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
                    datasets: [{
                        label: 'Doanh Thu (VNĐ)',
                        data: serverData,
                        borderColor: '#4a69bd',
                        backgroundColor: 'rgba(74, 105, 189, 0.1)',
                        fill: true,
                        tension: 0.4,
                        borderWidth: 2,
                        pointRadius: 4,
                        pointHoverRadius: 6
                    }]
                }, options: {
                    responsive: true, maintainAspectRatio: false, plugins: {
                        legend: {position: 'top'}, tooltip: {
                            callbacks: {
                                label: function (context) {
                                    let label = context.dataset.label || '';
                                    if (label) label += ': ';
                                    if (context.parsed.y !== null) {
                                        label += formatCurrency(context.parsed.y);
                                    }
                                    return label;
                                }
                            }
                        }
                    }, scales: {
                        y: {
                            beginAtZero: true, ticks: {
                                callback: function (value) {
                                    return new Intl.NumberFormat('vi-VN').format(value);
                                }
                            }
                        }
                    }
                }
            });
        }, error: function (err) {
            console.error("Lỗi tải biểu đồ:", err);
        }
    });
}

// Sort sản phẩm bán chạy
function sortBestsellerItems() {
    const $sortSelect = $('#bestseller-sort');
    const $bestsellerList = $('#bestseller-list');
    const sortOrder = $sortSelect.val();
    const items = $bestsellerList.find('.product-item').get();

    items.sort((a, b) => {
        const salesA = parseInt($(a).data('sales'), 10);
        const salesB = parseInt($(b).data('sales'), 10);
        return (sortOrder === 'desc') ? (salesB - salesA) : (salesA - salesB);
    });
    $bestsellerList.html('').append(items);
}

// Khởi tạo sort sản phẩm bán chạy
function initBestsellerSort() {
    const $sortSelect = $('#bestseller-sort');
    if (!$sortSelect.length || !$('#bestseller-list').length) return;
    sortBestsellerItems();
    $sortSelect.on('change', sortBestsellerItems);
}

// Hàm tải dữ liệu phụ (Low Stock, Best Seller, Logs)
function loadDashboardExtraData() {
    if (!$('.inventory-status').length) return;

    $.ajax({
        url: 'admin/api/dashboard-extra', method: 'GET', dataType: 'json', success: function (data) {
            renderLowStock(data.lowStock);
            renderBestSellers(data.bestSellers);
            renderActivities(data.activities);
        }
    });
}

// Hàm xử lý Cảnh báo tồn kho
function renderLowStock(items) {
    const $container = $('.inventory-list');
    $container.empty();

    if (!items || items.length === 0) {
        $container.html('<li class="empty-state-text">Không có sản phẩm sắp hết hàng.</li>');
        return;
    }

    items.forEach(item => {
        const typeClass = item.stock <= 3 ? 'critical' : 'low';
        const img = item.imageUrl ? (ctx + '/' + item.imageUrl) : 'https://via.placeholder.com/50';

        const html = `
            <li class="inventory-item ${typeClass}">
                <img src="${img}" alt="Product" onerror="this.src='https://via.placeholder.com/50'">
                <div class="product-details">
                    <p class="product-name">${item.name}</p>
                    <p class="product-sku">${item.sku}</p>
                </div>
                <p class="product-stock">Còn ${item.stock} sp</p>
            </li>
        `;
        $container.append(html);
    });
}

renderBestSellers

// Hàm xử lý sản phẩm bán chạy
function renderBestSellers(items) {
    const $container = $('#bestseller-list');
    $container.empty();

    if (!items || items.length === 0) {
        $container.html('<li class="empty-state-text">Chưa có dữ liệu bán hàng.</li>');
        return;
    }

    items.forEach(item => {
        const img = item.imageUrl ? (ctx + '/' + item.imageUrl) : 'https://via.placeholder.com/50';
        const html = `
            <li class="product-item" data-sales="${item.totalSold}">
                <img src="${img}" alt="Product" onerror="this.src='https://via.placeholder.com/50'">
                <div class="product-details">
                    <p class="product-name">${item.name}</p>
                    <p class="product-category">${item.categoryName}</p>
                </div>
                <p class="product-sales">${item.totalSold} sp</p>
            </li>
        `;
        $container.append(html);
    });
    // Sort lại theo dropdown hiện tại
    sortBestsellerItems();
}

// Hàm xử lý hiển thị log gần đây
function renderActivities(items) {
    const $container = $('.activity-feed');
    $container.empty();

    if (!items || items.length === 0) {
        $container.html('<li class="empty-state-text">Chưa có hoạt động nào.</li>');
        return;
    }

    items.forEach(item => {
        const timeAgo = calculateTimeAgo(item.timeAgo);
        const html = `
            <li class="activity-item">
                <div class="activity-icon ${item.type}">
                    <i class="${item.icon}"></i>
                </div>
                <div class="activity-details">
                    <p>${item.description}</p>
                    <span class="activity-time">${timeAgo}</span>
                </div>
            </li>
        `;
        $container.append(html);
    });
}

// Xuất Excel
function excelExport() {
    $('#export-report-btn').on('click', function () {
        let wb = XLSX.utils.book_new();
        let summaryData = [];
        summaryData.push(["Tiêu Đề", "Giá Trị", "So Sánh"]);
        $('.revenue-summary .summary-card').each(function () {
            var $card = $(this);
            var title = $card.find('.summary-title').text();
            var value = $card.find('.summary-value').text();
            var comparison = $card.find('.summary-comparison').text().trim();
            summaryData.push([title, value, comparison]);
        });
        const ws_summary = XLSX.utils.aoa_to_sheet(summaryData);
        XLSX.utils.book_append_sheet(wb, ws_summary, "Tóm Tắt Doanh Thu");
        const table = $('#revenue .data-table')[0];
        const ws_details = XLSX.utils.table_to_sheet(table);
        XLSX.utils.book_append_sheet(wb, ws_details, "Chi Tiết Theo Tháng");
        XLSX.writeFile(wb, "BaoCaoDoanhThu.xlsx");
    })
}

// Xử lý Form Nhập/Xuất kho nhanh
function setupQuickInventory() {
    const $form = $('#inventory-form');
    if (!$form.length) return;

    $form.find('.btn-import').click(function (e) {
        e.preventDefault();
        handleQuickAction('import');
    });

    $form.find('.btn-export').click(function (e) {
        e.preventDefault();
        handleQuickAction('export');
    });
}

function handleQuickAction(type) {
    const productVal = $('#product-select').val().trim();
    const qtyVal = $('#product-quantity').val();

    if (!productVal) {
        showNotification('Vui lòng nhập Tên hoặc ID sản phẩm', 'error');
        return;
    }

    $.ajax({
        url: 'admin/api/quick-inventory', method: 'POST', data: {
            product: productVal, quantity: qtyVal, type: type
        }, success: function (res) {
            if (res.success) {
                showNotification(res.message, 'success');
                $('#product-select').val('');
                $('#product-quantity').val(1);
                // Reload lại dữ liệu dashboard
                loadDashboardExtraData();
            } else {
                showNotification(res.message, 'error');
            }
        }, error: function () {
            showNotification('Lỗi kết nối server', 'error');
        }
    });
}

// Datepicker cho Staff
function initDateStaffWork(input) {
    const dateInput = $(input);
    if (!dateInput.length) return;
    new Litepicker({
        element: dateInput[0],
        singleMode: true,
        allowInput: true,
        format: 'DD/MM/YYYY',
        startDate: new Date(),
        footer: true,
        lang: 'vi-VN'
    });
}

// Tìm kiếm động
function setupDynamicSearch(inputSelector, tableBodySelector, columnsToSearch) {
    $(inputSelector).on('input', function () {
        const searchTerm = $(this).val().trim().toLowerCase();
        const $rows = $(tableBodySelector).find('tr');

        $rows.each(function () {
            const $row = $(this);
            let isMatch = false;
            for (const colIndex of columnsToSearch) {
                const cellText = $row.find('td').eq(colIndex).text().toLowerCase();
                if (cellText.includes(searchTerm)) {
                    isMatch = true;
                    break;
                }
            }
            $row.toggle(isMatch);
        });
    });
}

// Khởi tạo lọc ngày
function initDateFilter(dateSelector, textSelector) {
    const dateBtn = $(dateSelector);
    const dateSpan = $(textSelector);
    if (!dateBtn.length || !dateSpan.length) return;

    const picker = new Litepicker({
        element: dateBtn[0],
        singleMode: true,
        format: 'DD/MM/YYYY',
        autoApply: true,
        startDate: new Date(),
        lang: 'vi-VN'
    });

    picker.on('selected', (date) => {
        dateSpan.text(date.format('DD/MM/YYYY'));
        loadOverviewData(date.format('YYYY-MM-DD'));
    });

    // Load lần đầu
    const start = picker.getStartDate();
    if (start) {
        dateSpan.text(start.format('DD/MM/YYYY'));
        loadOverviewData(start.format('YYYY-MM-DD'));
    }
}

// Modal set up
function setupModalTrigger(containerSelector, btnSelector) {
    $(containerSelector).on('click', btnSelector, function () {
        const $button = $(this);
        const $row = $button.closest('tr');
        const modalSelector = $button.data('target');
        const $modal = $(modalSelector);
        if ($modal.length === 0) return;

        const rowData = $row.data();
        $modal.trigger('modal:fillData', [rowData]);
        $modal.show();
    });
}

// Hàm fill data vào modal
function setupHandleModal() {
    $(document).on('modal:fillData', '.modal', function (event, data) {
        const $modal = $(this);
        for (var key in data) {
            if (data.hasOwnProperty(key)) {
                const value = data[key];

                const $input = $modal.find('[data-fill="' + key + '"]');
                if ($input.length) $input.val(value);

                const $text = $modal.find('[data-fill-text="' + key + '"]');
                if ($text.length) $text.text(value);

                const $img = $modal.find('[data-fill-src="' + key + '"]');
                if ($img.length) {
                    $img.attr('src', value || 'https://via.placeholder.com/200x200?text=No+Image').show();
                }

                if (key === 'hide') {
                    $modal.find('input[name="active"]').prop('checked', !value);
                }
            }
        }
    });
}

// Đóng modal
function closeModal() {
    $('.modal .close-btn, .modal [data-dismiss="modal"]').on('click', function () {
        $(this).closest('.modal').hide();
    });
    $(window).on('click', function (event) {
        if ($(event.target).hasClass('modal')) $(event.target).hide();
    });
}

// Ẩn hiện item
function hideItem() {
    $(document).on('click', '.btn-hide', function (e) {
        e.preventDefault();
        const $btn = $(this);
        const $row = $btn.closest("tr");
        const id = $row.data('id');
        const isCurrentlyHidden = $row.attr("data-hide") === "true";
        const newActiveStatus = isCurrentlyHidden;

        $.ajax({
            url: 'admin/api/product-status',
            method: 'POST',
            data: {id: id, active: newActiveStatus},
            success: function () {
                showNotification("Cập nhật trạng thái thành công!", "success");
                const newHiddenState = !isCurrentlyHidden;
                $row.attr("data-hide", newHiddenState);

                if (newHiddenState) {
                    $row.addClass("hidden-row");
                    $btn.html('<i class="fa-regular fa-eye-slash"></i>');
                } else {
                    $row.removeClass("hidden-row");
                    $btn.html('<i class="fa-regular fa-eye"></i>');
                }
            },
            error: function () {
                showNotification("Lỗi kết nối!", "error");
            }
        });
    });
}

// Plugin preview hình ảnh
$.fn.setupImagePreview = function (previewSelector, removeSelector) {
    return this.each(function () {
        const $input = $(this);
        const $preview = $(previewSelector);
        const $removeBtn = $(removeSelector);

        $input.on('change', function () {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    $preview.attr('src', e.target.result).show();
                    $removeBtn.show();
                }
                reader.readAsDataURL(file);
            }
        });

        $removeBtn.on('click', function () {
            $preview.attr('src', '').hide();
            $removeBtn.hide();
            $input.val(null);
        });
    });
};

// Hàm khởi tạo chung
$(function () {
    // 1. Setup chung
    closeModal();
    setupHandleModal();
    excelExport();

    // Auto hide alert
    if ($(".alert").length) {
        setTimeout(function () {
            $(".alert").fadeOut();
        }, 3000);
    }

    // 2. DASHBOARD
    if ($('.live-orders').length) {
        loadLiveOrders();
        setInterval(loadLiveOrders, 30000);

        // Event delegation cho Live Orders
        $('.order-list').on('click', '.btn-live-action', function () {
            updateOrderStatus($(this).data('id'), $(this).data('action'));
        });
        $('.order-list').on('click', '.btn-open-payment', function () {
            openPaymentModal($(this).data('id'), $(this).data('table'), $(this).data('price'));
        });

        loadDashboardExtraData();
        setInterval(loadDashboardExtraData, 60000);
        setupQuickInventory();

        // Setup Bestsellers Sort
        initBestsellerSort();
    }

    // Setup Date Filter & Chart
    if ($('#date-filter-button').length) {
        initDateFilter('#date-filter-button', '#date-filter-text');
        setupRevenueChart();
    }

    // Nút trong Modal Thanh toán
    $('#btn-confirm-payment').click(function () {
        if (currentOrderIdToPay) {
            updateOrderStatus(currentOrderIdToPay, 'pay');
            $('#payment-modal').hide();
        }
    });

    $('#btn-confirm-cancel-order').click(function () {
        if (currentOrderIdToPay) {
            updateOrderStatus(currentOrderIdToPay, 'cancel');
            $('#payment-modal').hide();
        }
    });

    // Setup đóng modal thanh toán
    closeModal('', '#payment-modal');

    // 3. PAGE: PRODUCTS
    if ($('#product').length) {
        setupDynamicSearch('#product-search-input', '#product .data-table tbody', [0, 2]);
        setupModalTrigger('#product', '.btn-edit');
        setupModalTrigger('#product', '.btn-delete');
        hideItem();

        $('#create-product-btn').click(function () {
            $('#create-product-modal').show();
        });
        if ($('#newPic').length) $('#newPic').setupImagePreview('#imagePreview', '#removeImageBtn');
        $('#edit-product-image').setupImagePreview('#edit-image-preview', '#fake-remove-btn');

        // Image Viewer Fullsize
        $('#edit-image-preview').on('click', function () {
            const currentSrc = $(this).attr('src');
            if (!currentSrc || currentSrc.includes('via.placeholder.com')) return;
            $('#full-size-image').attr('src', currentSrc);
            $('#image-viewer-modal').css('display', 'flex').hide().fadeIn(200);
        });
        $('#image-viewer-modal .close-btn-img, #image-viewer-modal').on('click', function (e) {
            if (e.target.id !== 'full-size-image') $('#image-viewer-modal').fadeOut(200);
        });
    }

    // 4. PAGE: ORDERS
    if ($('#order').length) {
        setupDynamicSearch('#order-search-input', '#order .data-table tbody', [0, 1]);

        setupModalTrigger('#order', '.btn-viewdetail');

        $('#order-detail-modal').on('modal:fillData', function (e, data) {
            const orderId = data.id;
            const $tbody = $('#modal-order-items-body');

            // Hiển thị loading
            $tbody.html('<tr><td colspan="5" class="text-center">Đang tải chi tiết món ăn...</td></tr>');

            $.ajax({
                url: 'admin/api/order-details',
                method: 'GET',
                data: {id: orderId},
                dataType: 'json',
                success: function (items) {
                    let html = '';
                    if (!items || items.length === 0) {
                        html = '<tr><td colspan="5" class="text-center">Đơn hàng trống.</td></tr>';
                    } else {
                        items.forEach(item => {
                            const price = formatCurrency(item.price);
                            const total = formatCurrency(item.price * item.quantity);

                            html += `
                            <tr>
                                <td>${item.productName}</td>
                                <td class="text-center">${item.quantity}</td>
                                <td>${price}</td>
                                <td style="font-weight:bold;">${total}</td>
                                <td style="font-style:italic; color:#666;">${item.note || ''}</td>
                            </tr>
                        `;
                        });
                    }
                    $tbody.html(html);
                },
                error: function () {
                    $tbody.html('<tr><td colspan="5" class="text-center text-danger">Không thể tải dữ liệu!</td></tr>');
                }
            });
        });
    }

    // 5. PAGE: ACCOUNTS
    if ($('#account').length) {
        if (typeof setupModalTrigger === 'function') {
            setupModalTrigger('#account', '.btn-edit');
            setupModalTrigger('#account', '.btn-delete');
        }

        // Xử lý đóng modal
        $('.close-btn').click(function () {
            $('.modal').hide();
        });

        // 2. Ajax Create
        $('#btn-submit-create').click(function () {
            $.ajax({
                url: ctx + '/admin/api/create-account',
                method: 'POST',
                data: $('#create-account-form').serialize(),
                dataType: 'json',
                success: function (res) {
                    alert(res.message);
                    if (res.success) location.reload();
                },
                error: function () {
                    alert("Lỗi kết nối server");
                }
            });
        });

        // 3. Ajax Edit
        $('#btn-submit-edit').click(function () {
            $.ajax({
                url: ctx + '/admin/api/edit-account',
                method: 'POST',
                data: $('#edit-account-form').serialize(),
                dataType: 'json',
                success: function (res) {
                    alert(res.message);
                    if (res.success) location.reload();
                },
                error: function () {
                    alert("Lỗi kết nối server");
                }
            });
        });

        // 4. Ajax Delete
        $('#btn-submit-delete').click(function () {
            const id = $('#delete-id').val();
            $.ajax({
                url: ctx + '/admin/api/delete-account',
                method: 'POST',
                data: {id: id},
                dataType: 'json',
                success: function (res) {
                    alert(res.message);
                    if (res.success) location.reload();
                },
                error: function () {
                    alert("Lỗi kết nối server");
                }
            });
        });
    }

    // 6. PAGE: STAFF
    if ($('#staff').length) {
        setupModalTrigger('#staff', '.btn-edit');
        setupModalTrigger('#staff', '.btn-delete');
        initDateStaffWork('#createStaffJoinDate');
    }

    // . PAGE: LOG
    if ($('#log').length) {
        if (typeof calculateTimeAgo === 'function') {
            $('.log-timestamp').each(function () {
                const rawTime = $(this).data('time');
                if (rawTime) {
                    $(this).text(calculateTimeAgo(rawTime));
                    $(this).attr('title', rawTime);
                }
            });
        }

        $('#log-search-input').on('keyup', function () {
            let value = $(this).val().toLowerCase();
            $('.log-entry').filter(function () {
                $(this).toggle($(this).data('search').toLowerCase().indexOf(value) > -1)
            });
        });
    }
});