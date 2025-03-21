package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleForm;
import codesquad.codestagram.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
                .orElseThrow(() -> new NoSuchElementException("해당하는 게시물이 없습니다."));
        return article;
    }

    public Article createArticleAndSave(ArticleForm articleForm) {
        User user = userService.findByUserId(articleForm.getUserId());
        Article article = articleForm.createParsedArticle(user);
        saveArticle(article);
        return article;
    }

    private Article saveArticle(Article article) {
        return articleRepository.save(article);
    }
}
