package com.laptrinhweb.zerostarcafe.domain.auth.dto;

import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * Represents the forgot-password form input.
 */
@AllArgsConstructor
@Getter
public final class ForgotPasswordDTO {
    private final String email;

    public Map<String, String> formState() {
        return Map.of("email", email);
    }

    public ValidationResult validate() {
        return AuthValidator.forgotPasswordCheck(email);
    }
}
