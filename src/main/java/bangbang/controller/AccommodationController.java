package bangbang.controller;

import bangbang.dto.AccommodationWatchedRequest;
import bangbang.service.WatchedAccommodationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/save-watched")
    public String saveWatchedAccommodation(@Valid @ModelAttribute AccommodationWatchedRequest request) {
        watchedAccommodationService.saveWatchedAccommodation(request.getName(), request.getAccommodationId(),
                request.getCheckIn(), request.getCheckOut());


        //TODO : how to process for failed case?
        return "success";
    }

}
