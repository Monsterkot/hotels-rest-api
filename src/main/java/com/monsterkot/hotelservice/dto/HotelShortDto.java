package com.monsterkot.hotelservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class HotelShortDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}
