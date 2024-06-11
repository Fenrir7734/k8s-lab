package com.fenrir.simplebookdatabasesite.exception.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstraintViolationInfo {
    private final String propertyName;
    private final String invalidValue;
    private final String violationMessage;

    public static ConstraintViolationInfo from(ConstraintViolation<?> cv) {
        return new ConstraintViolationInfo(
                cv.getPropertyPath().toString(),
                cv.getInvalidValue().toString(),
                cv.getMessage()
        );
    }

    public static ConstraintViolationInfo from(FieldError fe) {
        return new ConstraintViolationInfo(
                fe.getField(),
                fe.getRejectedValue() != null ? fe.getRejectedValue().toString() : null,
                fe.getDefaultMessage()
        );
    }
}
