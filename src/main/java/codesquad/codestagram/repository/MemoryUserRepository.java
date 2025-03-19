package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;

import java.util.*;

public class MemoryUserRepository implements UserRepository {

    private static Map<String, User> memory = new HashMap<>();

    @Override
    public User save(User user) {
        memory.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.of(memory.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(memory.values());
    }

    public void clearMemory() {
        memory.clear();
    }
}
