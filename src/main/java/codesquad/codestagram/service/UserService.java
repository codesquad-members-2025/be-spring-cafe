package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserDto;
import codesquad.codestagram.dto.UserDto.UserRequestDto;
import codesquad.codestagram.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public boolean joinUser(UserRequestDto requestDto) {
        Optional<User> existingUser = userRepository.findByUserId(requestDto.getUserId());

        if (existingUser.isPresent()) {
            // 이미 존재하는 user_id일 경우
            return false;
        }

        // 중복되지 않으면 사용자 저장
        User user = requestDto.toUser();
        userRepository.save(user);

        return true;
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    public User updateUser(Long id, String name, String email) {
        User user = userRepository.findById(id).get();
        user.updateUser(name, email);
        userRepository.save(user);

        return user;
    }

    public User getUserForLogin(String userId, String password) {
        Optional<User> optionalUser = userRepository.findByUserIdAndPassword(userId, password);
        return optionalUser.orElseThrow(IllegalArgumentException::new);
    }
}
