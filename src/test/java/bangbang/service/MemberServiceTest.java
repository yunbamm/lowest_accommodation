package bangbang.service;

import bangbang.entity.Member;
import bangbang.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")  // Use H2 in-memory database for testing
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        //TODO : is it bettter to use @Transactional on class level?
        // Clean up DB after each test to ensure test isolation
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save member successfully with given name")
    void saveMember_Success() {
        // given
        String memberName = "testUser";

        // when
        Member savedMember = memberService.saveMember(memberName);

        // then
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getId()).isNotNull();

        // Verify member is actually saved in DB
        Member foundMember = memberRepository.findByName(memberName).orElse(null);
        assertThat(foundMember).isNotNull();
    }

    @Test
    @DisplayName("Should find member by name when member exists")
    void findByName_WhenMemberExists_Success() {
        // given
        String memberName = "existingUser";
        memberService.saveMember(memberName);

        // when
        Member foundMember = memberService.findByName(memberName);

        // then
        assertThat(foundMember).isNotNull();
    }

    @Test
    @DisplayName("Should return null when member does not exist")
    void findByName_WhenMemberDoesNotExist_ReturnsNull() {
        // given
        String nonExistentName = "nonExistentUser";

        // when
        Member foundMember = memberService.findByName(nonExistentName);

        // then
        assertThat(foundMember).isNull();
    }

    @Test
    @DisplayName("Should throw exception when saving duplicate member")
    void saveMember_WhenDuplicateName_ThrowsException() {
        // given
        String duplicateName = "duplicateUser";
        memberService.saveMember(duplicateName);

        // when & then
        assertThrows(DataIntegrityViolationException.class, () -> memberService.saveMember(duplicateName));
    }
}
