/**
 * ------------------------------------------------------------
 * Module: Form Utilities
 * ------------------------------------------------------------
 * @description
 * Handles form interactions like password visibility toggle,
 * form validation, and other common form behaviors.
 *
 * @example
 * import { initPasswordToggle, initFormValidation } from './modules/form.js';
 * initPasswordToggle();
 * initFormValidation();
 *
 * @version 1.0.2
 * @since 1.0.0
 * @lastModified 28/12/2025
 * @module form
 * @author Dang Van Trung
 */

import * as Cookie from "./cookie.js";
import { getValidationMessages } from "./i18n.js";

// Get current locale from cookie, fallback to Vietnamese
let currentLocale = "vi";
try {
  const localeCookie = Cookie.get("locale");
  if (localeCookie && ["en", "vi"].includes(localeCookie)) {
    currentLocale = localeCookie;
  }
} catch (error) {
  }

// Get validation messages for current locale
const messages = getValidationMessages(currentLocale);

// Track which forms have been submitted (for lazy validation)
const formSubmitted = new Set();

/**
 * Initializes password toggle functionality for password fields
 * Looks for buttons with data-toggle-password attribute
 */
export function initPasswordToggle() {
  // Use event delegation on document for dynamically loaded modals
  document.addEventListener("click", (e) => {
    const toggleBtn = e.target.closest("[data-toggle-password]");
    if (!toggleBtn) return;

    const targetId = toggleBtn.dataset.togglePassword;
    const passwordInput = document.getElementById(targetId);

    if (!passwordInput) return;

    // Toggle password visibility
    const isPassword = passwordInput.type === "password";
    passwordInput.type = isPassword ? "text" : "password";

    // Toggle icon
    const icon = toggleBtn.querySelector("i");
    if (icon) {
      icon.classList.toggle("fi-rr-eye", !isPassword);
      icon.classList.toggle("fi-rr-eye-crossed", isPassword);
    }

    // Update aria-label
    toggleBtn.setAttribute(
      "aria-label",
      isPassword ? "Hide password" : "Show password"
    );
  });
}

/**
 * Initializes form validation for multiple forms
 * Validates fields and prevents empty form submission for:
 * - login: email + password (only notEmpty check)
 * - register: email + password + confirm password + terms
 * - forgot-password: email
 * - reset-password: password + confirm password
 * 
 * Real-time validation on change only activates after first submit attempt
 */
export function initFormValidation() {
  // Validation on submit
  document.addEventListener("submit", (e) => {
    const form = e.target;
    const formId = form.id;

    // Mark form as submitted for lazy validation
    formSubmitted.add(formId);

    // Handle different form types
    if (formId === "registerForm") {
      e.preventDefault();
      e.stopPropagation();
      if (validateRegisterForm(form)) {
        form.submit();
      }
    } else if (formId === "loginForm") {
      e.preventDefault();
      e.stopPropagation();
      if (validateLoginForm(form)) {
        form.submit();
      }
    } else if (formId === "forgotPasswordForm") {
      e.preventDefault();
      e.stopPropagation();
      if (validateForgotPasswordForm(form)) {
        form.submit();
      }
    } else if (formId === "resetPasswordForm") {
      e.preventDefault();
      e.stopPropagation();
      if (validateResetPasswordForm(form)) {
        form.submit();
      }
    }
  });

  // Validation onChange for register form fields (after first submit)
  document.addEventListener("input", (e) => {
    const input = e.target;
    const form = input.closest("#registerForm");

    if (!form || !formSubmitted.has("registerForm")) return;

    // Validate specific field on change
    if (input.id === "email") {
      validateEmail(input);
    } else if (input.id === "password") {
      validatePassword(input);
      // Also revalidate confirm password if it has value
      const confirmInput = form.querySelector("#confirmPassword");
      if (confirmInput && confirmInput.value) {
        validateConfirmPassword(confirmInput, input);
      }
    } else if (input.id === "confirmPassword") {
      const passwordInput = form.querySelector("#password");
      validateConfirmPassword(input, passwordInput);
    }
  });

  // Validation onChange for login form fields (after first submit)
  // Only check for empty/blank, no format validation on change
  document.addEventListener("input", (e) => {
    const input = e.target;
    const form = input.closest("#loginForm");

    if (!form || !formSubmitted.has("loginForm")) return;

    if (input.id === "email") {
      validateNotEmpty(input, messages.emailRequired);
    } else if (input.id === "password") {
      validateNotEmpty(input, messages.passwordRequired);
    }
  });

  // Validation onChange for forgot-password form fields (after first submit)
  document.addEventListener("input", (e) => {
    const input = e.target;
    const form = input.closest("#forgotPasswordForm");

    if (!form || !formSubmitted.has("forgotPasswordForm")) return;

    if (input.id === "email" || input.id === "forgotEmail") {
      validateEmail(input);
    }
  });

  // Validation onChange for reset-password form fields (after first submit)
  document.addEventListener("input", (e) => {
    const input = e.target;
    const form = input.closest("#resetPasswordForm");

    if (!form || !formSubmitted.has("resetPasswordForm")) return;

    if (input.id === "password" || input.id === "resetPassword") {
      validatePassword(input);
      const confirmInput = form.querySelector("#confirmPassword, #resetConfirmPassword");
      if (confirmInput && confirmInput.value) {
        validateConfirmPassword(confirmInput, input);
      }
    } else if (input.id === "confirmPassword" || input.id === "resetConfirmPassword") {
      const passwordInput = form.querySelector("#password, #resetPassword");
      validateConfirmPassword(input, passwordInput);
    }
  });

  // Validation onChange for terms checkbox (after first submit)
  document.addEventListener("change", (e) => {
    const input = e.target;
    const form = input.closest("#registerForm");

    if (!form || !formSubmitted.has("registerForm")) return;

    if (input.id === "agreeTerms") {
      validateTerms(input);
    }
  });
}

/**
 * Validates all fields in register form
 * @param {HTMLFormElement} form - The form element
 * @returns {boolean} True if all fields are valid
 */
function validateRegisterForm(form) {
  let isValid = true;

  const emailInput = form.querySelector("#email");
  const passwordInput = form.querySelector("#password");
  const confirmPasswordInput = form.querySelector("#confirmPassword");
  const termsCheckbox = form.querySelector("#agreeTerms");

  if (emailInput && !validateEmail(emailInput)) isValid = false;
  if (passwordInput && !validatePassword(passwordInput)) isValid = false;
  if (
    confirmPasswordInput &&
    passwordInput &&
    !validateConfirmPassword(confirmPasswordInput, passwordInput)
  )
    isValid = false;
  if (termsCheckbox && !validateTerms(termsCheckbox)) isValid = false;

  return isValid;
}

/**
 * Validates all fields in login form
 * Only checks that fields are not empty/blank
 * @param {HTMLFormElement} form - The form element
 * @returns {boolean} True if all fields are valid
 */
function validateLoginForm(form) {
  let isValid = true;

  // Support both id patterns: email, password
  const emailInput = form.querySelector("#email");
  const passwordInput = form.querySelector("#password");

  if (emailInput && !validateNotEmpty(emailInput, messages.emailRequired)) isValid = false;
  if (passwordInput && !validateNotEmpty(passwordInput, messages.passwordRequired)) isValid = false;

  return isValid;
}

/**
 * Validates all fields in forgot-password form
 * @param {HTMLFormElement} form - The form element
 * @returns {boolean} True if all fields are valid
 */
function validateForgotPasswordForm(form) {
  const emailInput = form.querySelector("#email, #forgotEmail");
  return emailInput ? validateEmail(emailInput) : false;
}

/**
 * Validates all fields in reset-password form
 * @param {HTMLFormElement} form - The form element
 * @returns {boolean} True if all fields are valid
 */
function validateResetPasswordForm(form) {
  let isValid = true;

  const passwordInput = form.querySelector("#password, #resetPassword");
  const confirmPasswordInput = form.querySelector("#confirmPassword, #resetConfirmPassword");

  if (passwordInput && !validatePassword(passwordInput)) isValid = false;
  if (
    confirmPasswordInput &&
    passwordInput &&
    !validateConfirmPassword(confirmPasswordInput, passwordInput)
  )
    isValid = false;

  return isValid;
}

/**
 * Validates email field
 * @param {HTMLInputElement} input - Email input element
 * @returns {boolean} True if valid
 */
function validateEmail(input) {
  const emailValue = input.value.trim();
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  clearError(input);

  if (!emailValue) {
    showError(input, messages.emailRequired);
    return false;
  } else if (!emailRegex.test(emailValue)) {
    showError(input, messages.emailInvalid);
    return false;
  }

  return true;
}

/**
 * Validates password field
 * @param {HTMLInputElement} input - Password input element
 * @returns {boolean} True if valid
 */
function validatePassword(input) {
  const passwordValue = input.value;

  clearError(input);

  if (!passwordValue) {
    showError(input, messages.passwordRequired);
    return false;
  }

  return true;
}

/**
 * Validates confirm password field
 * @param {HTMLInputElement} input - Confirm password input element
 * @param {HTMLInputElement} passwordInput - Password input element
 * @returns {boolean} True if valid
 */
function validateConfirmPassword(input, passwordInput) {
  const confirmValue = input.value;
  const passwordValue = passwordInput ? passwordInput.value : "";

  clearError(input);

  if (!confirmValue) {
    showError(input, messages.confirmPasswordRequired);
    return false;
  } else if (confirmValue !== passwordValue) {
    showError(input, messages.passwordMismatch);
    return false;
  }

  return true;
}

/**
 * Validates terms checkbox
 * @param {HTMLInputElement} checkbox - Terms checkbox element
 * @returns {boolean} True if valid
 */
function validateTerms(checkbox) {
  clearError(checkbox);

  if (!checkbox.checked) {
    showError(checkbox, messages.termsRequired);
    return false;
  }

  return true;
}

/**
 * Validates that a field is not empty/blank
 * Simple check without format validation
 * @param {HTMLInputElement} input - Input element
 * @param {string} message - Error message to display
 * @returns {boolean} True if valid
 */
function validateNotEmpty(input, message) {
  const value = input.value.trim();

  clearError(input);

  if (!value) {
    showError(input, message);
    return false;
  }

  return true;
}

/**
 * Shows error message for a form field
 * @param {HTMLElement} input - The input element
 * @param {string} message - Error message to display
 */
function showError(input, message) {
  input.classList.add("is-invalid");
  const feedback = input.parentElement.querySelector(".invalid-feedback");
  if (feedback) {
    feedback.textContent = message;
  }
}

/**
 * Clears error state from a form field
 * @param {HTMLElement} input - The input element
 */
function clearError(input) {
  input.classList.remove("is-invalid", "is-valid");
  const feedback = input.parentElement.querySelector(".invalid-feedback");
  if (feedback) {
    feedback.textContent = "";
  }
}

