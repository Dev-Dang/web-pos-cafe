package com.laptrinhweb.zerostarcafe.domain.auth.dto;

import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the reset-password form input.
 */
@AllArgsConstructor
@Getter
public final class ResetPasswordDTO {
    private final String token;
    private final String password;
    private final String confirmPassword;

    public ValidationResult validate() {
        return AuthValidator.resetPasswordCheck(password, confirmPassword);
    }
}
