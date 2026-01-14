<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="statusCode" value="${requestScope['jakarta.servlet.error.status_code']}"/>
<c:set var="exception" value="${requestScope['jakarta.servlet.error.exception']}"/>
<c:set var="message" value="${requestScope['jakarta.servlet.error.message']}"/>
<c:set var="requestUri" value="${requestScope['jakarta.servlet.error.request_uri']}"/>
<c:set var="area" value="${requestScope.area}"/>

<c:choose>
    <c:when test="${statusCode == 404}">
        <c:set var="errorTitle" value="${i18n.trans('general.error.404.title')}"/>
        <c:set var="errorSubtitle" value="${i18n.trans('general.error.404.subtitle')}"/>
        <c:set var="errorIcon" value="fi fi-rr-search"/>
    </c:when>

    <c:when test="${statusCode == 500}">
        <c:set var="errorTitle" value="${i18n.trans('general.error.500.title')}"/>
        <c:set var="errorSubtitle" value="${i18n.trans('general.error.500.subtitle')}"/>
        <c:set var="errorIcon" value="fi fi-rr-bug"/>
    </c:when>

    <c:when test="${statusCode == 403}">
        <c:set var="errorTitle" value="${i18n.trans('general.error.403.title')}"/>
        <c:set var="errorSubtitle" value="${i18n.trans('general.error.403.subtitle')}"/>
        <c:set var="errorIcon" value="fi fi-rr-warning"/>
    </c:when>

    <c:otherwise>
        <c:set var="errorTitle" value="${i18n.trans('general.error.unknown.title')}"/>
        <c:set var="errorSubtitle" value="${i18n.trans('general.error.unknown.subtitle')}"/>
        <c:set var="errorIcon" value="fi fi-rr-warning"/>
    </c:otherwise>
</c:choose>

<main class="container">
    <div class="py-12 text-center">
        <i class="${errorIcon} text-primary fs-1 d-block mb-8"></i>
        <h1 class="fw-bold mb-4 fs-2">${errorTitle}</h1>
        <p class="text-muted fs-5 mb-8">${errorSubtitle}</p>

        <!-- ========== DEBUG MODE DETAIL ========== -->
        <c:if test="${pageContext.servletContext.getInitParameter('APP_MODE') eq 'development'}">
            <div class="w-25 mx-auto my-5">
                <div class="alert alert-danger text-start">
                    <p><strong>Status Code:</strong> ${statusCode}</p>
                    <p><strong>Request URI:</strong> ${requestUri != null ? requestUri : "Not Defined"}</p>
                    <p><strong>Message:</strong> ${message}</p>

                    <c:if test="${not empty exception}">
                        <pre class="fs-6">${exception}</pre>
                    </c:if>
                </div>
            </div>
        </c:if>

        <a href="${area eq 'ADMIN' ? 'admin/dashboard' : 'home'}"
           class="btn btn-primary px-4">
            ${i18n.trans("general.button.backToHome")}
        </a>
        <a href="javascript:history.back()" class="btn btn-outline-secondary px-4 ms-2">
            ${i18n.trans("general.button.backToPreviousPage")}
        </a>
    </div>
</main>