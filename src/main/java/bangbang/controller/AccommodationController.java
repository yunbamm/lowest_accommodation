package bangbang.controller;

import bangbang.dto.AccommodationWatchedRequest;
import bangbang.entity.WatchedAccommodation;
import bangbang.service.WatchedAccommodationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/accommodation")
@RequiredArgsConstructor
public class AccommodationController {
    private final WatchedAccommodationService watchedAccommodationService;

    //TODO : is it better to make response entity?
    @PostMapping("/save-watched")
    public ResponseEntity<WatchedAccommodation> saveWatchedAccommodation(@Valid @ModelAttribute
                                                                         AccommodationWatchedRequest request) {
        try {
            WatchedAccommodation saved = watchedAccommodationService.saveWatchedAccommodation(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (DuplicateWatchedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
