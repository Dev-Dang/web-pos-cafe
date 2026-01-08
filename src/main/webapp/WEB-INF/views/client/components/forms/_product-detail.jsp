<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Product modal fragment - dynamically loaded via Unpoly --%>
<c:set var="hasPromotion" value="${productDetail.basePrice > productDetail.currentPrice}"/>
<c:set var="optionGroupCount" value="${fn:length(productDetail.options)}"/>

<%-- Determine layout: 
     - 0-1 groups = 2 columns compact (image left, info+options right)
     - 2+ groups = 2 columns full (info+image left, options right)
--%>
<c:set var="layoutType" value="${optionGroupCount <= 1 ? 'compact' : 'full'}"/>

<div class="modal product-modal" tabindex="-1" data-menu-item-id="${productDetail.id}" data-product-slug="${productDetail.slug}">
    <div class="modal-dialog modal-lg product-modal__dialog">
        <div class="modal-content">
            <%-- Mobile Header --%>
            <div class="modal-mobile-header">
                <button type="button" class="modal-mobile-header__back" data-bs-dismiss="modal" aria-label="Close">
                    <i class="fi fi-rr-angle-small-left"></i>
                </button>
                <h3 class="modal-mobile-header__title">Thêm vào giỏ</h3>
                <div class="modal-mobile-header__spacer"></div>
            </div>
            
            <%-- Close Button (Desktop) --%>
            <button class="product-modal__close" type="button" data-bs-dismiss="modal">
                <span class="icon-base"><i class="fi fi-rr-cross-small"></i></span>
            </button>

            <%-- Dynamic layout based on option group count --%>
            <c:choose>
                <%-- Layout 1: 0-1 option groups - Compact 2 columns (image left, info+options right) --%>
                <c:when test="${layoutType == 'compact'}">
                    <div class="row g-3 product-modal__panel product-modal__panel--compact">
                        <%-- Column 1: Image only --%>
                        <div class="col-lg-6 col-12 product-modal__col product-modal__col-1">
                            <div class="product-modal__image-wrapper">
                                <img src="${item.imageUrl}" class="product-modal__image" alt="${item.name}" data-modal-image>
                            </div>
                        </div>

                        <%-- Column 2: Info + Options + Actions --%>
                        <div class="col-lg-6 col-12 product-modal__col product-modal__col-2">
                            <%-- Product Info --%>
                            <div class="product-modal__info">
                                <div class="product-modal__top">
                                    <div class="d-flex align-items-start justify-content-between gap-2 w-100">
                                        <p class="product-modal__title flex-grow-1 mb-0" data-modal-name>
                                                ${item.name}
                                        </p>
                                        <c:if test="${hasPromotion}">
                                            <c:set var="discountAmount" value="${item.basePrice - item.resolvedPrice}"/>
                                            <c:set var="discountPercent" value="${(discountAmount * 100.0) / item.basePrice}"/>
                                            <div class="product-modal__badge">
                                                <c:choose>
                                                    <c:when test="${discountAmount >= 10000}">
                                                        <p class="mb-0">Giảm <fmt:formatNumber value="${discountAmount / 1000}" maxFractionDigits="0"/>K</p>
                                                    </c:when>
                                                    <c:when test="${discountPercent >= 5}">
                                                        <p class="mb-0">Giảm <fmt:formatNumber value="${discountPercent}" maxFractionDigits="0"/>%</p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p class="mb-0">Khuyến mãi</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </c:if>
                                    </div>
                                    <p class="product-modal__desc mb-0" data-modal-desc>
                                            ${item.description}
                                    </p>
                                </div>
                                <div class="product-modal__price d-flex flex-wrap align-items-end gap-2">
                                    <span class="product-modal__price-current" data-modal-price><fmt:formatNumber value="${item.resolvedPrice}" type="number" groupingUsed="true"/>đ</span>
                                    <c:if test="${hasPromotion}">
                                        <span class="product-modal__price-old" data-modal-old-price><fmt:formatNumber value="${item.basePrice}" type="number" groupingUsed="true"/>đ</span>
                                    </c:if>
                                </div>
                            </div>

                            <%-- Single scrollable container for ALL options --%>
                            <div class="product-modal__options-wrapper">
                                <c:forEach var="optionGroup" items="${productDetail.optionGroups}">
                                    <div class="product-modal__section">
                                        <div class="product-modal__section-header">
                                            <span>${optionGroup.name}</span>
                                            <c:choose>
                                                <c:when test="${optionGroup.required}">
                                                    <span class="product-modal__section-sub">(Bắt buộc)</span>
                                                </c:when>
                                                <c:when test="${optionGroup.maxSelect > 1}">
                                                    <span class="product-modal__section-sub">(Tối đa ${optionGroup.maxSelect})</span>
                                                </c:when>
                                            </c:choose>
                                        </div>
                                        <c:choose>
                                            <%-- Single choice options (button style) --%>
                                            <c:when test="${optionGroup.maxSelect <= 1}">
                                                <div class="option-group">
                                                    <c:forEach var="value" items="${optionGroup.values}">
                                                        <button class="option-card" type="button"
                                                                data-option-item
                                                                data-option-group="${optionGroup.name}"
                                                                data-option-type="single"
                                                                data-option-value="${value.name}"
                                                                data-option-value-id="${value.id}"
                                                                data-option-price="${value.priceDelta}">
                                                                ${value.name}
                                                            <c:if test="${value.priceDelta > 0}">
                                                                + <fmt:formatNumber value="${value.priceDelta}" type="number" groupingUsed="true"/>đ
                                                            </c:if>
                                                        </button>
                                                    </c:forEach>
                                                </div>
                                            </c:when>
                                            <%-- Multi choice options (checkbox style) --%>
                                            <c:otherwise>
                                                <div class="option-list">
                                                    <c:forEach var="value" items="${optionGroup.values}">
                                                        <div class="option-row"
                                                             data-option-item
                                                             data-option-group="${optionGroup.name}"
                                                             data-option-type="multi"
                                                             data-option-value="${value.name}"
                                                             data-option-value-id="${value.id}"
                                                             data-option-price="${value.priceDelta}">
                                                            <span class="option-row__check"><i class="fi fi-sr-check"></i></span>
                                                            <span>
                                                                    ${value.name}
                                                                <c:if test="${value.priceDelta > 0}">
                                                                    (+ <fmt:formatNumber value="${value.priceDelta}" type="number" groupingUsed="true"/>đ)
                                                                </c:if>
                                                            </span>
                                                        </div>
                                                    </c:forEach>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:forEach>
                            </div>

                            <%-- Footer Actions --%>
                            <div class="product-modal__footer">
                                <div class="product-modal__note-field">
                                    <label class="product-modal__note-label">Ghi chú</label>
                                    <textarea class="product-modal__note-input" data-modal-note placeholder="Thêm ghi chú cho món này..." rows="2" maxlength="200"></textarea>
                                </div>
                                <div class="product-modal__qty-row">
                                    <span>Số lượng</span>
                                    <div class="modal-stepper" data-modal-stepper>
                                        <button class="modal-stepper__btn" type="button" data-modal-minus><i class="fi fi-rr-minus"></i></button>
                                        <span class="modal-stepper__value" data-modal-qty>1</span>
                                        <button class="modal-stepper__btn" type="button" data-modal-plus><i class="fi fi-rr-plus"></i></button>
                                    </div>
                                </div>
                                <button class="product-modal__cta" data-modal-action>
                                    <span>Thêm vào giỏ</span>
                                    <span class="product-modal__cta-dot"></span>
                                    <span data-modal-total><fmt:formatNumber value="${item.resolvedPrice}" type="number" groupingUsed="true"/>đ</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </c:when>

                <%-- Layout 2: 2+ option groups - Full 2 columns (info+image left, all options right) --%>
                <c:otherwise>
                    <div class="row g-3 product-modal__panel product-modal__panel--full">
                        <%-- Column 1: Product Info + Image --%>
                        <div class="col-lg-6 col-12 product-modal__col product-modal__col-1">
                            <div class="product-modal__info">
                                <div class="product-modal__top">
                                    <div class="d-flex align-items-start justify-content-between gap-2 w-100">
                                        <p class="product-modal__title flex-grow-1 mb-0" data-modal-name>
                                                ${item.name}
                                        </p>
                                        <c:if test="${hasPromotion}">
                                            <c:set var="discountAmount" value="${item.basePrice - item.resolvedPrice}"/>
                                            <c:set var="discountPercent" value="${(discountAmount * 100.0) / item.basePrice}"/>
                                            <div class="product-modal__badge">
                                                <c:choose>
                                                    <c:when test="${discountAmount >= 10000}">
                                                        <p class="mb-0">Giảm <fmt:formatNumber value="${discountAmount / 1000}" maxFractionDigits="0"/>K</p>
                                                    </c:when>
                                                    <c:when test="${discountPercent >= 5}">
                                                        <p class="mb-0">Giảm <fmt:formatNumber value="${discountPercent}" maxFractionDigits="0"/>%</p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <p class="mb-0">Khuyến mãi</p>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </c:if>
                                    </div>
                                    <p class="product-modal__desc mb-0" data-modal-desc>
                                            ${item.description}
                                    </p>
                                </div>
                                <div class="product-modal__price d-flex flex-wrap align-items-end gap-2">
                                    <span class="product-modal__price-current" data-modal-price><fmt:formatNumber value="${item.resolvedPrice}" type="number" groupingUsed="true"/>đ</span>
                                    <c:if test="${hasPromotion}">
                                        <span class="product-modal__price-old" data-modal-old-price><fmt:formatNumber value="${item.basePrice}" type="number" groupingUsed="true"/>đ</span>
                                    </c:if>
                                </div>
                            </div>
                            <div class="product-modal__image-wrapper">
                                <img src="${item.imageUrl}" class="product-modal__image" alt="${item.name}" data-modal-image>
                            </div>
                        </div>

                        <%-- Column 2: ALL Options + Actions (scrollable) --%>
                        <div class="col-lg-6 col-12 product-modal__col product-modal__col-2">
                            <%-- Single scrollable container for ALL options --%>
                            <div class="product-modal__options-wrapper">
                                <c:forEach var="optionGroup" items="${productDetail.optionGroups}">
                                    <div class="product-modal__section">
                                        <div class="product-modal__section-header">
                                            <span>${optionGroup.name}</span>
                                            <c:choose>
                                                <c:when test="${optionGroup.required}">
                                                    <span class="product-modal__section-sub">(Bắt buộc)</span>
                                                </c:when>
                                                <c:when test="${optionGroup.maxSelect > 1}">
                                                    <span class="product-modal__section-sub">(Tối đa ${optionGroup.maxSelect})</span>
                                                </c:when>
                                            </c:choose>
                                        </div>
                                        <c:choose>
                                            <%-- Single choice options (button style) --%>
                                            <c:when test="${optionGroup.maxSelect <= 1}">
                                                <div class="option-group">
                                                    <c:forEach var="value" items="${optionGroup.values}">
                                                        <button class="option-card" type="button"
                                                                data-option-item
                                                                data-option-group="${optionGroup.name}"
                                                                data-option-type="single"
                                                                data-option-value="${value.name}"
                                                                data-option-value-id="${value.id}"
                                                                data-option-price="${value.priceDelta}">
                                                                ${value.name}
                                                            <c:if test="${value.priceDelta > 0}">
                                                                + <fmt:formatNumber value="${value.priceDelta}" type="number" groupingUsed="true"/>đ
                                                            </c:if>
                                                        </button>
                                                    </c:forEach>
                                                </div>
                                            </c:when>
                                            <%-- Multi choice options (checkbox style) --%>
                                            <c:otherwise>
                                                <div class="option-list">
                                                    <c:forEach var="value" items="${optionGroup.values}">
                                                        <div class="option-row"
                                                             data-option-item
                                                             data-option-group="${optionGroup.name}"
                                                             data-option-type="multi"
                                                             data-option-value="${value.name}"
                                                             data-option-value-id="${value.id}"
                                                             data-option-price="${value.priceDelta}">
                                                            <span class="option-row__check"><i class="fi fi-sr-check"></i></span>
                                                            <span>
                                                                    ${value.name}
                                                                <c:if test="${value.priceDelta > 0}">
                                                                    (+ <fmt:formatNumber value="${value.priceDelta}" type="number" groupingUsed="true"/>đ)
                                                                </c:if>
                                                            </span>
                                                        </div>
                                                    </c:forEach>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:forEach>
                            </div>

                            <%-- Footer Actions --%>
                            <div class="product-modal__footer">
                                <div class="product-modal__note-field">
                                    <label class="product-modal__note-label">Ghi chú</label>
                                    <textarea class="product-modal__note-input" data-modal-note placeholder="Thêm ghi chú cho món này..." rows="2" maxlength="200"></textarea>
                                </div>
                                <div class="product-modal__qty-row">
                                    <span>Số lượng</span>
                                    <div class="modal-stepper" data-modal-stepper>
                                        <button class="modal-stepper__btn" type="button" data-modal-minus><i class="fi fi-rr-minus"></i></button>
                                        <span class="modal-stepper__value" data-modal-qty>1</span>
                                        <button class="modal-stepper__btn" type="button" data-modal-plus><i class="fi fi-rr-plus"></i></button>
                                    </div>
                                </div>
                                <button class="product-modal__cta" data-modal-action>
                                    <span>Thêm vào giỏ</span>
                                    <span class="product-modal__cta-dot"></span>
                                    <span data-modal-total><fmt:formatNumber value="${item.resolvedPrice}" type="number" groupingUsed="true"/>đ</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
