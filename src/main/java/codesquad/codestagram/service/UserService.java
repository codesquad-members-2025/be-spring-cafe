package codesquad.codestagram.service;

import codesquad.codestagram.entity.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final List<User> users = new ArrayList<>(); // DB 대신 ArrayList 사용

    // 회원 저장
    public void saveUser(String name, String email) {
        User user = new User(name, email);
        users.add(user);
    }

    // 모든 회원 조회
    public List<User> getAllUsers() {
        return users;
    }

    // 특정 회원 조회 (id 기준)
    public User getUserById(Long id) {
        Optional<User> user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
        return user.orElse(null);
    }
}