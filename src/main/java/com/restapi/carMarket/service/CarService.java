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

    public List<Car> getAllCarsOnMarket() {
        return carDao.findAll();
    }

    public Car getCarById(Long id) {
        Optional<Car> car = carDao.findById(id);

        if(car.isPresent())
            return car.get();
        else
            throw new CarNotFoundException();
    }

    public void removeCarFromMarket(Car car) {
        if(carDao.exists(Example.of(car)))
            carDao.delete(car);
        else
            throw new CarNotFoundException();

    }

    public void removeCarFromMarketById(Long id) {
        if(carDao.existsById(id))
            carDao.deleteById(id);
        else
            throw new CarNotFoundException();
    }

    public Car updateCarInformation(Long id, Car car) {
        checkIfCarIsValid(car);
        Optional<Car> oldCar = carDao.findById(id);
        
        if(oldCar.isPresent()) {
            BeanUtils.copyProperties(oldCar.get(), car, "id");
            return oldCar.get();
        }
        else{
            car.setPostTime(LocalDateTime.now());
            return carDao.save(car);
        }
    }

    public Car addCarToMarket(Car car) {
        checkIfCarIsValid(car);
        car.setPostTime(LocalDateTime.now());
        return carDao.save(car);
    }

    private void checkIfCarIsValid(Car car) {
        if(isCarBrandNull(car)){
            throw new CarNotValidException("Brand name cannot be null");
        }
        else if(isCarBrandBlank(car)){
            throw new CarNotValidException("Brand name cannot be blank");
        }
        else if(isCarModelNull(car)){
            throw new CarNotValidException("Model name cannot be null");
        }
        else if(isCarModelBlank(car)){
            throw new CarNotValidException("Model name cannot be blank");
        }
        else if(isInvalidPrice(car)){
            throw new CarNotValidException("The price is out of range");
        }
        else if(isInvalidYear(car)) {
            throw new CarNotValidException("The year is invalid");
        }
    }

    private boolean isCarModelBlank(Car car) {
        return car.getModel().length()==0;
    }

    private boolean isCarModelNull(Car car) {
        return car.getModel()==null;
    }

    private boolean isCarBrandBlank(Car car) {
        return car.getBrand().length()==0;
    }

    private boolean isCarBrandNull(Car car) {
        return car.getBrand()==null;
    }

    private boolean isInvalidYear(Car car) {
        return car.getYear()<1885 || car.getYear()> LocalDateTime.now().getYear();
    }

    private boolean isInvalidPrice(Car car) {
        return car.getPrice()<1 || car.getPrice()>2000000000;
    }
}
