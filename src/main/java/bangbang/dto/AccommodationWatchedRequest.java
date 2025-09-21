package bangbang.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AccommodationWatchedRequest {
    @NotEmpty(message = "accommodationId is required")
    private String accommodationId;
    @NotEmpty(message = "checkIn is required")
    @FutureOrPresent(message = "checkIn must be today or in the future")
    private LocalDate checkIn;
    @NotEmpty(message = "checkOut is required")
    private LocalDate checkOut;
    @NotEmpty(message = "name is required")
    private String name;

    @AssertTrue(message = "checkOut must be after checkIn")
    public boolean isCheckOutAfterCheckIn() {
        if (checkIn == null || checkOut == null) {
            return false;
        }
        return checkOut.isAfter(checkIn);
    }
}
