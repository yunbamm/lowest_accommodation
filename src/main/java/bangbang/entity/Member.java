package bangbang.entity;

import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "accommodation_member")
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Before saving DB, automatically set createdAt
    @PrePersist
    private void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
