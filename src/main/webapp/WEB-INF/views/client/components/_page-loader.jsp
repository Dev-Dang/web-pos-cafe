<%--
  Description: Page loader component
  Author: Dang Van Trung
  Date: 31/12/2025
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="page-loader" data-page-loader>
    <div class="page-loader__spinner">
        <c:forEach begin="1" end="3">
            <div class="page-loader__circle"></div>
        </c:forEach>
    </div>
</div>