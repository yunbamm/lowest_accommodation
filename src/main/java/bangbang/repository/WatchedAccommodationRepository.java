package bangbang.repository;

import bangbang.entity.WatchedAccommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WatchedAccommodationRepository extends JpaRepository<WatchedAccommodation, Long> {
    /**
     * Get all WatchedAccommodation by accommodationId (ordered by createdAt desc)
     */
    List<WatchedAccommodation> findByAccommodationIdOrderByCreatedAtDesc(String accommodationId);

    /**
     * Check if member already watches this accommodation for the same period
     *
     * @param accommodationId accommodation ID
     * @param checkIn         check-in date
     * @param checkOut        check-out date
     * @param memberId        member ID
     * @return true if already exists
     */
    @Query("""
            SELECT COUNT(w) > 0
            FROM WatchedAccommodation w
            WHERE w.accommodationId = :accommodationId
            AND w.checkIn = :checkIn
            AND w.checkOut = :checkOut
            AND w.member.id = :memberId
            """)
    boolean existWatchedAccommodation(@Param("accommodationId") String accommodationId,
                                      @Param("checkIn") LocalDate checkIn,
                                      @Param("checkOut") LocalDate checkOut,
                                      @Param("memberId") Long memberId);
}