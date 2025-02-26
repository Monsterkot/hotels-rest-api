package com.monsterkot.hotelservice.utils;

import com.monsterkot.hotelservice.dto.*;
import com.monsterkot.hotelservice.model.Amenity;
import com.monsterkot.hotelservice.model.Hotel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HotelMapper {

    public HotelFullDto toHotelFullDto(Hotel hotel) {
        if (hotel == null) {
            return null;
        }

        return HotelFullDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .brand(hotel.getBrand())
                .address(AddressDto.builder()
                        .houseNumber(hotel.getAddress().getHouseNumber())
                        .street(hotel.getAddress().getStreet())
                        .city(hotel.getAddress().getCity())
                        .country(hotel.getAddress().getCountry())
                        .postCode(hotel.getAddress().getPostCode())
                        .build())
                .contacts(ContactsDto.builder()
                        .phone(hotel.getContactInfo().getPhone())
                        .email(hotel.getContactInfo().getEmail())
                        .build())
                .arrivalTime(ArrivalTimeDto.builder()
                        .checkIn(hotel.getArrivalTime().getCheckIn())
                        .checkOut(hotel.getArrivalTime().getCheckOut())
                        .build())
                .amenities(hotel.getAmenities().stream()
                        .map(Amenity::getName)
                        .collect(Collectors.toList()))
                .build();
    }

    public HotelShortDto toHotelShortDto(Hotel hotel) {
        if (hotel == null) {
            return null;
        }

        return HotelShortDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .address(hotel.getAddress().getFormattedAddress())
                .phone(hotel.getContactInfo().getPhone())
                .build();
    }
}
