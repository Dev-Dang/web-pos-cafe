<%--
  Order History Modal
  Author: Dang Van Trung
  Date: 09/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctxPath" value="${pageContext.request.contextPath}"/>

<div class="order-history-modal" data-order-history-modal>
    <div class="order-history">
        <aside class="order-history__aside">
            <h3 class="order-history__title">
                ${i18n.trans('general.orderHistory.title')}
            </h3>

            <div class="order-history__list" data-order-history-list>
                <c:choose>
                    <c:when test="${not empty invoices}">
                        <c:forEach var="invoice" items="${invoices}">
                            <c:set var="isActive"
                                   value="${not empty selectedInvoice && invoice.id == selectedInvoice.id}"/>
                            <a class="order-history__item ${isActive ? 'is-active' : ''}"
                               href="${ctxPath}/user/orders?invoiceId=${invoice.id}"
                               up-follow
                               up-layer="current"
                               up-target="[data-order-history-detail]"
                               up-cache="true">
                                <div class="order-history__item-meta">
                                    <div class="order-history__item-meta-left">
                                        <span class="order-history__item-time">
                                            <c:out value="${invoice.issuedAtDisplay}"/>
                                        </span>
                                        <c:if test="${not empty invoice.invoiceNumber}">
                                            <span class="order-history__item-invoice">
                                                &bull; <c:out value="${invoice.invoiceNumber}"/>
                                            </span>
                                        </c:if>
                                    </div>
                                    <span class="order-history__item-total">
                                        <fmt:formatNumber value="${invoice.totalAmount}" type="number"
                                                          groupingUsed="true"/> ${i18n.trans('general.currency.vnd')}
                                    </span>
                                </div>
                                <div class="order-history__item-store">
                                    <c:out value="${invoice.storeName}"/>
                                </div>
                            </a>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="order-history__empty">
                            ${i18n.trans('general.orderHistory.empty')}
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </aside>

        <section class="order-history__detail"
                 data-order-history-detail
                 data-order-history-print-area>
            <c:choose>
                <c:when test="${not empty invoiceDetail}">
                    <div class="order-history__detail-header">
                        <div class="order-history__store">
                            <div class="order-history__store-name">
                                <c:out value="${invoiceDetail.storeName}"/>
                            </div>
                            <div class="order-history__store-address">
                                <c:out value="${invoiceDetail.storeAddress}"/>
                            </div>
                        </div>

                        <div class="order-history__invoice-title">
                            ${i18n.trans('general.orderHistory.invoiceTitle')}
                        </div>

                        <div class="order-history__issued-at">
                            ${i18n.trans('general.orderHistory.issuedAt')}:
                            <span class="order-history__issued-time">
                                <c:out value="${invoiceDetail.issuedTimeDisplay}"/>
                            </span>
                            - ${i18n.trans('general.orderHistory.dateLabel')}
                            <span class="order-history__issued-date">
                                <c:out value="${invoiceDetail.issuedDateDisplay}"/>
                            </span>
                        </div>
                    </div>

                    <div class="order-history__detail-body">
                        <div class="order-history__items">
                            <c:forEach var="item" items="${invoiceDetail.items}">
                                <div class="order-history__line">
                                    <div class="order-history__line-info">
                                        <div class="order-history__line-name">
                                            <c:out value="${item.productName}"/>
                                        </div>
                                        <c:if test="${not empty item.optionLines}">
                                            <div class="order-history__line-options">
                                                <c:forEach var="optionLine" items="${item.optionLines}">
                                                    <span><c:out value="${optionLine}"/></span>
                                                </c:forEach>
                                            </div>
                                        </c:if>
                                        <c:if test="${not empty item.note}">
                                            <div class="order-history__line-note">
                                                <i class="fi fi-rr-comment-alt"></i>
                                                <c:out value="${item.note}"/>
                                            </div>
                                        </c:if>
                                    </div>
                                    <div class="order-history__line-meta">
                                        <div class="order-history__line-price">
                                            <fmt:formatNumber value="${item.unitPrice}" type="number"
                                                              groupingUsed="true"/> ${i18n.trans('general.currency.vnd')}
                                        </div>
                                        <div class="order-history__line-qty">
                                            x <c:out value="${item.quantity}"/>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <div class="order-history__summary">
                            <div class="order-history__summary-row">
                                <span>${i18n.trans('general.payment.invoiceTotal')}</span>
                                <span>
                                    <fmt:formatNumber value="${invoiceDetail.subtotal}" type="number"
                                                      groupingUsed="true"/> ${i18n.trans('general.currency.vnd')}
                                </span>
                            </div>
                            <c:if test="${invoiceDetail.discountAmount > 0}">
                                <div class="order-history__summary-row order-history__summary-row--discount">
                                    <span>${i18n.trans('general.loyalty.discount')}</span>
                                    <span>
                                        - <fmt:formatNumber value="${invoiceDetail.discountAmount}" type="number"
                                                            groupingUsed="true"/> ${i18n.trans('general.currency.vnd')}
                                    </span>
                                </div>
                            </c:if>
                            <div class="order-history__summary-row order-history__summary-row--total">
                                <span>
                                    ${i18n.trans('general.payment.finalTotal')}:
                                    <span class="order-history__payment-method">
                                        <c:choose>
                                            <c:when test="${invoiceDetail.paymentMethod == 'cash'}">
                                                ${i18n.trans('general.payment.method.cash')}
                                            </c:when>
                                            <c:when test="${invoiceDetail.paymentMethod == 'vnpay'}">
                                                VNPay
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${invoiceDetail.paymentMethod}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </span>
                                <span class="order-history__summary-value order-history__summary-value--total">
                                    <fmt:formatNumber value="${invoiceDetail.totalAmount}" type="number"
                                                      groupingUsed="true"/> ${i18n.trans('general.currency.vnd')}
                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="order-history__actions">
                        <button type="button"
                                class="order-history__action order-history__action--outline"
                                data-order-history-print>
                            <span>${i18n.trans('general.orderHistory.action.invoice')}</span>
                            <i class="fi fi-rr-print"></i>
                        </button>

                        <form action="${ctxPath}/user/orders/clone"
                              method="POST"
                              up-submit
                              up-layer="root"
                              up-target=".cart-panel"
                              up-on-loaded="up.layer.get('overlay').dismiss()">
                            <input type="hidden" name="invoiceId" value="${invoiceDetail.id}">
                            <button type="submit"
                                    class="order-history__action order-history__action--primary">
                                <span>${i18n.trans('general.orderHistory.action.reorder')}</span>
                                <i class="fi fi-rr-shopping-bag"></i>
                            </button>
                        </form>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="order-history__empty-detail">
                        ${i18n.trans('general.orderHistory.empty')}
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </div>
</div>
