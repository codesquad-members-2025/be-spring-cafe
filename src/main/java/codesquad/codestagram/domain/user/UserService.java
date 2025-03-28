package codesquad.codestagram.domain.user;

import codesquad.codestagram.domain.auth.exception.UnauthorizedException;
import codesquad.codestagram.domain.user.exception.DuplicatedUserException;
import codesquad.codestagram.domain.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원 가입: 중복 사용자 검증 후 저장
    public void signUp(String userId, String password, String name, String email) {
        Optional<User> existingUser = userRepository.findByUserId(userId);
        if (existingUser.isPresent()) {
            throw new DuplicatedUserException("이미 존재하는 사용자입니다.");
        }
        User newUser = new User(userId, password, name, email);
        userRepository.save(newUser);
    }

    // 전체 사용자 조회
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // 특정 사용자 조회 (없으면 예외)
    public User getUserProfile(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }

    // 사용자 수정: 로그인 여부, 대상 존재, 작성자 일치 및 비밀번호 확인
    public User updateUser(Long id, String email, String name, String currentPassword, String newPassword, User sessionUser) {
        if (sessionUser == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        if (!sessionUser.equals(userEntity)) {
            throw new UnauthorizedException("본인의 정보만 수정할 수 있습니다.");
        }

        if (!userEntity.isMatchPassword(currentPassword)) {
            throw new UnauthorizedException("비밀번호가 올바르지 않습니다.");
        }

        userEntity.setEmail(email);
        userEntity.setName(name);
        if (newPassword != null && !newPassword.isEmpty()) {
            userEntity.setPassword(newPassword);
        }

        return userEntity;
    }

}
