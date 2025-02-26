package com.monsterkot.hotelservice.model;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArrivalTime {
    private LocalTime checkIn;
    private LocalTime checkOut;
}

