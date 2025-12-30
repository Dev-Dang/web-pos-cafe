<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 
    Cart item fragment - represents a single item in the cart.
    Expects 'cartItem' to be set in request scope by the parent JSP.
--%>

<c:if test="${cartItem != null}">
    <div class="cart-item" 
         data-cart-item 
         data-item-id="${cartItem.id}"
         data-menu-item-id="${cartItem.menuItemId}">
        
        <div class="cart-item__info">
            <div class="cart-item__name">${cartItem.itemNameSnapshot}</div>
            
            <c:if test="${not empty cartItem.options}">
                <div class="cart-item__options">
                    <c:forEach var="option" items="${cartItem.options}" varStatus="status">
                        <span class="cart-item__option">
                            ${option.optionValueNameSnapshot}<c:if test="${!status.last}">, </c:if>
                        </span>
                    </c:forEach>
                </div>
            </c:if>
            
            <c:if test="${not empty cartItem.note}">
                <div class="cart-item__note">
                    <i class="fi fi-rr-comment-alt icon-base"></i>
                    <span>${cartItem.note}</span>
                </div>
            </c:if>
            
            <div class="cart-item__price">
                <fmt:formatNumber value="${cartItem.unitPriceWithOptions}" type="number" groupingUsed="true"/>đ
            </div>
        </div>
        
        <div class="cart-item__controls">
            <div class="cart-item__qty-controls">
                <button type="button" 
                        class="cart-item__qty-btn" 
                        data-qty-action="decrease"
                        data-item-id="${cartItem.id}"
                        ${cartItem.qty <= 1 ? 'data-will-remove="true"' : ''}>
                    <i class="fi fi-rr-minus icon-base"></i>
                </button>
                <span class="cart-item__qty" data-item-qty>${cartItem.qty}</span>
                <button type="button" 
                        class="cart-item__qty-btn" 
                        data-qty-action="increase"
                        data-item-id="${cartItem.id}">
                    <i class="fi fi-rr-plus icon-base"></i>
                </button>
            </div>
            
            <div class="cart-item__line-total">
                <fmt:formatNumber value="${cartItem.lineTotal}" type="number" groupingUsed="true"/>đ
            </div>
            
            <button type="button" 
                    class="cart-item__remove" 
                    data-remove-item
                    data-item-id="${cartItem.id}"
                    title="Xóa">
                <i class="fi fi-rr-trash icon-base"></i>
            </button>
        </div>
    </div>
</c:if>
