package pl.pwr.peaklogistic.common.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class FloatRangeValidator implements ConstraintValidator<FloatRange, Float> {

    private float max;
    private float min;
    private boolean twoDecimalPlaces;

    @Override
    public void initialize(FloatRange constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        twoDecimalPlaces = constraintAnnotation.twoDecimalPlaces();
    }

    @Override
    public boolean isValid(Float value, ConstraintValidatorContext context) {
        return value >= min && value <= max && (!twoDecimalPlaces || BigDecimal.valueOf(value).scale() == 2);
    }
}