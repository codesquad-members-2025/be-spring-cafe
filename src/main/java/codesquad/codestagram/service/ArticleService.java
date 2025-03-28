package codesquad.codestagram.service;

import static codesquad.codestagram.controller.AuthController.SESSIONED_USER;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleDto.ArticleRequestDto;
import codesquad.codestagram.repository.ArticleRepository;
import codesquad.codestagram.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import java.nio.file.AccessDeniedException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
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
}
