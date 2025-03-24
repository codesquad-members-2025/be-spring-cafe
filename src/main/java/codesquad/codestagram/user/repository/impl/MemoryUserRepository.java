package codesquad.codestagram.user.repository.impl;

import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryUserRepository implements UserRepository {

    private static final Map<Long, User> store = new ConcurrentHashMap<>();
    private static final AtomicLong sequence = new AtomicLong(0L);

    @Override
    public void save(User user) {
        user.setSeq(sequence.incrementAndGet());
        store.put(user.getSeq(), user);
    }
    @Override
    public Optional<User> findBySeq(Long seq) {
        return Optional.ofNullable(store.get(seq));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }

    @Override
    public void update(User updatedUser) {
        store.replace(updatedUser.getSeq(), updatedUser);
    }
}
