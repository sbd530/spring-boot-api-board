package study.board.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import study.board.api.entity.User;

import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<User, Long> {
    Optional<User> findByUid(String email);

    Optional<User> findByUidAndProvider(String uid, String provider);
}
