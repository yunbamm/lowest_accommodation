package bangbang.repository;

import bangbang.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    /**
     * get all price histories by accommodationId (ordered by createdAt desc)
     */
    List<PriceHistory> findByAccommodationIdOrderByCreatedAtDesc(String accommodationId);


    /**
     * get all price histories by accommodationId, checkIn, checkOut (ordered by createdAt desc)
     */
    List<PriceHistory> findByAccommodationId_AndCheckIn_AndCheckOut_OrderByCreatedAtDesc(String accommodationId,
                                                                                      LocalDate checkIn,
                                                                                      LocalDate checkOut);

}
