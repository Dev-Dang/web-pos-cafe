<%--
  Description:
  Author: Dang Van Trung
  Date: 05/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="bottom-nav d-lg-none">
    <a href="#" class="bottom-nav__item active">
        <span class="bottom-nav__icon"><i class="fi fi-rr-home"></i></span>
        <span class="bottom-nav__label">Trang chủ</span>
    </a>
    <a href="#" class="bottom-nav__item">
        <span class="bottom-nav__icon"><i class="fi fi-rr-search"></i></span>
        <span class="bottom-nav__label">Tìm kiếm</span>
    </a>
    <a
            href="#"
            class="bottom-nav__item bottom-nav__item--cart"
            data-cart-toggle
    >
        <span class="bottom-nav__icon">
            <i class="fi fi-rr-shopping-cart"></i>
            <span class="bottom-nav__badge" data-cart-count>0</span>
        </span>
        <span class="bottom-nav__label">Giỏ hàng</span>
    </a>
    <a href="#" class="bottom-nav__item">
        <span class="bottom-nav__icon"><i class="fi fi-rr-user"></i></span>
        <span class="bottom-nav__label">Tài khoản</span>
    </a>
</nav>