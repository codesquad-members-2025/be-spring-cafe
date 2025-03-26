package codesquad.codestagram.service;

import codesquad.codestagram.dto.UserRequestDto;
import codesquad.codestagram.entity.User;
import codesquad.codestagram.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initExampleUsers(){
        userRepository.save(new User("a","a","a@a","bazzi"));
        userRepository.save(new User("b","b","b@b","bazzi1"));
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void join(UserRequestDto dto){
        userRepository.save(dto.toEntity());
    }
}
