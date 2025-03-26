package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleForm;
import codesquad.codestagram.exception.ArticleNotFoundException;
import codesquad.codestagram.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;

    public ArticleService(ArticleRepository articleRepository, UserService userService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    public List<Article> findAllArticles() {
        return articleRepository.findAll();
    }

    public List<Article> findArticlesByTitle(String title) {
        return articleRepository.findByTitle(title);
    }

    public List<Article> findArticlesByUser(User user) {
        return articleRepository.findByUser(user);
    }

    public Article findArticleById(int id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("해당하는 ID("+ id +")의 게시글을 찾을 수 없습니다."));
        return article;
    }

    public Article createArticleAndSave(User user, ArticleForm articleForm) {
        Article article = articleForm.createParsedArticle(user);
        return articleRepository.save(article);
    }
}
