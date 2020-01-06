package com.restapi.carMarket.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapi.carMarket.api.CarController;
import com.restapi.carMarket.dao.CarDao;
import com.restapi.carMarket.exceptions.CarNotFoundException;
import com.restapi.carMarket.exceptions.CarNotValidException;
import com.restapi.carMarket.model.Car;
import com.restapi.carMarket.service.CarService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarMarketIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarController carController;

    @Autowired
    private CarService carService;

    @Autowired
    private CarDao carDao;

    private JacksonTester<Car> jsonCar;


    @Before
    public void setup(){
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Transactional
    @Test
    public void findCarsMustReturnAListOfCars() throws Exception {
        Car car1 = carController.insert(new Car("11111111111341111","Porsche", "Panamera", 2017, 75000));
        Car car2 = carController.insert(new Car("21111111111111111","BMW", "3 Series", 2019, 50000));


        MvcResult mvcResult = mockMvc.perform(get("/cars")
                .contentType(APPLICATION_JSON)).andReturn();

        JSONArray response = new JSONArray(mvcResult.getResponse().getContentAsString());

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertTrue(response.length()>1);
    }


    @Transactional
    @Test
    public void findByIdMustReturnACarObject() throws Exception {
        Car car = carController.insert(new Car("11111111111111111","Porsche", "Panamera", 2017, 75000));


        MvcResult mvcResult = mockMvc.perform(get("/cars/" + car.getId())
                .contentType(APPLICATION_JSON)).andReturn();

        JSONObject response = new JSONObject(mvcResult.getResponse().getContentAsString());

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertNotNull(response.get("id"));
        assertEquals("11111111111111111", response.get("vinCode"));
        assertEquals("Porsche", response.get("brand"));
        assertEquals("Panamera", response.get("model"));
        assertEquals(2017, response.get("year"));
        assertEquals(75000, response.get("price"));
        assertNotNull(response.get("postTime"));
    }

    @Transactional
    @Test
    public void findByIdWhenCarNotFoundShouldReturnCode404() throws Exception{
        mockMvc.perform(get("/cars/"+Long.valueOf(-1))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    public void insertShouldReturnCode201AndCarWhenSuccessful() throws Exception {
        Car car = new Car("11333111111111111","Porsche","Panamera",2017, 38000);


        MvcResult mvcResult =  mockMvc.perform(post("/cars")
                .contentType(APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(jsonCar.write(car).getJson()))
                .andReturn();

        JSONObject response = new JSONObject(mvcResult.getResponse().getContentAsString());

        assertEquals(201,mvcResult.getResponse().getStatus());

        assertNotNull(response.get("id"));
        assertEquals("11333111111111111",response.get("vinCode"));
        assertEquals("Porsche", response.get("brand"));
        assertEquals("Panamera", response.get("model"));
        assertEquals(2017, response.get("year"));
        assertEquals(38000, response.get("price"));
        assertNotNull(response.get("postTime"));
    }

    @Transactional
    @Test
    public void insertShouldReturnCode400WhenCarIsInvalid() throws Exception{
        Car car = new Car("11111111111111111","Porsche","Panamera",2017, 0);

        mockMvc.perform(post("/cars")
                .contentType(APPLICATION_JSON)
                .content(jsonCar.write(car).getJson()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Transactional
    @Test(expected = CarNotFoundException.class)
    public void deleteByIdShouldReturnCode204WhenSuccessful() throws Exception{
        Car car = carController.insert(new Car("22222222222222222","Porsche","Panamera",2017, 75000));

        mockMvc.perform(delete("/cars/"+car.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent()).andDo(print());

        assertNull(carController.findById(car.getId()));
    }

    @Transactional
    @Test
    public void deleteByIdShouldReturnCode404WhenCarIsNotFound() throws Exception{
        mockMvc.perform(delete("/cars/"+Long.valueOf(-10))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(print());
    }

    @Ignore
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

    @Ignore
    @Test
    public void updateShouldReturnCode200() throws Exception{
        Car car = new Car("11111111111111111","Porsche","Panamera",2017, 40000);

        mockMvc.perform(put("/cars/"+Long.valueOf(1))
                .contentType(APPLICATION_JSON)
                .content(jsonCar.write(car).getJson()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Ignore
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
