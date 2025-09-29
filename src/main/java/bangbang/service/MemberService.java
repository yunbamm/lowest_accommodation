package bangbang.service;

import bangbang.entity.Member;
import bangbang.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member saveMember(String name) {
        return memberRepository.save(Member.builder().name(name).build());
    }

    //TODO : need to @Transactional..? readOnly?
    //TODO : return Optional<Member> is better? I'm not sure service layer should return non optional..
    @Transactional
    public Optional<Member> findByName(String name) {
        return memberRepository.findByName(name);
    }

}
