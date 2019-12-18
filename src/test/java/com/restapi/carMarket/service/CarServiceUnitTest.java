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


import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarMarketApplication.class)
public class CarServiceUnitTest {
    @Autowired
    CarService carService;

    @MockBean
    CarDataAccessObject carDAO;

    //@Test
    public void insertionOfCarShouldReturnTrue() throws Exception{
        Car car = new Car();
        car.setBrand("BMW");
        car.setModel("5 Series");
        car.setPrice(40000);
        car.setYear(2017);
        car.setPostTime(LocalDateTime.now());
        System.out.println(car.getId());

        boolean inserted = carService.insert(car);
        //Assert.assertTrue(inserted);
        //Mockito.verify(carDAO,Mockito.times(1)).insert(car);
    }
}
