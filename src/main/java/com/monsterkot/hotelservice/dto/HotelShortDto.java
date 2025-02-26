package com.monsterkot.hotelservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelShortDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}
