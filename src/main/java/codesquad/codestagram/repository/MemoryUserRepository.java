package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;

import java.util.*;

public class MemoryUserRepository implements UserRepository {

    private static Map<Long, User> memory = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public User save(User user) {
        user.setId(++sequence);
        memory.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(long id) {
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
