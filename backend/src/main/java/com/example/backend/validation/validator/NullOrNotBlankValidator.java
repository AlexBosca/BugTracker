package com.example.backend.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.backend.validation.constraint.NullOrNotBlankConstraint;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlankConstraint, String> {

    @Override
    public boolean isValid(String field, ConstraintValidatorContext context) {
        return (field == null) || (!field.isBlank());
    }

    @Override
    public void initialize(NullOrNotBlankConstraint constraintAnnotation) {
    }
    
}
