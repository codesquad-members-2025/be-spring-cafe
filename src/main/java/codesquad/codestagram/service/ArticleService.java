package codesquad.codestagram.service;

import codesquad.codestagram.domain.Article;
import codesquad.codestagram.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    // 게시글 등록
    public Long ask(Article article) {
        articleRepository.save(article);
        return article.getId();
    }

    public Optional<Article> findOneArticle(Long articleId) {
        return articleRepository.findByArticleId(articleId);
    }

    public List<Article> findAllArticle() {
        return articleRepository.findAllArticles();
    }
}
