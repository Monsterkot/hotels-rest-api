package com.monsterkot.hotelservice.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class ArrivalTimeDto {
    private LocalTime checkIn;
    private LocalTime checkOut;
}
