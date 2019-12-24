package com.restapi.carMarket.service;

import com.restapi.carMarket.dao.CarDao;
import com.restapi.carMarket.exceptions.CarNotFoundException;
import com.restapi.carMarket.exceptions.CarNotValidException;
import com.restapi.carMarket.model.Car;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class CarService {

    @Autowired
    CarDao carDao;

    public void insert(Car car) {
        checkIfCarIsValid(car);
        carDao.save(car);
    }

    private void checkIfCarIsValid(Car car) {
        if(isValidPrice(car)){
            throw new CarNotValidException("The price is out of range");
        }
        else if(isValidYear(car)) {
            throw new CarNotValidException("The year is invalid");
        }
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
        if(carDao.exists(Example.of(car)))
            carDao.delete(car);
        else
            throw new CarNotFoundException();

    }

    public void deleteById(Long id) {
        if(carDao.existsById(id))
            carDao.deleteById(id);
        else
            throw new CarNotFoundException();
    }

    public void update(Long id, Car car) {
        checkIfCarIsValid(car);
        Optional<Car> oldCar = carDao.findById(id);
        
        if(oldCar.isPresent())
            BeanUtils.copyProperties(oldCar.get(), car, "id");
        else
            throw new CarNotFoundException();
        
    }
}
