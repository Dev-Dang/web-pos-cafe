<%--
Description: Reset password modal with new password and confirmation
Author: Dang Van Trung
LastModified: 02/01/2026
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- ========= RESET PASSWORD MODAL ========= --%>
<div
        class="auth-modal"
        id="resetPasswordModal"
        tabindex="-1"
        role="dialog"
        aria-modal="true"
        aria-labelledby="resetPasswordModalLabel"
        up-main="modal"
        up-accept-location="${deeplinkRoot}"
>
    <div class="auth-modal__content">
            <%-- Mobile Header --%>
            <div class="modal-mobile-header">
                <button
                        type="button"
                        class="modal-mobile-header__back"
                        up-dismiss
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
                        <h2 class="auth-modal__title" id="resetPasswordModalLabel">
                            ${i18n.trans("form.title.resetPassword")}
                        </h2>
                        <p class="auth-modal__subtitle">
                            ${i18n.trans("form.resetPassword.desc")}
                        </p>
                    </div>
                </div>

                <%-- Reset Password Form --%>
                <form
                        id="resetPasswordForm"
                        class="auth-modal__form"
                        novalidate
                        method="post"
                        action="auth/reset-password"
                >
                    <%-- Hidden token field (passed from email link) --%>
                    <input type="hidden" name="token" value="${param.token}"/>

                    <div class="auth-modal__fields">
                        <div class="form-floating password-field">
                            <input type="password" id="newPassword" name="password"
                                   autocomplete="new-password"
                                   class="form-control <c:if test="${not empty formErrors.password}">is-invalid</c:if>"
                                   placeholder="${i18n.trans("form.newPassword")}" required
                                   minlength="6"/>
                            <label for="newPassword">
                                ${i18n.trans("form.newPassword")}
                            </label>
                            <button
                                    type="button"
                                    class="password-toggle"
                                    data-toggle-password="newPassword"
                                    aria-label="Toggle password visibility"
                            >
                                <i class="fi fi-rr-eye"></i>
                            </button>
                            <div class="invalid-feedback">
                                <c:if test="${not empty formErrors.password}">
                                    ${i18n.trans(formErrors.password)}
                                </c:if>
                            </div>
                        </div>

                        <div class="form-floating password-field">
                            <input type="password" id="confirmNewPassword"
                                   name="confirmPassword" autocomplete="new-password"
                                   class="form-control <c:if test="${not empty formErrors.confirmPassword}">is-invalid</c:if>"
                                   placeholder="${i18n.trans("form.confirmPassword")}" required
                                   minlength="6"/>
                            <label for="confirmNewPassword">
                                ${i18n.trans("form.confirmPassword")}
                            </label>
                            <button
                                    type="button"
                                    class="password-toggle"
                                    data-toggle-password="confirmNewPassword"
                                    aria-label="Toggle password visibility"
                            >
                                <i class="fi fi-rr-eye"></i>
                            </button>
                            <div class="invalid-feedback">
                                <c:if test="${not empty formErrors.confirmPassword}">
                                    ${i18n.trans(formErrors.confirmPassword)}
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <div class="auth-modal__actions">
                        <button
                                type="submit"
                                class="btn btn-lg btn-primary--filled btn-full"
                        >
                            ${i18n.trans("form.button.resetPassword")}
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
                            class="auth-modal__footer-link"
                            up-follow
                            up-href="partial/login-form"
                            up-layer="current"
                            up-target="[up-main~=modal]"
                    >
                        ${i18n.trans("form.button.backToLogin")}
                    </button>
                </div>
            </div>
    </div>
</div>
