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
        Car car = new Car("11111111111111111","BMW","5 Series", 2017, 40000);

        carService.addCarToMarket(car);
        Mockito.verify(carDAO).save(car);

    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithVinCodeTooShortShouldThrowCarNotValidException() {
        Car car = new Car("111111111111111","BMW","5 Series", 2017, 40000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithVinCodeTooLongShouldThrowCarNotValidException() {
        Car car = new Car("1111111111111111111","BMW","5 Series", 2017, 40000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithYearLessThan1885ShouldThrowCarNotValidException()  {
        Car car = new Car("11111111111111111","BMW","5 Series", 0, 40000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithYearBiggerThanCurrentShouldThrowCarNotValidException() {
        Car car = new Car("11111111111111111","BMW","5 Series", 2200, 40000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithPriceLessThan1ShouldThrowCarNotValidException() {
        Car car = new Car("11111111111111111","BMW","5 Series", 2010, 0);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithPriceToHighShouldThrowCarNotValidException() {
        Car car = new Car("11111111111111111","BMW","5 Series", 2010, 2100000000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithBlankBrandShouldThrowCarNotValidException() {
        Car car = new Car("11111111111111111","","5 Series", 2010, 2100000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithNullBrandShouldThrowCarNotValidException() {
        Car car = new Car("11111111111111111",null,"5 Series", 2010, 2100000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithBlankModelShouldThrowCarNotValidException() {
        Car car = new Car("11111111111111111","BMW","", 2010, 2100000);
        carService.addCarToMarket(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithNullModelShouldReturnCarNotValidException() {
        Car car = new Car("11111111111111111","BMW",null, 2010, 2100000);
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
        Car car = new Car("11111111111111111","BMW","5 Series", 2010, 40000);
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
        Car newCar = new Car("11111111111111111","BMW","5 Series", 2010, 40000);
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
    public void removeExistentCarByVinWithValidVinShouldRemoveTheCar() {
        Car car = new Car("11111111111111111","BMW","5 Series", 2010, 40000);
        given(carDAO.findByVinCode("11111111111111111"))
                .willReturn(car);

        carService.removeCarByVinCode("11111111111111111");

        Mockito.verify(carDAO).delete(car);
    }

    @Test(expected = CarNotValidException.class)
    public void removeCarByVinWithNullVinShouldThrowCarNotValidException() {
        carService.removeCarByVinCode(null);

        Mockito.verify(carDAO, Mockito.times(0)).delete(any(Car.class));
    }

    @Test(expected = CarNotValidException.class)
    public void removeCarByVinWithBlankVinShouldThrowCarNotValidException() {
        carService.removeCarByVinCode("");

        Mockito.verify(carDAO, Mockito.times(0)).delete(any(Car.class));
    }

    @Test(expected = CarNotFoundException.class)
    public void removeNonExistentCarWithValidVinShouldThrowCarNotFoundException() {
        given(carDAO.findByVinCode("11111111111111111")).willReturn(null);

        carService.removeCarByVinCode("11111111111111111");

        Mockito.verify(carDAO, Mockito.times(0)).delete(any(Car.class));
    }

    @Test
    public void updateExistentCarWithValidCarShouldUpdateTheObject() {
        Long id = 10L;
        Car newCar = new Car("11111111111111111","BMW","7 Series", 2010, 10000);
        Car oldCar = new Car("11111111111111111","BMW","5 Series", 2010, 10000);

        given(carDAO.findById(id)).willReturn(Optional.of(oldCar));
        carService.updateCarInformation(id, newCar);

        Assert.assertEquals(newCar,oldCar);
    }

    @Test
    public void updateOfNonExistentCarMustSaveNewCar() {
        Long id = 10L;
        Car newCar = new Car("11111111111111111","BMW","7 Series", 2010, 10000);

        given(carDAO.findById(id)).willReturn(Optional.empty());

        carService.updateCarInformation(id, newCar);

        Mockito.verify(carDAO).save(any(Car.class));
    }

    @Test(expected = CarNotValidException.class)
    public void updateExistentCarWithCarWithYearTooLowShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("11111111111111111","BMW","7 Series", 0, 10000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateExistentCarWithCarWithYearTooHighShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("11111111111111111","BMW","7 Series", 2220, 10000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateExistentCarWithCarWithPriceTooHighShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("11111111111111111","BMW","7 Series", 2010, 2100000000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateExistentCarWithCarWithNullModelShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("11111111111111111","BMW",null, 2010, 10000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateExistentCarWithCarWithBlankModelShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("11111111111111111","BMW","", 2010, 10000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateExistentCarWithCarWithNullBrandShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("11111111111111111",null,"5 Series", 2010, 10000);

        carService.updateCarInformation(id, newCar);
    }

    @Test(expected = CarNotValidException.class)
    public void updateExistentCarWithCarWithBlankBrandShouldThrowCarNotValidException(){
        Long id = 10L;
        Car newCar = new Car("11111111111111111","","5 Series", 2100, 10000);

        carService.updateCarInformation(id, newCar);
    }



    // I Feel like I don't need to test this because my update method calls addCarToMarket,
    // which already has been tested for checking the validity of the new car


    /*@Test(expected = CarNotValidException.class)
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
    }*/
}
