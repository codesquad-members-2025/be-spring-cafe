package codesquad.codestagram.user.repository;

import codesquad.codestagram.user.domain.User;
import org.springframework.stereotype.Repository;

import java.util.*;

public class MemoryUserRepository implements UserRepository {

    private static final Map<Long, User> store = new HashMap<>();
    private static Long sequence = 0L;

    @Override
    public void save(User user) {
        user.setSeq(++sequence);
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
}
