package com.monsterkot.hotelservice.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactsDto {
    @NotBlank(message = "Phone number must be filled in")
    private String phone;
    @NotBlank(message = "Email must be filled in")
    private String email;
}
