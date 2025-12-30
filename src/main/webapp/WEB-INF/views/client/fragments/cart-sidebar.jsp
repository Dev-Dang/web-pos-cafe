<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- Cart sidebar fragment - loaded via AJAX for logged-in users --%>
<div class="cart-panel" data-cart-sidebar>
    <h5 class="cart-title mb-3">
        Giỏ hàng
        <span class="cart-count" data-cart-count>${cart.totalQty}</span>
    </h5>

    <div class="cart-list" data-cart-list data-cart-items>
        <c:choose>
            <c:when test="${empty cart.items}">
                <div class="cart-empty">
                    <div class="cart-empty__icon">
                        <i class="fi fi-rr-shopping-cart icon-base"></i>
                    </div>
                    <p class="cart-empty__text">Giỏ hàng trống</p>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="item" items="${cart.items}">
                    <c:set var="cartItem" value="${item}" scope="request"/>
                    <jsp:include page="cart-item.jsp"/>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${not empty cart.items}">
        <div class="cart-summary">
            <div class="cart-summary__row total">
                <span>Tổng cộng</span>
                <span data-cart-total>
                    <fmt:formatNumber value="${cart.totalPrice}" type="number" groupingUsed="true"/>đ
                </span>
            </div>
        </div>
        <button class="btn btn-lg btn-primary--filled w-100" data-checkout-btn>
            Thanh toán
        </button>
    </c:if>
</div>
