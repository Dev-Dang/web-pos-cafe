<%--
  Description: login form with (loginUsername, loginPassword)
  Author: Dang Van Trung
  Date: 03/01/2026
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>

<%-- ========= LOGIN MODAL ========= --%>
<div
        class="auth-modal"
        id="loginModal"
        tabindex="-1"
        role="dialog"
        aria-modal="true"
        aria-labelledby="loginModalLabel"
        up-main="modal"
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
                    <h2 class="auth-modal__title" id="loginModalLabel">
                        ${i18n.trans("form.title.login")}
                    </h2>
                    <p class="auth-modal__subtitle">${i18n.trans("form.login.desc")}</p>
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

            <%-- Login Form --%>
            <div id="loginFormContainer">
                <form id="loginForm"
                      class="auth-modal__form"
                      novalidate
                      method="post"
                      action="auth/login"
                      up-submit
                      up-disable
                      up-fail-target="#loginModal"
                >
                    <%-- CSRF Token --%>
                    <app:csrf/>

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
                                   autocomplete="current-password"
                                   class="form-control <c:if test="${not empty formErrors.password}">is-invalid</c:if>"
                                   placeholder="${i18n.trans("form.password")}" required/>
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
                    </div>

                    <div class="auth-modal__actions">
                        <button
                                type="submit"
                                class="btn btn-lg btn-primary--filled btn-full auth-modal__submit"
                        >
                            ${i18n.trans("form.button.login")}
                        </button>
                        <button
                                type="button"
                                class="auth-modal__forgot-link"
                                up-follow
                                up-href="partial/forgot-password-form"
                                up-layer="current"
                                up-target="[up-main~=modal]"
                                up-history="false"
                        >
                            ${i18n.trans("form.forgotPassword")}
                        </button>
                    </div>
                </form>
            </div>

            <%-- Footer Section --%>
            <div class="auth-modal__footer">
                <span class="auth-modal__footer-text">
                    ${i18n.trans("form.dontHaveAnAccount")}
                </span>
                <button
                        type="button"
                        class="auth-modal__footer-link"
                        up-follow
                        up-href="partial/register-form"
                        up-layer="current"
                        up-target="[up-main~=modal]"
                        up-history="false"
                >
                    ${i18n.trans("form.button.register")}
                </button>
            </div>
        </div>
    </div>
</div>
