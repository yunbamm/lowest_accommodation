package bangbang.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class PriceResponse {
    private String accommodationId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Long price;
    private LocalDateTime crawledAt;
    private boolean success;
    private String message;
}
