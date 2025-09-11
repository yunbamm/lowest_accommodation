package bangbang.service;

import bangbang.entity.PriceHistory;
import bangbang.repository.PriceHistoryRepository;
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
    public Long getRealtimePrice(String accommodationId, LocalDate checkIn, LocalDate checkOut) {
        String targetUrl = buildTargetUrl(accommodationId, checkIn, checkOut);

        // TODO: Redis 캐시 확인 로직 추가 예정

        // start crawling to get price
        return crawlingService.extractFinalPrice(targetUrl);
    }

    /**
     * read price and save to DB (For scheduling)
     */
    public void crawlAndSavePrice(String accommodationId, LocalDate checkIn, LocalDate checkOut) {
        String targetUrl = buildTargetUrl(accommodationId, checkIn, checkOut);


        //start crawling to get price
        Long price = crawlingService.extractFinalPrice(targetUrl);

        if (price != -1L) {
            // save to DB
            savePriceToDatabase(accommodationId, checkIn, checkOut, price);
        }
    }

    //TODO : need to handle exception??
    @Transactional
    private void savePriceToDatabase(String accommodationId, LocalDate checkIn, LocalDate checkOut, Long price) {
        PriceHistory priceHistory = PriceHistory.builder()
                .accommodationId(accommodationId)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .price(price)
                .build();

        priceHistoryRepository.save(priceHistory);

        log.info("complete save: accommodationId={}, checkIn={}, checkOut={}, price={}", 
                accommodationId, checkIn, checkOut, price);
    }

    private String buildTargetUrl(String accommodationId, LocalDate checkIn, LocalDate checkOut) {
        return String.format("https://www.yeogi.com/domestic-accommodations/%s?checkIn=%s&checkOut=%s", accommodationId,
                checkIn.toString(), checkOut.toString());
    }
}
