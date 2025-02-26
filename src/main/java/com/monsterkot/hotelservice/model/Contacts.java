package com.monsterkot.hotelservice.model;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contacts {
    private String phone;
    private String email;
}

