package codesquad.codestagram.login.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //로그인 검증
    public User validate(String userId, String password) {
        User user = userRepository.findByUserId(userId);
        if(user != null && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }
}
