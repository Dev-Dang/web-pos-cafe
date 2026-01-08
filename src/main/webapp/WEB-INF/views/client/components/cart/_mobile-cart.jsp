<%--
  Description:
  Author: Dang Van Trung
  Date: 05/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="mobile-cart-drawer" data-mobile-cart>
    <div class="mobile-cart-drawer__backdrop" data-cart-close></div>
    <div class="mobile-cart-drawer__panel">
        <div class="mobile-cart-drawer__header">
            <h5 class="mb-0">Giỏ hàng
                <span class="cart-count"></span>
            </h5>
            <button class="btn-close" data-cart-close></button>
        </div>
        <div class="mobile-cart-drawer__body">
            <div class="cart-list" data-cart-list>
                <!-- Cart items synced from localStorage -->
            </div>
        </div>
        <div class="mobile-cart-drawer__footer">
            <div class="cart-summary">
                <div class="cart-summary__row total">
                    <span>Tổng cộng</span>
                    <span data-cart-total>0 Đ</span>
                </div>
            </div>
            <button class="btn btn-lg btn-primary--filled w-100" data-checkout-btn>
                Thanh toán
            </button>
        </div>
    </div>
</div>