import {t} from "./i18n.js";
import {Toast} from "./toast.js";

const validate = window.validate;

// Validation rules for each form
const CONSTRAINTS = {
    loginForm: {
        email: {
            presence: {message: () => t("emailRequired")},
            email: {message: () => t("emailInvalid")}
        },
        password: {
            presence: {message: () => t("passwordRequired")}
        }
    },
    registerForm: {
        email: {
            presence: {message: () => t("emailRequired")},
            email: {message: () => t("emailInvalid")}
        },
        password: {
            presence: {message: () => t("passwordRequired")}
        },
        confirmPassword: {
            presence: {message: () => t("confirmPasswordRequired")},
            equality: {
                attribute: "password",
                message: () => t("passwordMismatch")
            }
        },
        agreeTerms: {
            presence: {message: () => t("termsRequired")}
        }
    },
    forgotPasswordForm: {
        email: {
            presence: {message: () => t("emailRequired")},
            email: {message: () => t("emailInvalid")}
        }
    },
    resetPasswordForm: {
        password: {
            presence: {message: () => t("passwordRequired")}
        },
        confirmPassword: {
            presence: {message: () => t("confirmPasswordRequired")},
            equality: {
                attribute: "password",
                message: () => t("passwordMismatch")
            }
        }
    }
};

/**
 * Password toggle functionality
 */
export function initPasswordToggle() {
    document.addEventListener("click", (e) => {
        const toggleBtn = e.target.closest("[data-toggle-password]");
        if (!toggleBtn) return;

        const passwordInput = document.getElementById(toggleBtn.dataset.togglePassword);
        if (!passwordInput) return;

        const isPassword = passwordInput.type === "password";
        passwordInput.type = isPassword ? "text" : "password";

        const icon = toggleBtn.querySelector("i");
        if (icon) {
            icon.classList.toggle("fi-rr-eye", !isPassword);
            icon.classList.toggle("fi-rr-eye-crossed", isPassword);
        }
    });
}

/**
 * Main validation function called by unpoly
 */
export function validateFormBeforeSubmit(form) {
    const constraints = CONSTRAINTS[form.id];
    if (!constraints) return true;

    // Get form values and ensure validate.js can access them properly
    const values = getFormValues(form);

    // Run validation with proper error handling
    const errors = validate(values, constraints, {
        format: "grouped",
        fullMessages: false
    });

    if (errors) {
        // Mark form as having attempted submission (enables live validation)
        form.dataset.validationEnabled = "true";

        showErrors(form, errors);
        return false;
    }

    clearErrors(form);
    return true;
}

/**
 * Enable live validation
 */
export function enableLiveValidation(form) {
    const constraints = CONSTRAINTS[form.id];
    if (!constraints) return;

    const submitBtn = form.querySelector('button[type="submit"]');
    const inputs = form.querySelectorAll('input, select, textarea');

    inputs.forEach(input => {
        // Always update submit button on input (for empty field check)
        input.addEventListener('input', () => updateSubmitButton(form, submitBtn, constraints));

        // Only validate on change/blur if form has been submitted once
        input.addEventListener('change', () => {
            if (form.dataset.validationEnabled === "true") {
                validateField(form, input, constraints);
            }
        });

        input.addEventListener('blur', () => {
            if (form.dataset.validationEnabled === "true") {
                validateField(form, input, constraints);
            }
        });

        input.addEventListener('focus', () => clearFieldError(input));
    });

    // Initial submit button state check
    updateSubmitButton(form, submitBtn, constraints);
}

// Helper functions
function getFormValues(form) {
    const values = {};
    form.querySelectorAll('input, select, textarea').forEach(el => {
        const name = el.name || el.id;
        if (name && name !== '_csrf') { // Skip CSRF token
            values[name] = el.type === 'checkbox' ? el.checked : el.value.trim();
        }
    });
    return values;
}

function validateField(form, field, constraints) {
    const name = field.name || field.id;
    if (!constraints[name] || name === '_csrf') return;

    const fieldValue = getFieldValue(field);
    const errors = validate({[name]: fieldValue}, {[name]: constraints[name]}, {
        format: "grouped",
        fullMessages: false
    });

    if (errors?.[name]) {
        showFieldError(field, errors[name][0]);
    } else {
        clearFieldError(field);
    }
}

function getFieldValue(field) {
    return field.type === 'checkbox' ? field.checked : field.value.trim();
}

function updateSubmitButton(form, submitBtn, constraints) {
    if (!submitBtn) return;

    const values = getFormValues(form);
    const hasEmptyRequired = Object.keys(constraints).some(name =>
        constraints[name].presence && (!values[name] || values[name] === '')
    );

    submitBtn.disabled = hasEmptyRequired;
}

function showErrors(form, errors) {
    // validate.js returns object format in grouped mode: {email: ["error msg"]}
    const firstError = Object.values(errors)[0];
    if (firstError?.[0]) {
        Toast.error(firstError[0]);
    }

    Object.keys(errors).forEach(name => {
        const field = form.querySelector(`[name="${name}"], #${name}`);
        if (field && errors[name][0]) {
            showFieldError(field, errors[name][0]);
        }
    });
}

function showFieldError(field, message) {
    field.classList.add("is-invalid");

    let feedback = field.parentElement.querySelector(".invalid-feedback");
    if (!feedback) {
        feedback = document.createElement("div");
        feedback.className = "invalid-feedback";
        field.parentElement.appendChild(feedback);
    }
    feedback.textContent = message;
}

function clearFieldError(field) {
    field.classList.remove("is-invalid");
    const feedback = field.parentElement.querySelector(".invalid-feedback");
    if (feedback) feedback.textContent = "";
}

function clearErrors(form) {
    form.querySelectorAll(".is-invalid").forEach(clearFieldError);
}