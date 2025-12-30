<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- Cart page content - included in main layout --%>

<main class="cart-page">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <h1 class="cart-page__title">Giỏ hàng của bạn</h1>
            </div>
        </div>
        
        <div class="row">
            <div class="col-lg-8">
                <c:choose>
                    <c:when test="${empty cart.items}">
                        <div class="cart-page__empty">
                            <i class="fi fi-rr-shopping-cart icon-base cart-page__empty-icon"></i>
                            <h2>Giỏ hàng trống</h2>
                            <p>Hãy thêm sản phẩm vào giỏ hàng để tiếp tục</p>
                            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">
                                Tiếp tục mua sắm
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="cart-page__items" data-cart-items>
                            <c:forEach var="item" items="${cart.items}">
                                <c:set var="cartItem" value="${item}" scope="request"/>
                                <jsp:include page="/WEB-INF/views/client/fragments/cart-item.jsp"/>
                            </c:forEach>
                        </div>
                        
                        <div class="cart-page__actions-secondary">
                            <button type="button" 
                                    class="btn btn-outline-danger btn-sm"
                                    data-cart-clear>
                                <i class="fi fi-rr-trash icon-base"></i>
                                Xóa tất cả
                            </button>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <c:if test="${not empty cart.items}">
                <div class="col-lg-4">
                    <div class="cart-page__summary">
                        <h3 class="cart-page__summary-title">Tổng đơn hàng</h3>
                        
                        <div class="cart-page__summary-row">
                            <span>Số lượng</span>
                            <span data-cart-count>${cart.totalQty} sản phẩm</span>
                        </div>
                        
                        <div class="cart-page__summary-row cart-page__summary-total">
                            <span>Tổng cộng</span>
                            <span data-cart-total>
                                <fmt:formatNumber value="${cart.totalPrice}" type="number" groupingUsed="true"/>đ
                            </span>
                        </div>
                        
                        <a href="${pageContext.request.contextPath}/checkout" 
                           class="btn btn-primary btn-lg w-100">
                            Tiến hành thanh toán
                        </a>
                        
                        <a href="${pageContext.request.contextPath}/home" 
                           class="btn btn-outline-secondary w-100 mt-2">
                            Tiếp tục mua sắm
                        </a>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</main>
