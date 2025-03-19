package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;
    //생성자를 통해 외부에서 UserRepository 주입 -> DI 활용 위해
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository; }

    /**
     * 회원가입
     */
    public Long join(User user) {
        validateDuplicateUser(user); //중복 회원 검증
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        //같은 이름이 있는 중복 회원 x
        userRepository.findByName(user.getName())
                .ifPresent(u -> {
                    throw new IllegalStateException("User already exists");
                });
    }

    /**
     * 전체 회원 조회
     */
    public List<User> findUsers() {
        return userRepository.findAll();
    }
    public Optional<User> findOne(Long userId) {
        return userRepository.findById(userId);
    }
}
