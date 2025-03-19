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

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User join(User user){
        Optional<User> result = userRepository.fineByUserId(user.getUserId());
        result.ifPresent(u-> {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        });
        return userRepository.save(user);
    }

    public List<User> findAllUsers(){
        return userRepository.fineAll();
    }

    public Optional<User> findByUserId(String userId){
        return userRepository.fineByUserId(userId);
    }
}
