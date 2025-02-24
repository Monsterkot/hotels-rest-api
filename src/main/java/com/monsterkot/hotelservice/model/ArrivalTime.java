package com.monsterkot.hotelservice.model;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArrivalTime {
    private LocalTime checkIn;
    private LocalTime checkOut;
}

