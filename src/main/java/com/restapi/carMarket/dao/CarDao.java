package com.restapi.carMarket.dao;

import com.restapi.carMarket.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarDao extends JpaRepository<Car,Long> {

}
