package com.restapi.carMarket.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.restapi.carMarket.model.validation.ValidCarYear;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@ToString(of = {"id", "brand","model", "year", "price","postTime"})
@EqualsAndHashCode(of = {"brand","model", "year", "price","postTime"})
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull(message = "Brand name is mandatory")
    @NotBlank(message = "Brand name is mandatory")
    private String brand;
    @NotNull(message = "Model name is mandatory")
    @NotBlank(message = "Model name is mandatory")
    private String model;
    @ValidCarYear
    private int year;
    @Min(1)
    @Max(2000000000)
    private int price;
    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postTime;

    public Car(){}

    public Car(String brand, String model, int year, int price) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        postTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public void setPostTime(LocalDateTime postTime) {
        this.postTime = postTime;
    }
}
