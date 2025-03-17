package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoryUserRepository implements UserRepository {
    private static final List<User> store = new ArrayList<>();

    @Override
    public User save(User user) {
        store.add(user);
        return user;
    }

    @Override
    public Optional<User> fineByLoginId(String loginId) {
        for(User user : store) {
            if(user.getLoginId().equals(loginId)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> fineByName(String name) {
        for(User user : store) {
            if(user.getName().equals(name)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> fineAll() {
        return new ArrayList<>(store);
    }
}
