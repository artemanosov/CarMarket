package com.restapi.carMarket.model.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = CarYearValidator.class)
public @interface ValidCarYear {
    String message() default "Car year is not valid. Must be 1884 < year <= current year";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
