package codesquad.codestagram.repository;

import codesquad.codestagram.domain.Article;
import org.springframework.stereotype.Repository;

import java.util.*;

public class MemoryArticleRepository implements ArticleRepository {

    private final List<Article> articleMemory = new ArrayList<>();

    @Override
    public Article save(Article article) {
        article.setId((long) (articleMemory.size() + 1));
        articleMemory.add(article);
        return article;
    }

    @Override
    public Optional<Article> findById(Long id) {
        return Optional.of(articleMemory.get((int) (id - 1)));
    }

    @Override
    public List<Article> findAll() {
        return articleMemory;
    }
}
