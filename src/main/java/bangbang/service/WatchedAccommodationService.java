package bangbang.service;

import bangbang.dto.AccommodationWatchedRequest;
import bangbang.entity.Member;
import bangbang.entity.WatchedAccommodation;
import bangbang.exception.DuplicateWatchedException;
import bangbang.exception.MemberNotFoundException;
import bangbang.repository.MemberRepository;
import bangbang.repository.WatchedAccommodationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WatchedAccommodationService {
    private final WatchedAccommodationRepository watchedAccommodationRepository;
    private final MemberRepository memberRepository;

    //TODO : can we improve transactional way & method?
    @Transactional public WatchedAccommodation saveWatchedAccommodation(AccommodationWatchedRequest request) {
        //TODO : it's better to use MemberService?
        Member member = memberRepository.findByName(request.getName())
                .orElseThrow(() -> new MemberNotFoundException("Member not found with name: " + request.getName()));

        // Check duplication for watched accommodation
        if (watchedAccommodationRepository.existWatchedAccommodation(request.getAccommodationId(), request.getCheckIn(),
                request.getCheckOut(), member.getId())) {
            throw new DuplicateWatchedException("Duplicate watched accommodation for the same member.");
        }

        WatchedAccommodation watchedAccommodation = WatchedAccommodation.builder()
                .accommodationId(request.getAccommodationId())
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .member(member)
                .build();

        return watchedAccommodationRepository.save(watchedAccommodation);
    }

}
