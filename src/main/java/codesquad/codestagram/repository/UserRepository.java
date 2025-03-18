package codesquad.codestagram.repository;

import codesquad.codestagram.entity.User;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>(); // DB 대신 ArrayList 사용

    // 회원 저장
    public void saveUser(String name, String email,String loginId, String password) {
        User user = new User(name, email ,loginId, password);
        users.add(user);
    }

    // 모든 회원 조회
    public List<User> getAllUsers() {
        return users;
    }

    // 특정 회원 조회 (id 기준)
    public User findById(Long id) {
        Optional<User> user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
        return user.orElse(null);
    }

    public Optional<User> findByLoginId(String loginId){
        return getAllUsers().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    public void clearStore(){
        users.clear();
    }
}