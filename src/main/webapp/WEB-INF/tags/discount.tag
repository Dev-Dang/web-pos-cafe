<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ attribute name="basePrice" required="true" type="java.lang.Integer" %>
<%@ attribute name="currentPrice" required="true" type="java.lang.Integer" %>
<%@ attribute name="type" required="false" type="java.lang.String" %>
<%@ attribute name="format" required="false" type="java.lang.String" %>
<%--
Discount Calculator Tag
Calculates discount amount or percentage between base and current price
Usage: 
  <app:discount basePrice="${product.basePrice}" currentPrice="${product.currentPrice}" type="amount" format="k"/>K
  <app:discount basePrice="${product.basePrice}" currentPrice="${product.currentPrice}" type="percent"/>%
--%>

<%-- Set defaults --%>
<c:set var="calcType" value="${empty type ? 'amount' : type}"/>
<c:set var="calcFormat" value="${empty format ? 'number' : format}"/>

<%-- Calculate discount amount --%>
<c:set var="discountAmount" value="${basePrice - currentPrice}"/>
<c:set var="discountAmount" value="${discountAmount > 0 ? discountAmount : 0}"/>

<%-- Output based on type --%>
<c:choose>
    <c:when test="${calcType == 'percent'}">
        <%-- Calculate percentage --%>
        <c:choose>
            <c:when test="${basePrice > 0}">
                <fmt:formatNumber value="${(discountAmount * 100) / basePrice}" maxFractionDigits="0"/>
            </c:when>
            <c:otherwise>0</c:otherwise>
        </c:choose>
    </c:when>
    <c:otherwise>
        <%-- Calculate amount --%>
        <c:choose>
            <c:when test="${calcFormat == 'k'}">
                <fmt:formatNumber value="${discountAmount / 1000}" maxFractionDigits="0"/>
            </c:when>
            <c:otherwise>
                <fmt:formatNumber value="${discountAmount}" maxFractionDigits="0"/>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>