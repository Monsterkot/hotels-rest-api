package com.monsterkot.hotelservice.controller;

import com.monsterkot.hotelservice.dto.HotelFullDto;
import com.monsterkot.hotelservice.dto.HotelShortDto;
import com.monsterkot.hotelservice.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;


    @GetMapping("/hotels")
    public List<HotelShortDto> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/hotels/{id}")
    public HotelFullDto getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }
}
