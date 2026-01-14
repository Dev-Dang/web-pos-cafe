<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="locale" value="${sessionScope.locale}"/>
<c:choose>
    <c:when test="${locale.toLanguageTag() eq 'vi-VN'}">
        <c:set var="nextLang" value="en-US"/>
        <c:set var="langLabel" value="English"/>
    </c:when>
    <c:otherwise>
        <c:set var="nextLang" value="vi-VN"/>
        <c:set var="langLabel" value="Tiếng Việt"/>
    </c:otherwise>
</c:choose>

<footer class="footer">
    <div class="container">
        <div class="footer-top">
            <h4 class="fw-semibold mb-5"> ${i18n.trans("general.company.name")} </h4>
            <ul class="p-0">
                <li class="d-flex gap-2 align-items-center mb-2">
                    <i class="icon-base text-primary fi fi-rr-search-alt"></i>
                    <p class="text-muted mb-0">
                        ${i18n.trans("general.company.licenseNumber")}
                        <strong class="fw-normal text-body">01234567889</strong>
                    </p>
                </li>
                <li class="d-flex gap-2 align-items-center mb-2">
                    <i class="icon-base text-primary fi fi-rr-building"></i>
                    <p class="text-muted mb-0">
                        ${i18n.trans("general.company.officeAddress")}
                        <strong class="fw-normal text-body">
                            Tầng 8 - Toà MPlaza Quận 1, Phường Sài Gòn, Thành phố Hồ Chí Minh
                        </strong>
                    </p>
                </li>
                <li class="d-flex gap-2 align-items-center mb-2">
                    <i class="icon-base text-primary fi fi-rr-envelope"></i>
                    <p class="text-muted mb-0">
                        Email:
                        <strong class="fw-normal text-body">cskh@zero-star-cafe.com</strong>
                    </p>
                </li>
            </ul>
        </div>

        <div class="footer-end d-flex justify-content-between align-items-center">
            <p class="text-muted text-center mb-0">&copy; 2025 ${i18n.trans("general.copyright")} Zero Star Coffee</p>

            <a class="btn btn-outline-primary d-flex align-items-center gap-2"
               href="?lang=${nextLang}">
                <i class="icon-base fi fi-rr-world"></i>
                ${langLabel}
            </a>
        </div>
    </div>
</footer>