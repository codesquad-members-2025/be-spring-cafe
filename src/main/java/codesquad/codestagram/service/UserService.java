package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserForm;
import codesquad.codestagram.exception.DuplicateUserIdException;
import codesquad.codestagram.exception.InvalidPasswordException;
import codesquad.codestagram.exception.UserNotFoundException;
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
            throw new DuplicateUserIdException(u.getUserId());
        });
        return userRepository.save(userform.toEntity());
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User findByUserId(String userId){
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
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
        throw new InvalidPasswordException();
    }
}
