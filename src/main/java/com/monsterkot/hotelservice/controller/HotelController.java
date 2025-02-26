package com.monsterkot.hotelservice.controller;

import com.monsterkot.hotelservice.dto.HotelCreateDto;
import com.monsterkot.hotelservice.dto.HotelFullDto;
import com.monsterkot.hotelservice.dto.HotelShortDto;
import com.monsterkot.hotelservice.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
@Validated
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/hotels")
    public ResponseEntity<List<HotelShortDto>> getAllHotels() {
        List<HotelShortDto> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelFullDto> getHotelById(@PathVariable Long id) {
        HotelFullDto hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) List<String> amenities
    ) {
        List<HotelShortDto> result = hotelService.searchHotels(name, brand, city, country, amenities);

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No hotels found"));
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/hotels")
    public ResponseEntity<HotelShortDto> addHotel(@Valid @RequestBody HotelCreateDto hotelCreateDto) {
        HotelShortDto addedHotel = hotelService.addHotel(hotelCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedHotel);
    }

    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<HotelFullDto> addAmenities(
            @PathVariable Long id,
            @RequestBody List<String> amenities
    ) {
        HotelFullDto updatedHotel = hotelService.addAmenities(id, amenities);
        return ResponseEntity.ok(updatedHotel);
    }
}
