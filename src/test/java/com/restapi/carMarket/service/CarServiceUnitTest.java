package com.restapi.carMarket.service;

import com.restapi.carMarket.dao.CarDao;
import com.restapi.carMarket.exceptions.CarNotFoundException;
import com.restapi.carMarket.exceptions.CarNotValidException;
import com.restapi.carMarket.model.Car;
import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;


import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@RunWith(MockitoJUnitRunner.class)
public class CarServiceUnitTest {
    @Mock
    CarDao carDAO;

    @InjectMocks
    CarService carService;

    @Test
    public void insertionOfValidCarShouldReturnTrue() {
        Car car = new Car("BMW","5 Series", 2017, 40000);
        carService.insert(car);
        Mockito.verify(carDAO,Mockito.times(1)).save(car);

    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithYearLessThan1885ShouldThrowCarNotValidException()  {
        Car car = new Car("BMW","5 Series", 0, 40000);
        carService.insert(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithYearBiggerThanCurrentShouldThrowCarNotValidException() {
        Car car = new Car("BMW","5 Series", 2200, 40000);
        carService.insert(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithPriceLessThan1ShouldThrowCarNotValidException() {
        Car car = new Car("BMW","5 Series", 2010, 0);
        carService.insert(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithPriceToHighShouldThrowCarNotValidException() {
        Car car = new Car("BMW","5 Series", 2010, 2100000000);
        carService.insert(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithBlankBrandShouldThrowCarNotValidException() {
        Car car = new Car("","5 Series", 2010, 2100000);
        carService.insert(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithNullBrandShouldThrowCarNotValidException() {
        Car car = new Car(null,"5 Series", 2010, 2100000);
        carService.insert(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithBlankModelShouldThrowCarNotValidException() {
        Car car = new Car("BMW","", 2010, 2100000);
        carService.insert(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithNullModelShouldReturnCarNotValidException() {
        Car car = new Car("BMW",null, 2010, 2100000);
        carService.insert(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test
    public void findAllCarsShouldCallFindAllMethodOfDAO() {
        carService.findAll();
        Mockito.verify(carDAO,Mockito.times(1)).findAll();
    }

    @Test
    public void findExistentCarByIdShouldReturnThisCar() {
        Car car = new Car("BMW","5 Series", 2200, 40000);
        Optional<Car> returnedCar = Optional.of(car);
        Long id = 10L;

        given(carDAO.findById(id)).willReturn(returnedCar);
        Car newCar = carService.findById(id);

        Assert.assertTrue(newCar.equals(car));
        Mockito.verify(carDAO,Mockito.times(1)).findById(id);
    }

    @Test(expected = CarNotFoundException.class)
    public void findNonExistentCarByIdShouldThrowCarNotFoundException() {
        Optional<Car> returnedCar = Optional.empty();
        Long id = 10L;

        given(carDAO.findById(id)).willReturn(returnedCar);
        carService.findById(id);

        Mockito.verify(carDAO,Mockito.times(1)).findById(id);
    }

    @Test
    public void deleteExistentCarByIdShouldCallDeleteByIdMethodOfDAO() {
        Long id = 10L;

        given(carDAO.existsById(id)).willReturn(true);
        carService.deleteById(id);

        Mockito.verify(carDAO,Mockito.times(1)).deleteById(id);
    }

    @Test(expected = CarNotFoundException.class)
    public void deleteNonExistentCarByIdShouldThrowCarNotFoundException() {
        Long id = 10L;

        given(carDAO.existsById(id)).willReturn(false);
        carService.deleteById(id);

        Mockito.verify(carDAO,Mockito.times(1)).deleteById(id);
    }

    @Test
    public void deleteExistentCarShouldCallDeleteMethodOfDAO() {
        Car car = new Car("BMW","5 Series", 2010, 40000);

        given(carDAO.exists(Example.of(car))).willReturn(true);
        carService.delete(car);

        Mockito.verify(carDAO,Mockito.times(1)).delete(car);
    }

    @Test(expected = CarNotFoundException.class)
    public void deleteNonExistentCarShouldThrowCarNotFoundException() {
        Car car = new Car("BMW","5 Series", 2010, 40000);

        given(carDAO.exists(Example.of(car))).willReturn(false);
        carService.delete(car);

        Mockito.verify(carDAO,Mockito.times(0)).delete(car);
    }

    @Test
    public void updateExistentCarWithValidCarShouldUpdateTheObject() {
        Long id = 10L;
        Car newCar = new Car("BMW","7 Series", 2010, 10000);
        Car oldCar = new Car("BMW","5 Series", 2010, 10000);

        given(carDAO.findById(id)).willReturn(Optional.of(oldCar));
        carService.update(id, newCar);

        Assert.assertTrue(newCar.equals(oldCar));
    }

    @Test
    public void updateNonExistentCarWithValidCarShouldSaveNewCar() {
        Long id = 10L;
        Car newCar = new Car("BMW","7 Series", 2010, 10000);

        given(carDAO.findById(id)).willReturn(Optional.empty());

        carService.update(id, newCar);

        Mockito.verify(carDAO,Mockito.times(1)).save(any(Car.class));
    }

    @Test(expected = CarNotValidException.class)
    public void updateExistentCarWithInValidCarShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("BMW","7 Series", 0, 10000);

        carService.update(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateNonExistentCarWithInvalidCarShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("BMW","7 Series", 1997, 0);

        carService.update(id, newCar);
    }
}
