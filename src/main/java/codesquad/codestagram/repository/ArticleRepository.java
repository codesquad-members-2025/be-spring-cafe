package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ArticleRepository {

    private static List<Article> articles = Collections.synchronizedList(new ArrayList<>());
    private static Long sequence = 0L;

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
