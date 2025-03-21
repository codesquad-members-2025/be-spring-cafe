package codesquad.codestagram.service;

import codesquad.codestagram.controller.User;
import codesquad.codestagram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user){
        userRepository.save(user);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다."));
    }

    public void update(User user, String name, String email){
        user.setName(name);
        user.setEmail(email);
    }
}
