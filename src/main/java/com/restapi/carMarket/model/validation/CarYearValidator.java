package com.restapi.carMarket.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CarYearValidator implements ConstraintValidator<ValidCarYear,Integer> {
    public void initialize(ValidCarYear constraint){}

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context){
        if(year == null)
            return false;
        else if(year<1885 || year> LocalDateTime.now().getYear())
            return false;
        return true;
    }
}
