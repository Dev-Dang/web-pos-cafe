<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<fmt:setLocale value="vi_VN"/>

<div id="revenue" class="page-content">
    <header>
        <h1>Báo Cáo Doanh Thu</h1>
        <button class="btn btn-primary" id="export-report-btn">
            <i class="fas fa-file-export"></i> Xuất Báo Cáo
        </button>
    </header>

    <div class="revenue-summary">
        <div class="summary-card">
            <p class="summary-title">Doanh Thu Thuần</p>
            <p class="summary-value">
                <fmt:formatNumber value="${cardRevenue}" type="currency"/>
            </p>
            <p class="summary-comparison ${cardGrowth >= 0 ? 'positive' : 'negative'}">
                <i class="fas fa-arrow-${cardGrowth >= 0 ? 'up' : 'down'}"></i>
                <fmt:formatNumber value="${cardGrowth}" maxFractionDigits="1"/>% so với tháng trước
            </p>
        </div>

        <div class="summary-card">
            <p class="summary-title">Tổng Đơn Hàng</p>
            <p class="summary-value">
                <fmt:formatNumber value="${cardOrders}"/>
            </p>
            <p class="summary-comparison positive">
                <i class="fas fa-receipt"></i> Đơn đã thanh toán
            </p>
        </div>

        <div class="summary-card">
            <p class="summary-title">Sản Phẩm Đã Bán</p>
            <p class="summary-value">
                <fmt:formatNumber value="${cardProducts}"/>
            </p>
            <p class="summary-comparison positive">
                <i class="fas fa-coffee"></i> Ly/Món
            </p>
        </div>

        <div class="summary-card">
            <p class="summary-title">Giá Trị Đơn TB</p>
            <p class="summary-value">
                <fmt:formatNumber value="${cardAvg}" type="currency"/>
            </p>
            <p class="summary-comparison">Trung bình/đơn</p>
        </div>
    </div>

    <div class="data-table-container">
        <h2>Lịch Sử Doanh Thu Theo Tháng</h2>
        <table class="data-table">
            <thead>
            <tr>
                <th>Thời Gian</th>
                <th>Số Đơn Hàng</th>
                <th>Số Sản Phẩm</th>
                <th>Tổng Doanh Thu</th>
                <th>Tăng Trưởng</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${monthlyList}">
                <tr>
                    <td>Tháng ${item.month}, ${item.year}</td>
                    <td>${item.totalOrders}</td>
                    <td>${item.totalProducts}</td>
                    <td style="font-weight: bold;">
                        <fmt:formatNumber value="${item.totalRevenue}" type="currency"/>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${item.growth > 0}">
                                <span class="summary-comparison positive">
                                    <i class="fas fa-arrow-up"></i> +<fmt:formatNumber value="${item.growth}"
                                                                                       maxFractionDigits="1"/>%
                                </span>
                            </c:when>
                            <c:when test="${item.growth < 0}">
                                <span class="summary-comparison negative">
                                    <i class="fas fa-arrow-down"></i> <fmt:formatNumber value="${item.growth}"
                                                                                        maxFractionDigits="1"/>%
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="summary-comparison" style="color: #999;">-</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty monthlyList}">
                <tr>
                    <td colspan="5" class="text-center">Chưa có dữ liệu.</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>