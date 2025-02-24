package com.monsterkot.hotelservice.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressDto {
    private String houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;
}
