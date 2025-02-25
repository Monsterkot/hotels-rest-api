package com.monsterkot.hotelservice.service;

import com.monsterkot.hotelservice.dto.*;
import com.monsterkot.hotelservice.exception.HotelNotFoundException;
import com.monsterkot.hotelservice.model.*;
import com.monsterkot.hotelservice.repository.HotelRepository;
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
                .map(hotel -> HotelShortDto.builder()
                        .id(hotel.getId())
                        .name(hotel.getName())
                        .description(hotel.getDescription())
                        .address(hotel.getAddress().getFormattedAddress())
                        .phone(hotel.getContactInfo().getPhone())
                        .build())
                .collect(Collectors.toList());
    }

    public HotelFullDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));

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
                .map(hotel -> HotelShortDto.builder()
                        .id(hotel.getId())
                        .name(hotel.getName())
                        .description(hotel.getDescription())
                        .address(hotel.getAddress().getFormattedAddress())
                        .phone(hotel.getContactInfo().getPhone())
                        .build())
                .collect(Collectors.toList());
    }

    public HotelShortDto addHotel(HotelCreateDto hotelCreateDto) {
        Hotel hotel = Hotel.builder()
                .name(hotelCreateDto.getName())
                .description(hotelCreateDto.getDescription())
                .brand(hotelCreateDto.getBrand())
                .address(new Address(
                        hotelCreateDto.getAddress().getHouseNumber(),
                        hotelCreateDto.getAddress().getStreet(),
                        hotelCreateDto.getAddress().getCity(),
                        hotelCreateDto.getAddress().getCountry(),
                        hotelCreateDto.getAddress().getPostCode()))
                .contactInfo(new Contacts(
                        hotelCreateDto.getContacts().getPhone(),
                        hotelCreateDto.getContacts().getEmail()))
                .arrivalTime(new ArrivalTime(
                        hotelCreateDto.getArrivalTime().getCheckIn(),
                        hotelCreateDto.getArrivalTime().getCheckOut()))
                .build();

        hotel = hotelRepository.save(hotel);

        return HotelShortDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .address(hotel.getAddress().getFormattedAddress())
                .phone(hotel.getContactInfo().getPhone())
                .build();
    }

}
