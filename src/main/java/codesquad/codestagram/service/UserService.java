package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserForm;
import codesquad.codestagram.repository.UserRepository;
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
            throw new NoSuchElementException("이미 존재하는 아이디입니다.");
        });
        return userRepository.save(userform.makeUser());
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User findByUserId(String userId){
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 아이디의 유저가 없습니다."));
    }

    public boolean updateUser(UserForm userForm){
        String userId = userForm.getUserId();
        User user = findByUserId(userId);
        if(isPasswordValid(user,userForm.getPassword())){
            user.setPassword(userForm.getChangedPassword());
            user.setName(userForm.getName());
            user.setEmail(userForm.getEmail());
            return true;
        }
        return false;
    }

    private boolean isPasswordValid(User user,String password){
        return user.getPassword().equals(password);
    }
}
