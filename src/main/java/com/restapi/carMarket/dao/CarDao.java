package com.restapi.carMarket.dao;

import com.restapi.carMarket.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarDao extends JpaRepository<Car,Long> {
    Car findByVinCode(String vinCode);
}
