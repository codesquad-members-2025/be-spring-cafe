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

    // 회원 가입
    public void join(User user) {
        userRepository.save(user);
    }

    // 모든 회원 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
/*
    public String join(User user){
        //같은 아이디가 있는 중복 회원 x
        validateDuplicateUser(user); // 중복 회원 검증
        userRepository.save(user);
        return user.getUserId();
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByUserId(user)
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 아이디입니다");
                });
    }
*/
}
