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
        carService.addCarToMarket(car);
        Mockito.verify(carDAO).save(car);

    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithYearLessThan1885ShouldThrowCarNotValidException()  {
        Car car = new Car("BMW","5 Series", 0, 40000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithYearBiggerThanCurrentShouldThrowCarNotValidException() {
        Car car = new Car("BMW","5 Series", 2200, 40000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithPriceLessThan1ShouldThrowCarNotValidException() {
        Car car = new Car("BMW","5 Series", 2010, 0);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithPriceToHighShouldThrowCarNotValidException() {
        Car car = new Car("BMW","5 Series", 2010, 2100000000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithBlankBrandShouldThrowCarNotValidException() {
        Car car = new Car("","5 Series", 2010, 2100000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithNullBrandShouldThrowCarNotValidException() {
        Car car = new Car(null,"5 Series", 2010, 2100000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithBlankModelShouldThrowCarNotValidException() {
        Car car = new Car("BMW","", 2010, 2100000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithNullModelShouldReturnCarNotValidException() {
        Car car = new Car("BMW",null, 2010, 2100000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test
    public void findAllCarsShouldCallFindAllMethodOfDAO() {
        carService.getAllCarsOnMarket();
        Mockito.verify(carDAO).findAll();
    }

    @Test
    public void findExistentCarByIdShouldReturnThisCar() {
        Car car = new Car("BMW","5 Series", 2010, 40000);
        Optional<Car> returnedCar = Optional.of(car);
        Long id = 10L;

        given(carDAO.findById(id)).willReturn(returnedCar);
        Car newCar = carService.getCarById(id);

        Assert.assertEquals(newCar,car);
    }

    @Test(expected = CarNotFoundException.class)
    public void findNonExistentCarByIdShouldThrowCarNotFoundException() {
        Long id = 10L;

        given(carDAO.findById(id)).willReturn(Optional.empty());
        carService.getCarById(id);
    }

    @Test
    public void deleteExistentCarByIdShouldCallDeleteByIdMethodOfDAO() {
        Car newCar = new Car("BMW","5 Series", 2010, 40000);
        newCar.setId(10L);

        given(carDAO.findById(newCar.getId())).willReturn( Optional.of(newCar));

        carService.removeCarFromMarketById(newCar.getId());

        Mockito.verify(carDAO).deleteById(newCar.getId());
    }

    @Test(expected = CarNotFoundException.class)
    public void deleteNonExistentCarByIdShouldThrowCarNotFoundException() {
        Long id = 10L;

        given(carDAO.findById(id)).willReturn(Optional.empty());
        carService.removeCarFromMarketById(id);
    }

    @Test
    public void deleteExistentCarShouldCallDeleteMethodOfDAO() {
        Car car = new Car("BMW","5 Series", 2010, 40000);

        given(carDAO.exists(Example.of(car))).willReturn(true);
        carService.removeCarFromMarket(car);

        Mockito.verify(carDAO).delete(car);
    }

    @Test(expected = CarNotFoundException.class)
    public void deleteNonExistentCarShouldThrowCarNotFoundException() {
        Car car = new Car("BMW","5 Series", 2010, 40000);

        given(carDAO.exists(Example.of(car))).willReturn(false);
        carService.removeCarFromMarket(car);

        Mockito.verify(carDAO,Mockito.times(0)).delete(car);
    }

    @Test
    public void updateExistentCarWithValidCarShouldUpdateTheObject() {
        Long id = 10L;
        Car newCar = new Car("BMW","7 Series", 2010, 10000);
        Car oldCar = new Car("BMW","5 Series", 2010, 10000);

        given(carDAO.findById(id)).willReturn(Optional.of(oldCar));
        carService.updateCarInformation(id, newCar);

        Assert.assertEquals(newCar,oldCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateExistentCarWithCarWithYearShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("BMW","7 Series", 0, 10000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateExistentCarWithCarWithPriceShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("BMW","7 Series", 2010, 2100000000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateExistentCarWithCarWithNullModelShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("BMW",null, 2010, 10000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateExistentCarWithCarWithBlankModelShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("BMW","", 2010, 10000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateExistentCarWithCarWithNullBrandShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car(null,"5 Series", 2010, 10000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateExistentCarWithCarWithBlankBrandShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("","5 Series", 2100, 10000);

        carService.updateCarInformation(id, newCar);
    }

    @Test
    public void updateNonExistentCarWithValidCarShouldSaveNewCar() {
        Long id = 10L;
        Car newCar = new Car("BMW","7 Series", 2010, 10000);

        given(carDAO.findById(id)).willReturn(Optional.empty());

        carService.updateCarInformation(id, newCar);

        Mockito.verify(carDAO).save(any(Car.class));
    }



    @Test(expected = CarNotValidException.class)
    public void updateNonExistentCarWithCarWithPriceShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("BMW","7 Series", 1997, 0);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateNonExistentCarWithCarWithYearShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("BMW","7 Series", 0, 10000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateNonExistentCarWithCarWithBlankModelShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("BMW","", 2010, 10000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateNonExistentCarWithCarWithNullModelShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("BMW",null, 2010, 10000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateNonExistentCarWithCarWithBlankBrandShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("","5 Series", 2010, 10000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateNonExistentCarWithCarWithNullBrandShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car(null,"5 Series", 2010, 10000);

        carService.updateCarInformation(id, newCar);
    }
}
