<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<base href="${pageContext.request.contextPath}/">
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <meta content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"
          name="viewport"/>
    <meta content="ie=edge" http-equiv="X-UA-Compatible"/>
    <title><c:out value="${i18n.trans(pageTitle)}" default="Admin | Zero Star Coffee"/></title>

    <%-- AJAX --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/litepicker/dist/css/litepicker.css"/>

    <%-- Thư viện cho xuất file excel --%>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.18.5/xlsx.full.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/litepicker/dist/litepicker.js"></script>

    <%-- JQuery --%>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"
            integrity="sha512-v2CJ7UaYy4JwqLDIrZUI/4hqeoQieOmAZNXBeQyjo21dadnwR+8ZaIJVT8EE2iyI61OV8e6M8PP2/4hpQINQ/g=="
            crossorigin="anonymous" referrerpolicy="no-referrer"></script>

    <%-- Style --%>
    <link rel='stylesheet' href='assets/shared/styles/base.css'>
    <link rel="stylesheet" href="assets/admin/styles/admin.css">
</head>
<body class="scroll-hidden" data-page="${pageId}">
<jsp:include page="/WEB-INF/views/admin/pages/sidebar.jsp"/>
<%--  Main content  --%>
<div class="main-content">
    <jsp:include page="${requestScope.pageContent}"/>

    <%-- Flash message --%>
    <c:if test="${not empty requestScope.messages}">
        <div class=" toast-container
" hidden>
            <c:forEach var="msg" items="${requestScope.messages}">
                <p data-type="${msg.type}"
                   data-message="${sessionScope.i18n.trans(msg.msgKey)}">
                </p>
            </c:forEach>
        </div>
    </c:if>
</div>
<%-- Script --%>
<script>
    window.__APP_MODE__ = "${initParam.APP_MODE}";
</script>
<script src="assets/shared/bootstrap/js/bootstrap.bundle.min.js"></script>
<script type="module" src="assets/shared/js/base.js"></script>
<script src="assets/admin/js/admin.js"></script>
</body>
</html>