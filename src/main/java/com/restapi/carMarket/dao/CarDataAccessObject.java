package com.restapi.carMarket.dao;

import com.restapi.carMarket.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CarDataAccessObject{
    private final CarDao carDao;

    @Autowired
    public CarDataAccessObject(CarDao carDao) {
        this.carDao = carDao;
    }

    public void insert(Car car){
        carDao.save(car);
    }

    public void delete(Car car){
        carDao.delete(car);
    }

    public void deleteById(Long id){
        carDao.deleteById(id);
    }

    public List<Car> findAll(){
        return carDao.findAll();
    }

    public Optional<Car> findById(Long id){
        return carDao.findById(id);
    }
}
