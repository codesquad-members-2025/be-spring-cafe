package codesquad.codestagram.service;

import static codesquad.codestagram.service.ArticleService.NO_USER;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserDto.UserRequestDto;
import codesquad.codestagram.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void joinUser(UserRequestDto requestDto) {
        // 중복되지 않으면 사용자 저장
        User user = requestDto.toUser();
        userRepository.save(user);
    }

    public boolean checkEqualUserId(String userId) {
        //같은 아이디를 가지는 유저가 있는지 확인
        return userRepository.findByUserId(userId).isPresent();
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NO_USER));
    }

    public User updateUser(Long id, String name, String email) {
        User user = getUserById(id);
        user.updateUser(name, email);
        userRepository.save(user);

        return user;
    }

    public Optional<User> getUserForLogin(String userId, String password) {
       return userRepository.findByUserIdAndPassword(userId, password);
    }
}
