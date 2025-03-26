package codesquad.codestagram.repository.userrepository;

import codesquad.codestagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long>, UserRepository {
}