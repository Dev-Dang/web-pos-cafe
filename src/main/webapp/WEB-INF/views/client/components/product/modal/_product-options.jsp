<%--
  Component: Product Options Section  
  Description: All product options (size, toppings, etc.)
  Usage: <jsp:include page="_product-options.jsp"/>
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="product-modal__options-wrapper">
    <c:forEach var="option" items="${productDetail.options}">
        <div class="product-modal__section" data-option-group="${option.id}" data-max-select="${option.maxSelect}" data-required="${option.required}">
            <div class="product-modal__section-header">
                <span>${option.name}</span>
                <c:choose>
                    <c:when test="${option.required}">
                        <span class="product-modal__section-sub">(${i18n.trans("general.product.modal.required")})
                        </span>
                    </c:when>
                    <c:when test="${option.maxSelect > 1}">
                        <span class="product-modal__section-sub">
                            (Tối đa ${option.maxSelect})
                        </span>
                    </c:when>
                </c:choose>
            </div>
            <c:choose>
                <%-- Single choice options (button style) --%>
                <c:when test="${option.maxSelect <= 1}">
                    <div class="option-group">
                        <c:forEach var="value" items="${option.values}">
                            <button class="option-card"
                                    type="button"
                                    data-option-item
                                    data-option-type="single"
                                    data-option-group="${option.id}"
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
                        <c:forEach var="value" items="${option.values}">
                            <div class="option-row"
                                 data-option-item
                                 data-option-type="multi"
                                 data-option-group="${option.id}"
                                 data-option-value-id="${value.id}"
                                 data-option-price="${value.priceDelta}">
                                <span class="option-row__check"><i class="fi fi-sr-check"></i></span>
                                <span>
                                        ${value.name}
                                    <c:if test="${value.priceDelta > 0}">
                                        (+ <fmt:formatNumber value="${value.priceDelta}" type="number"
                                                             groupingUsed="true"/>đ)
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