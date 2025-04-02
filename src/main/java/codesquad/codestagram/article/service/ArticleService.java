package codesquad.codestagram.article.service;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.article.dto.ArticleRequest;
import codesquad.codestagram.article.repository.ArticleRepository;
import codesquad.codestagram.common.exception.error.ForbiddenException;
import codesquad.codestagram.common.exception.error.InvalidRequestException;
import codesquad.codestagram.common.exception.error.ResourceNotFoundException;
import codesquad.codestagram.user.domain.User;
import codesquad.codestagram.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static codesquad.codestagram.user.service.UserService.USER_NOT_FOUND;

@Service
public class ArticleService {

    public static final String NOT_FOUND_ARTICLE = "존재하지 않는 게시글입니다.";

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Long create(ArticleRequest request, Long id) {

        User foundUser = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(USER_NOT_FOUND)
        );

        Article article = new Article(
                foundUser,
                request.title(),
                request.contents()
        );
        return articleRepository.save(article).getId();
    }

    public List<Article> findArticles() {
        return articleRepository.findAll();
    }

    public Article findArticle(Long id) {
        return articleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(NOT_FOUND_ARTICLE)
        );
    }

    @Transactional
    public void updateArticle(Long id, ArticleRequest request, Long loggedInUserId) {


        Article article = articleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(NOT_FOUND_ARTICLE)
        );

        if (!loggedInUserId.equals(article.getWriter().getId())) {
            throw new ForbiddenException("본인의 게시글만 수정할 수 있습니다.");
        }

        validateArticle(request.title(), request.contents());

        article.updateArticle(request.title(), request.contents());
    }

    private void validateArticle(String title, String contents) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidRequestException("제목을 입력해주세요");
        }

        if (contents == null || contents.trim().isEmpty()) {
            throw new InvalidRequestException("내용을 입력해주세요");
        }
    }

    public void delete(Long id) {
        Article article = findArticle(id);
        articleRepository.delete(article);
    }
}
