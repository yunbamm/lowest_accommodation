package bangbang.repository;

import bangbang.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    /**
     * 특정 숙소의 모든 가격 이력 조회 (최신순)
     * 용도: 해당 숙소의 전체 가격 트렌드 분석
     */
    List<PriceHistory> findByAccommodationIdOrderByCreatedAtDesc(String accommodationId);


    /**
     * 특정 숙소의 특정 날짜 조합에 대한 모든 가격 이력 조회 (최신순)
     * 용도: 특정 체크인/체크아웃 날짜의 가격 변동 추적
     */
    List<PriceHistory> findByAccommodationIdAndCheckInAndCheckOutOrderByByCreatedAtDesc(String accommodationId,
                                                                                        LocalDate checkIn,
                                                                                        LocalDate checkOut);

}
