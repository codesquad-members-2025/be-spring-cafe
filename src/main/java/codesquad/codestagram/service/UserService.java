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
        //같은 아이디 중복 회원 x
        userRepository.findByUserId(user.getUserId())
                .ifPresent(u -> {
                    throw new IllegalStateException("user already exists");
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

    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    /**
     * 회원 정보 수정
     */

    public boolean updateUser(Long id, String currentPassword, String newPassword, String name, String email ) {
        Optional<User> optionalUser = userRepository.findById(id);
        //실행 흐름상 유저가 없을 수는 없지만 . 사이에 누군가가 삭제했을 수도 있으니 넣어주기.
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!user.getPassword().equals(currentPassword)) {
                return false; // 비밀번호 틀림
            }

            user.setPassword(newPassword);
            user.setName(name);
            user.setEmail(email);

            userRepository.save(user);
            return true;
        }
        return false; // 유저 없음
    }


}
