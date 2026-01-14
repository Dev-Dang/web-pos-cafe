<%--
  Description: login form with (loginUsername, loginPassword)
  Author: Dang Van Trung
  Date: 10/11/2025
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/template" id="tpl-modal-login">
    <div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModal" aria-hidden="true">
        <div class="modal-dialog modal-sm modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="d-flex flex-column align-items-center">
                        <h3 class="modal-title fw-bold text-center text-primary mb-3">
                            ${i18n.trans("form.title.login")}
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

                        <%-- Login Form --%>
                        <form
                                id="loginForm"
                                class="needs-validation w-100 mb-0"
                                novalidate
                                method="post"
                                action="auth/login">

                            <div class="form-floating mb-4">
                                <input type="text"
                                       class="form-control <c:if test="${not empty formErrors.loginUsername}">is-invalid</c:if>"
                                       id="username"
                                       name="loginUsername"
                                       value="${formData.loginUsername}"
                                       placeholder="${i18n.trans("form.username")}"
                                       required/>
                                <label for="username">
                                    ${i18n.trans("form.username")}
                                </label>
                                <c:if test="${not empty formErrors.loginUsername}">
                                    <div class="invalid-feedback">
                                            ${i18n.trans(formErrors.loginUsername)}
                                    </div>
                                </c:if>
                            </div>

                            <div class="form-floating mb-4">
                                <input type="password"
                                       class="form-control <c:if test="${not empty formErrors.loginPassword}">is-invalid</c:if>"
                                       id="password"
                                       name="loginPassword"
                                       placeholder="${i18n.trans("form.password")}"
                                       required/>
                                <label for="password">
                                    ${i18n.trans("form.password")}
                                </label>
                                <c:if test="${not empty formErrors.loginPassword}">
                                    <div class="invalid-feedback">
                                            ${i18n.trans(formErrors.loginPassword)}
                                    </div>
                                </c:if>
                            </div>

                            <button type="submit" class="btn btn-lg btn-primary w-100">
                                ${i18n.trans("form.button.login")}
                            </button>
                        </form>

                        <p class="text-center text-muted mt-6 d-flex gap-2 justify-content-center align-items-center">
                            ${i18n.trans("form.dontHaveAnAccount")}
                            <button
                                    type="button"
                                    class="btn btn-link btn-open-modal p-0"
                                    data-type="register">
                                ${i18n.trans("form.button.register")}
                            </button>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>