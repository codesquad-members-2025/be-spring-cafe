package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserForm;
import codesquad.codestagram.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User join(UserForm userform){
        Optional<User> result = userRepository.findByUserId(userform.getUserId());
        result.ifPresent(u-> {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        });
        return userRepository.save(userform.toEntity());
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User findByUserId(String userId){
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 아이디의 유저가 없습니다."));
    }

    @Transactional
    public boolean updateUser(User loginUser, UserForm userForm){
        String userId = userForm.getUserId();
        User user = findByUserId(userId);
        if(!user.equals(loginUser)) {
            return false;
        }
        return user.updateIfPasswordValid(
                userForm.getPassword(),
                userForm.getChangedPassword(),
                userForm.getName(),
                userForm.getEmail());
    }

    public User userLogin(String userId, String password){
        User user = findByUserId(userId);
        if(user.isPasswordValid(password)){
            return user;
        }
        throw new NoSuchElementException("잘못된 비밀번호 입니다.");
    }
}
