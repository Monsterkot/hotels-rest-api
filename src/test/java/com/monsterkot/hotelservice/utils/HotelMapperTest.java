package com.monsterkot.hotelservice.utils;
import com.monsterkot.hotelservice.dto.*;
import com.monsterkot.hotelservice.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class HotelMapperTest {

    private HotelMapper hotelMapper;

    @BeforeEach
    void setUp() {
        hotelMapper = new HotelMapper();
    }

    @Test
    void toHotelFullDto_ShouldMapCorrectly() {
        Hotel hotel = Hotel.builder()
                .id(1L)
                .name("Test Hotel")
                .description("A nice place")
                .brand("Test Brand")
                .address(Address.builder()
                        .houseNumber("10")
                        .street("Main Street")
                        .city("Paris")
                        .country("France")
                        .postCode("75001")
                        .build())
                .contactInfo(Contacts.builder()
                        .phone("+123456789")
                        .email("info@luxurybrand.com")
                        .build())
                .arrivalTime(ArrivalTime.builder()
                        .checkIn(LocalTime.of(14, 0))
                        .checkOut(LocalTime.of(12, 0))
                        .build())
                .amenities(List.of(
                        new Amenity(1L, "WiFi", new ArrayList<>()),
                        new Amenity(2L, "Pool", new ArrayList<>())
                ))
                .build();


        HotelFullDto dto = hotelMapper.toHotelFullDto(hotel);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Test Hotel", dto.getName());
        assertEquals("A nice place", dto.getDescription());
        assertEquals("Test Brand", dto.getBrand());
        assertEquals("10", dto.getAddress().getHouseNumber());
        assertEquals("+123456789", dto.getContacts().getPhone());
    }

    @Test
    void toHotelShortDto_ShouldMapCorrectly() {
        Hotel hotel = new Hotel();
        hotel.setId(2L);
        hotel.setName("Short Hotel");
        hotel.setDescription("Cozy place");

        Address address = new Address("5", "Broadway", "New York", "USA", "10002");
        hotel.setAddress(address);

        Contacts contactInfo = new Contacts("+987654321", "short@example.com");
        hotel.setContactInfo(contactInfo);

        HotelShortDto dto = hotelMapper.toHotelShortDto(hotel);

        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("Short Hotel", dto.getName());
        assertEquals("Cozy place", dto.getDescription());
        assertEquals("5 Broadway, New York, 10002, USA", dto.getAddress());
        assertEquals("+987654321", dto.getPhone());
    }

    @Test
    void toHotelFullDto_ShouldReturnNull_WhenHotelIsNull() {
        assertNull(hotelMapper.toHotelFullDto(null));
    }

    @Test
    void toHotelShortDto_ShouldReturnNull_WhenHotelIsNull() {
        assertNull(hotelMapper.toHotelShortDto(null));
    }
}

