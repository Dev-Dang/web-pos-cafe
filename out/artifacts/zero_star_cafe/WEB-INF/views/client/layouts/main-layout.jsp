<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<base href="${pageContext.request.contextPath}/">

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <meta content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"
          name="viewport"/>
    <meta content="ie=edge" http-equiv="X-UA-Compatible"/>
    <title><c:out value="${i18n.trans(pageTitle)}" default="Zero Star Coffee"/></title>

    <%-- Google Font --%>
    <link href="https://fonts.googleapis.com" rel="preconnect"/>
    <link crossorigin href="https://fonts.gstatic.com" rel="preconnect"/>
    <link href="https://fonts.googleapis.com/css2?family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap"
          rel="stylesheet"/>

    <%-- Flat icon --%>
    <link rel="stylesheet"
          href="https://cdn-uicons.flaticon.com/3.0.0/uicons-solid-rounded/css/uicons-solid-rounded.css"
    />
    <link rel="stylesheet"
          href="https://cdn-uicons.flaticon.com/3.0.0/uicons-regular-rounded/css/uicons-regular-rounded.css"/>

    <%-- Style --%>
    <link rel="stylesheet" href="assets/shared/styles/base.css"/>
    <link rel="stylesheet" href="assets/client/styles/app.css"/>
</head>
<body class="scroll-hidden">
    <%-- Including the header component --%>
    <jsp:include page="/WEB-INF/views/client/layouts/header.jsp"/>

    <%--  Main content  --%>
    <jsp:include page="${requestScope.pageContent}"/>

    <%-- Including the footer component --%>
    <jsp:include page="/WEB-INF/views/client/layouts/footer.jsp"/>

    <%-- Flash message --%>
    <c:if test="${not empty requestScope.messages}">
        <div class="toast-container" hidden>
            <c:forEach var="msg" items="${requestScope.messages}">
                <p data-type="${msg.type}"
                   data-message="${sessionScope.i18n.trans(msg.msgKey)}">
                </p>
            </c:forEach>
        </div>
    </c:if>

    <%-- Script --%>
    <script>
        window.__APP_MODE__ = "${initParam.APP_MODE}";
    </script>
    <script src="assets/shared/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script type="module" src="assets/shared/js/base.js"></script>
    <script type="module" src="assets/client/js/main.js"></script>
</body>
</html>