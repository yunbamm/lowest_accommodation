package bangbang.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CrawlingRequest {

    @NotBlank(message = "숙소 ID는 필수입니다")
    @Pattern(regexp = "^\\d+$", message = "숙소 ID는 숫자만 가능합니다")
    private String accommodationId;

    @NotBlank(message = "체크인 날짜는 필수입니다")
    @FutureOrPresent(message = "체크인 날짜는 현재 날짜 이후여야 합니다")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkIn;

    @NotBlank(message = "체크아웃 날짜는 필수입니다")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkOut;

    @AssertTrue(message = "체크아웃 날짜는 체크인 날짜보다 이후여야 합니다")
    public boolean isCheckOutAfterCheckIn() {
        if (checkIn == null || checkOut == null) {
            return false;
        }
        return checkOut.isAfter(checkIn);
    }
}
