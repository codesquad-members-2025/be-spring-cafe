package codesquad.codestagram.repository;

import codesquad.codestagram.controller.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByUserIdAndPassword(String userId, String password);

}
