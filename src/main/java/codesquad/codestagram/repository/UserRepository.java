package codesquad.codestagram.repository;

import codesquad.codestagram.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("userJpaRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(String id);
}
