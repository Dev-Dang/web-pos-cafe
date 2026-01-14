<%--
  Description: Registration modal for customers (regEmail, regUsername, regPassword)
  Author: Dang Van Trung
  LastModified: 10/11/2025
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/template" id="tpl-modal-register">
    <div class="modal fade" id="registerModal" tabindex="-1" aria-labelledby="registerModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-sm modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="d-flex flex-column align-items-center">
                        <h3 class="modal-title fw-bold text-center text-primary mb-3">
                            ${i18n.trans("form.title.register")}
                        </h3>
                        <p class="text-center text-muted mb-4">
                            ${i18n.trans("form.social.desc")}
                        </p>

                        <%-- Social buttons --%>
                        <div class="d-flex gap-4 w-100">
                            <a href="#" class="btn btn-lg btn-outline-primary w-100">
                                <img src="assets/client/img/icons/google.svg" alt="Google" class="me-2"/>
                                ${i18n.trans("form.useGoogleAccount")}
                            </a>
                        </div>

                        <div class="text-center divider text-muted">
                            <p>${i18n.trans("form.or")}</p>
                        </div>

                        <%-- Register form --%>
                        <form id="registerForm"
                              class="needs-validation w-100 mb-0"
                              novalidate
                              method="post"
                              action="auth/register"
                        >
                            <div class="form-floating mb-4">
                                <input type="email"
                                       class="form-control <c:if test="${not empty formErrors.regEmail}">is-invalid</c:if>"
                                       id="email"
                                       name="regEmail"
                                       value="${formData.regEmail}"
                                       placeholder="Email"
                                       required
                                       autocomplete="email"
                                />
                                <label for="email">
                                    ${i18n.trans("form.email")}
                                </label>
                                <c:if test="${not empty formErrors.regEmail}">
                                    <div class="invalid-feedback">
                                            ${i18n.trans(formErrors.regEmail)}
                                    </div>
                                </c:if>
                            </div>

                            <div class="form-floating mb-4">
                                <input type="text"
                                       class="form-control <c:if test="${not empty formErrors.regUsername}">is-invalid</c:if>"
                                       id="username"
                                       name="regUsername"
                                       value="${formData.regUsername}"
                                       placeholder="${i18n.trans("form.username")}"
                                       required/>
                                <label for="username">
                                    ${i18n.trans("form.username")}
                                </label>
                                <c:if test="${not empty formErrors.regUsername}">
                                    <div class="invalid-feedback">
                                            ${i18n.trans(formErrors.regUsername)}
                                    </div>
                                </c:if>
                            </div>

                            <div class="form-floating mb-4">
                                <input type="password"
                                       class="form-control <c:if test="${not empty formErrors.regPassword}">is-invalid</c:if>"
                                       id="password"
                                       name="regPassword"
                                       placeholder="${i18n.trans("form.password")}"
                                       required
                                       minlength="6"/>
                                <label for="password">
                                    ${i18n.trans("form.password")}
                                </label>
                                <c:if test="${not empty formErrors.regPassword}">
                                    <div class="invalid-feedback">
                                            ${i18n.trans(formErrors.regPassword)}
                                    </div>
                                </c:if>
                                <div class="form-text">
                                    ${i18n.trans("form.password.error.invalidFormat")}
                                </div>
                            </div>

                            <div class="form-check mb-6 text-start">
                                <input type="checkbox"
                                       class="form-check-input <c:if test="${not empty formErrors.agreeTerms}">is-invalid</c:if>"
                                       id="agreeTerms"
                                       name="agreeTerms"
                                       required/>
                                <label class="form-check-label" for="agreeTerms">
                                    ${i18n.trans("form.agree")}
                                    <a href="#" class="btn-link">${i18n.trans("form.term")}</a>
                                    ${i18n.trans("form.usage")}
                                </label>
                                <c:if test="${not empty formErrors.agreeTerms}">
                                    <div class="invalid-feedback">
                                            ${i18n.trans(formErrors.agreeTerms)}
                                    </div>
                                </c:if>
                            </div>

                            <button type="submit" class="btn btn-lg btn-primary w-100">
                                ${i18n.trans("form.button.register")}
                            </button>
                        </form>

                        <p class="text-center text-muted mt-4">
                            ${i18n.trans("form.alreadyHasAnAccount")}
                            <button
                                    type='button'
                                    class="btn btn-link btn-open-modal p-0"
                                    data-type="login">
                                ${i18n.trans("form.button.login")}
                            </button>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>