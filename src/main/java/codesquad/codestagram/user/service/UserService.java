package codesquad.codestagram.user.service;

import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void join(User user) {
        //todo: 중복 회원에 대한 검증 필요
        userRepository.save(user);
    }

    public User findUser(Long seq) {
        return userRepository.findBySeq(seq).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 회원입니다.")
        );
    }
    public List<User> findUsers() {
        return userRepository.findAll();
    }
}
