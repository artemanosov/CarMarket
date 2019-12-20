package com.restapi.carMarket.service;



import com.restapi.carMarket.CarMarketApplication;
import com.restapi.carMarket.dao.CarDao;
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
        boolean inserted = carService.insert(car);

        Assert.assertTrue(inserted);
        Mockito.verify(carDAO,Mockito.times(1)).save(car);
    }

    @Test
    public void insertionOfMultipleValidCarsShouldBeSuccessful() throws Exception{
        Car car = new Car("BMW","5 Series", 2017, 40000);
        boolean inserted1 = carService.insert(car);

        Car car2 = new Car("BMW","5 Series", 2017, 40000);
        boolean inserted2 = carService.insert(car2);

        Assert.assertTrue(inserted1&&inserted2);
        Mockito.verify(carDAO,Mockito.times(2)).save(ArgumentMatchers.any(Car.class));
    }

    @Test
    public void insertionOfCarWithPriceLessThanOneReturnsFalseNoInsertionHappens() throws Exception{
        Car car = new Car("BMW","5 Series", 2019, 0);
        boolean inserted = carService.insert(car);

        Assert.assertFalse(inserted);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test
    public void insertionOfCarWithYearTooHighReturnsFalseNoInsertionHappens() throws Exception{
        Car car = new Car("BMW","5 Series", 3, 30000);
        boolean inserted = carService.insert(car);

        Assert.assertFalse(inserted);
        Mockito.verify(carDAO,Mockito.times(0)).save(car);
    }

    @Test
    public void findAllCarsShouldCallFindAllMethodOfDAO() throws Exception{
        carService.findAll();
        Mockito.verify(carDAO,Mockito.times(1)).findAll();
    }

    @Test
    public void findCarByIdShouldCallFindByIdMethodOfDAO() throws Exception{
        Long id = 10L;
        carService.findById(id);
        Mockito.verify(carDAO,Mockito.times(1)).findById(id);
    }

    @Test
    public void deleteCarByIdShouldCallDeleteByIdMethodOfDAO() throws Exception{
        Long id = 10L;
        carService.deleteById(id);
        Mockito.verify(carDAO,Mockito.times(1)).deleteById(id);
    }

    @Test
    public void deleteCarObjectShouldCallDeleteMethodOfDAO() throws Exception{
        carService.delete(new Car());
        Mockito.verify(carDAO,Mockito.times(1)).delete(new Car());
    }
}
