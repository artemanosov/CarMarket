package com.restapi.carMarket.service;



import com.restapi.carMarket.CarMarketApplication;
import com.restapi.carMarket.dao.CarDataAccessObject;
import com.restapi.carMarket.model.Car;
import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarMarketApplication.class)
public class CarServiceUnitTest {
    @Autowired
    CarService carService;

    @MockBean
    CarDataAccessObject carDAO;

    @Test
    public void insertionOfCarShouldReturnTrue() throws Exception{
        Car car = new Car("BMW","5 Series", 2017, 40000);
        boolean inserted = carService.insert(car);

        Assert.assertTrue(inserted);
        Mockito.verify(carDAO,Mockito.times(1)).insert(car);
    }

    @Test
    public void insertionOfCarWithPriceLessThanOneReturnsFalseNoInsertionHappens() throws Exception{
        Car car = new Car("BMW","5 Series", 2019, 0);
        boolean inserted = carService.insert(car);

        Assert.assertFalse(inserted);
        Mockito.verify(carDAO,Mockito.times(0)).insert(car);
    }

    @Test
    public void insertionOfCarWithYearTooHighReturnsFalseNoInsertionHappens() throws Exception{
        Car car = new Car("BMW","5 Series", 3, 30000);
        boolean inserted = carService.insert(car);

        Assert.assertFalse(inserted);
        Mockito.verify(carDAO,Mockito.times(0)).insert(car);
    }
}
