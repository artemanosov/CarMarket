package com.restapi.carMarket.service;

import com.restapi.carMarket.dao.CarDao;
import com.restapi.carMarket.exceptions.CarNotFoundException;
import com.restapi.carMarket.exceptions.CarNotValidException;
import com.restapi.carMarket.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class CarService {

    @Autowired
    CarDao carDao;

    public void insert(Car car) {
        boolean valid = isValid(car);

        if(valid)
            carDao.save(car);
        else
            throw new CarNotValidException();
    }

    private boolean isValid(Car car) {
        boolean valid = true;
        if(isValidPrice(car))
            valid = false;
        else if(isValidYear(car))
            valid = false;
        return valid;
    }

    private boolean isValidYear(Car car) {
        return car.getYear()<1885 || car.getYear()> LocalDateTime.now().getYear();
    }

    private boolean isValidPrice(Car car) {
        return car.getPrice()<1 || car.getPrice()>2000000000;
    }

    public List<Car> findAll() {
        return carDao.findAll();
    }

    public Car findById(Long id) {
        Optional<Car> car = carDao.findById(id);

        if(car.isPresent())
            return car.get();
        else
            throw new CarNotFoundException();
    }

    public void delete(Car car) {
        carDao.delete(car);
    }

    public void deleteById(Long id) {
        carDao.deleteById(id);
    }

    public void update(Long id, Car car) {

    }
}
