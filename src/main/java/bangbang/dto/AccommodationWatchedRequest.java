package bangbang.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AccommodationWatchedRequest {
    @NotBlank(message = "accommodationId is required")
    private String accommodationId;

    @NotNull(message = "checkIn is required")
    @FutureOrPresent(message = "checkIn must be today or in the future")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkIn;

    @NotNull(message = "checkOut is required")
    private LocalDate checkOut;

    @NotBlank(message = "name is required")
    private String name;

    @AssertTrue(message = "checkOut must be after checkIn")
    public boolean isCheckOutAfterCheckIn() {
        if (checkIn == null || checkOut == null) {
            return false;
        }
        return checkOut.isAfter(checkIn);
    }
}
