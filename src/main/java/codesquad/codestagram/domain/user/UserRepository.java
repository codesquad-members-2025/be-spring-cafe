package codesquad.codestagram.domain.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private final List<User> users;

    public UserRepository(List<User> users) {
        this.users = users;
    }

    public void save(User user) {
        users.add(user);
    }

    public List<User> findAll() {
        return users;
    }

    public User findById(String id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void delete(User user) {
        users.remove(user);
    }

}
