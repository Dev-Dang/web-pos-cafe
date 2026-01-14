package com.laptrinhweb.zerostarcafe.domain.auth.service;

import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.core.validation.Validator;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain-level validator for user registration and login.
 * Uses {@link Validator} for reusable core validation rules and i18n ready keys.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ValidationResult validation = AuthValidator.registerCheck(email, username, password);
 * if (!validation.valid()) { ... }
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 28/12/2025
 * @since 1.0.0
 */
public final class AuthValidator {

    // prevent instantiation
    private AuthValidator() {
    }

    /**
     * Validate new user registration fields.
     */
    public static ValidationResult registerCheck(String email, String password) {
        Map<String, String> errors = new LinkedHashMap<>();

        String emailErr = Validator.field("email", email)
                .notEmpty()
                .email()
                .maxLength(100)
                .getError();
        if (emailErr != null) errors.put("email", emailErr);

        String passErr = Validator.field("password", password)
                .notEmpty()
                .passwordBasic()
                .minLength(6)
                .maxLength(32)
                .getError();
        if (passErr != null) errors.put("password", passErr);

        return ValidationResult.merge(errors);
    }

    /**
     * Validate login fields (usually only username & password)
     */
    public static ValidationResult loginCheck(String email, String password) {
        Map<String, String> errors = new LinkedHashMap<>();

        String userErr = Validator.field("email", email)
                .notEmpty()
                .email()
                .getError();
        if (userErr != null) errors.put("email", userErr);

        String passErr = Validator.field("password", password)
                .notEmpty()
                .getError();
        if (passErr != null) errors.put("password", passErr);

        return ValidationResult.merge(errors);
    }

    /**
     * Validate forgot-password email field.
     */
    public static ValidationResult forgotPasswordCheck(String email) {
        Map<String, String> errors = new LinkedHashMap<>();

        String emailErr = Validator.field("email", email)
                .notEmpty()
                .email()
                .maxLength(100)
                .getError();
        if (emailErr != null) errors.put("email", emailErr);

        return ValidationResult.merge(errors);
    }

    /**
     * Validate reset-password fields.
     */
    public static ValidationResult resetPasswordCheck(String password, String confirmPassword) {
        Map<String, String> errors = new LinkedHashMap<>();

        String passErr = Validator.field("password", password)
                .notEmpty()
                .passwordBasic()
                .minLength(6)
                .maxLength(32)
                .getError();
        if (passErr != null) errors.put("password", passErr);

        String confirmErr = Validator.field("confirmPassword", confirmPassword)
                .notEmpty()
                .equalTo(password)
                .getError();
        if (confirmErr != null) errors.put("confirmPassword", confirmErr);

        return ValidationResult.merge(errors);
    }
}
