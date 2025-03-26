package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);
}
