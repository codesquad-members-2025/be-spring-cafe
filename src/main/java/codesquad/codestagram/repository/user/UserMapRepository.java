package codesquad.codestagram.repository.user;

import codesquad.codestagram.entity.User;
import java.util.*;

public class UserMapRepository implements UserRepository {
    private static final Map<Long, User> users = new HashMap();
    private static long sequence = 0L;

    @Override
    public User save(User user) {
        user.setId(++sequence);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findByUserid(String userid) {
        return users.values().stream()
                .filter(user -> user.getUserid().equals(userid))
                .findFirst();
    }

    public void clearStore(){
        users.clear();
    }
}
