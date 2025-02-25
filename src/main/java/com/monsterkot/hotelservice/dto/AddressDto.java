package com.monsterkot.hotelservice.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    @NotBlank(message = "House number must be filled in")
    private String houseNumber;
    @NotBlank(message = "Street must be filled in")
    private String street;
    @NotBlank(message = "City must be filled in")
    private String city;
    @NotBlank(message = "Country must be filled in")
    private String country;
    @NotBlank(message = "Post code must be filled in")
    private String postCode;

    public String toFormattedString() {
        return String.format("%s %s, %s, %s, %s", houseNumber, street, city, postCode, country);
    }
}
