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

    public static final String ARTICLE_NOT_FOUND = "존재하지 않는 게시글입니다.";

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

        validateArticle(request.title(), request.contents());

        Article article = new Article(
                foundUser,
                request.title(),
                request.contents()
        );
        return articleRepository.save(article).getId();
    }

    public List<Article> findArticles() {
        return articleRepository.findAllWithRepliesAndWriter();
    }

    public Article findArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(ARTICLE_NOT_FOUND)
        );

        if (article.isDeleted()) {
            throw new ResourceNotFoundException(ARTICLE_NOT_FOUND);
        }

        return article;
    }

    public Article findArticleWithReplies(Long id) {
        return articleRepository.findByIdWithRepliesAndWriter(id)
                .orElseThrow(() -> new ResourceNotFoundException(ARTICLE_NOT_FOUND));
    }

    public Article findArticleAndVerifyOwner(Long id, Long loggedInUserId) {
        Article article = findArticle(id);
        validateArticleOwner(loggedInUserId, article);
        return article;
    }

    @Transactional
    public void updateArticle(Long id, ArticleRequest request, Long loggedInUserId) {

        Article article = findArticleAndVerifyOwner(id, loggedInUserId);
        validateArticle(request.title(), request.contents());
        article.updateArticle(request.title(), request.contents());
    }

    @Transactional
    public void delete(Long id, Long loggedInUserId) {
        Article article = findArticleAndVerifyOwner(id, loggedInUserId);

        if (article.hasRepliesByOtherUsers()) {
            throw new ForbiddenException("다른 사용자가 작성한 댓글이 있는 게시글은 삭제할 수 없습니다.");
        }

        article.markAsDeleted();
        article.markRepliesAsDeleted();
    }

    private void validateArticleOwner(Long loggedInUserId, Article article) {
        if (!article.getWriter().getId().equals(loggedInUserId)) {
            throw new ForbiddenException("본인의 게시글만 수정할 수 있습니다.");
        }
    }

    private void validateArticle(String title, String contents) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidRequestException("제목을 입력해주세요");
        }

        if (contents == null || contents.trim().isEmpty()) {
            throw new InvalidRequestException("내용을 입력해주세요");
        }
    }
}
