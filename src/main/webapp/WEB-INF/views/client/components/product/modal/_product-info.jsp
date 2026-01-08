<%--
  Component: Product Info Section
  Description: Product title, description, price and badge display
  Usage: <jsp:include page="modal/_product-info.jsp"/>
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="app" %>

<%-- Ensure hasPromotion is available in this component --%>
<c:set var="hasPromotion" value="${productDetail.basePrice > productDetail.currentPrice}"/>

<div class="product-modal__info">
    <div class="product-modal__top">
        <div class="d-flex align-items-start justify-content-between gap-2 w-100">
            <p class="product-modal__title flex-grow-1 mb-0">
                    ${productDetail.name}
            </p>
        </div>
        <p class="product-modal__desc mb-0">
                ${productDetail.description}
        </p>
    </div>
    <div class="product-modal__price d-flex flex-wrap align-items-end gap-3">
        <span class="product-modal__price-current">
            <fmt:formatNumber value="${productDetail.currentPrice}" type="number" groupingUsed="true"/>đ
        </span>
        <c:if test="${hasPromotion}">
            <span class="product-modal__price-old">
                <fmt:formatNumber value="${productDetail.basePrice}" type="number" groupingUsed="true"/>đ
            </span>
            <div class="product-modal__badge">
                <c:set var="discountAmount" value="${productDetail.basePrice - productDetail.currentPrice}"/>
                <c:choose>
                    <c:when test="${discountAmount >= 10000}">
                        <p class="mb-0">${i18n.trans("general.product.discount.amount")} <app:discount basePrice="${productDetail.basePrice}" currentPrice="${productDetail.currentPrice}" type="amount" format="k"/>K</p>
                    </c:when>
                    <c:when test="${(discountAmount * 100) / productDetail.basePrice >= 5}">
                        <p class="mb-0">${i18n.trans("general.product.discount.percent")} <app:discount basePrice="${productDetail.basePrice}" currentPrice="${productDetail.currentPrice}" type="percent"/>%</p>
                    </c:when>
                    <c:otherwise>
                        <p class="mb-0">Khuyến mãi</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>
    </div>
</div>