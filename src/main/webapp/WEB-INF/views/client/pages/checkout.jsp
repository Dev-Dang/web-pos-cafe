<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<base href="${pageContext.request.contextPath}/">

<%-- ========= MAIN ========= --%>
<main class="page-main">
    <div class="page-container">
        <div class="row g-5 justify-content-center">
            <div class="col-lg-8 col-12">
                <section class="checkout-section">
                    <h2 class="checkout-title mb-4">Xác nhận đơn hàng</h2>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger mb-4" role="alert">
                            ${error}
                        </div>
                    </c:if>

                    <%-- Order Items Review --%>
                    <div class="checkout-items mb-4">
                        <h5 class="checkout-subtitle mb-3">Chi tiết đơn hàng</h5>
                        <div class="checkout-list">
                            <c:forEach var="item" items="${cart.items}">
                                <div class="checkout-item">
                                    <div class="checkout-item__info">
                                        <div class="checkout-item__name">${item.itemNameSnapshot}</div>
                                        <c:if test="${not empty item.options}">
                                            <div class="checkout-item__options">
                                                <c:forEach var="option" items="${item.options}" varStatus="status">
                                                    <span>${option.optionValueNameSnapshot}<c:if test="${!status.last}">, </c:if></span>
                                                </c:forEach>
                                            </div>
                                        </c:if>
                                        <c:if test="${not empty item.note}">
                                            <div class="checkout-item__note">
                                                <i class="fi fi-rr-comment-alt icon-base"></i>
                                                <span>${item.note}</span>
                                            </div>
                                        </c:if>
                                    </div>
                                    <div class="checkout-item__qty">x${item.qty}</div>
                                    <div class="checkout-item__price">
                                        <fmt:formatNumber value="${item.lineTotal}" type="number" groupingUsed="true"/>đ
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <%-- Order Summary --%>
                    <div class="checkout-summary mb-4">
                        <div class="checkout-summary__row">
                            <span>Số lượng sản phẩm</span>
                            <span>${cart.totalQty}</span>
                        </div>
                        <div class="checkout-summary__row checkout-summary__row--total">
                            <span>Tổng thanh toán</span>
                            <span class="checkout-total-price">
                                <fmt:formatNumber value="${cart.totalPrice}" type="number" groupingUsed="true"/>đ
                            </span>
                        </div>
                    </div>

                    <%-- Payment Actions --%>
                    <form method="post" action="${pageContext.request.contextPath}/checkout">
                        <div class="checkout-actions d-flex gap-3">
                            <a href="${pageContext.request.contextPath}/home" class="btn btn-lg btn-neutral--outlined flex-grow-1">
                                <i class="fi fi-rr-arrow-left icon-base"></i>
                                Quay lại
                            </a>
                            <button type="submit" class="btn btn-lg btn-primary--filled flex-grow-1">
                                <i class="fi fi-rr-credit-card icon-base"></i>
                                Thanh toán
                            </button>
                        </div>
                    </form>
                </section>
            </div>
        </div>
    </div>
</main>
