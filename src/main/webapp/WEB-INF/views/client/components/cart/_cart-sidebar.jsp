<%--
  Cart sidebar fragment - loaded via Unpoly for both guest and logged-in users
  Matches Figma design with existing CSS classes
  Author: Dang Van Trung
  Date: 08/01/2026  
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="cart-panel" id="cartAside" data-cart-sidebar data-node-id="11-2997">
    <!-- Cart Header -->
    <h6 class="cart-title" data-node-id="11-2999">
        Giỏ Hàng
        <span class="cart-count" data-cart-count>${cart.totalQty}</span>
    </h6>

    <!-- Cart Items List -->
    <div class="cart-list" data-cart-list data-cart-items data-node-id="11-3001">
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
                    <jsp:include page="_cart-item.jsp"/>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${not empty cart.items}">
        <!-- Reward Points Toggle Section -->
        <div class="cart-payment-setting" data-node-id="11-3427">
            <div class="cart-row" data-node-id="11-3424">
                <div class="cart-row__label" data-node-id="11-3425">
                    <i class="fi fi-rr-coins icon-base" data-node-id="26-2141"></i>
                    <span>Dùng 300 điểm tích luỹ</span>
                </div>
                <label class="cart-reward-switch" data-node-id="11-3447">
                    <input type="checkbox" 
                           class="cart-reward-checkbox" 
                           data-reward-toggle
                           ${cart.useRewardPoints ? 'checked' : ''}>
                    <span class="cart-reward-slider"></span>
                </label>
            </div>
        </div>

        <!-- Payment Summary -->
        <div class="cart-summary" data-node-id="11-3428">
            <h6 class="cart-summary__title" data-node-id="11-3430">Chi Tiết Thanh Toán</h6>
            
            <div class="cart-summary__content" data-node-id="11-3431">
                <!-- Subtotal Row -->
                <div class="cart-summary__row" data-node-id="11-3432">
                    <span data-node-id="11-3433">
                        Tạm tính (<span data-cart-total-qty>${cart.totalQty}</span> phần)
                    </span>
                    <span data-node-id="11-3434" data-cart-subtotal>
                        <fmt:formatNumber value="${cart.subtotal}" type="number" groupingUsed="true"/> Đ
                    </span>
                </div>
                
                <!-- Reward Points Discount Row -->
                <div class="cart-summary__row cart-summary__row--discount" 
                     data-node-id="11-3435" 
                     data-reward-discount-row
                     style="${cart.useRewardPoints ? '' : 'display: none;'}">
                    <span data-node-id="11-3436">Sử dụng điểm tích luỹ</span>
                    <span data-node-id="11-3437" data-reward-discount-amount class="text-success">
                        - <fmt:formatNumber value="${cart.rewardDiscount}" type="number" groupingUsed="true"/> Đ
                    </span>
                </div>
                
                <!-- Total Row with Border -->
                <div class="cart-divider"></div>
                <div class="cart-summary__row total" data-node-id="11-3441">
                    <span data-node-id="11-3442">Tổng thanh toán</span>
                    <span data-node-id="11-3443" data-cart-total>
                        <fmt:formatNumber value="${cart.total}" type="number" groupingUsed="true"/> Đ
                    </span>
                </div>
            </div>
        </div>

        <!-- Checkout Button -->
        <button class="btn btn-lg btn-primary--filled w-100" 
                data-checkout-btn
                data-node-id="26-2634">
            <span data-node-id="I26-2634;3-2975">Thanh Toán</span>
        </button>
    </c:if>
</div>
