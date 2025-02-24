package com.monsterkot.hotelservice.exception;

import jakarta.persistence.EntityNotFoundException;

public class HotelNotFoundException extends EntityNotFoundException {
    public HotelNotFoundException(Long id){
        super("Hotel with ID " + id + " not found");
    }
}
