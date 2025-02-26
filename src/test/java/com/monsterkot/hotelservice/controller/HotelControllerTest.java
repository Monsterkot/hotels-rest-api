package com.monsterkot.hotelservice.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monsterkot.hotelservice.dto.*;
import com.monsterkot.hotelservice.service.HotelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HotelController.class)
public class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllHotels_ShouldReturnHotels() throws Exception {
        when(hotelService.getAllHotels()).thenReturn(List.of(new HotelShortDto(), new HotelShortDto()));

        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetHotelById_ShouldReturnHotel() throws Exception {
        HotelFullDto hotel = HotelFullDto.builder()
                .id(42L)
                .name("Test Hotel")
                .description("Nice place")
                .brand("Luxury Brand")
                .address(AddressDto.builder()
                        .houseNumber("10A")
                        .street("Main Street")
                        .city("Paris")
                        .country("France")
                        .postCode("75001")
                        .build())
                .contacts(ContactsDto.builder()
                        .phone("+33123456789")
                        .email("info@luxurybrand.com")
                        .build())
                .arrivalTime(ArrivalTimeDto.builder()
                        .checkIn(LocalTime.of(14, 0))
                        .checkOut(LocalTime.of(12, 0))
                        .build())
                .build();

        when(hotelService.getHotelById(42L)).thenReturn(hotel);

        mockMvc.perform(get("/property-view/hotels/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.name").value("Test Hotel"));
    }

    @Test
    void testSearchHotels_ShouldReturnFilteredHotels() throws Exception {
        List<HotelShortDto> hotels = List.of(
                new HotelShortDto(1L, "Test Hotel", "Test Brand", "Test Address", "+123456789"),
                new HotelShortDto(2L, "Another Hotel", "Another Brand", "Another Address", "+987654321")
        );
        when(hotelService.searchHotels(any(), any(), any(), any(), any())).thenReturn(hotels);

        mockMvc.perform(get("/property-view/search")
                        .param("name", "Test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void testAddHotel_ShouldReturnCreatedHotel() throws Exception {
        HotelShortDto hotelDto = new HotelShortDto(1L, "Test Hotel", "Test Brand", "Test Address", "+123456789");
        when(hotelService.addHotel(any())).thenReturn(hotelDto);

        HotelCreateDto hotelCreateDto = HotelCreateDto.builder()
                .name("Test Hotel")
                .description("Nice place")
                .brand("Luxury Brand")
                .address(AddressDto.builder()
                        .houseNumber("10A")
                        .street("Main Street")
                        .city("Paris")
                        .country("France")
                        .postCode("75001")
                        .build())
                .contacts(ContactsDto.builder()
                        .phone("+33123456789")
                        .email("info@luxurybrand.com")
                        .build())
                .arrivalTime(ArrivalTimeDto.builder()
                        .checkIn(LocalTime.of(14, 0))
                        .checkOut(LocalTime.of(12, 0))
                        .build())
                .build();

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Hotel"));
    }

    @Test
    void testAddAmenities_ShouldReturnUpdatedHotel() throws Exception {
        List<String> amenities = List.of("WiFi", "Pool");
        HotelFullDto updatedHotel = HotelFullDto.builder()
                .id(42L)
                .name("Test Hotel")
                .description("Nice place")
                .brand("Luxury Brand")
                .address(AddressDto.builder()
                        .houseNumber("10A")
                        .street("Main Street")
                        .city("Paris")
                        .country("France")
                        .postCode("75001")
                        .build())
                .contacts(ContactsDto.builder()
                        .phone("+33123456789")
                        .email("info@luxurybrand.com")
                        .build())
                .arrivalTime(ArrivalTimeDto.builder()
                        .checkIn(LocalTime.of(14, 0))
                        .checkOut(LocalTime.of(12, 0))
                        .build())
                .amenities(amenities)
                .build();

        when(hotelService.addAmenities(42L, amenities)).thenReturn(updatedHotel);

        mockMvc.perform(post("/property-view/hotels/42/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amenities)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.name").value("Test Hotel"));
    }

    @Test
    void testGetHistogram_ShouldReturnHistogram() throws Exception {
        Map<String, Long> histogram = Map.of("Luxury", 10L, "Budget", 5L);
        when(hotelService.getHistogram("category")).thenReturn(histogram);

        mockMvc.perform(get("/property-view/histogram/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Luxury").value(10))
                .andExpect(jsonPath("$.Budget").value(5));
    }
}
