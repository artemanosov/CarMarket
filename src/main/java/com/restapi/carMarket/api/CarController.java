package com.restapi.carMarket.api;


import com.restapi.carMarket.model.Car;
import com.restapi.carMarket.service.CarService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/cars")
@RestController
public class CarController {

    @Autowired
    CarService carService;

    @GetMapping
    public List<Car> findAll() {
        return carService.getAllCarsOnMarket();
    }

    @GetMapping("{id}")
    public Car findById(@NotNull @PathVariable("id") Long id) {
        return carService.getCarById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Car insert(@NotNull @RequestBody Car car) {
        return carService.addCarToMarket(car);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@NotNull @RequestBody Car car){
        carService.removeCarFromMarket(car);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@NotNull @PathVariable("id") Long id){
        carService.removeCarFromMarketById(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@NotNull @PathVariable Long id, @NotNull @RequestBody Car car){
        carService.updateCarInformation(id,car);
    }

}
