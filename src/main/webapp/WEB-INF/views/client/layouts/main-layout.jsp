<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<base href="${pageContext.request.contextPath}/"/>

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
<body class="scroll-hidden"<c:if test="${not empty sessionScope.authUser}"> data-user-authenticated</c:if><c:if test="${not empty sessionScope.SESSION_STORE_CONTEXT}"> data-store-id="${sessionScope.SESSION_STORE_CONTEXT.storeId}"</c:if><c:if test="${sessionScope.needsCartMerge}"> data-needs-cart-merge</c:if>>
    <%-- Including the header component --%>
    <jsp:include page="/WEB-INF/views/client/layouts/header.jsp"/>

    <%--  Main content  --%>
    <jsp:include page="${requestScope.pageContent}"/>

    <%-- Including the footer component --%>
    <jsp:include page="/WEB-INF/views/client/layouts/footer.jsp"/>

    <%-- Modal container --%>
    <div id="modal-container"></div>

    <%-- Full page loading overlay --%>
    <div class="page-loader" data-page-loader>
        <div class="page-loader__spinner">
            <div class="page-loader__circle"></div>
            <div class="page-loader__circle"></div>
            <div class="page-loader__circle"></div>
        </div>
    </div>

    <%-- Flash data: messages for toasts, and form state for modal reopening --%>
    <c:if test="${not empty requestScope.messages or not empty requestScope.openModal or not empty requestScope.formData}">
        <div id="flash-data" hidden
             data-open-modal="${requestScope.openModal}"
             data-messages='${requestScope.messages != null ? "true" : "false"}'
        >
                <%-- Toast messages --%>
            <c:if test="${not empty requestScope.messages}">
                <c:forEach var="msg" items="${requestScope.messages}">
                    <p data-type="${msg.type}" data-message="${sessionScope.i18n.trans(msg.msgKey)}"></p>
                </c:forEach>
            </c:if>

                <%-- Form refill data --%>
            <c:if test="${not empty requestScope.formData}">
                <c:forEach var="entry" items="${requestScope.formData}">
                    <input type="hidden" name="${entry.key}" value="${entry.value}"/>
                </c:forEach>
            </c:if>
        </div>
    </c:if>

    <%-- Script --%>
    <script src="assets/shared/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script type="module">
        // Load Toast module globally
        import { Toast } from './assets/shared/js/modules/toast.js';
        window.Toast = Toast;
    </script>
    <script type="module" src="assets/shared/js/base.js"></script>
    <script type="module" src="assets/client/js/main.js"></script>
    <script type="module" src="assets/client/js/modules/mobile-nav.js"></script>
</body>
</html>