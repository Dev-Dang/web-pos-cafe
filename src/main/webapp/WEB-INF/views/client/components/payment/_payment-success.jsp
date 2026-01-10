<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="result" value="${sessionScope.paymentResult}" />

<!-- Wrap in .payment-modal to match Unpoly target -->
<div class="payment-modal payment-modal--success" data-payment-success>
    <div class="payment-success">
        <!-- Success Icon (Orange Circle) -->
        <div class="payment-success__icon">
            <i class="fi fi-sr-check-circle"></i>
        </div>

        <!-- Success Message (Figma Style) -->
        <div class="payment-success__header">
            <h2 class="payment-success__title">
                ${i18n.trans('general.payment.successTitle')}
            </h2>
            <p class="payment-success__subtitle">
                ${i18n.trans('general.payment.successSubtitle')}
            </p>
        </div>

        <!-- Points Earned (Prominent, Above Card) -->
        <c:if test="${result.pointsEarned > 0}">
            <div class="payment-success__points">
                +<c:out value="${result.pointsEarned}"/> ${i18n.trans('general.payment.pointsEarnedLabel')}
            </div>
        </c:if>

        <!-- Order Details Card -->
        <div class="payment-success__details">
            <div class="payment-success__detail-row">
                <span class="payment-success__label">
                    ${i18n.trans('general.payment.invoiceNumber')}:
                </span>
                <span class="payment-success__value">
                    <c:out value="${result.invoiceNumber}"/>
                </span>
            </div>

            <div class="payment-success__detail-row">
                <span class="payment-success__label">
                    ${i18n.trans('general.payment.totalPaid')}:
                </span>
                <span class="payment-success__value payment-success__value--amount">
                    <fmt:formatNumber value="${result.finalAmount}" type="number" groupingUsed="true"/> đ
                </span>
            </div>
        </div>

        <!-- Manual Close Button (Using project's btn classes) -->
        <button type="button"
                class="btn btn-lg btn-primary--filled btn-full"
                data-payment-success-close>
            ${i18n.trans('general.payment.backButton')}
        </button>

        <!-- Auto-close countdown -->
        <p class="payment-success__auto-close">
            ${i18n.trans('general.payment.autoCloseIn')} 
            <span class="payment-success__countdown">5</span>s...
        </p>
    </div>
</div>
