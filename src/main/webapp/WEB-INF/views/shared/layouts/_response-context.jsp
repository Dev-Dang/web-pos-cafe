<%--
  Description: Response context with flash messages and form data
  Author: Dang Van Trung
  Date: 03/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div id="response-context" up-hungry>

    <%-- Reopen modal --%>
    <c:if test="${not empty requestScope.reOpenModal}">
        <c:choose>
            <c:when test="${requestScope.reOpenModal == 'productModal'}">
                <div data-reopen-modal hidden
                     up-follow
                     up-target="#product-detail-modal"
                     up-href="${ctxPath}/products/${card.slug}"
                     up-layer="new modal"
                     up-size="large"
                     up-animation="fade-in"
                     up-cache="false"
                     up-history="true">
                    <jsp:include page="/WEB-INF/views/client/components/product/modal/_product-details.jsp"/>
                </div>
            </c:when>
            <%-- Add more modal types here as needed --%>
        </c:choose>
    </c:if>

    <%-- CSRF Token for JavaScript/Unpoly --%>
    <meta name="csrf-token" content="${sessionScope.csrfToken}">

    <%-- Flash messages for Unpoly - rendered into [up-flashes] target --%>
    <div up-flashes>
        <c:set var="ctx" value="${requestScope.responseContext}"/>
        <c:set var="msgs" value="${ctx.data['messages']}"/>

        <c:if test="${not empty msgs}">
            <c:forEach var="msg" items="${msgs}">
                <p data-type="${msg.type}"
                   data-message="${i18n.trans(msg.msgKey)}"></p>
            </c:forEach>
        </c:if>
    </div>
</div>
