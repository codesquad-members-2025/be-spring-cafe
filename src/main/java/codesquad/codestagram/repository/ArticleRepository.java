package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {

    private static List<Article> articles = new ArrayList<>();
    private static long sequence = 0L;

    public Article save(Article article) {
        article.setId(++sequence);
        articles.add(article);
        return article;
    }

    public Optional<Article> findById(Long id) {
        return articles.stream()
            .filter(article -> article.getId().equals(id))
            .findAny();
    }

    public List<Article> findAll() {
        return articles;
    }
}
