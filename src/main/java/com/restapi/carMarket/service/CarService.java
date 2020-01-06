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
        return carDao.findById(id).orElseThrow(() -> new CarNotFoundException("Car with id("+id+") is not found"));
    }

    public void removeCarByVinCode(String vinCode) {
        if(isVinCodeInvalid(vinCode))
            throw new CarNotValidException("Vin code is not valid");
        else{
            Car carToDelete = carDao.findByVinCode(vinCode);
            deleteCarIfPresent(vinCode, carToDelete);
        }
    }

    private void deleteCarIfPresent(String vinCode, Car carToDelete) {
        if(carToDelete!=null)
            carDao.delete(carToDelete);
        else
            throw new CarNotFoundException("Car with VIN:"+vinCode+"was not found");
    }


    public void removeCarFromMarketById(Long id) {
        Car carToRemove = getCarById(id);
        carDao.deleteById(carToRemove.getId());
    }

    public Car updateCarInformation(Long id, Car car) {
        Optional<Car> oldCar = carDao.findById(id);
        
        if(oldCar.isPresent()) {
            checkIfCarIsValid(car);
            BeanUtils.copyProperties(car, oldCar.get(), "id","vinCode");
            oldCar.get().setPostTime(LocalDateTime.now());
            return carDao.save(oldCar.get());

        }
        else{
            return addCarToMarket(car);
        }
    }

    public Car addCarToMarket(Car car) {
        checkIfCarIsValid(car);
        car.setPostTime(LocalDateTime.now());
        return carDao.save(car);
    }

    private void checkIfCarIsValid(Car car) {

        if(isVinCodeInvalid(car.getVinCode())){
            throw new CarNotValidException("VIN code length is not valid");
        }
        else if(isCarBrandNull(car)){
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
            throw new CarNotValidException("The price is out of range, valid price from 1 to 2,000,000,000");
        }
        else if(isInvalidYear(car)) {
            throw new CarNotValidException("The year is invalid, valid range from 1885 to current year");
        }
    }

    private boolean isVinCodeInvalid(String vinCode) {
        return vinCode == null || vinCode.length()!=17;
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
