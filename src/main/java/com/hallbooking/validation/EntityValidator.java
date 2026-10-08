package com.hallbooking.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EntityValidator {

    private final Validator validator;

    public EntityValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(Object value) {
        Set<ConstraintViolation<Object>> violations = validator.validate(value);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
