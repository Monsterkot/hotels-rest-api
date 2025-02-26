package com.monsterkot.hotelservice.exception;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping("/not-found")
    public void throwNotFound() {
        throw new HotelNotFoundException(999L);
    }

    @PostMapping("/validate")
    public String validateRequest(@Valid @RequestBody TestRequest request) {
        return "Valid request";
    }

    @Getter
    @Setter
    public static class TestRequest {
        @NotBlank(message = "Name must not be blank")
        private String name;
    }

    @GetMapping("/invalid-param")
    public void throwInvalidParam() {
        throw new InvalidParameterException("test parameter");
    }
}
