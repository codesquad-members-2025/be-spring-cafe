package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(long id);

    List<User> findAll();
}
