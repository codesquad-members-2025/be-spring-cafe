package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.UserRepositoryV2;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepositoryV2 userRepository;
    private final ArticleRepository articleRepository;

    private Long defaultUserId;

    public UserService(UserRepositoryV2 userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @PostConstruct
    public void createDefaultUser() {
        User defaultUser = new User("탈퇴한 유저", "deleted@example.com", "deleted_user", "deleted_password");
        userRepository.save(defaultUser);
        defaultUserId=defaultUser.getId();
    }

    public User getDefaultUser() {
        return userRepository.findById(defaultUserId)
                .orElseThrow(() -> new RuntimeException("Default user not found"));
    }

    // 회원 가입
    public User registerUser(String name, String email, String loginId, String password) {
        User newUser = new User(name, email, loginId, password);
        return userRepository.save(newUser);
    }

    // 모든 회원 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 특정 회원 조회
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    // 회원 프로필 업데이트
    public void updateUserProfile(Long userId, String password, String name, String email) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(password);
            user.setName(name);
            user.setEmail(email);
            userRepository.save(user);  // 변경 사항 저장
        } else {
            throw new IllegalArgumentException("해당 ID의 사용자가 존재하지 않습니다.");
        }
    }

    public void deleteUserWithPasswordCheck(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. ID: " + userId));

        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        User defaultUser = userRepository.findById(defaultUserId).orElseThrow(() -> new IllegalArgumentException(""));

        List<Article> articles = articleRepository.findArticlesByUser(user);
        for (Article article : articles) {
            article.setUser(defaultUser);
        }

        userRepository.delete(user);
    }
}
