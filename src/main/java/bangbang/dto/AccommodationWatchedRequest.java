package bangbang.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AccommodationWatchedRequest {
    @NotEmpty(message = "accommodationId is required")
    private String accommodationId;
    @NotEmpty(message = "checkIn is required")
    private LocalDate checkIn;
    @NotEmpty(message = "checkOut is required")
    private LocalDate checkOut;
    @NotEmpty(message = "name is required")
    private String name;
}
