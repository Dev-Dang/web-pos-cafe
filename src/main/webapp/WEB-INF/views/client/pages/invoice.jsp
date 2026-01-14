<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<base href="${pageContext.request.contextPath}/">

<%-- ========= MAIN ========= --%>
<main class="page-main">
    <div class="page-container">
        <div class="row g-5 justify-content-center">
            <div class="col-lg-8 col-12">
                <section class="invoice-section">
                    <%-- Invoice Header --%>
                    <div class="invoice-header text-center mb-4">
                        <div class="invoice-logo mb-2">
                            <i class="fi fi-sr-check-circle icon-success"></i>
                        </div>
                        <h2 class="invoice-title">Đặt hàng thành công!</h2>
                        <p class="invoice-subtitle text-muted">Cảm ơn bạn đã đặt hàng tại Zero Star Cafe</p>
                    </div>

                    <%-- Invoice Info --%>
                    <div class="invoice-card mb-4" id="invoice-print-area">
                        <div class="invoice-card__header">
                            <h5 class="invoice-card__title">Hóa đơn #${order.id}</h5>
                            <span class="invoice-status invoice-status--paid">Đã thanh toán</span>
                        </div>

                        <div class="invoice-meta mb-3">
                            <div class="invoice-meta__row">
                                <span class="invoice-meta__label">Ngày đặt:</span>
                                <span class="invoice-meta__value">
                                    <fmt:parseDate value="${order.openedAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDate" type="both"/>
                                    <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
                                </span>
                            </div>
                            <div class="invoice-meta__row">
                                <span class="invoice-meta__label">Nguồn:</span>
                                <span class="invoice-meta__value">${order.source.dbValue}</span>
                            </div>
                        </div>

                        <div class="invoice-divider"></div>

                        <%-- Invoice Items --%>
                        <div class="invoice-items">
                            <table class="invoice-table">
                                <thead>
                                    <tr>
                                        <th class="invoice-table__th">Sản phẩm</th>
                                        <th class="invoice-table__th text-center">SL</th>
                                        <th class="invoice-table__th text-end">Đơn giá</th>
                                        <th class="invoice-table__th text-end">Thành tiền</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${order.items}">
                                        <tr class="invoice-table__row">
                                            <td class="invoice-table__td">
                                                <div class="invoice-item-name">${item.itemNameSnapshot}</div>
                                                <c:if test="${not empty item.options}">
                                                    <div class="invoice-item-options">
                                                        <c:forEach var="option" items="${item.options}" varStatus="status">
                                                            <span>${option.optionValueNameSnapshot}<c:if test="${!status.last}">, </c:if></span>
                                                        </c:forEach>
                                                    </div>
                                                </c:if>
                                                <c:if test="${not empty item.note}">
                                                    <div class="invoice-item-note">Ghi chú: ${item.note}</div>
                                                </c:if>
                                            </td>
                                            <td class="invoice-table__td text-center">${item.qty}</td>
                                            <td class="invoice-table__td text-end">
                                                <fmt:formatNumber value="${item.unitPriceWithOptions}" type="number" groupingUsed="true"/>đ
                                            </td>
                                            <td class="invoice-table__td text-end">
                                                <fmt:formatNumber value="${item.lineTotal}" type="number" groupingUsed="true"/>đ
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div class="invoice-divider"></div>

                        <%-- Invoice Summary --%>
                        <div class="invoice-summary">
                            <div class="invoice-summary__row">
                                <span>Số lượng:</span>
                                <span>${order.totalQty} sản phẩm</span>
                            </div>
                            <div class="invoice-summary__row invoice-summary__row--total">
                                <span>Tổng cộng:</span>
                                <span class="invoice-total-price">
                                    <fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true"/>đ
                                </span>
                            </div>
                        </div>
                    </div>

                    <%-- Actions --%>
                    <div class="invoice-actions d-flex gap-3 justify-content-center">
                        <button type="button" class="btn btn-lg btn-neutral--outlined" onclick="window.print()">
                            <i class="fi fi-rr-print icon-base"></i>
                            In hóa đơn
                        </button>
                        <a href="${pageContext.request.contextPath}/home" class="btn btn-lg btn-primary--filled">
                            <i class="fi fi-rr-home icon-base"></i>
                            Về trang chủ
                        </a>
                    </div>
                </section>
            </div>
        </div>
    </div>
</main>

<%-- Print Styles --%>
<style>
    @media print {
        body * {
            visibility: hidden;
        }
        #invoice-print-area,
        #invoice-print-area * {
            visibility: visible;
        }
        #invoice-print-area {
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            padding: 20px;
        }
        .invoice-actions,
        .site-header,
        .site-footer,
        .bottom-nav {
            display: none !important;
        }
    }
</style>
