<%--
  Cart Panel - Main container with i18n support
  Author: Dang Van Trung  
  Date: 08/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<aside class="cart-panel" data-cart-panel>
    <!-- Cart Header -->
    <h6 class="cart-title">
        ${i18n.trans('general.cart.title')}
        <span class="cart-count" data-cart-count>
            <c:out value="${cart.totalQty}" default=""/>
        </span>
    </h6>

    <!-- Cart Items List -->
    <div class="cart-list" data-cart-list>
        <c:choose>
            <c:when test="${not empty sessionScope.authUser}">
                <!-- Logged-in users -->
                <c:choose>
                    <c:when test="${not empty cart and not empty cart.items}">
                        <c:forEach var="item" items="${cart.items}" varStatus="status">
                            <c:set var="cartItem" value="${item}" scope="request"/>
                            <c:set var="status" value="${status}" scope="request"/>
                            <jsp:include page="_cart-item.jsp"/>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <!-- Empty cart for logged-in users -->
                        <div class="cart-empty">
                            <div class="cart-empty__icon">
                                <i class="fi fi-rr-shopping-cart icon-base"></i>
                            </div>
                            <p class="cart-empty__text">${i18n.trans('general.cart.empty')}</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise>
                <!-- Empty cart for logged-in users -->
                <div class="cart-empty">
                    <div class="cart-empty__icon">
                        <i class="fi fi-rr-shopping-cart icon-base"></i>
                    </div>
                    <p class="cart-empty__text">${i18n.trans('general.cart.empty')}</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${not empty cart and not empty cart.items}">
        <!-- Loyalty Points Switcher -->
        <c:if test="${not empty loyaltyRedemption and loyaltyRedemption.canRedeem}">
            <div class="cart-payment-setting">
                <div class="cart-row">
                    <div class="cart-row__label">
                        <span class="icon-base text-primary">
                            <i class="fi fi-rr-coins"></i>
                        </span>
                        <span>
                                ${i18n.trans('general.loyalty.use')}
                            <span data-redeemable-points>
                                <fmt:formatNumber value="${loyaltyRedemption.maxRedeemablePoints}"
                                                  type="number"
                                                  groupingUsed="true"/>
                            </span>
                                ${i18n.trans('general.loyalty.points.label')}
                        </span>
                    </div>
                    <form action="${pageContext.request.contextPath}/cart/toggle-loyalty"
                          method="POST"
                          up-submit
                          up-target=".cart-panel"
                          up-layer="root"
                          class="cart-loyalty-form">
                        <label class="cart-reward-switch">
                            <input type="checkbox"
                                   class="cart-reward-checkbox"
                                   name="apply"
                                   value="true"
                                   data-loyalty-toggle
                                   onchange="this.form.requestSubmit()"
                                ${loyaltyRedemption.applied ? 'checked' : ''}>
                            <span class="cart-reward-slider"></span>
                        </label>
                    </form>
                </div>
            </div>
        </c:if>

        <!-- Payment Summary -->
        <div class="cart-summary">
            <h6 class="cart-summary__title">
                    ${i18n.trans('general.cart.payment.details')}
            </h6>

            <div class="cart-summary__content">
                <!-- Subtotal Row -->
                <div class="cart-summary__row">
                    <span>
                            ${i18n.trans('general.cart.subtotal')}
                        (
                        <span data-cart-total-qty>${cart.totalQty}</span>
                            ${cart.totalQty == 1 ? i18n.trans('general.cart.item.count.single') : i18n.trans('general.cart.item.count.multiple')})
                    </span>
                    <span data-cart-subtotal>
                        <fmt:formatNumber value="${cart.subtotal}" type="number"
                                          groupingUsed="true"/> ${i18n.trans('general.currency.vnd')}
                    </span>
                </div>

                <!-- Loyalty Discount Row -->
                <c:if test="${not empty loyaltyRedemption and loyaltyRedemption.applied and loyaltyRedemption.discountAmount > 0}">
                    <div class="cart-summary__row cart-summary__row--discount">
                        <span>${i18n.trans('general.loyalty.discount')}</span>
                        <span class="cart-summary__row-value" data-loyalty-discount>
                            - <fmt:formatNumber value="${loyaltyRedemption.discountAmount}" type="number"
                                                groupingUsed="true"/> ${i18n.trans('general.currency.vnd')}
                        </span>
                    </div>
                </c:if>

                <!-- Total Row with Border -->
                <div class="cart-summary__row total">
                    <span>${i18n.trans('general.cart.total')}</span>
                    <span data-cart-total>
                        <c:choose>
                            <c:when test="${not empty loyaltyRedemption and loyaltyRedemption.applied and loyaltyRedemption.discountAmount > 0}">
                                <%-- Total after loyalty discount --%>
                                <fmt:formatNumber value="${cart.total - loyaltyRedemption.discountAmount}"
                                                  type="number"
                                                  groupingUsed="true"/>
                            </c:when>
                            <c:otherwise>
                                <%-- Original total --%>
                                <fmt:formatNumber value="${cart.total}"
                                                  type="number"
                                                  groupingUsed="true"/>
                            </c:otherwise>
                        </c:choose>
                            ${i18n.trans('general.currency.vnd')}
                    </span>
                </div>
            </div>
        </div>

        <!-- Checkout Button -->
        <button type="button"
                class="btn btn-lg btn-primary--filled w-100"
                up-follow
                up-layer="new modal"
                up-size="small"
                up-href="${pageContext.request.contextPath}/payment/select-method"
                up-target=".payment-modal"
                data-checkout-btn>
            <span>${i18n.trans('general.cart.checkout')}</span>
        </button>
    </c:if>
</aside>