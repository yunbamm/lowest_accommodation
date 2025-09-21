package bangbang.service;

import bangbang.entity.Member;
import bangbang.entity.WatchedAccommodation;
import bangbang.repository.MemberRepository;
import bangbang.repository.WatchedAccommodationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class WatchedAccommodationService {
    private final WatchedAccommodationRepository watchedAccommodationRepository;
    private final MemberRepository memberRepository;

    //TODO : can we improve transactional way & method?
    @Transactional
    public void saveWatchedAccommodation(String userName, String accommodationId, LocalDate checkIn,
                                         LocalDate checkOut) {
        Member member = memberRepository.findByName(userName);
        //TODO : for now, just log error. how about general exception handling way?
        if (member == null) {
            log.error("Can't save watched accommodation because Member not found: userName={}", userName);
            return;
        }

        WatchedAccommodation watchedAccommodation = WatchedAccommodation.builder()
                .accommodationId(accommodationId)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .member(member)
                .build();

        watchedAccommodationRepository.save(watchedAccommodation);

        log.info("complete save: accommodationId={}, checkIn={}, checkOut={}, userName={}", accommodationId, checkIn,
                checkOut, userName);
    }
}
