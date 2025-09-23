package bangbang.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import bangbang.LowestAccommodationApplication;

@SpringBootTest(classes = LowestAccommodationApplication.class)
@ActiveProfiles("test")
class CrawlingServiceTest {

    @Autowired
    CrawlingService crawlingService;

    @Test
    void extractFinalPriceTest() {
        // test URL
        String[] testUrls = {"https://www.yeogi.com/domestic-accommodations/66268?checkIn=2025-09-30&checkOut=2025-10-01",
                "https://www.yeogi.com/domestic-accommodations/64806?checkIn=2025-09-30&checkOut=2025-10-01",
                "https://www.yeogi.com/domestic-accommodations/76992?checkIn=2025-09-30&checkOut=2025-10-01"};

        for (int i = 0; i < testUrls.length; i++) {
            String url = testUrls[i];
            Long result = crawlingService.extractFinalPrice(url);

            if (result == -1L) {
                System.out.println("❌ 가격 추출 실패 # url : " + url);
            } else {
                System.out.println("✅ 추출된 가격: " + result + "원" + " # url : " + url);
            }
        }
    }
}
