package com.laptrinhweb.zerostarcafe.domain.auth.dto;

import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.domain.auth.service.AuthValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents the registration form input submitted by a new user.
 * Provides built-in validation using {@link AuthValidator}
 * and includes user consent (agree-to-terms) check.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 13/12/2025
 * @since 1.0.0
 */

@AllArgsConstructor
@Getter
public final class RegisterDTO {
    private final String email;
    private final String username;
    private final String password;
    private final String agreeTerms;

    public Map<String, String> formState() {
        return Map.of("regEmail", email, "regUsername", username);
    }

    public ValidationResult validate() {
        ValidationResult result = AuthValidator.registerCheck(email, username, password);

        if (result.valid() && !"on".equals(agreeTerms))
            return ValidationResult.fail(
                    "agreeTerms", "form.agreeTerms.error.notChecked"
            );

        return result;
    }
}
