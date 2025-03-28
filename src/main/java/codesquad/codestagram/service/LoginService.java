package codesquad.codestagram.service;

import codesquad.codestagram.controller.User;
import codesquad.codestagram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String userId, String password){
        return userRepository.findByUserIdAndPassword(userId, password)
                .orElse(null);
    }



}
