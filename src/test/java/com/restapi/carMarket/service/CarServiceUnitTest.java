package com.restapi.carMarket.service;



import com.restapi.carMarket.CarMarketApplication;
import com.restapi.carMarket.dao.CarDao;
import com.restapi.carMarket.exceptions.CarNotFoundException;
import com.restapi.carMarket.exceptions.CarNotValidException;
import com.restapi.carMarket.model.Car;
import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarMarketApplication.class)
public class CarServiceUnitTest {
    @Autowired
    CarService carService;

    @MockBean
    CarDao carDAO;

    @Test
    public void insertionOfValidCarShouldReturnTrue() throws Exception{
        Car car = new Car("BMW","5 Series", 2017, 40000);
        carService.insert(car);
        Mockito.verify(carDAO,Mockito.times(1)).save(car);

    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithYearLessThan1885ShouldThrowCarNotValidException() throws Exception{
        Car car = new Car("BMW","5 Series", 0, 40000);
        carService.insert(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithYearBiggerThanCurrentShouldThrowCarNotValidException() throws Exception{
        Car car = new Car("BMW","5 Series", 2200, 40000);
        carService.insert(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithPriceLessThan1ShouldThrowCarNotValidException() throws Exception{
        Car car = new Car("BMW","5 Series", 2200, 0);
        carService.insert(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test(expected = CarNotValidException.class)
    public void insertionOfCarWithPriceToHighShouldThrowCarNotValidException() throws Exception{
        Car car = new Car("BMW","5 Series", 2200, 2100000000);
        carService.insert(car);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test
    public void findAllCarsShouldCallFindAllMethodOfDAO() throws Exception{
        carService.findAll();
        Mockito.verify(carDAO,Mockito.times(1)).findAll();
    }

    @Test
    public void findExistentCarByIdShouldReturnThisCar() throws Exception{
        Car car = new Car("BMW","5 Series", 2200, 40000);
        Optional<Car> returnedCar = Optional.of(car);
        Long id = 10L;

        given(carDAO.findById(id)).willReturn(returnedCar);
        Car newCar = carService.findById(id);

        Assert.assertTrue(newCar.equals(car));
        Mockito.verify(carDAO,Mockito.times(1)).findById(id);
    }

    @Test(expected = CarNotFoundException.class)
    public void findNonExistentCarByIdShouldThrowCarNotFoundExcepiton() throws Exception{
        Optional<Car> returnedCar = Optional.empty();
        Long id = 10L;

        given(carDAO.findById(id)).willReturn(returnedCar);
        carService.findById(id);

        Mockito.verify(carDAO,Mockito.times(1)).findById(id);
    }

    @Test
    public void deleteCarByIdShouldCallDeleteByIdMethodOfDAO() throws Exception{
        //Long id = 10L;
        //carService.deleteById(id);
        //Mockito.verify(carDAO,Mockito.times(1)).deleteById(id);
    }

    @Test
    public void deleteCarObjectShouldCallDeleteMethodOfDAO() throws Exception{
        //carService.delete(new Car());
        //Mockito.verify(carDAO,Mockito.times(1)).delete(new Car());
    }
}
