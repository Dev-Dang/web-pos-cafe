<%--
  Component: Product Footer Actions
  Description: Note field, quantity stepper, and add to cart button
  Usage: <jsp:include page="_product-footer.jsp"/>
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="product-modal__footer">
    <div class="product-modal__note-field">
        <label class="product-modal__note-label">${i18n.trans("general.product.modal.note")}</label>
        <textarea class="product-modal__note-input"
                  data-modal-note
                  placeholder="${i18n.trans("general.product.modal.notePlaceholder")}"
                  rows="2"
                  maxlength="200"></textarea>
    </div>
    <div class="product-modal__qty-row">
        <span>${i18n.trans("general.product.modal.quantity")}</span>
        <div class="modal-stepper">
            <button class="modal-stepper__btn"
                    type="button"
                    data-modal-minus><i class="fi fi-rr-minus"></i></button>
            <span class="modal-stepper__value" data-modal-qty>1</span>
            <button class="modal-stepper__btn"
                    type="button"
                    data-modal-plus><i class="fi fi-rr-plus"></i></button>
        </div>
    </div>
    <button class="product-modal__cta"
            type="submit"
            data-modal-action>
        <span>${i18n.trans(modalTitleKey)}</span>
        <span class="product-modal__cta-dot"></span>
        <span data-modal-total>
            <fmt:formatNumber value="${productDetail.currentPrice}" type="number" groupingUsed="true"/>đ
        </span>
    </button>
</div>
