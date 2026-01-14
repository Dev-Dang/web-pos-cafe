<%@ tag body-content="empty" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
CSRF Token Hidden Input
Injects CSRF token as hidden input for form submission
Usage: <app:csrf/>
--%>
<input type="hidden" name="_csrf" value="${sessionScope.csrfToken}">
