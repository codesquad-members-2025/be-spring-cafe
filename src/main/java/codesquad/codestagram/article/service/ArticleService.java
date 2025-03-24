package codesquad.codestagram.article.service;

import codesquad.codestagram.article.domain.Article;
import codesquad.codestagram.article.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void create(Article article) {
        articleRepository.save(article);
    }

    public List<Article> findArticles() {
        return articleRepository.findAll();
    }

    public Article findArticle(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(
                () -> new NoSuchElementException("존재하지 않는 질문입니다.")
        );
    }

}
