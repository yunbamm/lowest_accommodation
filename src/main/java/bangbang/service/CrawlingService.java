package bangbang.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingService {
    private static final String KR_SYMBOL = "원";

    private final WebDriverFactory webDriverFactory;

    /**
     * only do Crawling
     * @param targetUrl (e.g. https://www.yeogi.com/domestic-accommodations/66268?checkIn=2025-09-30&checkOut=2025-10-01)
     * @return final price (e.g. 76960) or -1L (in case of error)
     */
    public Long extractFinalPrice(String targetUrl) {
        WebDriver webDriver = null;
        try {
            // 매번 새로운 WebDriver 인스턴스 생성
            webDriver = webDriverFactory.createWebDriver();

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

            webDriver.get(targetUrl);

            // 먼저 overview 요소를 찾기
            WebElement overviewElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("overview")));

            // overview 내에서 stay-price-tag 요소 찾기
            WebElement priceElement = overviewElement.findElement(By.className("stay-price-tag"));

            // stay-price-tag 내의 모든 span 요소들을 찾아서 숫자와 "원"을 찾기
            java.util.List<WebElement> spans = priceElement.findElements(By.tagName("span"));

            String priceNumber = "";
            boolean foundUnit = false;

            for (int i = 0; i < spans.size(); i++) {
                WebElement span = spans.get(i);
                String innerHTML = span.getAttribute("innerHTML").trim();

                // 숫자,숫자 패턴 (예: "76,960")
                if (innerHTML.matches("^[0-9,]+$")) {
                    priceNumber = innerHTML;
                }
                // "원" 패턴
                else if (KR_SYMBOL.equals(innerHTML)) {
                    foundUnit = true;
                }
            }

            if (!priceNumber.isEmpty() && foundUnit) {
                String cleanPrice = priceNumber.replace(",", "");
                return Long.parseLong(cleanPrice);
            } else {
                log.error("Can't find valid price information. priceNumber: '{}', foundUnit: {}", priceNumber, foundUnit);
                return -1L;
            }

        } catch (Exception e) {
            log.error("Error during crawling for finding price: ", e);
            return -1L;
        } finally {
            // 매번 WebDriver 종료
            if (webDriver != null) {
                try {
                    webDriver.quit();
                } catch (Exception e) {
                    log.warn("Error closing WebDriver: ", e);
                }
            }
        }
    }
}
