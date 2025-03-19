package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> fineByUserId(String userId);
    Optional<User> fineByName(String name);
    Optional<User> fineByEmail(String email);
    List<User> fineAll();
    void clearStore();
}
