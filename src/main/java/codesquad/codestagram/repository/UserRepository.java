package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> fineByLoginId(String loginId);
    Optional<User> fineByName(String name);
    List<User> fineAll();
    void clearStore();
}
