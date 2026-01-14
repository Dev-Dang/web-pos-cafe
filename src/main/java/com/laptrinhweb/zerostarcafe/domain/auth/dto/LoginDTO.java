package com.laptrinhweb.zerostarcafe.domain.auth.dto;

import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents the login form input submitted by user.
 * Provides built-in validation using {@link AuthValidator}
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 13/12/2025
 * @since 1.0.0
 */

@AllArgsConstructor
@Getter
public final class LoginDTO {
    private final String email;
    private final String password;

    public Map<String, String> formState() {
        return Map.of("email", email);
    }

    public ValidationResult validate() {
        return AuthValidator.loginCheck(email, password);
    }
}
