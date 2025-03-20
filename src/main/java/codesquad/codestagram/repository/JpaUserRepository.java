package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends UserRepository, JpaRepository<User,String> {
    @Override
    User save(User user);

    @Override
    Optional<User> findByUserId(String userId);

    @Override
    Optional<User> findByName(String name);

    @Override
    Optional<User> findByEmail(String email);

    @Override
    List<User> findAll();
}
