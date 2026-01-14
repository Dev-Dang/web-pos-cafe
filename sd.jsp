<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Product detail modal fragment for Unpoly - uses ProductDetailDTO --%>
<c:set var="detail" value="${productDetail}"/>
<c:set var="hasPromotion" value="${detail.originalPrice != null}"/>
<c:set var="optionGroupCount" value="${fn:length(detail.optionGroups)}"/>
<c:set var="isSoldOut" value="${detail.availabilityStatus == 'SOLD_OUT'}"/>

<%-- Determine layout: 
     - 0-1 groups = 2 columns compact (image left, info+options right)
     - 2+ groups = 2 columns full (info+image left, options right)
--%>
<c:set var="layoutType" value="${optionGroupCount <= 1 ? 'compact' : 'full'}"/>

<div class="modal product-modal" tabindex="-1" data-menu-item-id="${detail.id}" data-product-slug="${detail.slug}">
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
                <%-- Layout 1: 0-1 option groups - Compact 2 columns --%>
                <c:when test="${layoutType == 'compact'}">
                    <div class="row g-3 product-modal__panel product-modal__panel--compact">
                        <%-- Column 1: Image only --%>
                        <div class="col-lg-6 col-12 product-modal__col product-modal__col-1">
                            <div class="product-modal__image-wrapper">
                                <img src="${detail.imageUrl}" class="product-modal__image" alt="${detail.name}" data-modal-image>
                            </div>
                        </div>

                        <%-- Column 2: Info + Options + Actions --%>
                        <div class="col-lg-6 col-12 product-modal__col product-modal__col-2">
                            <%-- Product Info --%>
                            <div class="product-modal__info">
                                <div class="product-modal__top">
                                    <h2 class="product-modal__name">${detail.name}</h2>
                                    <div class="product-modal__price">
                                        <span class="product-modal__price-current" data-product-price="${detail.currentPrice}">
                                            <fmt:formatNumber value="${detail.currentPrice}" type="number" groupingUsed="true"/>đ
                                        </span>
                                        <c:if test="${hasPromotion}">
                                            <span class="product-modal__price-original">
                                                <fmt:formatNumber value="${detail.originalPrice}" type="number" groupingUsed="true"/>đ
                                            </span>
                                        </c:if>
                                        <c:if test="${detail.unit != null}">
                                            <span class="product-modal__unit">/${detail.unit}</span>
                                        </c:if>
                                    </div>
                                </div>
                                
                                <c:if test="${not empty detail.description}">
                                    <div class="product-modal__description">
                                        <p>${detail.description}</p>
                                    </div>
                                </c:if>
                            </div>

                            <%-- Options --%>
                            <c:if test="${detail.hasOptions && not empty detail.optionGroups}">
                                <div class="product-modal__options">
                                    <c:forEach var="group" items="${detail.optionGroups}">
                                        <div class="option-group" data-option-group-id="${group.id}">
                                            <h4 class="option-group__title">
                                                ${group.name}
                                                <c:if test="${group.required}">
                                                    <span class="text-danger">*</span>
                                                </c:if>
                                            </h4>
                                            <div class="option-group__values">
                                                <c:forEach var="value" items="${group.values}">
                                                    <c:set var="isValueSoldOut" value="${value.availabilityStatus == 'SOLD_OUT'}"/>
                                                    <label class="option-value ${isValueSoldOut ? 'option-value--disabled' : ''}"
                                                           data-option-value-id="${value.id}">
                                                        <input type="${group.type == 'single' ? 'radio' : 'checkbox'}"
                                                               name="option_group_${group.id}"
                                                               value="${value.id}"
                                                               data-price-delta="${value.priceDelta}"
                                                               ${isValueSoldOut ? 'disabled' : ''}
                                                               ${group.required && group.type == 'single' ? 'required' : ''}>
                                                        <span class="option-value__name">${value.name}</span>
                                                        <c:if test="${value.priceDelta != 0}">
                                                            <span class="option-value__price">
                                                                <c:choose>
                                                                    <c:when test="${value.priceDelta > 0}">
                                                                        +<fmt:formatNumber value="${value.priceDelta}" type="number" groupingUsed="true"/>đ
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <fmt:formatNumber value="${value.priceDelta}" type="number" groupingUsed="true"/>đ
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </span>
                                                        </c:if>
                                                        <c:if test="${isValueSoldOut}">
                                                            <span class="option-value__sold-out">(Hết)</span>
                                                        </c:if>
                                                    </label>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:if>

                            <%-- Actions --%>
                            <div class="product-modal__actions">
                                <div class="product-modal__quantity">
                                    <button type="button" class="quantity-btn quantity-btn--minus" data-quantity-decrease>
                                        <i class="fi fi-rr-minus-small"></i>
                                    </button>
                                    <input type="number" class="quantity-input" value="1" min="1" max="99" data-quantity-input>
                                    <button type="button" class="quantity-btn quantity-btn--plus" data-quantity-increase>
                                        <i class="fi fi-rr-plus-small"></i>
                                    </button>
                                </div>
                                
                                <c:choose>
                                    <c:when test="${isSoldOut}">
                                        <button type="button" class="btn btn-secondary btn-block product-modal__add-btn" disabled>
                                            Hết hàng
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" class="btn btn-primary btn-block product-modal__add-btn" data-add-to-cart>
                                            <span data-btn-text>Thêm vào giỏ</span>
                                            <span class="product-modal__total-price" data-total-price>
                                                <fmt:formatNumber value="${detail.currentPrice}" type="number" groupingUsed="true"/>đ
                                            </span>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:when>

                <%-- Layout 2: 2+ option groups - Full 2 columns --%>
                <c:otherwise>
                    <div class="row g-3 product-modal__panel product-modal__panel--full">
                        <%-- Column 1: Info + Image --%>
                        <div class="col-lg-6 col-12 product-modal__col product-modal__col-1">
                            <%-- Product Info --%>
                            <div class="product-modal__info">
                                <div class="product-modal__top">
                                    <h2 class="product-modal__name">${detail.name}</h2>
                                    <div class="product-modal__price">
                                        <span class="product-modal__price-current" data-product-price="${detail.currentPrice}">
                                            <fmt:formatNumber value="${detail.currentPrice}" type="number" groupingUsed="true"/>đ
                                        </span>
                                        <c:if test="${hasPromotion}">
                                            <span class="product-modal__price-original">
                                                <fmt:formatNumber value="${detail.originalPrice}" type="number" groupingUsed="true"/>đ
                                            </span>
                                        </c:if>
                                        <c:if test="${detail.unit != null}">
                                            <span class="product-modal__unit">/${detail.unit}</span>
                                        </c:if>
                                    </div>
                                </div>
                                
                                <c:if test="${not empty detail.description}">
                                    <div class="product-modal__description">
                                        <p>${detail.description}</p>
                                    </div>
                                </c:if>
                            </div>

                            <%-- Image --%>
                            <div class="product-modal__image-wrapper">
                                <img src="${detail.imageUrl}" class="product-modal__image" alt="${detail.name}" data-modal-image>
                            </div>
                        </div>

                        <%-- Column 2: Options + Actions --%>
                        <div class="col-lg-6 col-12 product-modal__col product-modal__col-2">
                            <%-- Options --%>
                            <c:if test="${detail.hasOptions && not empty detail.optionGroups}">
                                <div class="product-modal__options">
                                    <c:forEach var="group" items="${detail.optionGroups}">
                                        <div class="option-group" data-option-group-id="${group.id}">
                                            <h4 class="option-group__title">
                                                ${group.name}
                                                <c:if test="${group.required}">
                                                    <span class="text-danger">*</span>
                                                </c:if>
                                            </h4>
                                            <div class="option-group__values">
                                                <c:forEach var="value" items="${group.values}">
                                                    <c:set var="isValueSoldOut" value="${value.availabilityStatus == 'SOLD_OUT'}"/>
                                                    <label class="option-value ${isValueSoldOut ? 'option-value--disabled' : ''}"
                                                           data-option-value-id="${value.id}">
                                                        <input type="${group.type == 'single' ? 'radio' : 'checkbox'}"
                                                               name="option_group_${group.id}"
                                                               value="${value.id}"
                                                               data-price-delta="${value.priceDelta}"
                                                               ${isValueSoldOut ? 'disabled' : ''}
                                                               ${group.required && group.type == 'single' ? 'required' : ''}>
                                                        <span class="option-value__name">${value.name}</span>
                                                        <c:if test="${value.priceDelta != 0}">
                                                            <span class="option-value__price">
                                                                <c:choose>
                                                                    <c:when test="${value.priceDelta > 0}">
                                                                        +<fmt:formatNumber value="${value.priceDelta}" type="number" groupingUsed="true"/>đ
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <fmt:formatNumber value="${value.priceDelta}" type="number" groupingUsed="true"/>đ
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </span>
                                                        </c:if>
                                                        <c:if test="${isValueSoldOut}">
                                                            <span class="option-value__sold-out">(Hết)</span>
                                                        </c:if>
                                                    </label>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:if>

                            <%-- Actions --%>
                            <div class="product-modal__actions">
                                <div class="product-modal__quantity">
                                    <button type="button" class="quantity-btn quantity-btn--minus" data-quantity-decrease>
                                        <i class="fi fi-rr-minus-small"></i>
                                    </button>
                                    <input type="number" class="quantity-input" value="1" min="1" max="99" data-quantity-input>
                                    <button type="button" class="quantity-btn quantity-btn--plus" data-quantity-increase>
                                        <i class="fi fi-rr-plus-small"></i>
                                    </button>
                                </div>
                                
                                <c:choose>
                                    <c:when test="${isSoldOut}">
                                        <button type="button" class="btn btn-secondary btn-block product-modal__add-btn" disabled>
                                            Hết hàng
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" class="btn btn-primary btn-block product-modal__add-btn" data-add-to-cart>
                                            <span data-btn-text>Thêm vào giỏ</span>
                                            <span class="product-modal__total-price" data-total-price>
                                                <fmt:formatNumber value="${detail.currentPrice}" type="number" groupingUsed="true"/>đ
                                            </span>
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>