<%--
  Cart Item - Single item matching Figma design with existing CSS classes
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
         data-node-id="239-3822">
         
        <!-- Product Image (64x64 as per Figma) -->
        <div class="cart-item__image" data-node-id="I239-3822;11-1198">
            <img src="/assets/images/products/placeholder.jpg" 
                 alt="${cartItem.itemNameSnapshot}" 
                 class="cart-thumb"
                 data-node-id="I239-3822;11-1199">
        </div>

        <!-- Product Info Section -->
        <div class="cart-item__info" data-node-id="I239-3822;11-1200">
            <!-- Product Name with Optional Note Icon -->
            <div class="cart-item__name-row" data-node-id="I239-3822;239-5814">
                <div class="cart-item__name" data-node-id="I239-3822;11-1202">
                    <c:out value="${cartItem.itemNameSnapshot}"/>
                </div>
                <c:if test="${not empty cartItem.note}">
                    <div class="cart-item__note-icon" data-node-id="I239-3822;239-6032">
                        <i class="fi fi-rr-comment-alt icon-base"></i>
                    </div>
                </c:if>
            </div>
            
            <!-- Options & Notes -->
            <div class="cart-item__options" data-node-id="I239-3822;11-1203">
                <c:forEach var="option" items="${cartItem.options}" varStatus="status">
                    <p class="cart-meta">
                        <c:out value="${option.optionValueNameSnapshot}"/>
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
        <div class="cart-item__controls" data-node-id="I239-3822;239-5509">
            <div class="cart-item__price-controls" data-node-id="I239-3822;11-1892">
                <!-- Unit Price -->
                <div class="cart-item__price" data-node-id="I239-3822;11-1204">
                    <fmt:formatNumber value="${cartItem.unitPriceSnapshot + cartItem.optionsPriceSnapshot}" 
                                      type="number" 
                                      groupingUsed="true"/> Đ
                </div>
                
                <!-- Quantity Controls (matching Figma design) -->
                <div class="cart-item__qty-controls" data-node-id="I239-3822;11-1891">
                    <button type="button" 
                            class="cart-item__qty-btn" 
                            data-qty-action="decrease"
                            data-item-id="${cartItem.id}"
                            data-node-id="I239-3822;11-1889"
                            ${cartItem.qty <= 1 ? 'data-will-remove="true"' : ''}>
                        <i class="fi fi-rr-minus icon-base"></i>
                    </button>
                    
                    <span class="cart-item__qty" 
                          data-item-qty
                          data-node-id="I239-3822;11-1885">
                        ${cartItem.qty}
                    </span>
                    
                    <button type="button" 
                            class="cart-item__qty-btn" 
                            data-qty-action="increase"
                            data-item-id="${cartItem.id}"
                            data-node-id="I239-3822;11-1886">
                        <i class="fi fi-rr-plus icon-base"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>
</c:if>
