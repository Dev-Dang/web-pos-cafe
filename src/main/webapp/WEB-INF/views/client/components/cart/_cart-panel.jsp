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
            <c:out value="${cart.totalQty}" default="0"/>
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
                <!-- Guest users: Items rendered by JavaScript from localStorage -->
                <div class="cart-empty" data-guest-empty style="display: none;">
                    <div class="cart-empty__icon">
                        <i class="fi fi-rr-shopping-cart icon-base"></i>
                    </div>
                    <p class="cart-empty__text">${i18n.trans('general.cart.empty')}</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${not empty cart and not empty cart.items}">
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

                <!-- Total Row with Border -->
                <div class="cart-divider"></div>
                <div class="cart-summary__row total">
                    <span>${i18n.trans('general.cart.total')}</span>
                    <span data-cart-total>
                        <fmt:formatNumber value="${cart.total}" type="number"
                                          groupingUsed="true"/> ${i18n.trans('general.currency.vnd')}
                    </span>
                </div>
            </div>
        </div>

        <!-- Checkout Button -->
        <button type="button"
                class="btn btn-lg btn-primary--filled w-100"

                data-checkout-btn>
            <span>${i18n.trans('general.cart.checkout')}</span>
        </button>
    </c:if>
</aside>