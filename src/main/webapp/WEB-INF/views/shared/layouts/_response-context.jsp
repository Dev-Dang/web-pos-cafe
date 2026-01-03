<%--
  Description: Response context with flash messages and form data
  Author: Dang Van Trung
  Date: 03/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- CSRF Token for JavaScript/Unpoly --%>
<meta name="csrf-token" content="${sessionScope.csrfToken}">

<%-- Flash messages for Unpoly - rendered into [up-flashes] target --%>
<div up-flashes>
    <c:if test="${not empty requestScope.messages}">
        <%-- Toast messages --%>
        <c:forEach var="msg" items="${requestScope.messages}">
            <p data-type="${msg.type}"
               data-message="${sessionScope.i18n.trans(msg.msgKey)}">
            </p>
        </c:forEach>
    </c:if>
</div>

<%-- Form refill data for modal error handling --%>
<c:if test="${not empty requestScope.formData}">
    <div id="flash-data" style="display: none;">
        <c:forEach var="entry" items="${requestScope.formData}">
            <input type="hidden" name="${entry.key}" value="${entry.value}"/>
        </c:forEach>
    </div>
</c:if>