<%--
  Description: Pagination trigger for infinite scroll
  Author: Dang Van Trung
  Date: 07/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctxPath" value="${pageContext.request.contextPath}"/>
<c:if test="${hasMore}">
    <%-- Invisible trigger element for Unpoly infinite scroll --%>
    <div data-load-trigger
         data-pagination-url="${ctxPath}${paginationUrl}"
         style="height: 20px; margin-top: 2rem; opacity: 0; pointer-events: none;">
    </div>
</c:if>