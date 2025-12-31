<%--
Description: Forgot password modal for password recovery (email only)
Author: Dang Van Trung
LastModified: 28/12/2025
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- ========= FORGOT PASSWORD MODAL ========= --%>
<div
        class="modal fade"
        id="forgotPasswordModal"
        tabindex="-1"
        aria-labelledby="forgotPasswordModalLabel"
        aria-hidden="true"
>
    <div class="modal-dialog modal-dialog-centered auth-modal">
        <div class="modal-content auth-modal__content">
            <%-- Mobile Header --%>
            <div class="modal-mobile-header">
                <button
                        type="button"
                        class="modal-mobile-header__back"
                        data-bs-dismiss="modal"
                        aria-label="Close"
                >
                    <i class="fi fi-rr-angle-small-left"></i>
                </button>
                <h3 class="modal-mobile-header__title"></h3>
                <div class="modal-mobile-header__spacer"></div>
            </div>

            <div class="modal-body auth-modal__body">
                <%-- Header Section --%>
                <div class="auth-modal__header-section">
                    <div class="auth-modal__header">
                        <h2 class="auth-modal__title" id="forgotPasswordModalLabel">
                            ${i18n.trans("form.title.forgotPassword")}
                        </h2>
                        <p class="auth-modal__subtitle">
                            ${i18n.trans("form.forgotPassword.desc")}
                        </p>
                    </div>
                </div>

                <%-- Forgot Password Form --%>
                <form
                        id="forgotPasswordForm"
                        class="auth-modal__form"
                        novalidate
                        method="post"
                        action="auth/forgot-password"
                >
                    <div class="auth-modal__fields">
                        <div class="form-floating">
                            <input type="email" id="forgotEmail" name="email"
                                   autocomplete="email"
                                   class="form-control <c:if test="${not empty formErrors.email}">is-invalid</c:if>"
                                   placeholder="${i18n.trans("form.email")}"
                                   value="${formData.email}" required/>
                            <label for="forgotEmail"> ${i18n.trans("form.email")} </label>
                            <div class="invalid-feedback">
                                <c:if test="${not empty formErrors.email}">
                                    ${i18n.trans(formErrors.email)}
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <div class="auth-modal__actions">
                        <button
                                type="submit"
                                class="btn btn-lg btn-primary--filled btn-full"
                        >
                            ${i18n.trans("form.button.sendResetLink")}
                        </button>
                    </div>
                </form>

                <%-- Footer Section --%>
                <div class="auth-modal__footer">
                    <span class="auth-modal__footer-text">
                        ${i18n.trans("form.rememberPassword")}
                    </span>
                    <button
                            type="button"
                            class="auth-modal__footer-link btn-open-modal"
                            data-type="login"
                    >
                        ${i18n.trans("form.button.backToLogin")}
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
