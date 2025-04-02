package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static codesquad.codestagram.config.AppConstants.*;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findUserList() {
        return userRepository.findAll();
    }

    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    public boolean updateUser(User user, Long id, String confirmPassword) {
        User existingUser = userRepository.findById(id).orElseThrow();

        if (!existingUser.getPassword().equals(confirmPassword)) {
            return false;
        }

        userRepository.save(user);

        return true;
    }

    public Map<String, Object> authenticateUser(String loginId, String password) {
        Optional<User> optionalUser = userRepository.findByLoginId(loginId);

        if (optionalUser.isEmpty()) {
            return Map.of(SUCCESS, false, MESSAGE, "존재하지 않는 아이디입니다.");
        }

        User user = optionalUser.get();
        if (!user.getPassword().equals(password)) {
            return Map.of(SUCCESS, false, MESSAGE, "비밀번호가 일치하지 않습니다.");
        }

        return Map.of(SUCCESS, true, "user", user);
    }

}
