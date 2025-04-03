package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserForm;
import codesquad.codestagram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    //생성자를 통해 외부에서 UserRepository 주입 -> DI 활용 위해
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository; }

    /**
     * 회원가입
     */
    @Transactional
    public  boolean join(User user) {
        // Entity를 저장
        if (userRepository.findByLoginId(user.getLoginId()).isPresent()) {
            return false;
        }
        userRepository.save(user);
        return true;
    }


    /**
     * 전체 회원 조회
     */
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findOne(Long loginId) {
        return userRepository.findById(loginId);
    }

    public Optional<User> findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    /**
     * 회원 정보 수정
     */

    @Transactional
    public boolean updateUser(Long id, UserForm.UpdateUser update ) {
        Optional<User> optionalUser = userRepository.findById(id);
        //실행 흐름상 유저가 없을 수는 없지만 . 사이에 누군가가 삭제했을 수도 있으니 넣어주기.
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!user.getPassword().equals(update.getCurrentPassword())) {
                return false; // 비밀번호 틀림
            }
            user.updateform(update);
            userRepository.save(user);
            return true;
        }
        return false; // 유저 없음
    }


}
