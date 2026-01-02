<%--
  Description: Flash messages fragment for Unpoly [up-flashes]
  This fragment is prepended to HTML responses containing flash messages
  Author: Dang Van Trung
  Date: 01/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${not empty requestScope.messages}">
<div up-flashes>
    <c:forEach var="msg" items="${requestScope.messages}">
        <p data-type="${msg.type}" data-message="${sessionScope.i18n.trans(msg.msgKey)}"></p>
    </c:forEach>
</div>
</c:if>
