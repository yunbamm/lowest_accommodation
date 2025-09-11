package bangbang.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "accommodation_price_history")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accommodationId;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Before saving DB, automatically set createdAt
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
