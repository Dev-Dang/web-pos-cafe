<%--
  Cart Panel - Main container matching Figma design
  Handles both guest (localStorage) and logged-in users (database)
  Author: Dang Van Trung  
  Date: 08/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<aside class="cart-panel" data-cart-panel data-node-id="11-2997">
    <!-- Cart Header -->
    <h6 class="cart-title" data-node-id="11-2999">
        Giỏ Hàng
        <span class="cart-count" data-cart-count>
            <c:out value="${cart.totalQty}" default="0"/>
        </span>
    </h6>
    
    <!-- Cart Items List -->
    <div class="cart-list" data-cart-list data-node-id="11-3001">
        <c:choose>
            <c:when test="${not empty sessionScope.authUser}">
                <!-- Logged-in users -->
                <c:choose>
                    <c:when test="${not empty cart and not empty cart.items}">
                        <c:forEach var="item" items="${cart.items}">
                            <c:set var="cartItem" value="${item}" scope="request"/>
                            <jsp:include page="_cart-item.jsp"/>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <!-- Empty cart for logged-in users -->
                        <div class="cart-empty">
                            <div class="cart-empty__icon">
                                <i class="fi fi-rr-shopping-cart icon-base"></i>
                            </div>
                            <p class="cart-empty__text">Giỏ hàng trống</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <!-- Guest users: Items rendered by JavaScript from localStorage -->
                <div class="cart-empty" data-guest-empty style="display: none;">
                    <div class="cart-empty__icon">
                        <i class="fi fi-rr-shopping-cart icon-base"></i>
                    </div>
                    <p class="cart-empty__text">Giỏ hàng trống</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

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
                    Tạm tính (<span data-cart-total-qty>${cart.totalQty != null ? cart.totalQty : 0}</span> phần)
                </span>
                <span data-node-id="11-3434" data-cart-subtotal>
                    <c:choose>
                        <c:when test="${not empty cart}">
                            <fmt:formatNumber value="${cart.subtotal}" type="number" groupingUsed="true"/>
                        </c:when>
                        <c:otherwise>0</c:otherwise>
                    </c:choose> Đ
                </span>
            </div>
            
            <!-- Reward Points Discount Row -->
            <div class="cart-summary__row cart-summary__row--discount" 
                 data-node-id="11-3435" 
                 data-reward-discount-row
                 style="${cart.useRewardPoints ? '' : 'display: none;'}">
                <span data-node-id="11-3436">Sử dụng điểm tích luỹ</span>
                <span data-node-id="11-3437" data-reward-discount-amount class="text-success">
                    <c:choose>
                        <c:when test="${cart.useRewardPoints}">
                            - <fmt:formatNumber value="${cart.rewardDiscount}" type="number" groupingUsed="true"/>
                        </c:when>
                        <c:otherwise>- 0</c:otherwise>
                    </c:choose> Đ
                </span>
            </div>
            
            <!-- Total Row with Border -->
            <div class="cart-divider"></div>
            <div class="cart-summary__row total" data-node-id="11-3441">
                <span data-node-id="11-3442">Tổng thanh toán</span>
                <span data-node-id="11-3443" data-cart-total>
                    <c:choose>
                        <c:when test="${not empty cart}">
                            <fmt:formatNumber value="${cart.total}" type="number" groupingUsed="true"/>
                        </c:when>
                        <c:otherwise>0</c:otherwise>
                    </c:choose> Đ
                </span>
            </div>
        </div>
    </div>

    <!-- Checkout Button -->
    <button type="button" 
            class="btn btn-lg btn-primary--filled w-100" 
            data-node-id="26-2634"
            data-checkout-btn
            ${empty cart or empty cart.items ? 'disabled' : ''}>
        <span data-node-id="I26-2634;3-2975">Thanh Toán</span>
    </button>
</aside>