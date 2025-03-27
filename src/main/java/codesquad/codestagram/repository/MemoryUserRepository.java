package codesquad.codestagram.repository;

import codesquad.codestagram.domain.User;

import java.util.*;

public class MemoryUserRepository implements UserRepository {
    private static Map<Long, User> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public User save(User user) {
        //이미 ID가 있는 사용자면 → 그 ID 그대로 덮어쓰기
        //없는 사용자면 → 새로 등록 (ID 생성)
        if (user.getId() == null) {
            user.setId(++sequence);
        }
        store.put(user.getId(), user); // ID 기준으로 덮어쓰기 (업데이트 포함)
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        return store.values().stream()
                .filter(user -> user.getLoginId().equals(loginId))
                .findFirst();
    }

    @Override
    public Optional<User> findByName(String name) {
        return store.values().stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }


    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
