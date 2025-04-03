package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.domain.User;
import codesquad.codestagram.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findArticleList() {
        return articleRepository.findAll();
    }

    public void saveArticle(Article article) {
        articleRepository.save(article);
    }

    public Article findArticle(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));
    }

    public boolean updateArticle(Long id, Article article, User loginUser) {
        Article findArticle = findArticle(id);

        if (loginUser.getId() != findArticle.getUser().getId()) {
            return false;
        }

        article.setUser(loginUser);
        articleRepository.save(article);
        return true;
    }

    public boolean removeArticle(Long id, User loginUser) {
        Article findArticle = findArticle(id);

        if (loginUser.getId() != findArticle.getUser().getId()) {
            return false;
        }
        articleRepository.deleteById(id);
        return true;
    }
}
