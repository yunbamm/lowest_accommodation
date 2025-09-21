package bangbang.repository;

import bangbang.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    /**
     * get Member by name
     */
    Optional<Member> findByName(String name);
}
