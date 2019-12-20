package com.restapi.carMarket.service;


import com.restapi.carMarket.api.CarController;
import com.restapi.carMarket.model.Car;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;


import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CarController.class)
public class CarControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarController carController;

    @MockBean
    private CarService carService;

    @Test
    public void findCarsMustReturnAListOfCars() throws Exception {

        List<Car> cars = new ArrayList<Car>();
        cars.add(new Car("Porsche", "Panamera", 2017, 75000));
        cars.add(new Car("BMW", "3 Series", 2019, 50000));

        given(carController.findAll())
                .willReturn(cars);

        mockMvc.perform(get("/cars")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
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
        Car car = new Car("Porsche", "Panamera", 2017, 75000);
        car.setId(Long.valueOf(1));
        given(carController.findById(car.getId())).willReturn(car);

        mockMvc.perform(get("/cars/" + car.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("brand", is(car.getBrand())))
                .andExpect(jsonPath("model", is(car.getModel())))
                .andExpect(jsonPath("year", is(car.getYear())))
                .andExpect(jsonPath("price", is(car.getPrice())));
    }

    @Test
    public void insertShouldReturnCode201() throws Exception {
        JSONObject car = new JSONObject();
        car.put("brand", "Porsche");
        car.put("model", "Panamera");
        car.put("year", 2017);
        car.put("price", 70000);

        mockMvc.perform(post("/cars")
                .contentType(APPLICATION_JSON)
                .content(car.toJSONString()))
                .andExpect(status().isCreated()).andDo(print());
    }

    @Test
    public void deleteByIdShouldReturnCode204() throws Exception{
        mockMvc.perform(delete("/cars/"+Long.valueOf(1))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    public void deleteShouldReturnStatusCode204() throws Exception{
        JSONObject car = new JSONObject();
        car.put("brand", "Porsche");
        car.put("model", "Panamera");
        car.put("year", 2017);
        car.put("price", 70000);

        mockMvc.perform(delete("/cars")
                .contentType(APPLICATION_JSON)
                .content(car.toJSONString()))
                .andExpect(status().isNoContent()).andDo(print());
    }
}
