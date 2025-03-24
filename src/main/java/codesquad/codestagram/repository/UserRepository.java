package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final List<User> users = Collections.synchronizedList(new ArrayList<>());
    private static Long sequence = 0L;

    public User save(User user) {
        if (user.getId() != null) { // id 값이 존재하면 수정
            users.set(user.getId().intValue() - 1, user);
            return user;
        }

        user.setId(++sequence);
        users.add(user);
        return user;
    }

    public Optional<User> findById(Long id) {
        if (id == null || id <= 0 || id > users.size()) {
            return Optional.empty();
        }
        return Optional.ofNullable(users.get(id.intValue() - 1));
    }

    public List<User> findAll() {
        return users;
    }
}
