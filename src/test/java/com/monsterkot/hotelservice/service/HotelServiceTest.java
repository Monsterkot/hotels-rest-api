package com.monsterkot.hotelservice.service;
import com.monsterkot.hotelservice.dto.*;
import com.monsterkot.hotelservice.model.Amenity;
import com.monsterkot.hotelservice.model.Hotel;
import com.monsterkot.hotelservice.repository.AmenityRepository;
import com.monsterkot.hotelservice.repository.HotelRepository;
import com.monsterkot.hotelservice.utils.HotelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {
    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private AmenityRepository amenityRepository;
    @Mock
    private HotelMapper hotelMapper;
    @InjectMocks
    private HotelService hotelService;

    @Test
    void testGetAllHotels_ShouldReturnList() {
        List<Hotel> hotels = List.of(new Hotel(), new Hotel());
        when(hotelRepository.findAll()).thenReturn(hotels);
        when(hotelMapper.toHotelShortDto(any())).thenReturn(new HotelShortDto());

        List<HotelShortDto> result = hotelService.getAllHotels();

        assertEquals(2, result.size());
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void testFindHotelById_ShouldReturnHotelFullDto() {
        Long hotelId = 1L;
        Hotel mockHotel = new Hotel();
        mockHotel.setId(hotelId);
        mockHotel.setName("Test Hotel");

        HotelFullDto mockDto = new HotelFullDto();
        mockDto.setId(hotelId);
        mockDto.setName("Test Hotel");

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(mockHotel));
        when(hotelMapper.toHotelFullDto(mockHotel)).thenReturn(mockDto);

        HotelFullDto result = hotelService.getHotelById(hotelId);

        assertNotNull(result);
        assertEquals(hotelId, result.getId());
        assertEquals("Test Hotel", result.getName());

        verify(hotelRepository, times(1)).findById(hotelId);
        verify(hotelMapper, times(1)).toHotelFullDto(mockHotel);
    }

    @Test
    void testSearchHotels_ShouldReturnFilteredHotels() {
        List<Hotel> hotels = List.of(
                Hotel.builder()
                        .id(1L)
                        .name("Test Hotel")
                        .description("Description")
                        .brand("Test Brand")
                        .build(),
                Hotel.builder()
                        .id(2L)
                        .name("Another Hotel")
                        .description("Another Description")
                        .brand("Another Brand")
                        .build()
        );

        when(hotelRepository.findAll()).thenReturn(hotels);
        when(hotelMapper.toHotelShortDto(any())).thenReturn(new HotelShortDto());

        List<HotelShortDto> result = hotelService.searchHotels("Test", null, null, null, null);

        assertNotNull(result);
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void testAddHotel_ShouldReturnHotelShortDto() {
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

        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");

        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        when(hotelMapper.toHotelShortDto(any(Hotel.class)))
                .thenReturn(new HotelShortDto(1L, "Test Hotel", "Nice place", "10A Main Street, Paris, France", "+33123456789" ));


        HotelShortDto result = hotelService.addHotel(hotelCreateDto);

        assertNotNull(result);
        assertEquals("Test Hotel", result.getName());
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }


    @Test
    void testAddAmenities_ShouldReturnUpdatedHotelFullDto() {
        Long hotelId = 1L;
        Hotel hotel = new Hotel();
        hotel.setAmenities(new ArrayList<>());
        Amenity amenity = new Amenity();
        amenity.setName("WiFi");

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(amenityRepository.findByNameInIgnoreCase(any())).thenReturn(Collections.emptyList());
        when(amenityRepository.saveAll(any())).thenReturn(List.of(amenity));
        when(hotelRepository.save(any())).thenReturn(hotel);
        when(hotelMapper.toHotelFullDto(hotel)).thenReturn(new HotelFullDto());

        HotelFullDto result = hotelService.addAmenities(hotelId, List.of("WiFi"));

        assertNotNull(result);
        verify(hotelRepository, times(1)).findById(hotelId);
        verify(amenityRepository, times(1)).saveAll(any());
        verify(hotelRepository, times(1)).save(any());
    }

    @Test
    void testGetHistogram_ShouldReturnHistogram() {
        when(hotelRepository.countHotelsByCity()).thenReturn(List.of(new Object[][]{{"Paris", 5L}}));

        Map<String, Long> histogram = hotelService.getHistogram("city");

        assertNotNull(histogram);
        assertEquals(5L, histogram.get("Paris"));
        verify(hotelRepository, times(1)).countHotelsByCity();
    }
}
