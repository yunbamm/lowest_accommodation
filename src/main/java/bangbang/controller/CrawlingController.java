package bangbang.controller;

import bangbang.dto.CrawlingRequest;
import bangbang.dto.PriceResponse;
import bangbang.service.CrawlingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/crawl")
@RequiredArgsConstructor
public class CrawlingController {

    private final CrawlingService crawlingService;

    @GetMapping("/price")
    public ResponseEntity<PriceResponse> crawlPrice(@Valid @ModelAttribute CrawlingRequest request) {
        log.info("calling /api/crawl/price - accommodationId: {}, checkIn: {}, checkOut: {}", request.getAccommodationId(), request.getCheckIn(), request.getCheckOut());

        String targetUrl = buildTargetUrl(request);

        // 크롤링 실행
        Long price = crawlingService.extractFinalPrice(targetUrl);

        PriceResponse response = PriceResponse.builder()
                .url(targetUrl)
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .price(price)
                .crawledAt(LocalDateTime.now())
                .success(price != -1L)
                .message(price != -1L ? "success" : "fail")
                .build();

        return ResponseEntity.ok(response);
    }


    private String buildTargetUrl(CrawlingRequest request) {
        return String.format("https://www.yeogi.com/domestic-accommodations/%s?checkIn=%s&checkOut=%s", request.getAccommodationId(), request.getCheckIn().toString(), request.getCheckOut().toString());
    }
}
