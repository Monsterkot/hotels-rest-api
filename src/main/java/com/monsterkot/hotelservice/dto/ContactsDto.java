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
public class ContactsDto {
    @NotBlank(message = "Phone number must be filled in")
    private String phone;
    @NotBlank(message = "Email must be filled in")
    private String email;
}
