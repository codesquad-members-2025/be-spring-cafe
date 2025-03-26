package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.dto.ArticleForm;
import codesquad.codestagram.exception.ArticleNotFoundException;
import codesquad.codestagram.exception.UnauthorizedAccessException;
import codesquad.codestagram.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
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
        Article article = articleForm.toEntity(user);
        return articleRepository.save(article);
    }

    public void delete(Article article) {
        articleRepository.delete(article);
    }

    @Transactional
    public void update(User user, int id, ArticleForm articleForm) {
        Article article = findArticleById(id);
        if(!article.isAuthor(user)){
            throw new UnauthorizedAccessException("게시물을 수정할 권한이 없습니다.");
        }
        article.update(articleForm);
    }
}
