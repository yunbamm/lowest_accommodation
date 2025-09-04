package bangbang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * main class of Spring Boot
 * 테스트 및 실행 시 이 클래스를 기준으로 설정을 로드
 */
@SpringBootApplication
public class LowestAccommodationApplication {
    public static void main(String[] args) {
        SpringApplication.run(LowestAccommodationApplication.class, args);
    }
}
