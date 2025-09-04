package bangbang.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import bangbang.LowestAccommodationApplication;

@SpringBootTest(classes = LowestAccommodationApplication.class)
class CrawlingServiceTest {

    @Autowired
    CrawlingService crawlingService;

    @Test
    void googleTitleTest() {
        String title = crawlingService.getGoogleTitle();
        System.out.println("Google 페이지 타이틀: " + title);
        assertThat(title).contains("Google");
    }
}

