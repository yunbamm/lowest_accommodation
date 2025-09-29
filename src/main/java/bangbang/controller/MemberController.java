package bangbang.controller;

import bangbang.dto.MemberRegisterRequest;
import bangbang.entity.Member;
import bangbang.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    //TODO : RequestBody is okay for single field?
    @PostMapping("/register")
    public ResponseEntity<Member> registerMember(@Valid @RequestBody MemberRegisterRequest request) {
        if (memberService.findByName(request.getName()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Member saved = memberService.saveMember(request.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
