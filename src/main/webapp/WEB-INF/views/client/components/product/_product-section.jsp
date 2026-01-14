<%--
  Description: Product section with category slider and product grid
  Author: Dang Van Trung
  Date: 06/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="app" %>

<c:set var="ctxPath" value="${pageContext.request.contextPath}"/>

<div class="product-section__content" product-section__content>
    <div class="category-slider" data-category-slider>
        <%-- Slider nav button --%>
        <button class="category-nav category-nav--prev"
                type="button"
                data-category-prev>
            <span class="icon-base">
                <i class="fi fi-rr-angle-small-left"></i>
            </span>
        </button>
        <button class="category-nav category-nav--next"
                type="button"
                data-category-next>
            <span class="icon-base">
                <i class="fi fi-rr-angle-small-right"></i>
            </span>
        </button>

        <%-- Main category slider --%>
        <div class="category-track scroll-hidden" data-category-track>
            <c:forEach var="category" items="${categories}" varStatus="status">

                <c:set var="isActive"
                       value="${(not empty selectedCategory and category.slug == selectedCategory)
                        or (empty selectedCategory and status.first)}"/>

                <button class="category-item ${isActive ? 'is-active' : ''}"
                        type="button"
                        up-follow
                        up-href="${ctxPath}/categories/${category.slug}"
                        up-target=".section-header, .product-grid"
                        up-transition="cross-fade"
                        up-history="true"
                        up-cache="false">
                    <img class="category-image"
                         src="${category.iconUrl}"
                         alt="${category.name}"/>
                    <span class="category-label">${category.name}</span>
                </button>

            </c:forEach>
        </div>
    </div>

    <div class="section-header py-6">
        <h5 class="section-title mb-0" data-category-title>
            ${i18n.trans("general.product.section.title")}
        </h5>
        <span class="section-meta" data-category-meta>
            <c:choose>
                <c:when test="${not empty searchResultsCount and searchResultsCount >= 0}">
                    ${searchResultsCount} ${i18n.trans("general.product.count.multiple")}
                </c:when>
                <c:when test="${fn:length(productCards) == 1}">
                    ${i18n.trans("general.product.count.single")}
                </c:when>
                <c:otherwise>
                    ${fn:length(productCards)} ${i18n.trans("general.product.count.multiple")}
                </c:otherwise>
            </c:choose>
        </span>
    </div>

    <div class="product-grid">
        <%-- Product grid fragment for Unpoly - uses ProductCardDTO --%>
        <jsp:include page="_product-card.jsp"/>

        <%-- Show empty state if no products --%>
        <c:if test="${empty productCards}">
            <div class="col-12 text-center py-5">
                <p class="text-muted">
                        ${i18n.trans("general.product.empty")}
                </p>
            </div>
        </c:if>
    </div>
</div>
