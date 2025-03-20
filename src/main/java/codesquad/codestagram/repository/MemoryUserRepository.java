package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MemoryUserRepository implements UserRepository{

    private static final ArrayList<User> store = new ArrayList<>();

    @Override
    public User save(User user) {
        store.add(user);
        return user;
    }

    @Override
    public Optional<User> findByUserId(String userId) {  // 매개변수 타입 수정
        for (User user : store) {
            if (user.getUserId().equals(userId)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();  //null 대신 Optional.empty() 반환
    }

    @Override
    public List<User> findAll() {
        return store;
    }
}
