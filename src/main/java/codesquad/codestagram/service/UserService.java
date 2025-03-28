package codesquad.codestagram.service;

import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.UserForm;
import codesquad.codestagram.exception.DuplicateUserIdException;
import codesquad.codestagram.exception.InvalidPasswordException;
import codesquad.codestagram.exception.UnauthorizedAccessException;
import codesquad.codestagram.exception.UserNotFoundException;
import codesquad.codestagram.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User join(UserForm userform) {
        Optional<User> result = userRepository.findByUserId(userform.getUserId());
        result.ifPresent(u -> {
            throw new DuplicateUserIdException("ID가 " + u.getUserId() + "인 사용자가 이미 존재합니다.");
        });
        return userRepository.save(userform.toEntity());
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("ID가 " + userId + "인 사용자가 존재하지 않습니다."));
    }

    @Transactional
    public void updateUser(User loginUser, UserForm userForm) {
        String userId = userForm.getUserId();
        User user = findByUserId(userId);
        if (!user.equals(loginUser)) {
            throw new UnauthorizedAccessException("해당 사용자가 아닙니다.");
        }
        user.updateIfPasswordValid(
                userForm.getPassword(),
                userForm.getChangedPassword(),
                userForm.getName(),
                userForm.getEmail());
    }

    public User userLogin(String userId, String password) {
        User user = findByUserId(userId);
        if (!user.isPasswordValid(password)) {
            throw new InvalidPasswordException();
        }
        return user;
    }
}
