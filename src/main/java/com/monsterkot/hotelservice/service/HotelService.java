package com.monsterkot.hotelservice.service;

import com.monsterkot.hotelservice.dto.*;
import com.monsterkot.hotelservice.exception.HotelNotFoundException;
import com.monsterkot.hotelservice.model.*;
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

    public List<HotelShortDto> searchHotels(String name, String brand, String city, String country, List<String> amenities) {
        return hotelRepository.findAll().stream()
                .filter(hotel ->
                        (name == null || hotel.getName().toLowerCase().contains(name.toLowerCase())) &&
                                (brand == null || (hotel.getBrand() != null && hotel.getBrand().toLowerCase().contains(brand.toLowerCase()))) &&
                                (city == null || hotel.getAddress().getCity().equalsIgnoreCase(city)) &&
                                (country == null || hotel.getAddress().getCountry().equalsIgnoreCase(country)) &&
                                (amenities == null || hotel.getAmenities().stream()
                                        .anyMatch(a -> amenities.stream()
                                                .map(String::toLowerCase)
                                                .collect(Collectors.toSet())
                                                .contains(a.getName().toLowerCase())))
                )
                .map(hotel -> new HotelShortDto(
                        hotel.getId(),
                        hotel.getName(),
                        hotel.getDescription(),
                        hotel.getAddress().getFormattedAddress(),
                        hotel.getContactInfo().getPhone()
                ))
                .collect(Collectors.toList());
    }

    public HotelShortDto addHotel(HotelCreateDto hotelCreateDto) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelCreateDto.getName());

        hotel.setDescription(hotelCreateDto.getDescription());


        hotel.setBrand(hotelCreateDto.getBrand());

        Address address = new Address(
                hotelCreateDto.getAddress().getHouseNumber(),
                hotelCreateDto.getAddress().getStreet(),
                hotelCreateDto.getAddress().getCity(),
                hotelCreateDto.getAddress().getCountry(),
                hotelCreateDto.getAddress().getPostCode());
        hotel.setAddress(address);

        Contacts contacts = new Contacts(
                hotelCreateDto.getContacts().getPhone(),
                hotelCreateDto.getContacts().getEmail());
        hotel.setContactInfo(contacts);

        ArrivalTime arrivalTime = new ArrivalTime(
                hotelCreateDto.getArrivalTime().getCheckIn(),
                hotelCreateDto.getArrivalTime().getCheckOut());
        hotel.setArrivalTime(arrivalTime);


        hotel = hotelRepository.save(hotel);

        return new HotelShortDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                hotel.getAddress().getFormattedAddress(),
                hotel.getContactInfo().getPhone()
        );
    }

}
