package com.monsterkot.hotelservice.controller;
import com.monsterkot.hotelservice.dto.HotelCreateDto;
import com.monsterkot.hotelservice.dto.HotelFullDto;
import com.monsterkot.hotelservice.dto.HotelShortDto;
import com.monsterkot.hotelservice.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/property-view")
@RequiredArgsConstructor
@Validated
@Tag(name = "Hotel Controller", description = "Endpoints for managing hotels")
public class HotelController {

    private final HotelService hotelService;

    @Operation(summary = "Get all hotels", description = "Retrieve a list of all available hotels.")
    @ApiResponse(responseCode = "200", description = "List of hotels retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HotelShortDto.class)))
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelShortDto>> getAllHotels() {
        List<HotelShortDto> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }

    @Operation(summary = "Get hotel by ID", description = "Retrieve a specific hotel by its unique identifier.")
    @ApiResponse(responseCode = "200", description = "Hotel found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HotelFullDto.class)))
    @ApiResponse(responseCode = "404", description = "Hotel not found")
    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelFullDto> getHotelById(@PathVariable Long id) {
        HotelFullDto hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotel);
    }

    @Operation(summary = "Search hotels", description = "Find hotels based on various search parameters.")
    @ApiResponse(responseCode = "200", description = "Hotels found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HotelShortDto.class)))
    @ApiResponse(responseCode = "404", description = "No hotels found")
    @GetMapping("/search")
    public ResponseEntity<?> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) List<String> amenities
    ) {
        List<HotelShortDto> result = hotelService.searchHotels(name, brand, city, country, amenities);

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No hotels found"));
        }

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Add a new hotel", description = "Create a new hotel record.")
    @ApiResponse(responseCode = "201", description = "Hotel successfully created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HotelShortDto.class)))
    @PostMapping("/hotels")
    public ResponseEntity<HotelShortDto> addHotel(@Valid @RequestBody HotelCreateDto hotelCreateDto) {
        HotelShortDto addedHotel = hotelService.addHotel(hotelCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedHotel);
    }

    @Operation(summary = "Add amenities to a hotel", description = "Attach new amenities to a specific hotel.")
    @ApiResponse(responseCode = "200", description = "Amenities successfully added",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HotelFullDto.class)))
    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<HotelFullDto> addAmenities(
            @PathVariable Long id,
            @RequestBody List<String> amenities
    ) {
        HotelFullDto updatedHotel = hotelService.addAmenities(id, amenities);
        return ResponseEntity.ok(updatedHotel);
    }

    @Operation(summary = "Get histogram", description = "Retrieve histogram data based on a specified parameter.")
    @ApiResponse(responseCode = "200", description = "Histogram retrieved successfully",
            content = @Content(mediaType = "application/json"))
    @GetMapping("/histogram/{param}")
    public ResponseEntity<Map<String, Long>> getHistogram(@PathVariable String param) {
        Map<String, Long> histogram = hotelService.getHistogram(param);
        return ResponseEntity.ok(histogram);
    }
}
