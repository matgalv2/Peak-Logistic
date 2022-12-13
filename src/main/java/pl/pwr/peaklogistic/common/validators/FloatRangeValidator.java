package pl.pwr.peaklogistic.common.validators;

import jdk.jshell.execution.Util;
import lombok.extern.slf4j.Slf4j;
import pl.pwr.peaklogistic.common.Utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


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
        return value >= min && value <= max && (!twoDecimalPlaces || Utils.decimalDigits(value) <= 2);
    }
}