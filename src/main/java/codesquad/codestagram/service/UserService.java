package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public User authenticateUser(String loginId, String password) {
        User findUser = userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!password.equals(findUser.getPassword())) { //비밀번호가 틀릴 경우
            return null;
        }

        return findUser;
    }

}
