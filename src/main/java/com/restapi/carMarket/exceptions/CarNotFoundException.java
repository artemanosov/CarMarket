package com.restapi.carMarket.exceptions;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(){super("Requested Car is not found");}
    public CarNotFoundException(String message){super(message);}
}
