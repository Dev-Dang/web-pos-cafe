<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- ========= HEADER ========= --%>
<header class="site-header header">
    <div class="page-container">
        <div class="site-header__inner">
            <a class="site-brand" href="home">
                Zero Star Cafe
            </a>

            <div class="dropdown store-dropdown">
                <button
                        class="btn btn-md dropdown-toggle"
                        type="button"
                        data-bs-toggle="dropdown"
                        aria-expanded="false"
                >
                    <div class="store-tool">
                        <span class="icon-base text-primary">
                            <i class="fi fi-rr-marker"></i>
                        </span>
                        <div class="store-pickup">
                            <span class="store-name">${currentStore != null ? currentStore.name : i18n.trans("general.selectStore")}</span>
                            <span class="store-address">
                                <c:if test="${currentStore != null}">${currentStore.address}</c:if>
                            </span>
                        </div>
                        <span class="icon-base">
                            <i class="fi fi-rr-angle-small-down"></i>
                        </span>
                    </div>
                </button>
                <ul class="dropdown-menu">
                    <c:if test="${currentStore != null}">
                        <li class="store-chip">
                            <a class="dropdown-item active"
                               href="${pageContext.request.contextPath}/store/check-in?storeId=${currentStore.id}">
                                <div class="store-detail">
                                    <p class="store-name mb-0">${currentStore.name}</p>
                                    <p class="store-address mb-0">${currentStore.address}</p>
                                </div>
                                <span class="store-check icon-base">
                                    <i class="fi fi-sr-check-circle"></i>
                                </span>
                            </a>
                        </li>
                    </c:if>

                    <c:forEach items="${stores}" var="store">
                        <c:if test="${empty currentStore or store.id != currentStore.id}">
                            <li class="store-chip">
                                <a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/store/check-in?storeId=${store.id}">
                                    <div class="store-detail">
                                        <p class="store-name mb-0">${store.name}</p>
                                        <p class="store-address mb-0">${store.address}</p>
                                    </div>
                                </a>
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
            </div>

            <div class="site-header__actions">
                <c:choose>
                    <c:when test="${empty sessionScope.authUser}">
                        <button type="button"
                                class="btn btn-md btn-primary--filled"
                                up-follow
                                up-href="auth/login"
                                up-layer="new"
                                up-mode="modal"
                                up-target="[up-main~=modal]"
                                up-history="false">
                            <span class="icon-base">
                                <i class="fi fi-rr-user"></i>
                            </span>
                            <span class="btn-text">${i18n.trans("general.login")}</span>
                        </button>
                    </c:when>

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
                                             alt="User Image"
                                             class="user-avatar object-fit-cover rounded-circle"/>
                                    </div>
                                </div>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="#">Lịch sử đơn hàng</a></li>
                                <li><a class="dropdown-item" href="#">Đặt bàn</a></li>
                                <li>
                                    <hr class="dropdown-divider"/>
                                </li>
                                <li>
                                    <a class="dropdown-item text-danger" href="auth/logout">
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
