<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctxPath" value="${pageContext.request.contextPath}"/>
<header class="site-header header">
    <div class="page-container">
        <div class="site-header__inner">
            <%-- Brand Logo --%>
            <a class="site-brand" href="${ctxPath}/home">
                Zero Star Cafe
            </a>

            <%-- Store Selector --%>
            <c:if test="${not empty stores}">
                <div class="dropdown store-dropdown">
                    <button class="btn btn-md dropdown-toggle"
                            type="button"
                            data-bs-toggle="dropdown"
                            aria-expanded="false">
                        <div class="store-tool">
                            <span class="icon-base text-primary">
                                <i class="fi fi-rr-marker"></i>
                            </span>
                            <div class="store-pickup">
                                <span class="store-name">
                                    <c:choose>
                                        <c:when test="${not empty currentStore}">
                                            ${currentStore.name}
                                        </c:when>
                                        <c:otherwise>
                                            ${i18n.trans("general.selectStore")}
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                                <c:if test="${not empty currentStore}">
                                    <span class="store-address">${currentStore.address}</span>
                                </c:if>
                            </div>
                            <span class="icon-base">
                                <i class="fi fi-rr-angle-small-down"></i>
                            </span>
                        </div>
                    </button>

                    <ul class="dropdown-menu">
                        <c:forEach items="${stores}" var="store">
                            <c:set var="isActive" value="${not empty currentStore && store.id == currentStore.id}"/>
                            <li class="store-chip">
                                <a class="dropdown-item ${isActive ? 'active' : ''}"
                                   href="${ctxPath}/store/check-in?storeId=${store.id}">
                                    <div class="store-detail">
                                        <p class="store-name mb-0">${store.name}</p>
                                        <p class="store-address mb-0">${store.address}</p>
                                    </div>
                                    <c:if test="${isActive}">
                                        <span class="store-check icon-base">
                                            <i class="fi fi-sr-check-circle"></i>
                                        </span>
                                    </c:if>
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </c:if>

            <%-- User Actions --%>
            <div class="site-header__actions">
                <c:choose>
                    <%-- Guest User --%>
                    <c:when test="${empty sessionScope.authUser}">
                        <button type="button"
                                class="btn btn-md btn-primary--filled"
                                up-follow
                                up-href="partial/login-form"
                                up-layer="new modal"
                                up-size="small"
                                up-animation="fade-in"
                                up-target="[up-main~=modal]"
                                up-history="false"
                                up-cache="false">
                            <span class="icon-base">
                                <i class="fi fi-rr-user"></i>
                            </span>
                            <span>${i18n.trans("general.login")}</span>
                        </button>
                    </c:when>

                    <%-- Authenticated User --%>
                    <c:otherwise>
                        <div class="dropdown">
                            <button class="btn btn-md dropdown-toggle"
                                    type="button"
                                    data-bs-toggle="dropdown"
                                    aria-expanded="false">
                                <div class="user-tool">
                                    <div class="user-info">
                                        <span class="user-name">${authUser.username}</span>
                                        <img src="https://images.unsplash.com/photo-1631947430066-48c30d57b943?auto=format&fit=crop&q=80&w=832"
                                             alt="${authUser.username}"
                                             class="user-avatar object-fit-cover rounded-circle"/>
                                    </div>
                                </div>
                            </button>
                            <ul class="dropdown-menu">
                                <li>
                                    <a class="dropdown-item" href="${ctxPath}/user/orders">
                                            ${i18n.trans("general.user.orderHistory")}
                                    </a>
                                </li>
                                <li>
                                    <a class="dropdown-item" href="${ctxPath}/user/booking">
                                            ${i18n.trans("general.user.bookTable")}
                                    </a>
                                </li>
                                <li>
                                    <hr class="dropdown-divider"/>
                                </li>
                                <li>
                                    <a class="dropdown-item text-danger"
                                       href="${ctxPath}/auth/logout">
                                            ${i18n.trans("general.logout")}
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</header>
