package codesquad.codestagram.service;

import static codesquad.codestagram.controller.AuthController.SESSIONED_USER;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleDto.ArticleRequestDto;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import java.nio.file.AccessDeniedException;
import java.util.List;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
@DependsOn("userService")
public class ArticleService {
    public static final String NO_USER = "유저가 존재하지 않습니다.";
    public static final String NO_ARTICLE = "게시글이 존재하지 않습니다.";
    public static final String NOT_AUTHOR = "글의 작성자가 아닙니다.";
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }
    @PostConstruct
    public void initArticle() {
        User user1 = userRepository.findById(1L).orElseGet(() -> {
            User newUser = new User("javajigi", "test", "자바지기", "javajigi@slipp.net");
            return userRepository.save(newUser);
        });

        User user2 = userRepository.findById(2L).orElseGet(() -> {
            User newUser = new User("sanjigi", "test", "산지기", "sanjigi@slipp.net");
            return userRepository.save(newUser);
        });

        articleRepository.save(new Article("test1", "content1", user1));
        articleRepository.save(new Article("test2", "content2", user1));
        articleRepository.save(new Article("test3", "content3", user2));
    }
    public void saveArticle(ArticleRequestDto requestDto) {
        User user = userRepository.findByUserId(requestDto.getUserId())
                .orElseThrow(()->new IllegalArgumentException(NO_USER));

        Article article = requestDto.toArticle(user);
        articleRepository.save(article);
    }

    public List<Article> findArticles() {
        return articleRepository.findAllIsNotDeleted();
    }

    public Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException(NO_ARTICLE));
    }

    public void updateArticle(Long articleId, String title, String content) {
        Article article = findArticleById(articleId);
        article.update(title, content);
        articleRepository.save(article);
    }

    public void delete(Long articleId) {
        //해당 게시글이 DB에 존재하는지 확인
        Article article = findArticleById(articleId);
        article.changeDeleteStatus(true);
        articleRepository.save(article);
    }
    public void matchArticleAuthor(HttpSession session, Article article) throws AccessDeniedException {
        User sessionedUser = (User) session.getAttribute(SESSIONED_USER);
        if (!sessionedUser.getUserId().equals(article.getUser().getUserId())) {
            throw new AccessDeniedException(NOT_AUTHOR);
        }
    }
    public boolean isArticleAuthor(HttpSession session, Article article){
        User sessionedUser = (User) session.getAttribute(SESSIONED_USER);
        return sessionedUser.getUserId().equals(article.getUser().getUserId());
    }
}
