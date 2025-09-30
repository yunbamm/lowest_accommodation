package bangbang.controller;

import bangbang.dto.CrawlingRequest;
import bangbang.dto.PriceResponse;
import bangbang.service.CrawlingService;
import bangbang.service.PriceService;
import bangbang.util.YeogiUrlBuilder;
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
    private final PriceService priceService;

    @GetMapping("/price")
    public ResponseEntity<PriceResponse> crawlPrice(@Valid @ModelAttribute CrawlingRequest request) {
        log.info("calling /api/crawl/price - accommodationId: {}, checkIn: {}, checkOut: {}",
                request.getAccommodationId(), request.getCheckIn(), request.getCheckOut());

        String targetUrl = YeogiUrlBuilder.build(request);

        // start crawling to get price
        Long price = crawlingService.extractFinalPrice(targetUrl);

        PriceResponse response = PriceResponse.builder()
                .accommodationId(request.getAccommodationId())
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .price(price)
                .crawledAt(LocalDateTime.now())
                .success(price != -1L)
                .message(price != -1L ? "success" : "fail")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/price/realtime")
    public ResponseEntity<PriceResponse> getPriceInRealTime(@Valid @ModelAttribute CrawlingRequest request) {
        log.info("calling /api/crawl/price/realtime - accommodationId: {}, checkIn: {}, checkOut: {}",
                request.getAccommodationId(), request.getCheckIn(), request.getCheckOut());

        Long price = priceService.getRealtimePrice(request);

        PriceResponse response = PriceResponse.builder()
                .accommodationId(request.getAccommodationId())
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .price(price)
                .crawledAt(LocalDateTime.now())
                .success(price != -1L)
                .message(price != -1L ? "success" : "fail")
                .build();

        return ResponseEntity.ok(response);
    }

}
