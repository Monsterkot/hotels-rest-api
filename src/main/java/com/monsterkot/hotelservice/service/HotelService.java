package com.monsterkot.hotelservice.service;

import com.monsterkot.hotelservice.dto.*;
import com.monsterkot.hotelservice.exception.HotelNotFoundException;
import com.monsterkot.hotelservice.model.Amenity;
import com.monsterkot.hotelservice.model.Hotel;
import com.monsterkot.hotelservice.repository.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;

    public List<HotelShortDto> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotel -> new HotelShortDto(
                        hotel.getId(),
                        hotel.getName(),
                        hotel.getDescription(),
                        hotel.getAddress().getFormattedAddress(),
                        hotel.getContactInfo().getPhone()
                ))
                .collect(Collectors.toList());
    }

    public HotelFullDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));

        return new HotelFullDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                hotel.getBrand(),
                new AddressDto(
                        hotel.getAddress().getHouseNumber(),
                        hotel.getAddress().getStreet(),
                        hotel.getAddress().getCity(),
                        hotel.getAddress().getCountry(),
                        hotel.getAddress().getPostCode()
                ),
                new ContactsDto(
                        hotel.getContactInfo().getPhone(),
                        hotel.getContactInfo().getEmail()
                ),
                new ArrivalTimeDto(
                        hotel.getArrivalTime().getCheckIn(),
                        hotel.getArrivalTime().getCheckOut()
                ),
                hotel.getAmenities().stream()
                        .map(Amenity::getName)
                        .collect(Collectors.toList())
        );
    }
}
