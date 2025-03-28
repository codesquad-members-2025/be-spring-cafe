package codesquad.codestagram.domain.article;

import codesquad.codestagram.domain.article.exception.ArticleNotFoundException;
import codesquad.codestagram.domain.auth.exception.UnauthorizedException;
import codesquad.codestagram.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article createArticle(String title, String content, User currentUser) {
        Article article = new Article(currentUser.getId(), title, content);

        return articleRepository.save(article);
    }

    public Article findArticle(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("게시물을 찾을 수 없습니다."));
    }

    // 로그인 여부, 게시물 존재, 작성자 일치 여부를 검증 후 게시물 반환
    public Article getAuthorizedArticle(Long id, User currentUser) {
        Article article = findArticle(id);
        if (!article.isSameWriter(currentUser.getId())) {
            throw new UnauthorizedException("본인이 작성한 글만 수정할 수 있습니다.");
        }

        return article;
    }

    public Article updateArticle(Long id, String title, String content, User currentUser) {
        Article article = getAuthorizedArticle(id, currentUser);
        article.setTitle(title);
        article.setContent(content);

        return article;
    }

    public void deleteArticle(Long id, User currentUser) {
        Article article = getAuthorizedArticle(id, currentUser);
        articleRepository.delete(article);
    }

}
