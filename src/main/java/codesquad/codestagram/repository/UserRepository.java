package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    //회원가입
    User join(User user);

    Optional<User> findByUserId(String userId);

    List<User> findAll();

    void save(User user);
}
