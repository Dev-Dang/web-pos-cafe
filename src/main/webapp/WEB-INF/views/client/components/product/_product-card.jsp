<%--
  Component: Product Card
  Description: Individual product card for product grid
  Usage: <jsp:include page="_product-card.jsp"/>
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="app" %>

<c:set var="ctxPath" value="${pageContext.request.contextPath}"/>

<c:forEach var="card" items="${productCards}">
    <c:set var="isSoldOut" value="${!card.available}"/>
    <c:set var="hasPromotion" value="${card.hasPromotion}"/>
    <c:set var="basePrice" value="${card.basePrice}"/>
    <c:set var="currentPrice" value="${card.currentPrice}"/>

    <article class="product-card ${isSoldOut ? 'is-sold-out' : ''}"
             up-follow
             up-target="#product-detail-modal"
             up-href="${ctxPath}/products/${card.slug}"
             up-layer="new modal"
             up-size="large"
             up-animation="fade-in"
             up-cache="false"
             up-history="true">
        <div class="product-card__media"
             <c:if test="${isSoldOut}">style="position: relative"</c:if>>
            <img src="${card.imageUrl}"
                 class="product-card__image"
                 alt="${card.name}"/>
            <c:if test="${isSoldOut}">
                <div class="product-card__overlay"
                     style="position: absolute; inset: 0; background: rgba(245, 245, 244, 0.7); display: flex; align-items: center; justify-content: center; font-weight: 600; color: #222;">
                        ${i18n.trans("general.product.soldOut")}
                </div>
            </c:if>
        </div>
        <div class="product-card__info">
            <div class="product-card__top">
                <p class="product-card__title mb-0">${card.name}</p>
            </div>
            <div class="product-card__price">
                <span class="product-card__price-current">
                    <fmt:formatNumber
                            value="${currentPrice}"
                            type="number"
                            groupingUsed="true"/>
                        ${i18n.trans("general.currency.vnd")}
                </span>
                <c:if test="${basePrice > currentPrice}">
                    <span class="product-card__price-original">
                        <fmt:formatNumber
                                value="${basePrice}"
                                type="number"
                                groupingUsed="true"/>
                            ${i18n.trans("general.currency.vnd")}
                    </span>
                </c:if>
            </div>
        </div>
        <c:if test="${hasPromotion && !isSoldOut && basePrice > currentPrice}">
            <div class="product-card__badge">
                <p>
                    <c:choose>
                        <c:when test="${card.discountAmount >= 5000}">
                            ${i18n.trans("general.product.discount.amount")}
                            <app:discount basePrice="${card.basePrice}" currentPrice="${card.currentPrice}"
                                          type="amount" format="k"/>K
                        </c:when>
                        <c:otherwise>
                            ${i18n.trans("general.product.discount.percent")}
                            <app:discount basePrice="${card.basePrice}" currentPrice="${card.currentPrice}"
                                          type="percent"/>%
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
        </c:if>
    </article>
</c:forEach>

<%-- Include pagination trigger for infinite scroll --%>
<jsp:include page="../_pagination-trigger.jsp"/>