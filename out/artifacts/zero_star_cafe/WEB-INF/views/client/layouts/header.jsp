<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- ========= HEADER ========= --%>
<header class="header">
    <div class="container header-main">
        <nav class="navbar navbar-expand-lg">
            <div class="container-fluid">
                <%-- Brand name --%>
                <a class="navbar-brand fw-semibold text-primary" href="home">
                    Zero Star Coffee
                </a>

                <%-- Menu toggle --%>
                <button
                        class="navbar-toggler"
                        type="button"
                        data-bs-toggle="collapse"
                        data-bs-target="#navbarNavDropdown"
                        aria-controls="navbarNavDropdown"
                        aria-expanded="false"
                        aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <%-- Navigation --%>
                <div class="collapse navbar-collapse" id="navbarNavDropdown">
                    <ul class="navbar-nav w-100 justify-content-md-center align-items-md-center">

                        <%-- Store select --%>
                        <li class="nav-item ms-md-auto dropdown store-dropdown">
                            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown"
                               aria-expanded="false">
                                <div class="store-tool d-inline-flex align-items-center">
                                    <i class="fi fi-rr-marker icon-base"></i>
                                    <div class="store-pickup d-inline-flex flex-column me-4">
                                        <p class="store-name">${currentStore != null ? currentStore.name : i18n.trans("general.selectStore")}</p>
                                        <span class="store-address text-muted text-sm-start">${currentStore.address}</span>
                                    </div>
                                    <i class="fi fi-rr-angle-down"></i>
                                </div>
                            </a>

                            <ul class="dropdown-menu">
                                <c:if test="${currentStore != null}">
                                    <li class="store-chip">
                                        <a class="dropdown-item active"
                                           href="${pageContext.request.contextPath}/store/check-in?storeId=${currentStore.id}">
                                            <div class="store-pickup">
                                                <div class="store-detail">
                                                    <p class="store-name">${currentStore.name}</p>
                                                    <span class="store-address text-muted text-sm-start">${currentStore.address}</span>
                                                </div>
                                                <i class="fi fi-sr-check-circle icon-base"></i>
                                            </div>
                                        </a>
                                    </li>
                                </c:if>

                                <c:forEach items="${stores}" var="store">
                                    <c:if test="${empty currentStore or store.id != currentStore.id}">
                                        <li class="store-chip">
                                            <a class="dropdown-item"
                                               href="${pageContext.request.contextPath}/store/check-in?storeId=${store.id}">
                                                <div class="store-pickup">
                                                    <div class="store-detail">
                                                        <p class="store-name">${store.name}</p>
                                                        <span class="store-address text-muted text-sm-start">${store.address}</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </li>

                        <c:choose>
                            <c:when test="${empty sessionScope.authUser}">
                                <li class="nav-item ms-md-auto">
                                    <button
                                            type="button"
                                            class="btn btn-primary btn-open-modal"
                                            data-type="login">
                                            ${i18n.trans("general.login")}
                                    </button>
                                </li>
                            </c:when>

                            <c:otherwise>
                                <li class="nav-item ms-md-auto dropdown">
                                    <a class="nav-link dropdown-toggle" href="#" role="button"
                                       data-bs-toggle="dropdown" aria-expanded="false">
                                        <div class="user-tool d-inline-flex align-items-center">
                                            <div class="user-info d-inline-flex flex-column me-4">
                                                <span class="user-name text-black fw-semibold">${authUser.username}</span>
                                            </div>
                                            <img
                                                    src="https://images.unsplash.com/photo-1631947430066-48c30d57b943?auto=format&fit=crop&q=80&w=832"
                                                    alt="User Image"
                                                    class="user-avatar object-fit-cover rounded-circle"
                                            />
                                        </div>
                                    </a>
                                    <ul class="dropdown-menu">
                                        <li><a class="dropdown-item" href="#">Lịch sử đơn hàng</a></li>
                                        <li><a class="dropdown-item" href="#">Đặt bàn</a></li>
                                        <li>
                                            <hr class="dropdown-divider"/>
                                        </li>
                                        <li>
                                            <a class="dropdown-item text-danger"
                                               href="auth/logout">
                                                    ${i18n.trans("general.logout")}
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </div>
            </div>
        </nav>
    </div>
</header>
