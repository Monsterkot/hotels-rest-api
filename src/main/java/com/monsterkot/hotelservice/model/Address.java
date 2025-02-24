package com.monsterkot.hotelservice.model;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;

    public String getFormattedAddress() {
        return String.format("%s %s, %s, %s, %s", houseNumber, street, city, postCode, country);
    }
}
