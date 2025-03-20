package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입
    public String join(User user) {
//        validateDuplication(user);
        userRepository.save(user);
        return user.getUserId();
    }

    private void validateDuplication(User user) {
        userRepository.findById(user.getUserId())
                .ifPresent(u -> {
                    throw new IllegalStateException("사용할 수 없는 아이디입니다.");
                });
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findOne(String userId) {
        return userRepository.findById(userId);
    }
}
