package codesquad.codestagram.repository.user;

import codesquad.codestagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long>, UserRepository {
}