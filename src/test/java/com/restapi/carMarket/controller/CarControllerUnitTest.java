package com.restapi.carMarket.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapi.carMarket.api.CarController;
import com.restapi.carMarket.exceptions.CarNotFoundException;
import com.restapi.carMarket.exceptions.CarNotValidException;
import com.restapi.carMarket.exceptions.RestExceptionHandler;
import com.restapi.carMarket.model.Car;
import com.restapi.carMarket.service.CarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CarControllerUnitTest {
    private MockMvc mockMvc;

    @InjectMocks
    private CarController carController;

    @Mock
    private CarService carService;

    private JacksonTester<Car> jsonCar;

    @Before
    public void setup(){
        JacksonTester.initFields(this, new ObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(carController)
                .setControllerAdvice(new RestExceptionHandler())
                .build();
    }

    @Test
    public void findCarsMustReturnAListOfCars() throws Exception {
        List<Car> cars = new ArrayList<Car>();
        cars.add(new Car("11111111111111111","Porsche", "Panamera", 2017, 75000));
        cars.add(new Car("11111111111111111","BMW", "3 Series", 2019, 50000));

        given(carService.getAllCarsOnMarket())
                .willReturn(cars);

        mockMvc.perform(get("/cars")
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].brand", is(cars.get(0).getBrand())))
                .andExpect(jsonPath("$[0].model", is(cars.get(0).getModel())))
                .andExpect(jsonPath("$[0].year", is(cars.get(0).getYear())))
                .andExpect(jsonPath("$[0].price", is(cars.get(0).getPrice())))
                .andExpect(jsonPath("$[1].brand", is(cars.get(1).getBrand())))
                .andExpect(jsonPath("$[1].model", is(cars.get(1).getModel())))
                .andExpect(jsonPath("$[1].year", is(cars.get(1).getYear())))
                .andExpect(jsonPath("$[1].price", is(cars.get(1).getPrice())));

    }

    @Test
    public void findByIdMustReturnACarObject() throws Exception {
        Car car = new Car("11111111111111111","Porsche", "Panamera", 2017, 75000);
        car.setId(Long.valueOf(1));
        given(carService.getCarById(car.getId())).willReturn(car);

        mockMvc.perform(get("/cars/" + car.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("brand", is(car.getBrand())))
                .andExpect(jsonPath("model", is(car.getModel())))
                .andExpect(jsonPath("year", is(car.getYear())))
                .andExpect(jsonPath("price", is(car.getPrice())));
    }

    @Test
    public void findByIdWhenCarNotFoundShouldReturnCode404() throws Exception{
        given(carService.getCarById(10L)).willThrow(new CarNotFoundException());

        mockMvc.perform(get("/cars/10")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnCode201AndCarWhenSuccessful() throws Exception {
        Car car = new Car("11111111111111111","Porsche","Panamera",2017, 38000);

        given(carService.addCarToMarket(car)).willReturn(car);

        mockMvc.perform(post("/cars")
                .contentType(APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(jsonCar.write(car).getJson()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("brand", is(car.getBrand())))
                .andExpect(jsonPath("model", is(car.getModel())))
                .andExpect(jsonPath("year", is(car.getYear())))
                .andExpect(jsonPath("price", is(car.getPrice())));
    }

    @Test
    public void insertShouldReturnCode400WhenNotSuccessful() throws Exception{
        Car car = new Car("11111111111111111","Porsche","Panamera",2017, 0);

        given(carService.addCarToMarket(car)).willThrow(CarNotValidException.class);

        mockMvc.perform(post("/cars")
                .contentType(APPLICATION_JSON)
                .content(jsonCar.write(car).getJson()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteByIdShouldReturnCode204() throws Exception{
        mockMvc.perform(delete("/cars/"+Long.valueOf(1))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    public void deleteByIdShouldReturnCode404WhenCarIsNotFound() throws Exception{
        doThrow(new CarNotFoundException()).when(carService).removeCarFromMarketById(10L);
        mockMvc.perform(delete("/cars/"+10L)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void deleteShouldReturnStatusCode204() throws Exception{
        Car car = new Car("11111111111111111","Porsche","Panamera",2017, 40000);

        mockMvc.perform(delete("/cars")
                .contentType(APPLICATION_JSON)
                .content(jsonCar.write(car).getJson()))
                .andExpect(status().isNoContent()).andDo(print());
    }

    /*@Test
    public void deleteShouldReturnStatsCode404WhenCarIsNotFound() throws Exception{
        Car car = new Car("11111111111111111","Porsche","Panamera",2017, 40000);
        doThrow(new CarNotFoundException()).when(carService).removeCarFromMarket(car);

        mockMvc.perform(delete("/cars")
                .contentType(APPLICATION_JSON)
                .content(jsonCar.write(car).getJson()))
                .andExpect(status().isNotFound()).andDo(print());
    }*/

    @Test
    public void updateShouldReturnCode200() throws Exception{
        Car car = new Car("11111111111111111","Porsche","Panamera",2017, 40000);

        mockMvc.perform(put("/cars/"+Long.valueOf(1))
                .contentType(APPLICATION_JSON)
                .content(jsonCar.write(car).getJson()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void updateMustReturnCode400WhenCarIsInvalid() throws Exception{
        Car car = new Car("11111111111111111","Porsche","Panamera",2017, 40000);

        given(carService.updateCarInformation(10L, car)).willThrow(CarNotValidException.class);

        mockMvc.perform(put("/cars/"+10L)
                .contentType(APPLICATION_JSON)
                .content(jsonCar.write(car).getJson()))
                .andExpect(status().isBadRequest()).andDo(print());
    }
}
