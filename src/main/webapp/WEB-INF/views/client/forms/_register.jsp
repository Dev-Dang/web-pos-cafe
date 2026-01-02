<%--
  Description: Registration modal for customers (regEmail, regUsername, regPassword)
  Author: Dang Van Trung
  LastModified: 02/01/2026
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- ========= REGISTER MODAL ========= --%>
<div
        class="auth-modal"
        id="registerModal"
        tabindex="-1"
        role="dialog"
        aria-modal="true"
        aria-labelledby="registerModalLabel"
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
                <%-- Header Section with Title and Social Button --%>
                <div class="auth-modal__header-section">
                    <div class="auth-modal__header">
                        <h2 class="auth-modal__title" id="registerModalLabel">
                            ${i18n.trans("form.title.register")}
                        </h2>
                        <p class="auth-modal__subtitle">
                            ${i18n.trans("form.social.desc")}
                        </p>
                    </div>

                    <%-- Social Login Section --%>
                    <div class="auth-modal__social">
                        <button
                                type="button"
                                class="btn btn-lg btn-neutral--outlined btn-full auth-modal__social-btn"
                        >
                            <span class="auth-modal__social-icon">
                                <img
                                        src="${pageContext.request.contextPath}/assets/client/img/icons/google.svg"
                                        alt="Google"
                                        class="auth-modal__social-img"
                                />
                            </span>
                            <span class="auth-modal__social-text">
                                ${i18n.trans("form.useGoogleAccount")}
                            </span>
                        </button>
                    </div>
                </div>

                <%-- Divider --%>
                <div class="auth-modal__divider" role="separator">
                    <span class="auth-modal__divider-text">
                        ${i18n.trans("form.or")}
                    </span>
                </div>

                <%-- Register Form --%>
                <div id="registerFormContainer">
                <form
                        id="registerForm"
                        class="auth-modal__form"
                        novalidate
                        method="post"
                        action="auth/register"
                        up-submit
                        up-target="#registerModal"
                        up-fail-target="#registerModal"
                        up-history="false"
                >
                    <div class="auth-modal__fields">
                        <div class="form-floating">
                            <input type="email" id="email" name="email"
                                   autocomplete="email"
                                   class="form-control <c:if test="${not empty formErrors.email}">is-invalid</c:if>"
                                   placeholder="${i18n.trans("form.email")}"
                                   value="${formData.email}" required/>
                            <label for="email"> ${i18n.trans("form.email")} </label>
                            <div class="invalid-feedback">
                                <c:if test="${not empty formErrors.email}">
                                    ${i18n.trans(formErrors.email)}
                                </c:if>
                            </div>
                        </div>

                        <div class="form-floating password-field">
                            <input type="password" id="password" name="password"
                                   autocomplete="new-password"
                                   class="form-control <c:if test="${not empty formErrors.password}">is-invalid</c:if>"
                                   placeholder="${i18n.trans("form.password")}" required
                                   minlength="6"/>
                            <label for="password"> ${i18n.trans("form.password")} </label>
                            <button
                                    type="button"
                                    class="password-toggle"
                                    data-toggle-password="password"
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
                            <input type="password" id="confirmPassword" name="confirmPassword"
                                   autocomplete="new-password"
                                   class="form-control <c:if test="${not empty formErrors.confirmPassword}">is-invalid</c:if>"
                                   placeholder="${i18n.trans("form.confirmPassword")}" required
                                   minlength="6"/>
                            <label for="confirmPassword">
                                ${i18n.trans("form.confirmPassword")}
                            </label>
                            <button
                                    type="button"
                                    class="password-toggle"
                                    data-toggle-password="confirmPassword"
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

                    <div class="form-check auth-modal__terms">
                        <input type="checkbox"
                               class="form-check-input <c:if test="${not empty formErrors.agreeTerms}">is-invalid</c:if>"
                               id="agreeTerms" name="agreeTerms" required/>
                        <label class="form-check-label" for="agreeTerms">
                            ${i18n.trans("form.agree")}
                            <a href="#" class="auth-modal__terms-link">
                                ${i18n.trans("form.term")}
                            </a>
                            ${i18n.trans("form.usage")}
                        </label>
                        <div class="invalid-feedback">
                            <c:if test="${not empty formErrors.agreeTerms}">
                                ${i18n.trans(formErrors.agreeTerms)}
                            </c:if>
                        </div>
                    </div>

                    <div class="auth-modal__actions">
                        <button
                                type="submit"
                                class="btn btn-lg btn-primary--filled btn-full auth-modal__submit"
                        >
                            ${i18n.trans("form.button.register")}
                        </button>
                    </div>
                </form>
                </div>

                <%-- Footer Section --%>
                <div class="auth-modal__footer">
                    <span class="auth-modal__footer-text">
                        ${i18n.trans("form.alreadyHasAnAccount")}
                    </span>
                    <button
                            type="button"
                            class="auth-modal__footer-link"
                            up-follow
                            up-href="partial/login-form"
                            up-layer="current"
                            up-target="[up-main~=modal]"
                            up-history="false"
                    >
                        ${i18n.trans("form.button.login")}
                    </button>
                </div>
            </div>
    </div>
</div>
