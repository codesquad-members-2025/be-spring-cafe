package codesquad.codestagram.user.service;

import codesquad.codestagram.common.exception.error.DuplicateResourceException;
import codesquad.codestagram.common.exception.error.InvalidRequestException;
import codesquad.codestagram.common.exception.error.ResourceNotFoundException;
import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.dto.SignUpRequest;
import codesquad.codestagram.user.dto.UserUpdateRequest;
import codesquad.codestagram.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    public static final  String USER_NOT_FOUND = "존재하지 않는 회원입니다.";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User join(SignUpRequest request) {
        validateDuplicateUserId(request.userId());
//        validatePassword(request.password());
        return userRepository.save(request.toEntity());
    }

    private void validateDuplicateUserId(String userId) {
        userRepository.findByUserId(userId).ifPresent(user ->
                {
                    throw new DuplicateResourceException("이미 존재하는 사용자 아이디입니다.");
                }
        );
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(USER_NOT_FOUND)
        );
    }
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public boolean verifyPassword(Long id, String inputPassword) {
        User user = findById(id);
        return inputPassword.equals(user.getPassword());
    }

    @Transactional
    public User updateUser(Long id, UserUpdateRequest request) {

        User findUser = findById(id);

        if (request.password().equals(findUser.getPassword())) {
            throw new InvalidRequestException("새 비밀번호가 현재 비밀번호와 동일합니다. 다른 비밀번호를 입력해주세요.");
        }

        if (!request.password().isEmpty()) {
            validatePassword(request.password());
        }

        String newPassword = request.password().isEmpty() ? findUser.getPassword() : request.password();

        findUser.updateUser(newPassword, request.name(), request.email());

        return userRepository.save(findUser);
    }

    private void validatePassword(String password) {
        validatePasswordLength(password);
        validatePasswordUpperCase(password);
        validatePasswordDigit(password);
        validatePasswordSpecialChar(password);
    }

    private void validatePasswordLength(String password) {
        if (password.length() < 8) {
            throw new InvalidRequestException("비밀번호는 최소 8자 이상이어야 합니다.");
        }

        if (password.length() > 20) {
            throw new InvalidRequestException("비밀번호는 최대 20자 이하여야 합니다.");
        }
    }

    private void validatePasswordUpperCase(String password) {
        if (password.chars().noneMatch(Character::isUpperCase)) {
            throw new InvalidRequestException("비밀번호는 최소 하나의 대문자를 포함해야 합니다.");
        }
    }

    private void validatePasswordDigit(String password) {
        if (password.chars().noneMatch(Character::isDigit)) {
            throw new InvalidRequestException("비밀번호는 최소 하나의 숫자를 포함해야 합니다.");
        }
    }

    private void validatePasswordSpecialChar(String password) {
        String specialChars = "!@#$%^&*()_-+=<>?/[]{}|";
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (specialChars.contains(String.valueOf(c))) {
                hasSpecialChar = true;
                break;
            }
        }

        if (!hasSpecialChar) {
            throw new InvalidRequestException("비밀번호는 최소 하나의 특수 문자를 포함해야 합니다.");
        }
    }

    public User authenticate(String userId, String password) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        if (!user.getPassword().equals(password)) {
            throw new InvalidRequestException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
}
