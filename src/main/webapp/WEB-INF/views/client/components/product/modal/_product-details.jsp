<%--
  Description: Product detail fragment for modal display
  Author: Dang Van Trung
  Date: 06/01/2026
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="app" %>

<%-- Product modal fragment - dynamically loaded via Unpoly --%>
<c:set var="hasPromotion" value="${productDetail.basePrice > productDetail.currentPrice}"/>
<c:set var="optionGroupCount" value="${fn:length(productDetail.options)}"/>
<c:set var="layoutType" value="${optionGroupCount <= 1 ? 'compact' : 'full'}"/>
<c:set var="ctxPath" value="${pageContext.request.contextPath}"/>
<c:set var="isEditMode" value="${requestScope.editMode == true}" scope="request"/>
<c:set var="modalTitleKey"
       value="${isEditMode ? 'general.product.modal.updateCart' : 'general.product.modal.addToCart'}"
       scope="request"/>
<c:set var="formAction"
       value="${isEditMode ? ctxPath.concat('/cart/update-item') : ctxPath.concat('/cart/add')}"
       scope="request"/>

<div id="product-detail-modal"
     class="product-modal"
     tabindex="-1">
    <div class="product-modal__dialog">
        <div class="product-modal__content">
            <%-- Mobile Header --%>
            <div class="product-modal__mobile-header">
                <button type="button" class="product-modal__mobile-header__back" aria-label="Close">
                    <i class="fi fi-rr-angle-small-left"></i>
                </button>
                <h3 class="product-modal__mobile-header__title">${i18n.trans(modalTitleKey)}</h3>
                <div class="product-modal__mobile-header__spacer"></div>
            </div>

            <%-- Close Button (Desktop) --%>
            <button class="product-modal__close" type="button">
                <span class="icon-base"><i class="fi fi-rr-cross-small"></i></span>
            </button>

            <%-- Hidden form for cart submission --%>
            <form id="add-to-cart-form"
                  action="${formAction}"
                  method="POST"
                  up-submit
                  up-layer="root"
                  up-target=".cart-panel"
                  up-fail-target="#product-detail-modal"
                  up-on-loaded="up.layer.get('overlay').dismiss()"
                  data-product-modal-form>

                <%-- Hidden inputs for form data --%>
                <input type="hidden" name="menuItemId" value="${productDetail.id}">
                <input type="hidden" name="qty" value="1" data-form-quantity>
                <input type="hidden" name="note" value="" data-form-note>
                <c:if test="${isEditMode}">
                    <input type="hidden" name="cartItemId" value="${cartItemId}">
                    <input type="hidden" data-edit-mode value="true">
                    <input type="hidden" data-edit-qty value="${selectedQty}">
                    <input type="hidden" data-edit-note value="${fn:escapeXml(selectedNote)}">
                    <input type="hidden" data-edit-options value="${selectedOptionValueIdsCsv}">
                </c:if>
                <%-- Option values will be added dynamically via JS as multiple inputs with name="optionValueIds" --%>

                <%-- Dynamic layout based on option group count --%>
                <c:choose>
                    <%-- Layout 1: 0-1 option groups - Compact 2 columns (image left, info+options right) --%>
                    <c:when test="${layoutType == 'compact'}">
                        <div class="row g-3 product-modal__panel product-modal__panel--compact">
                                <%-- Column 1: Image only --%>
                            <div class="col-lg-6 col-12 product-modal__col product-modal__col-1">
                                <div class="product-modal__image-wrapper">
                                    <img src="${productDetail.imageUrl}" class="product-modal__image"
                                         alt="${productDetail.name}">
                                </div>
                            </div>

                                <%-- Column 2: Info + Options + Actions --%>
                            <div class="col-lg-6 col-12 product-modal__col product-modal__col-2">
                                <jsp:include page="/WEB-INF/views/client/components/product/modal/_product-info.jsp"/>
                                <jsp:include
                                        page="/WEB-INF/views/client/components/product/modal/_product-options.jsp"/>
                                <jsp:include page="/WEB-INF/views/client/components/product/modal/_product-footer.jsp"/>
                            </div>
                        </div>
                    </c:when>

                    <%-- Layout 2: 2+ option groups - Full 2 columns (info+image left, all options right) --%>
                    <c:otherwise>
                        <div class="row g-3 product-modal__panel product-modal__panel--full">
                                <%-- Column 1: Product Info + Image --%>
                            <div class="col-lg-6 col-12 product-modal__col product-modal__col-1">
                                <jsp:include page="/WEB-INF/views/client/components/product/modal/_product-info.jsp"/>
                                <div class="product-modal__image-wrapper">
                                    <img src="${productDetail.imageUrl}" class="product-modal__image"
                                         alt="${productDetail.name}">
                                </div>
                            </div>

                                <%-- Column 2: ALL Options + Actions (scrollable) --%>
                            <div class="col-lg-6 col-12 product-modal__col product-modal__col-2">
                                <jsp:include
                                        page="/WEB-INF/views/client/components/product/modal/_product-options.jsp"/>
                                <jsp:include page="/WEB-INF/views/client/components/product/modal/_product-footer.jsp"/>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </form>
        </div>
    </div>
</div>
