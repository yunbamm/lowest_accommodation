package bangbang.repository;

import bangbang.entity.PriceHistory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")  // Use H2 in-memory database for testing
@Transactional // after each test will be roll back --> so can isolate each test
class PriceHistoryRepositoryTest {

    @Autowired
    private PriceHistoryRepository repository;

    @Test
    void save_and_find_price_history_test() {
        // Given
        PriceHistory priceHistory = createPriceHistory("12345", 50000L);

        // When
        PriceHistory saved = repository.save(priceHistory);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getAccommodationId()).isEqualTo("12345");
        assertThat(saved.getPrice()).isEqualTo(50000L);
        assertThat(saved.getCreatedAt()).isNotNull(); // @PrePersist check
    }

    @Test
    void find_by_accommodation_id_test() {
        // Given
        PriceHistory history1 = createPriceHistory("12345", 50000L);
        PriceHistory history2 = createPriceHistory("12345", 60000L);
        PriceHistory history3 = createPriceHistory("99999", 70000L);

        repository.save(history1);
        repository.save(history2);
        repository.save(history3);

        // When
        List<PriceHistory> results = repository.findByAccommodationIdOrderByCreatedAtDesc("12345");

        // Then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getAccommodationId()).isEqualTo("12345");
        assertThat(results.get(1).getAccommodationId()).isEqualTo("12345");
    }

    @Test
    void find_by_accommodation_id_and_checkIn_and_checkOut_test() {
        // Given
        PriceHistory history1 = createPriceHistory("12345", 50000L, LocalDate.of(2025, 10, 1),
                LocalDate.of(2025, 10, 2));
        PriceHistory history2 = createPriceHistory("12345", 60000L, LocalDate.of(2025, 10, 2),
                LocalDate.of(2025, 10, 3));
        PriceHistory history3 = createPriceHistory("99999", 70000L, LocalDate.of(2025, 10, 1),
                LocalDate.of(2025, 10, 2));

        repository.save(history1);
        repository.save(history2);
        repository.save(history3);

        // When
        List<PriceHistory> results = repository.findByAccommodationId_AndCheckIn_AndCheckOut_OrderByCreatedAtDesc(
                "12345", LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 2));

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getAccommodationId()).isEqualTo("12345");
    }

    private PriceHistory createPriceHistory(String accommodationId, Long price) {
        return PriceHistory.builder()
                .accommodationId(accommodationId)
                .checkIn(LocalDate.of(2025, 10, 1))
                .checkOut(LocalDate.of(2025, 10, 2))
                .price(price)
                .build();
    }

    private PriceHistory createPriceHistory(String accommodationId, Long price, LocalDate checkIn, LocalDate checkOut) {
        return PriceHistory.builder()
                .accommodationId(accommodationId)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .price(price)
                .build();
    }
}
