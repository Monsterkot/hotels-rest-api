package com.monsterkot.hotelservice.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelFullDto {
    private Long id;
    private String name;
    private String description;
    private String brand;
    private AddressDto address;
    private ContactsDto contacts;
    private ArrivalTimeDto arrivalTime;
    private List<String> amenities;
}

