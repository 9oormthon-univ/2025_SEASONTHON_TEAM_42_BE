package next.career.domain.user.repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}