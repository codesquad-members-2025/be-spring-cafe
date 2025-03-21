package codesquad.codestagram.repository;

import codesquad.codestagram.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository("userJpaRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);
}
