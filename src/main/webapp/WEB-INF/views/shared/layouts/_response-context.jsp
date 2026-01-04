<%--
  Description: Response context with flash messages and form data
  Author: Dang Van Trung
  Date: 03/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div id="response-context" up-hungry>
    <%-- CSRF Token for JavaScript/Unpoly --%>
    <meta name="csrf-token" content="${sessionScope.csrfToken}">

    <%-- Flash messages for Unpoly - rendered into [up-flashes] target --%>
    <div up-flashes>
        <c:set var="ctx" value="${requestScope.responseContext}"/>
        <c:set var="msgs" value="${ctx.data['messages']}"/>

        <c:if test="${not empty msgs}">
            <c:forEach var="msg" items="${msgs}">
                <p data-type="${msg.type}"
                   data-message="${sessionScope.i18n.trans(msg.msgKey)}"></p>
            </c:forEach>
        </c:if>
    </div>
</div>