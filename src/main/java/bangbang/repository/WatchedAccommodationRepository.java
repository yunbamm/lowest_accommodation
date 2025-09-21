package bangbang.repository;

import bangbang.entity.WatchedAccommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchedAccommodationRepository extends JpaRepository<WatchedAccommodation, Long> {
    /**
     * get all WatchedAccommodation by accommodationId (ordered by createdAt desc)
     */
    List<WatchedAccommodation> findByAccommodationIdOrderByCreatedAtDesc(String accommodationId);
}
