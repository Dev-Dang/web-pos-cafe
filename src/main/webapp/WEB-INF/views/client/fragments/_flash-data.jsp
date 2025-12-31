<%--
  Description: Flash data (messages for toasts, and form state for modal reopening)
  Author: Dang Van Trung
  Date: 31/12/2025
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div id="flash-data"
     data-open-modal="${requestScope.openModal}"
     data-messages='${requestScope.messages != null ? "true" : "false"}'>

    <c:if test="${not empty requestScope.messages}">
        <%-- Toast messages --%>
        <c:forEach var="msg" items="${requestScope.messages}">
            <p data-type="${msg.type}"
               data-message="${sessionScope.i18n.trans(msg.msgKey)}">
            </p>
        </c:forEach>
    </c:if>

    <c:if test="${not empty requestScope.formData}">
        <%-- Form refill data --%>
        <c:forEach var="entry" items="${requestScope.formData}">
            <input type="hidden" name="${entry.key}" value="${entry.value}"/>
        </c:forEach>
    </c:if>
</div>