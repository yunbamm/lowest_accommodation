package bangbang.service;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrawlingService {
    private final WebDriver webDriver;

    //only for testing
    @VisibleForTesting
    public String getGoogleTitle() {
        try {
            webDriver.get("https://www.google.com");
            return webDriver.getTitle();
        } finally {
            webDriver.quit();
        }
    }
}
