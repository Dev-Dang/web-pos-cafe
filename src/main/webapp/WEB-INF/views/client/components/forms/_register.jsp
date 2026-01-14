<%--
  Description: Registration modal adapted for unpoly modal layer
  Author: Dang Van Trung
  LastModified: 04/01/2026
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>

<%-- ========= REGISTER FORM FOR UNPOLY MODAL ========= --%>
<div class="auth-modal__content" up-main="modal">
    <%-- Mobile Header --%>
    <div class="modal-mobile-header">
        <button type="button"
                class="modal-mobile-header__back"
                up-dismiss
                aria-label="Close">
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
                <button type="button"
                        class="btn btn-lg btn-neutral--outlined btn-full auth-modal__social-btn">
                    <span class="auth-modal__social-icon">
                        <img src="${pageContext.request.contextPath}/assets/client/img/icons/google.svg"
                             alt="Google"
                             class="auth-modal__social-img"/>
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
                    up-disable
                    up-fail-target="[up-main~=modal]"
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
                               autocomplete="new-password"
                               class="form-control <c:if test="${not empty formErrors.password}">is-invalid</c:if>"
                               placeholder="${i18n.trans("form.password")}" required
                               minlength="6"/>
                        <label for="password">${i18n.trans("form.password")}</label>
                        <button type="button"
                                class="password-toggle"
                                data-toggle-password="password"
                                aria-label="Toggle password visibility">
                            <i class="fi fi-rr-eye"></i>
                        </button>
                        <c:choose>
                            <c:when test="${not empty formErrors.password}">
                                <div class="invalid-feedback">
                                        ${i18n.trans(formErrors.password)}
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="form-text text-muted">
                                        ${i18n.trans("form.password.hint")}
                                </div>
                            </c:otherwise>
                        </c:choose>
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
                        <button type="button"
                                class="password-toggle"
                                data-toggle-password="confirmPassword"
                                aria-label="Toggle password visibility">
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
                    <button type="submit"
                            class="btn btn-lg btn-primary--filled btn-full auth-modal__submit">
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
                    up-href="auth/login"
                    up-layer="current"
                    up-transition="cross-fade"
                    up-target="[up-main~=modal]"
                    up-history="false"
                    up-cache="false">
                ${i18n.trans("form.button.login")}
            </button>
        </div>
    </div>
</div>
</div>
