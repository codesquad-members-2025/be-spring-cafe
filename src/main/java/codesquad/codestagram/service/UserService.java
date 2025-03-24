package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.MemoryUserRepository;
import codesquad.codestagram.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MemoryUserRepository memoryUserRepository;

    public UserService(UserRepository userRepository, MemoryUserRepository memoryUserRepository) {
        this.userRepository = userRepository;
        this.memoryUserRepository = memoryUserRepository;
    }

    // 회원 가입
    public void join(User user) {
        userRepository.save(user);
    }

    // 모든 회원 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional<User> findByUserId(String userId) {
        for (User user : memoryUserRepository.findAll()) {
            if (user.getUserId().equals(userId)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
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
