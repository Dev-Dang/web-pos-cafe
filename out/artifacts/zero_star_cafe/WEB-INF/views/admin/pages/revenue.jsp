<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
    <!--Revenue-->
    <div id="revenue" class="page-content">
        <header>
            <h1>Báo Cáo Doanh Thu</h1>
            <button class="btn btn-primary" id="export-report-btn"><i class="fas fa-file-export"></i> Xuất Báo Cáo
            </button>
        </header>

        <div class="revenue-summary">
            <div class="summary-card">
                <p class="summary-title">Doanh Thu Thuần</p>
                <p class="summary-value">980,500,000 ₫</p>
                <p class="summary-comparison positive"><i class="fas fa-arrow-up"></i> 12.5% so với tháng trước</p>
            </div>
            <div class="summary-card">
                <p class="summary-title">Lợi Nhuận</p>
                <p class="summary-value">420,000,000 ₫</p>
                <p class="summary-comparison positive"><i class="fas fa-arrow-up"></i> 9.8% so với tháng trước</p>
            </div>
            <div class="summary-card">
                <p class="summary-title">Chi Phí</p>
                <p class="summary-value">55,200,000 ₫</p>
                <p class="summary-comparison negative"><i class="fas fa-arrow-down"></i> 5.2% so với tháng trước</p>
            </div>
            <div class="summary-card">
                <p class="summary-title">Đơn Hàng Trung Bình</p>
                <p class="summary-value">845,000 ₫</p>
                <p class="summary-comparison positive"><i class="fas fa-arrow-up"></i> 2.1% so với tháng trước</p>
            </div>
        </div>

        <div class="data-table-container">
            <h2>Doanh Thu Theo Tháng</h2>
            <table class="data-table">
                <thead>
                <tr>
                    <th>Tháng</th>
                    <th>Số Đơn Hàng</th>
                    <th>Doanh Thu</th>
                    <th>Thay Đổi</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Tháng 10, 2025</td>
                    <td>1,480</td>
                    <td>1,250,000,000 ₫</td>
                    <td>
                        <span class="summary-comparison positive">+15%</span>
                    </td>
                </tr>
                <tr>
                    <td>Tháng 9, 2025</td>
                    <td>1,250</td>
                    <td>1,080,000,000 ₫</td>
                    <td>
                        <span class="summary-comparison positive">+8%</span>
                    </td>
                </tr>
                <tr>
                    <td>Tháng 8, 2025</td>
                    <td>1,100</td>
                    <td>990,000,000 ₫</td>
                    <td>
                        <span class="summary-comparison negative">-2%</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
