package com.monsterkot.hotelservice.dto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelCreateDto {
    @NotBlank(message = "Hotel name must be filled in")
    private String name;

    private String description;

    @NotBlank(message = "Hotel brand must be filled in")
    private String brand;

    @Valid
    @NotNull(message = "Address(house number, street, city, country, post code) must be filled in")
    private AddressDto address;

    @Valid
    @NotNull(message = "Contacts(phone and email) must be filled in")
    private ContactsDto contacts;

    @Valid
    @NotNull(message = "Arrival time(check-in and check-out) must be filled in")
    private ArrivalTimeDto arrivalTime;
}
