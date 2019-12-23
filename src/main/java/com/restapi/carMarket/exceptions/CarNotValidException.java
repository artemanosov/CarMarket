package com.restapi.carMarket.exceptions;

public class CarNotValidException extends RuntimeException {
    public CarNotValidException(){
        super("Car information is not valid");
    }
}
