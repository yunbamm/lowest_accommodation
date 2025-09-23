package bangbang.service;

import bangbang.dto.AccommodationWatchedRequest;
import bangbang.entity.Member;
import bangbang.entity.WatchedAccommodation;
import bangbang.exception.MemberNotFoundException;
import bangbang.repository.MemberRepository;
import bangbang.repository.WatchedAccommodationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatchedAccommodationServiceTest {

    @InjectMocks
    private WatchedAccommodationService watchedAccommodationService;

    @Mock
    private WatchedAccommodationRepository watchedAccommodationRepository;

    @Mock
    private MemberRepository memberRepository;

    private AccommodationWatchedRequest validRequest;
    private Member testMember;
    private WatchedAccommodation expectedWatchedAccommodation;

    @BeforeEach
    void setUp() {
        validRequest = createValidRequest();
        testMember = createTestMember();
        expectedWatchedAccommodation = createExpectedWatchedAccommodation();
    }

    @Test
    @DisplayName("Should save watched accommodation successfully when valid request")
    void saveWatchedAccommodation_Success() {
        // given
        when(memberRepository.findByName(validRequest.getName())).thenReturn(Optional.of(testMember));
        when(watchedAccommodationRepository.save(any(WatchedAccommodation.class))).thenReturn(expectedWatchedAccommodation);

        // when
        WatchedAccommodation result = watchedAccommodationService.saveWatchedAccommodation(validRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedWatchedAccommodation);

        verify(memberRepository).findByName(validRequest.getName());
        verify(watchedAccommodationRepository).save(any(WatchedAccommodation.class));
    }

    @Test
    @DisplayName("Should throw MemberNotFoundException when member does not exist")
    void saveWatchedAccommodation_MemberNotFound() {
        // given
        when(memberRepository.findByName(validRequest.getName())).thenReturn(Optional.empty());

        // when & then
        assertThrows(MemberNotFoundException.class, () -> watchedAccommodationService.saveWatchedAccommodation(validRequest));

        verify(memberRepository).findByName(validRequest.getName());
        verify(watchedAccommodationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should verify WatchedAccommodation object fields are set correctly")
    void saveWatchedAccommodation_VerifyObjectFields() {
        // given
        when(memberRepository.findByName(validRequest.getName())).thenReturn(Optional.of(testMember));
        when(watchedAccommodationRepository.save(any(WatchedAccommodation.class))).thenReturn(
                expectedWatchedAccommodation);

        // when
        WatchedAccommodation result = watchedAccommodationService.saveWatchedAccommodation(validRequest);

        assertEquals("12345", result.getAccommodationId());
    }

    @Test
    @DisplayName("Should propagate repository exception")
    void saveWatchedAccommodation_RepositoryException() {
        // given
        when(memberRepository.findByName(validRequest.getName())).thenReturn(Optional.of(testMember));
        when(watchedAccommodationRepository.save(any(WatchedAccommodation.class))).thenThrow(new DataAccessException("Repository error") {});

        // when & then
        assertThrows(DataAccessException.class, () -> watchedAccommodationService.saveWatchedAccommodation(validRequest));
    }

    private AccommodationWatchedRequest createValidRequest() {
        AccommodationWatchedRequest request = new AccommodationWatchedRequest();

        request.setAccommodationId("12345");
        request.setCheckIn(LocalDate.now().plusDays(1));
        request.setCheckOut(LocalDate.now().plusDays(3));
        request.setName("TestUser");

        return request;
    }

    private Member createTestMember() {
        return Member.builder().name("TestUser").build();
    }

    private WatchedAccommodation createExpectedWatchedAccommodation() {
        return WatchedAccommodation.builder()
                .accommodationId("12345")
                .checkIn(LocalDate.now().plusDays(1))
                .checkOut(LocalDate.now().plusDays(3))
                .member(testMember)
                .build();
    }
}