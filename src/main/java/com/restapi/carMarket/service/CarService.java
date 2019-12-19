package com.restapi.carMarket.service;

import com.restapi.carMarket.dao.CarDataAccessObject;
import com.restapi.carMarket.exceptions.CarIsNotValidException;
import com.restapi.carMarket.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class CarService {

    @Autowired
    CarDataAccessObject carDao;

    public boolean insert(Car car) {
        boolean valid = isValid(car);

        if(valid)
            carDao.insert(car);

        return valid;
    }

    private boolean isValid(Car car) {
        boolean valid = true;
        if(isValidPrice(car))
            valid = false;
        else if(car.getYear()<1885 || car.getYear()> LocalDateTime.now().getYear())
            valid = false;
        return valid;
    }

    private boolean isValidPrice(Car car) {
        return car.getPrice()<1 || car.getPrice()>2000000000;
    }
}
