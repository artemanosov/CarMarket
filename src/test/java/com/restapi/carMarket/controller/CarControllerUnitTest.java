package com.restapi.carMarket.controller;


import com.restapi.carMarket.api.CarController;
import com.restapi.carMarket.model.Car;
import com.restapi.carMarket.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CarControllerUnitTest {
    @InjectMocks
    private CarController carController;

    @Mock
    private CarService carService;


    @Test
    public void findAllMustReturnTheListOfAllCars() {
        List<Car> cars = new ArrayList<Car>();
        cars.add(new Car("12121212HD2345N5Y","Bentley","Bentayga",2019,350000));
        cars.add(new Car("456HF84HT732H845K","Rolls Royce","Phantom",2019,640000));
        cars.add(new Car("475GKL372432H8431","Rolls Royce","Cullinan",2019,660000));

        given(carService.getAllCarsOnMarket()).willReturn(cars);

        List<Car> foundCars = carController.findAll();

        assertSame(cars,foundCars);
    }

    @Test
    public void findAllMustReturnNullWhenNoCarsOnMarket() {
        given(carService.getAllCarsOnMarket()).willReturn(null);

        List<Car> foundCars = carController.findAll();

        assertNull(foundCars);
    }

    @Test
    public void findByIdMustReturnRequestedCar() {
        Car car = new Car("12121212HD2345N5Y","Bentley","Bentayga",2019,350000);
        given(carService.getCarById(10L))
                .willReturn(
                        new Car("12121212HD2345N5Y","Bentley","Bentayga",2019,350000));

        Car foundCar = carController.findById(10L);

        assertEquals(car,foundCar);
    }

    @Test
    public void insertValidCarMustReturnThisSavedCar() {
        Car car = new Car("12121212HD2345N5Y","Bentley","Bentayga",2019,350000);

        given(carService.addCarToMarket(car))
                .willReturn(
                        new Car("12121212HD2345N5Y","Bentley","Bentayga",2019,350000));

        Car insertedCar = carController.insert(car);

        assertEquals(car,insertedCar);
    }

    @Test
    public void deleteByIdMustCallAppropriateMethodInServiceLayer() {
        carController.deleteById(10L);

        Mockito.verify(carService).removeCarFromMarketById(10L);
    }

    @Test
    public void deleteByVinMustCallAppropriateMethodInServiceLayer() {
        carController.deleteByVinCode("11111111111111111");

        Mockito.verify(carService).removeCarByVinCode("11111111111111111");
    }

    @Test
    public void updateCarMustReturnUpdatedCar() {
        Car car = new Car("12121212HD2345N5Y","Bentley","Bentayga",2019,350000);

        given(carService.updateCarInformation(10L,car))
                .willReturn(
                        new Car("12121212HD2345N5Y","Bentley","Bentayga",2019,350000));

        Car updatedCar = carController.update(10L,car);

        assertEquals(car,updatedCar);
    }
}
