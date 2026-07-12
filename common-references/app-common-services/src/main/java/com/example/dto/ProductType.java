package com.example.dto;

import lombok.Getter;

@Getter
public enum ProductType {
	FLIGHT(1), HOTEL(2),VISA(3), INSURANCE(7), BUS(8),UNDEFINED(0);

    private final int value;

    ProductType(int i) {
        this.value = i;
    }

    public static ProductType valueOf(int value) {
        switch (value) {
            case 1:
                return FLIGHT;
            case 2:
                return HOTEL;
            case 3:
            	return VISA;  
            case 7:
            	return INSURANCE;    
            case 8:
                return BUS;
            default:
                return UNDEFINED;
        }
    }

    public static String getProductName(int productId) {
        switch (productId) {
            case 1:
                return FLIGHT.name();
            case 2:
                return HOTEL.name();
            case 3:
            	return VISA.name();  
            case 7:
            	return INSURANCE.name();    
            case 8:
                return BUS.name();
            default:
                return UNDEFINED.name();
        }
    }
}