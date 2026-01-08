<%--
  Description: Home page carousel component
  Author: Dang Van Trung
  Date: 05/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="heroCarousel"
     class="carousel slide hero"
     data-bs-ride="carousel"
     data-bs-interval="5000">
    <div class="carousel-inner">
        <div class="carousel-item active">
            <img src="assets/client/img/banner/banner-2.png"
                 class="d-block w-100"
                 alt="Banner"/>
        </div>
        <div class="carousel-item">
            <img src="assets/client/img/banner/banner-3.png"
                 class="d-block w-100"
                 alt="Banner"/>
        </div>
    </div>
    <div class="carousel-indicators">
        <button type="button"
                data-bs-target="#heroCarousel"
                data-bs-slide-to="0"
                class="active"
                aria-current="true"
                aria-label="Slide 1"
        ></button>
        <button type="button"
                data-bs-target="#heroCarousel"
                data-bs-slide-to="1"
                aria-label="Slide 2"
        ></button>
    </div>
</div>