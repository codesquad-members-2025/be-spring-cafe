package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;
import org.springframework.stereotype.Repository;

import java.util.*;

public class MemoryUserRepository implements UserRepository {

    private static Map<String, User> userMemory = new HashMap<>();

    @Override
    public User save(User user) {
        userMemory.put(user.getUserId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUserId(String id) {
        return Optional.of(userMemory.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMemory.values());
    }

    public void clearMemory() {
        userMemory.clear();
    }
}
