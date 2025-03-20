package codesquad.codestagram.user.repository;

import codesquad.codestagram.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findBySeq(Long seq);
    List<User> findAll();
}
