<%--
  Description: Wrapper for partial responses (Unpoly fragments)
  Author: Dang Van Trung
  Date: 02/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Response context (flash messages, etc) --%>
<jsp:include page="_response-context.jsp"/>

<%-- Render the actual partial content --%>
<jsp:include page="${requestScope.pageContent}"/>