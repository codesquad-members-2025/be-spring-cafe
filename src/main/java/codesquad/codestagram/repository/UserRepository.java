package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUserId(String userId);

    List<User> findAll();
}
