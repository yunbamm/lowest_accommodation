package bangbang.service;

import bangbang.dto.CrawlingRequest;
import bangbang.entity.PriceHistory;
import bangbang.repository.PriceHistoryRepository;
import bangbang.util.YeogiUrlBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceService {

    private final CrawlingService crawlingService;
    private final PriceHistoryRepository priceHistoryRepository;

    /**
     * read price without saving (for real-time price check)
     * TODO : will add Redis caching logic later (how long?)
     */
    public Long getRealtimePrice(CrawlingRequest request) {
        String targetUrl = YeogiUrlBuilder.build(request);

        // TODO: Redis 캐시 확인 로직 추가 예정
        // Redis 적용 후:
        // 1. 캐시 히트 → DB 저장 X, 바로 반환
        // 2. 캐시 미스 → 크롤링 → DB 저장 + 캐싱
        // start crawling to get price
        Long price = crawlingService.extractFinalPrice(targetUrl);
        if (price != -1L) {
            // Question : what happens if price == -1L (crawling fail)? & failed to save to DB?
            savePriceToDatabase(request, price);
        }
        return price;
    }

    /**
     * read price and save to DB (For scheduling)
     */
    public void crawlAndSavePrice(CrawlingRequest request) {
        String targetUrl = YeogiUrlBuilder.build(request);

        //start crawling to get price
        Long price = crawlingService.extractFinalPrice(targetUrl);

        if (price != -1L) {
            // save to DB
            savePriceToDatabase(request, price);
        }
    }

    //TODO : need to handle exception??
    @Transactional
    private void savePriceToDatabase(CrawlingRequest request, Long price) {
        PriceHistory priceHistory = PriceHistory.builder()
                .accommodationId(request.getAccommodationId())
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .price(price)
                .build();

        priceHistoryRepository.save(priceHistory);

        log.info("complete save: accommodationId={}, checkIn={}, checkOut={}, price={}",
                request.getAccommodationId(), request.getCheckIn(), request.getCheckOut(), price);
    }
}
