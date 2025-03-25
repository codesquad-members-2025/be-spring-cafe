package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public User findOne(String userId) {
        return userRepository.findByUserId(userId);
    }

    //회원 정보 수정
    @Transactional
    public void updateUser(String userId, User updatedUser) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            userRepository.save(user);
        }
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
