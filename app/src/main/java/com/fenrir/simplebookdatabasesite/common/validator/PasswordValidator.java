package com.fenrir.simplebookdatabasesite.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Objects.isNull;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final String LOWER_CASE_REGEX = ".*[a-z].*";
    private static final String UPPER_CASE_REGEX = ".*[A-Z].*";
    private static final String DIGIT_REGEX = ".*\\d.*";
    private static final String SPECIAL_CHARACTERS_REGEX = ".*[@#$%^&+=!].*";

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (isNull(s) || s.isBlank() || s.length() < PASSWORD_MIN_LENGTH) return false;

        return s.matches(LOWER_CASE_REGEX)
                && s.matches(UPPER_CASE_REGEX)
                && s.matches(DIGIT_REGEX)
                && s.matches(SPECIAL_CHARACTERS_REGEX);
    }
}
