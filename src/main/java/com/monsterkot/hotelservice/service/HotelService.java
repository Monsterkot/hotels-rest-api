package com.monsterkot.hotelservice.service;

import com.monsterkot.hotelservice.dto.*;
import com.monsterkot.hotelservice.exception.HotelNotFoundException;
import com.monsterkot.hotelservice.model.*;
import com.monsterkot.hotelservice.repository.AmenityRepository;
import com.monsterkot.hotelservice.repository.HotelRepository;
import com.monsterkot.hotelservice.utils.HotelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final HotelMapper hotelMapper;

    public List<HotelShortDto> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::toHotelShortDto)
                .collect(Collectors.toList());
    }

    public HotelFullDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(id));

        return hotelMapper.toHotelFullDto(hotel);
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
                .map(hotelMapper::toHotelShortDto)
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

        return hotelMapper.toHotelShortDto(hotel);
    }

    @Transactional
    public HotelFullDto addAmenities(Long hotelId, List<String> amenities) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        amenities = amenities.stream()
                .map(name -> name.trim().replaceAll("\\s+", " "))
                .toList();

        List<String> normalizedAmenities = amenities.stream()
                .map(String::toLowerCase)
                .toList();

        List<Amenity> existingAmenities = amenityRepository.findByNameInIgnoreCase(normalizedAmenities);

        List<Amenity> newAmenities = amenities.stream()
                .filter(name -> existingAmenities.stream()
                        .noneMatch(a -> a.getName().equalsIgnoreCase(name)))
                .map(name -> Amenity.builder().name(name).build())
                .toList();

        if (!newAmenities.isEmpty()){
            newAmenities = amenityRepository.saveAll(newAmenities);
        }

        Set<Amenity> updatedAmenities = new HashSet<>(hotel.getAmenities());
        updatedAmenities.addAll(existingAmenities);
        updatedAmenities.addAll(newAmenities);

        hotel.setAmenities(new ArrayList<>(updatedAmenities));
        hotel = hotelRepository.save(hotel);

        return hotelMapper.toHotelFullDto(hotel);
    }

}
