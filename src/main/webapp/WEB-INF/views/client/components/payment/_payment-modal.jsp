<%--
  Payment Method Selection Modal
  Author: Dang Van Trung
  Date: 09/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="payment-modal" data-payment-modal>
    <!-- Store Info Section -->
    <div class="payment-modal__store-info">
        <h6 class="payment-modal__store-name">
            <c:out value="${currentStore.name}"/>
        </h6>
        <p class="payment-modal__store-address">
            <c:out value="${currentStore.address}"/>
        </p>
    </div>

    <!-- Order List Section -->
    <div class="payment-modal__order-list">
        <h6 class="payment-modal__section-title">
            ${i18n.trans('general.payment.orderDetails')}
        </h6>

        <div class="payment-modal__items">
            <c:forEach var="item" items="${cart.items}">
                <div class="payment-modal__item">
                    <!-- Left: Product Info -->
                    <div class="payment-modal__item-left">
                        <div class="payment-modal__item-name">
                            <c:out value="${item.itemName}"/>
                        </div>
                        <div class="payment-modal__item-options">
                            <c:forEach var="option" items="${item.options}">
                                <p>
                                    <c:out value="${option.optionValueName}"/>
                                    <c:if test="${option.priceDelta > 0}">
                                        (+<fmt:formatNumber value="${option.priceDelta}" type="number"
                                                            groupingUsed="true"/> ${i18n.trans('general.currency.vnd')})
                                    </c:if>
                                </p>
                            </c:forEach>
                            <c:if test="${not empty item.note}">
                                <p class="payment-modal__item-note">
                                    <i class="fi fi-rr-comment-alt"></i>
                                    <c:out value="${item.note}"/>
                                </p>
                            </c:if>
                        </div>
                    </div>

                    <!-- Right: Price & Quantity -->
                    <div class="payment-modal__item-right">
                        <div class="payment-modal__item-price">
                            <fmt:formatNumber value="${item.unitPrice + item.optionsPrice}" type="number"
                                              groupingUsed="true"/> ${i18n.trans('general.currency.vnd')}
                        </div>
                        <div class="payment-modal__item-qty">
                            x <c:out value="${item.qty}"/>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Payment Details Section -->
    <div class="payment-modal__payment-details">
        <h6 class="payment-modal__section-title">
            ${i18n.trans('general.payment.title')}
        </h6>

        <div class="payment-modal__details-content">
            <!-- Subtotal -->
            <div class="payment-modal__detail-row">
                <span>${i18n.trans('general.payment.invoiceTotal')}</span>
                <span>
                    <fmt:formatNumber value="${cart.total}" type="number"
                                      groupingUsed="true"/> ${i18n.trans('general.currency.vnd')}
                </span>
            </div>

            <!-- Loyalty Discount (if applied) -->
            <c:if test="${not empty loyaltyRedemption and loyaltyRedemption.applied and loyaltyRedemption.discountAmount > 0}">
                <div class="payment-modal__detail-row payment-modal__detail-row--discount">
                    <span>${i18n.trans('general.loyalty.discount')}</span>
                    <span>
                        - <fmt:formatNumber value="${loyaltyRedemption.discountAmount}" type="number"
                                            groupingUsed="true"/> ${i18n.trans('general.currency.vnd')}
                    </span>
                </div>
            </c:if>

            <!-- Final Total -->
            <div class="payment-modal__detail-row payment-modal__detail-row--total">
                <span>${i18n.trans('general.payment.finalTotal')}</span>
                <span class="payment-modal__final-amount">
                    <c:choose>
                        <c:when test="${not empty loyaltyRedemption and loyaltyRedemption.applied and loyaltyRedemption.discountAmount > 0}">
                            <fmt:formatNumber value="${cart.total - loyaltyRedemption.discountAmount}" type="number"
                                              groupingUsed="true"/>
                        </c:when>
                        <c:otherwise>
                            <fmt:formatNumber value="${cart.total}" type="number" groupingUsed="true"/>
                        </c:otherwise>
                    </c:choose>
                    ${i18n.trans('general.currency.vnd')}
                </span>
            </div>
        </div>
    </div>

    <!-- Payment Type Selection -->
    <div class="payment-modal__payment-types">
        <!-- Cash Payment -->
        <form action="${pageContext.request.contextPath}/payment/process-cash"
              method="POST"
              up-target=".payment-modal"
              up-layer="current">
            <button type="submit"
                    class="payment-modal__type-btn"
                    data-payment-type="cash">
                <div class="payment-modal__type-icon">
                    <i class="fi fi-rr-wallet icon-base"></i>
                </div>
                <span class="payment-modal__type-label">
                    ${i18n.trans('general.payment.method.cash')}
                </span>
            </button>
        </form>

        <!-- QR Payment (Future) -->
        <button type="button"
                class="payment-modal__type-btn payment-modal__type-btn--disabled"
                disabled
                data-payment-type="qr">
            <div class="payment-modal__type-icon">
                <img src="${pageContext.request.contextPath}/assets/client/img/icons/vnpay.svg"
                     alt="VNPay"
                     class="payment-modal__type-img">
            </div>
            <span class="payment-modal__type-label">
                ${i18n.trans('general.payment.method.qr')}
            </span>
        </button>
    </div>
</div>
