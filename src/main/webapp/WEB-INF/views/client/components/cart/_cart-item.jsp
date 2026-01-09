<%--
  Cart Item - Single item with i18n support
  Author: Dang Van Trung
  Date: 08/01/2026
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${cartItem != null}">
    <div class="cart-item"
         data-cart-item
         data-item-id="${cartItem.id}"
         data-menu-item-id="${cartItem.menuItemId}"
         data-item-index="${status.index}"
         data-note="${cartItem.note}">

        <!-- Product Image -->
        <div class="cart-item__image">
            <img src="${cartItem.imageUrl}"
                 alt="${cartItem.itemName}"
                 class="cart-thumb">
        </div>

        <!-- Product Info Section -->
        <div class="cart-item__info">
            <!-- Product Name with Optional Note Icon -->
            <div class="cart-item__name-row">
                <div class="cart-item__name">
                    <c:out value="${cartItem.itemName}"/>
                </div>
                <c:if test="${not empty cartItem.note}">
                    <div class="cart-item__note-icon">
                        <i class="fi fi-rr-comment-alt icon-base"></i>
                    </div>
                </c:if>
            </div>

            <!-- Options & Notes -->
            <div class="cart-item__options">
                <c:forEach var="option" items="${cartItem.options}" varStatus="status">
                    <p class="cart-meta">
                        <c:out value="${option.optionValueName}"/>
                        <c:if test="${option.priceDelta > 0}">
                            (+<fmt:formatNumber value="${option.priceDelta}" type="number"
                                                groupingUsed="true"/> ${i18n.trans('general.currency.vnd')})
                        </c:if>
                    </p>
                </c:forEach>
                <c:if test="${not empty cartItem.note}">
                    <div class="cart-item__note">
                        <i class="fi fi-rr-comment-alt icon-base"></i>
                        <span><c:out value="${cartItem.note}"/></span>
                    </div>
                </c:if>
            </div>
        </div>

        <!-- Price & Controls Section -->
        <div class="cart-item__controls">
            <div class="cart-item__price-controls">
                <!-- Unit Price -->
                <div class="cart-item__price">
                    <fmt:formatNumber value="${cartItem.unitPrice + cartItem.optionsPrice}"
                                      type="number"
                                      groupingUsed="true"/> ${i18n.trans('general.currency.vnd')}
                </div>

                <!-- Quantity Controls Form -->
                <form action="${pageContext.request.contextPath}/cart/update"
                      method="POST"
                      up-submit
                      up-target=".cart-panel"
                      up-layer="root"
                      class="cart-item__qty-form">

                    <input type="hidden" name="cartItemId" value="${cartItem.id}">
                    <input type="hidden" name="qty" value="${cartItem.qty}">

                    <div class="cart-item__qty-controls">
                        <button type="submit"
                                name="action"
                                value="decrease"
                                class="cart-item__qty-btn"
                            ${cartItem.qty <= 1 ? 'formaction="'.concat(pageContext.request.contextPath).concat('/cart/remove"') : ''}>
                            <i class="fi fi-rr-minus icon-base"></i>
                        </button>

                        <span class="cart-item__qty">
                                ${cartItem.qty}
                        </span>

                        <button type="submit"
                                name="action"
                                value="increase"
                                class="cart-item__qty-btn">
                            <i class="fi fi-rr-plus icon-base"></i>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</c:if>
