package pl.pwr.peaklogistic.common.validators;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FloatRangeValidator.class)
@Target( {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FloatRange {
    float min();
    float max() default Float.MAX_VALUE;
    boolean twoDecimalPlaces() default false;
    String message() default "Value cannot be negative";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
