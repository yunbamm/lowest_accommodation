package bangbang.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

@Component
public class WebDriverFactory {
    public WebDriver createWebDriver() {
        ChromeOptions options = new ChromeOptions();

        // 서버 환경 필수 설정
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Cloudflare 우회를 위한 최소 설정
        options.addArguments("--user-agent=Mozilla/5.0 (Linux; Android 10; SM-G973F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");
        options.addArguments("--disable-blink-features=AutomationControlled");

        return new ChromeDriver(options);
    }
}
