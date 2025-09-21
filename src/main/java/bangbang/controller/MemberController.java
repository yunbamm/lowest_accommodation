package bangbang.controller;

import bangbang.entity.Member;
import bangbang.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    public ResponseEntity<Member> registerMember(@RequestBody String name) {
        //TODO : How can I add validation check for each field?
        if (name == null || StringUtils.isEmpty(name)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        boolean existing = memberService.findByName(name) != null;
        if (existing) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Member saved = memberService.saveMember(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
