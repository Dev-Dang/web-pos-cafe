<%--
  Mobile Cart - Drawer/modal version matching desktop design
  Author: Dang Van Trung
  Date: 08/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="mobile-cart-drawer" data-mobile-cart>
    <div class="mobile-cart-drawer__backdrop" data-cart-close></div>
    <div class="mobile-cart-drawer__panel">
        <div class="mobile-cart-drawer__header">
            <h6 class="cart-title mb-0">
                Giỏ Hàng
                <span class="cart-count" data-cart-count>
                    <c:out value="${cart.totalQty}" default="0"/>
                </span>
            </h6>
            <button class="btn-close" data-cart-close>
                <i class="fi fi-rr-cross icon-base"></i>
            </button>
        </div>
        <div class="mobile-cart-drawer__body">
            <!-- Cart Items List -->
            <div class="cart-list" data-cart-list>
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

            <c:if test="${not empty cart and not empty cart.items}">
                <!-- Reward Points Toggle Section -->
                <div class="cart-payment-setting">
                    <div class="cart-row">
                        <div class="cart-row__label">
                            <i class="fi fi-rr-coins icon-base"></i>
                            <span>Dùng 300 điểm tích luỹ</span>
                        </div>
                        <label class="cart-reward-switch">
                            <input type="checkbox" 
                                   class="cart-reward-checkbox" 
                                   data-reward-toggle
                                   ${cart.useRewardPoints ? 'checked' : ''}>
                            <span class="cart-reward-slider"></span>
                        </label>
                    </div>
                </div>

                <!-- Payment Summary -->
                <div class="cart-summary">
                    <h6 class="cart-summary__title">Chi Tiết Thanh Toán</h6>
                    
                    <div class="cart-summary__content">
                        <!-- Subtotal Row -->
                        <div class="cart-summary__row">
                            <span>
                                Tạm tính (<span data-cart-total-qty>${cart.totalQty}</span> phần)
                            </span>
                            <span data-cart-subtotal>
                                <fmt:formatNumber value="${cart.subtotal}" type="number" groupingUsed="true"/> Đ
                            </span>
                        </div>
                        
                        <!-- Reward Points Discount Row -->
                        <div class="cart-summary__row cart-summary__row--discount" 
                             data-reward-discount-row
                             style="${cart.useRewardPoints ? '' : 'display: none;'}">
                            <span>Sử dụng điểm tích luỹ</span>
                            <span data-reward-discount-amount class="text-success">
                                - <fmt:formatNumber value="${cart.rewardDiscount}" type="number" groupingUsed="true"/> Đ
                            </span>
                        </div>
                        
                        <!-- Total Row with Border -->
                        <div class="cart-divider"></div>
                        <div class="cart-summary__row total">
                            <span>Tổng thanh toán</span>
                            <span data-cart-total>
                                <fmt:formatNumber value="${cart.total}" type="number" groupingUsed="true"/> Đ
                            </span>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
        <div class="mobile-cart-drawer__footer">
            <c:if test="${empty cart or empty cart.items}">
                <div class="cart-summary">
                    <div class="cart-summary__row total">
                        <span>Tổng cộng</span>
                        <span data-cart-total>0 Đ</span>
                    </div>
                </div>
            </c:if>
            <button class="btn btn-lg btn-primary--filled w-100" 
                    data-checkout-btn
                    ${empty cart or empty cart.items ? 'disabled' : ''}>
                Thanh toán
            </button>
        </div>
    </div>
</div>