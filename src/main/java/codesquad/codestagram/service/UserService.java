package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (userRepository.existsUserByLoginId(user.getLoginId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        return userRepository.save(user);
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
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

    public boolean login(String loginId, String password, HttpSession session) {
        User findUser = userRepository.findByLoginId(loginId);

        if (!userRepository.existsUserByLoginId(loginId)) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }

        if (!password.equals(findUser.getPassword())) { //비밀번호가 틀릴 경우
            return false;
        }

        session.setAttribute("loginUser", findUser);

        return true;
    }

}
